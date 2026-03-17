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
            @Configurable
            @Configurable.Comment("(Brace Expansion supported)")
            public String[] tankContainers = {
                // @formatter:off
                "gtceu:{steel,aluminium,stainless_steel,titanium,tungsten_steel}_fluid_cell",
                "gtceu:glass_vial",
                "gtceu:{lv,mv,hv,ev}_super_tank",
                "gtceu:{iv,luv,zpm,uv,uhv,uev,uiv,uxv,opv}_quantum_tank",
                "gtceu:{wood,bronze,steel,aluminium,stainless_steel,gold,titanium,tungsten_steel}_drum",
                // @formatter:on
            };
        }

        public static class RecipeDataContent {

            @Configurable
            public boolean renderRecipeDataAtCorner = true;
            @Configurable
            @Configurable.Comment("(Brace Expansion supported)")
            public String[] recipeDataContainers = {
                "gtceu:data_{stick,orb,module}"
            };
        }

        public static class WaferRecipeLens {

            @Configurable
            public boolean renderWaferRecipeLens = true;
            @Configurable
            @Configurable.Comment("(Brace Expansion supported)")
            public String[] waferRecipeLensExtraLensItems = {};
            @Configurable
            @Configurable.Comment("(Brace Expansion supported)")
            public String[] waferRecipeLensExtraWaferItems = {};
        }

        @Configurable
        public TankContent tankContent = new TankContent();
        @Configurable
        public RecipeDataContent recipeDataContent = new RecipeDataContent();
        @Configurable
        public WaferRecipeLens waferRecipeLens = new WaferRecipeLens();
    }

    @Configurable
    public ItemDecorator itemDecorator = new ItemDecorator();

    public static QualityConfig get() {
        return GregTechModernQualityOfLife.config;
    }

}
