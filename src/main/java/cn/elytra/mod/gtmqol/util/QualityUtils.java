package cn.elytra.mod.gtmqol.util;

import cn.elytra.mod.gtmqol.GregTechModernQualityOfLife;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class QualityUtils {

    public static final Logger LOG = LoggerFactory.getLogger(GregTechModernQualityOfLife.MOD_ID);

    @SuppressWarnings("DataFlowIssue")
    public static <T> @Nullable T getLazyOptionalNullable(LazyOptional<T> lazyOptional) {
        return lazyOptional.orElse(null);
    }

    public static boolean isDev() {
        return !FMLLoader.isProduction();
    }

    public static Marker getMarkerForClass(Class<?> clazz) {
        return MarkerFactory.getMarker(clazz.getSimpleName());
    }

    /// Get items by a string. If the string is started with '#', items under the given tag is returned. Otherwise, item
    /// with the registry name is returned.
    public static Stream<@Nullable Item> getItems(String value) {
        if (value.startsWith("#")) {
            return getItemsByTag(new ResourceLocation(value.substring(1)));
        } else {
            return getItemByKey(value).stream();
        }
    }

    public static Stream<Item> getItemsByTag(ResourceLocation rl) {
        ITagManager<Item> itemTagManager = ForgeRegistries.ITEMS.tags();
        assert itemTagManager != null;
        TagKey<Item> tagKey = ItemTags.create(rl);
        if (itemTagManager.isKnownTagName(tagKey)) {
            return itemTagManager.getTag(tagKey).stream();
        }
        return Stream.of();
    }

    public static List<Item> getItemsByKeys(String[] keys) {
        return Arrays.stream(keys)
            .map(QualityUtils::getItemByKey)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }

    public static Optional<Item> getItemByKey(ResourceLocation rl) {
        return Optional.ofNullable(ForgeRegistries.ITEMS.getValue(rl));
    }

    public static Optional<Item> getItemByKey(String identifier) {
        return getItemByKey(new ResourceLocation(identifier));
    }

    private static List<ItemStack> getItemContent(List<Content> contents) {
        return contents.stream()
            .map(content -> ItemRecipeCapability.CAP.of(content.getContent()).getItems())
            .flatMap(Arrays::stream)
            .toList();
    }

    public static List<ItemStack> getItemContent(Function<RecipeCapability<?>, List<Content>> getter) {
        return getItemContent(getter.apply(ItemRecipeCapability.CAP));
    }

    private static List<FluidStack> getFluidContent(List<Content> contents) {
        return contents.stream()
            .map(content -> FluidRecipeCapability.CAP.of(content.getContent()).getStacks())
            .flatMap(Arrays::stream)
            .toList();
    }

    public static List<FluidStack> getFluidContent(Function<RecipeCapability<?>, List<Content>> getter) {
        return getFluidContent(getter.apply(FluidRecipeCapability.CAP));
    }
}
