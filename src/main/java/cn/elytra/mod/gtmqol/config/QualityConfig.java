package cn.elytra.mod.gtmqol.config;

import cn.elytra.mod.gtmqol.client.item_decorator.FluidHandlerItemDecorator;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class QualityConfig {

    private static final Logger LOG = LoggerFactory.getLogger(QualityConfig.class);

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue USE_DRUM_ITEM_DECORATOR = BUILDER.define(
        "drum_item_decorator.enabled",
        true);

    private static final ForgeConfigSpec.IntValue DRUM_ITEM_DECORATOR_MAX_TYPE = BUILDER.defineInRange("drum_item_decorator.max_type",
        1,
        0,
        4);

    private static final ForgeConfigSpec.BooleanValue DRUM_ITEM_DECORATOR_RENDER_ON_TOP_ITEM = BUILDER.define(
        "drum_item_decorator.render_on_top_item",
        true);

    public static ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(ModConfigEvent event) {
        LOG.info("Loading Configurations");

        FluidHandlerItemDecorator.INSTANCE.enabled = USE_DRUM_ITEM_DECORATOR.get();
        FluidHandlerItemDecorator.INSTANCE.maxTankCountToRender = DRUM_ITEM_DECORATOR_MAX_TYPE.get();
        FluidHandlerItemDecorator.INSTANCE.renderOnTopOfItem = DRUM_ITEM_DECORATOR_RENDER_ON_TOP_ITEM.get();
    }

}
