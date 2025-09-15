package cn.elytra.mod.gtmqol;

import cn.elytra.mod.gtmqol.config.QualityConfig;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.config.format.ConfigFormats;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(GregTechModernQualityOfLife.MOD_ID)
public class GregTechModernQualityOfLife {

    public static final String MOD_ID = "gtmqol";

    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    public static QualityConfig config;
    // save this for config key generation
    public static ConfigHolder<QualityConfig> configHolder;

    public GregTechModernQualityOfLife() {
        LOG.info("Quality!");

        configHolder = Configuration.registerConfig(QualityConfig.class, ConfigFormats.json());
        config = configHolder.getConfigInstance();
    }

}
