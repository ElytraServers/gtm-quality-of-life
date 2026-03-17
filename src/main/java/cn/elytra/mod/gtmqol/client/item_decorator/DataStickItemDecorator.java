package cn.elytra.mod.gtmqol.client.item_decorator;

import cn.elytra.mod.gtmqol.config.QualityConfig;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.utils.ResearchManager;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class DataStickItemDecorator extends CornerItemDecorator {

    public static final DataStickItemDecorator INSTANCE = new DataStickItemDecorator();

    @Override
    protected @Nullable ItemStack getItemToRender(ItemStack containerItem) {
        Pair<GTRecipeType, String> pair = ResearchManager.readResearchId(containerItem);
        if (pair == null) return null;

        Collection<GTRecipe> recipes = pair.getFirst().getDataStickEntry(pair.getSecond());
        if (recipes == null || recipes.isEmpty()) return null;

        GTRecipe recipe = recipes.iterator().next();
        ItemStack[] outputs = ItemRecipeCapability.CAP.of(recipe.getOutputContents(ItemRecipeCapability.CAP)
            .get(0).content).getItems();
        if (outputs.length > 0) {
            return outputs[0];
        } else {
            return null;
        }
    }

    @Override
    protected boolean shouldRender(boolean shiftKeyPressed) {
        return QualityConfig.get().itemDecorator.recipeDataContent.renderRecipeDataAtCorner && !shiftKeyPressed;
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    static class RegisterDataStick {

        @SubscribeEvent
        static void registerItemDecorator(RegisterItemDecorationsEvent event) {
            for(Item item : ForgeRegistries.ITEMS) {
                if (ResearchManager.isStackDataItem(new ItemStack(item), true)) {
                    event.register(item, INSTANCE);
                }
            }
        }

    }
}
