package cn.elytra.mod.gtmqol;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(GregTechModernQualityOfLife.MOD_ID)
public class GregTechModernQualityOfLife {

    public static final String MOD_ID = "gtmqol";

    public GregTechModernQualityOfLife() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();

        IEventBus bus = context.getModEventBus();
        Registrar.init(bus);
    }

    public static class Registrar {

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

        public static void init(IEventBus bus) {
            ITEMS.register(bus);
        }

    }

}
