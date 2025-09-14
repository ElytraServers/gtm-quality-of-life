package cn.elytra.mod.gtmqol.config;

import cn.elytra.mod.gtmqol.client.item_decorator.DataStickItemDecorator;
import cn.elytra.mod.gtmqol.client.item_decorator.FluidHandlerContentItemDecorator;
import cn.elytra.mod.gtmqol.client.item_decorator.FluidHandlerUsageBarItemDecorator;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class QualityConfig {

    private static final Logger LOG = LoggerFactory.getLogger(QualityConfig.class);

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final BooleanValue USE_DRUM_ITEM_DECORATOR = BUILDER.define(
        "drum_item_decorator.enabled",
        true);

    private static final ForgeConfigSpec.IntValue DRUM_ITEM_DECORATOR_MAX_TYPE = BUILDER.defineInRange("drum_item_decorator.max_type",
        1,
        0,
        4);

    private static final BooleanValue DRUM_ITEM_DECORATOR_RENDER_ON_TOP_ITEM = BUILDER.define(
        "drum_item_decorator.render_on_top_item",
        true);

    private static final BooleanValue USE_DATA_STICK_LIKE_ITEM_DECORATOR = BUILDER.define(
        "data_stick_decorator.enabled",
        true);

    private static final BooleanValue USE_TANK_USAGE_DURA_BAR = BUILDER.define(
        "tank_usage_dura_bar.enabled",
        true);

    private static final BooleanValue TANK_USAGE_DURA_BAR_SHOW_ON_EMPTY = BUILDER.define(
        "tank_usage_dura_bar.show_on_empty",
        false);

    private static final BooleanValue TANK_USAGE_DURA_BAR_SHOW_ON_FULL = BUILDER.define(
        "tank_usage_dura_bar.show_on_full",
        false);

    public static ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(ModConfigEvent event) {
        LOG.info("Loading Configurations");

        FluidHandlerContentItemDecorator.INSTANCE.enabled = USE_DRUM_ITEM_DECORATOR.get();
        FluidHandlerContentItemDecorator.INSTANCE.maxTankCountToRender = DRUM_ITEM_DECORATOR_MAX_TYPE.get();
        FluidHandlerContentItemDecorator.INSTANCE.renderOnTopOfItem = DRUM_ITEM_DECORATOR_RENDER_ON_TOP_ITEM.get();

        DataStickItemDecorator.INSTANCE.enabled = USE_DATA_STICK_LIKE_ITEM_DECORATOR.get();

        FluidHandlerUsageBarItemDecorator.INSTANCE.enabled = USE_TANK_USAGE_DURA_BAR.get();
        FluidHandlerUsageBarItemDecorator.INSTANCE.showEmptyBar = TANK_USAGE_DURA_BAR_SHOW_ON_EMPTY.get();
        FluidHandlerUsageBarItemDecorator.INSTANCE.showFullBar = TANK_USAGE_DURA_BAR_SHOW_ON_FULL.get();
    }

}
