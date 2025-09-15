package cn.elytra.mod.gtmqol.client.item_decorator;

import cn.elytra.mod.gtmqol.config.QualityConfig;
import cn.elytra.mod.gtmqol.util.QualityUtils;
import com.gregtechceu.gtceu.api.item.component.IDurabilityBar;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.machine.storage.QuantumTankMachine;
import com.gregtechceu.gtceu.utils.GradientUtil;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.forge.FluidHelperImpl;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;

import java.util.stream.IntStream;

public class FluidHandlerUsageBarItemDecorator implements IDurabilityBar, IItemDecorator {

    public enum MultiTankStrategy {
        /// when there's more than one tank in the item, only the **FIRST** tank is used.
        FIRST_ONLY,
        /// when there's more than one tank in the item, **ALL** of them are used accumulatively.
        COUNT_TOTAL,
    }

    public static FluidHandlerUsageBarItemDecorator INSTANCE = new FluidHandlerUsageBarItemDecorator();

    /// @return the [IFluidHandlerItem] instance related to the given stack
    protected @Nullable IFluidHandlerItem getFluidHandlerItem(ItemStack itemStack) {
        return QualityUtils.getLazyOptionalNullable(itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM));
    }

    /// @return the amount of existing fluids in the tank, regarding the strategy if there's more than one tank.
    protected int getFluidAmount(IFluidHandlerItem item) {
        return switch (item.getTanks()) {
            case 0 -> 0;
            case 1 -> item.getFluidInTank(0).getAmount();
            default -> switch (getMultiTankStrategy()) {
                case FIRST_ONLY -> item.getFluidInTank(0).getAmount();
                case COUNT_TOTAL -> IntStream.range(0, item.getTanks())
                    .mapToObj(item::getFluidInTank)
                    .mapToInt(FluidStack::getAmount)
                    .sum();
            };
        };
    }

    /// @return the amount of the fluid capacity of the tank, regarding the strategy if there's more than one tank.
    protected int getFluidCapacity(IFluidHandlerItem item) {
        return switch (item.getTanks()) {
            case 0 -> 0;
            case 1 -> item.getTankCapacity(0);
            default -> switch (getMultiTankStrategy()) {
                case FIRST_ONLY -> item.getTankCapacity(0);
                case COUNT_TOTAL -> IntStream.range(0, item.getTanks()).map(item::getTankCapacity).sum();
            };
        };
    }

    /// @return the first fluid in the item, `null` otherwise.
    protected @Nullable FluidStack getFluidKind(IFluidHandlerItem item) {
        if (item.getTanks() > 0) {
            return item.getFluidInTank(0);
        }
        return null;
    }

    @Override
    public float getDurabilityForDisplay(ItemStack stack) { // range: 0-1
        IFluidHandlerItem item = getFluidHandlerItem(stack);
        if (item != null) {
            int fluidAmount = getFluidAmount(item);
            int capacityAmount = getFluidCapacity(item);
            return (float) fluidAmount / capacityAmount;
        }
        return IDurabilityBar.super.getDurabilityForDisplay(stack);
    }

    @Override
    public int getMaxDurability(ItemStack stack) {
        IFluidHandlerItem item = getFluidHandlerItem(stack);
        if (item != null) {
            return getFluidCapacity(item);
        }
        return IDurabilityBar.super.getMaxDurability(stack);
    }

    @Override
    public boolean showEmptyBar(ItemStack itemStack) {
        return QualityConfig.get().itemDecorator.tankContent.renderContentDurabilityBarAtEmpty;
    }

    @Override
    public boolean showFullBar(ItemStack itemStack) {
        return QualityConfig.get().itemDecorator.tankContent.renderContentDurabilityBarAtFull;
    }

    @Override
    public @Nullable IntIntPair getDurabilityColorsForDisplay(ItemStack itemStack) {
        IFluidHandlerItem item = getFluidHandlerItem(itemStack);
        if (item == null) return null;

        FluidStack fluidKind = getFluidKind(item);
        if (fluidKind == null) return null;

        int color = FluidHelper.getColor(FluidHelperImpl.toFluidStack(fluidKind));
        return GradientUtil.getGradient(color, 10);
    }

    @Override
    public boolean doDamagedStateColors(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        if (!QualityConfig.get().itemDecorator.tankContent.renderContentDurabilityBar) return false;
        return IDurabilityBar.super.render(guiGraphics, font, stack, xOffset, yOffset);
    }

    protected MultiTankStrategy getMultiTankStrategy() {
        return QualityConfig.get().itemDecorator.tankContent.renderContentDurabilityBarMultiTankStrategy;
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    static class UsageBarRegister {

        private static final Marker M = QualityUtils.getMarkerForClass(UsageBarRegister.class);

        @SubscribeEvent
        static void registerItemDecorations(RegisterItemDecorationsEvent event) {
            // drums
            registerImpl(
                event,
                GTMachineUtils.DRUM_CAPACITY.keySet().stream().map(MachineDefinition::getItem).toList());
            // super tanks, quantum tanks
            registerImpl(
                event,
                QuantumTankMachine.TANK_CAPACITY.keySet().stream().map(MachineDefinition::getItem).toList());

            // register from configuration
            registerImpl(
                event,
                QualityUtils.getItemsByKeys(QualityConfig.get().itemDecorator.tankContent.renderContentDurabilityBarForItems));
        }

        private static void registerImpl(RegisterItemDecorationsEvent event, Iterable<? extends ItemLike> list) {
            list.forEach(item -> {
                event.register(item, FluidHandlerUsageBarItemDecorator.INSTANCE);
                QualityUtils.LOG.info(M, "Registering {}", item);
            });
        }

    }
}
