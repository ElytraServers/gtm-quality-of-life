package cn.elytra.mod.gtmqol.util;

import cn.elytra.mod.gtmqol.GregTechModernQualityOfLife;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.stream.Stream;

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
            return Stream.ofNullable(getItemByKey(new ResourceLocation(value)));
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

    public static @Nullable Item getItemByKey(ResourceLocation rl) {
        return ForgeRegistries.ITEMS.getValue(rl);
    }

    public static @Nullable Item getItemByKey(String identifier) {
        return getItemByKey(new ResourceLocation(identifier));
    }
}
