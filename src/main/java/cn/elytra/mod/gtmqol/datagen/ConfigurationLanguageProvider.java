package cn.elytra.mod.gtmqol.datagen;

import cn.elytra.mod.gtmqol.GregTechModernQualityOfLife;
import dev.toma.configuration.config.value.ConfigValue;
import dev.toma.configuration.config.value.ObjectValue;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static cn.elytra.mod.gtmqol.GregTechModernQualityOfLife.MOD_ID;

public class ConfigurationLanguageProvider extends LanguageProvider {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationLanguageProvider.class);

    public ConfigurationLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        iterate(GregTechModernQualityOfLife.configHolder.getValueMap(), new HashSet<>());
    }

    private void iterate(Map<String, ConfigValue<?>> configOptions, Set<String> addedKeys) {
        configOptions.forEach((id, configValue) -> {
            String text = StringUtils.capitalize(id);
            String langKey = "config.%s.option.%s".formatted(MOD_ID, id);
            if (addedKeys.add(langKey)) {
                add(langKey, text);
            } else {
                LOG.warn("Duplicated language key '{}' is found for {}", langKey, configValue.getId());
            }

            if(configValue instanceof ObjectValue objectValue) {
                iterate(objectValue.get(), addedKeys);
            }
        });
    }
}
