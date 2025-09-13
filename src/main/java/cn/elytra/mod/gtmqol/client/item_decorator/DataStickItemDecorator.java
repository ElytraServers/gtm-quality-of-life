package cn.elytra.mod.gtmqol.client.item_decorator;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.utils.ResearchManager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class DataStickItemDecorator extends CornerItemDecorator {

    public static final DataStickItemDecorator INSTANCE = new DataStickItemDecorator();

    public boolean enabled = true;

    @Override
    protected @Nullable ItemStack getItemToRender(ItemStack containerItem) {
        ResearchManager.ResearchItem researchItem = ResearchManager.readResearchId(containerItem);
        if (researchItem == null) return null;

        Collection<GTRecipe> recipes = researchItem.recipeType().getDataStickEntry(researchItem.researchId());
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
        return !shiftKeyPressed;
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    static class Register {

        @SubscribeEvent
        static void registerItemDecorator(RegisterItemDecorationsEvent event) {
            event.register(GTItems.TOOL_DATA_STICK, INSTANCE);
            event.register(GTItems.TOOL_DATA_ORB, INSTANCE);
            event.register(GTItems.TOOL_DATA_MODULE, INSTANCE);
        }

    }
}
