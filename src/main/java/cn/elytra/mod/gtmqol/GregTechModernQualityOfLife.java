package cn.elytra.mod.gtmqol;

import cn.elytra.mod.gtmqol.config.QualityConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(GregTechModernQualityOfLife.MOD_ID)
public class GregTechModernQualityOfLife {

    public static final String MOD_ID = "gtmqol";

    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    public GregTechModernQualityOfLife() {
        LOG.info("Quality!");

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, QualityConfig.SPEC);
    }

}
