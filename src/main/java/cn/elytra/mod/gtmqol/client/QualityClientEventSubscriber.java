package cn.elytra.mod.gtmqol.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class QualityClientEventSubscriber {

    private static final Logger LOG = LoggerFactory.getLogger("QualityClientEventSubscriber");

}
