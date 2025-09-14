package cn.elytra.mod.gtmqol.client.item_decorator;

import cn.elytra.mod.gtmqol.config.QualityConfig;
import cn.elytra.mod.gtmqol.util.QualityUtils;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.machine.storage.QuantumTankMachine;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.side.fluid.forge.FluidHelperImpl;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Marker;

public class FluidHandlerContentItemDecorator implements IItemDecorator {

    /**
     * A default instance for rendering GTM tanks.
     */
    public static final FluidHandlerContentItemDecorator INSTANCE = new FluidHandlerContentItemDecorator();

    private static final float FLUID_ICON_WIDTH = 8;
    private static final float FLUID_ICON_HEIGHT = 8;

    private static final float[][] FLUID_ICON_OFFSETS = { { 8, 8 }, { 0, 8 }, { 8, 0 }, { 0, 0 } };

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack itemStack, int x, int y) {
        if (!QualityConfig.get().itemDecorator.tankContent.renderContentAtCorner) {
            return false;
        }

        IFluidHandlerItem handler = QualityUtils.getLazyOptionalNullable(itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM));
        if (handler == null) {
            if (QualityUtils.isDev()) {
                QualityUtils.LOG.warn(
                    "Unable to render fluid icons for {}, because it doesn't have [ForgeCapabilities.FLUID_HANDLER_ITEM] capability.",
                    itemStack);
            }
            return false;
        }

        // disable the depth test, so that the fluid icon is rendered on top of the item.
        // otherwise, it's on the background, and may be covered by the item icon.
        if (isRenderOnTopOfItem()) {
            RenderSystem.disableDepthTest();
        }

        // render the fluid icon in the order of bottom right, bottom left, top right, top left (defined in the array)
        // taking 1/4 slot area.
        // it will iterate tanks in the item, util either the tanks are all iterated or reached the limit of rendering
        // count, which is 1 by default.
        for (int index = 0, count = 0; index < handler.getTanks() && count < getMaxTankCountToRender(); index++) {
            FluidStack fluidInTank = getFluidInTankCatching(handler, index);
            if (!fluidInTank.isEmpty()) {
                DrawerHelper.drawFluidForGui(
                    guiGraphics,
                    FluidHelperImpl.toFluidStack(fluidInTank),
                    x + FLUID_ICON_OFFSETS[count][0],
                    y + FLUID_ICON_OFFSETS[count][1],
                    FLUID_ICON_WIDTH,
                    FLUID_ICON_HEIGHT);
                count++;
            }
        }

        return true;
    }

    private static FluidStack getFluidInTankCatching(IFluidHandler handler, int index) {
        try {
            return handler.getFluidInTank(index);
        } catch (Exception e) {
            // ignored, we don't truly care if it's malformed data. A bug of GTM.
            return FluidStack.EMPTY;
        }
    }

    /**
     * The count of tanks in the ItemStack to be rendered, maximum at 4.
     */
    protected int getMaxTankCountToRender() {
        return QualityConfig.get().itemDecorator.tankContent.renderContentAtCornerMaxType;
    }

    /**
     * If {@code true}, the fluid icon is rendered on top of the item.
     */
    protected boolean isRenderOnTopOfItem() {
        return QualityConfig.get().itemDecorator.tankContent.renderContentAtCornerOnTopOfItem;
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    static class ContentCornerRegister {

        private static final Marker M = QualityUtils.getMarkerForClass(ContentCornerRegister.class);

        @SubscribeEvent
        public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
            // drums
            register(event, GTMachineUtils.DRUM_CAPACITY.keySet());
            // super tanks, quantum tanks
            register(event, QuantumTankMachine.TANK_CAPACITY.keySet());
        }

        private static void register(RegisterItemDecorationsEvent event, Iterable<MachineDefinition> list) {
            for (MachineDefinition drumDefinition : list) {
                MetaMachineItem item = drumDefinition.getItem();
                event.register(item, FluidHandlerContentItemDecorator.INSTANCE);
                QualityUtils.LOG.info(M, "Registered FluidHandlerItemDecorator {}", item);
            }
        }

    }

}
