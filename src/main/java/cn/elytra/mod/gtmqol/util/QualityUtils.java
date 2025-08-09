package cn.elytra.mod.gtmqol.util;

import cn.elytra.mod.gtmqol.GregTechModernQualityOfLife;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

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
        return MarkerFactory.getMarker(clazz.getCanonicalName());
    }
}
