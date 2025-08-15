package cn.elytra.mod.gtmqol;

import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(GregTechModernQualityOfLife.MOD_ID)
public class GregTechModernQualityOfLife {

    public static final String MOD_ID = "gtmqol";

    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    public GregTechModernQualityOfLife() {
        LOG.info("Quality!");
    }

}
