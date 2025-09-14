package cn.elytra.mod.gtmqol.config;

import cn.elytra.mod.gtmqol.GregTechModernQualityOfLife;
import cn.elytra.mod.gtmqol.client.item_decorator.FluidHandlerUsageBarItemDecorator;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;

@Config(id = GregTechModernQualityOfLife.MOD_ID)
public class QualityConfig {

    public static class ItemDecorator {

        public static class TankContent {

            @Configurable
            public boolean renderContentAtCorner = false;
            @Configurable
            @Configurable.Range(min = 1, max = 4)
            @Configurable.Gui.Slider
            public int renderContentAtCornerMaxType = 1;
            @Configurable
            public boolean renderContentAtCornerOnTopOfItem = true;
            @Configurable
            public boolean renderContentDurabilityBar = true;
            @Configurable
            public boolean renderContentDurabilityBarAtEmpty = false;
            @Configurable
            public boolean renderContentDurabilityBarAtFull = true;
            @Configurable
            public FluidHandlerUsageBarItemDecorator.MultiTankStrategy renderContentDurabilityBarMultiTankStrategy = FluidHandlerUsageBarItemDecorator.MultiTankStrategy.COUNT_TOTAL;
        }

        public static class RecipeDataContent {

            @Configurable
            public boolean renderRecipeDataAtCorner = true;
        }

        @Configurable
        public TankContent tankContent = new TankContent();
        @Configurable
        public RecipeDataContent recipeDataContent = new RecipeDataContent();
    }

    @Configurable
    public ItemDecorator itemDecorator = new ItemDecorator();

    public static QualityConfig get() {
        return GregTechModernQualityOfLife.config;
    }

}
