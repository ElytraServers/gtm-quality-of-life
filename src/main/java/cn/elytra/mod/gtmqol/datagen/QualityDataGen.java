package cn.elytra.mod.gtmqol.datagen;

import cn.elytra.mod.gtmqol.GregTechModernQualityOfLife;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GregTechModernQualityOfLife.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QualityDataGen {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(
            event.includeClient(),
            new ConfigurationLanguageProvider(packOutput, GregTechModernQualityOfLife.MOD_ID, "en_us"));
    }

}
