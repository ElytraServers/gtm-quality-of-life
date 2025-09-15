package cn.elytra.mod.gtmqol.client.item_decorator;

import cn.elytra.mod.gtmqol.GregTechModernQualityOfLife;
import cn.elytra.mod.gtmqol.config.QualityConfig;
import cn.elytra.mod.gtmqol.util.CleanableMemoizedFunction;
import cn.elytra.mod.gtmqol.util.MemoizeUtils;
import cn.elytra.mod.gtmqol.util.QualityUtils;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class WaferItemDecorator extends CornerItemDecorator {

    private static final Lazy<List<Item>> GLASS_LENSES_LAZY = Lazy.of(() -> GTItems.GLASS_LENSES.values()
        .stream()
        .map(ItemEntry::asItem)
        .toList());

    private static final List<Item> EXTRA_LENSES = new ArrayList<>();

    private static final CleanableMemoizedFunction<Item, Item> CORRESPONDING_LENS_ITEM_FUNCTION_MEMOIZED = MemoizeUtils.memoize(
        WaferItemDecorator::getCorrespondingLens);

    private static final WaferItemDecorator INSTANCE = new WaferItemDecorator();

    @Override
    protected @Nullable ItemStack getItemToRender(ItemStack waferStack) {
        Item lens = CORRESPONDING_LENS_ITEM_FUNCTION_MEMOIZED.apply(waferStack.getItem());
        return lens != null ? new ItemStack(lens) : null;
    }

    @Override
    protected float getScaling() {
        return 0.5F;
    }

    @Override
    protected Corner getCorner() {
        return Corner.TOP_RIGHT;
    }

    @Override
    protected boolean shouldRender(boolean shiftKeyPressed) {
        return QualityConfig.get().itemDecorator.waferRecipeLens.renderWaferRecipeLens;
    }

    private static @Nullable Item getCorrespondingLens(Item wafer) {
        List<GTRecipe> recipes = getRecipesThatOutputs(wafer);
        if (recipes.isEmpty()) return null;

        for (GTRecipe recipe : recipes) {
            List<ItemStack> inputItems = QualityUtils.getItemContent(recipe::getInputContents);
            Optional<Item> lens = inputItems.stream()
                .map(ItemStack::getItem)
                .filter(WaferItemDecorator::isValidLens)
                .findFirst();
            if (lens.isPresent()) {
                QualityUtils.LOG.info("Found laser engraving recipe lens {} for {}", lens.get(), wafer);
                return lens.get();
            }
        }
        return null;
    }

    private static List<GTRecipe> getRecipesThatOutputs(Item wafer) {
        return GTRecipeTypes.LASER_ENGRAVER_RECIPES.getCategoryMap()
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(recipe -> {
                List<ItemStack> outputItems = QualityUtils.getItemContent(recipe::getOutputContents);
                //QualityUtils.getItemContent(recipe.getOutputContents(ItemRecipeCapability.CAP));
                return outputItems.stream().anyMatch(i -> i.is(wafer));
            })
            .toList();
    }

    private static boolean isValidLens(Item lens) {
        return GLASS_LENSES_LAZY.get().contains(lens) || EXTRA_LENSES.contains(lens);
    }

    @ApiStatus.Internal
    public static void clearMemoized() {
        CORRESPONDING_LENS_ITEM_FUNCTION_MEMOIZED.clear();
    }

    @Mod.EventBusSubscriber(modid = GregTechModernQualityOfLife.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    static class WaferDecoratorRegister {

        @SubscribeEvent
        static void onRegisterDecorator(RegisterItemDecorationsEvent event) {
            event.register(GTItems.CENTRAL_PROCESSING_UNIT_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.RANDOM_ACCESS_MEMORY_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.INTEGRATED_LOGIC_CIRCUIT_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.NANO_CENTRAL_PROCESSING_UNIT_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.QUBIT_CENTRAL_PROCESSING_UNIT_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.SIMPLE_SYSTEM_ON_CHIP_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.SYSTEM_ON_CHIP_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.ADVANCED_SYSTEM_ON_CHIP_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.HIGHLY_ADVANCED_SOC_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.NAND_MEMORY_CHIP_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.NOR_MEMORY_CHIP_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.ULTRA_LOW_POWER_INTEGRATED_CIRCUIT_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.LOW_POWER_INTEGRATED_CIRCUIT_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.POWER_INTEGRATED_CIRCUIT_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.HIGH_POWER_INTEGRATED_CIRCUIT_WAFER, WaferItemDecorator.INSTANCE);
            event.register(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT_WAFER, WaferItemDecorator.INSTANCE);

            QualityConfig.ItemDecorator.WaferRecipeLens config = QualityConfig.get().itemDecorator.waferRecipeLens;
            Arrays.stream(config.renderWaferRecipeLensExtraWafers)
                .map(QualityUtils::getItemByKey)
                .filter(Optional::isPresent)
                .forEach(i -> event.register(i.get(), WaferItemDecorator.INSTANCE));
            Arrays.stream(config.renderWaferRecipeLensExtraLens)
                .map(QualityUtils::getItemByKey)
                .filter(Optional::isPresent)
                .forEach(i -> EXTRA_LENSES.add(i.get()));
        }

    }
}
