package cn.elytra.mod.gtmqol.common;

import cn.elytra.mod.gtmqol.GregTechModernQualityOfLife;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import it.unimi.dsi.fastutil.objects.Reference2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Mod.EventBusSubscriber(modid = GregTechModernQualityOfLife.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class Data {

    // MachineDefinition -> Capacity
    public static Reference2LongMap<MachineDefinition> QUANTUM_TANK_CAPACITY = new Reference2LongArrayMap<>();

    public static void putQuantumTankCapacity(MetaMachine machine, long capacity) {
        QUANTUM_TANK_CAPACITY.put(machine.getDefinition(), capacity);
    }

    // this part is commented out because I don't want to modify the server behavior now.
    // the code will probably used for another mod, or we make gtmqol a 2-side mod (client-only for now)
//    @SubscribeEvent
//    static void registerItemStackCaps(AttachCapabilitiesEvent<ItemStack> event) {
//        ItemStack itemStack = event.getObject();
//        if (itemStack.getItem() instanceof MetaMachineItem metaMachineItem) {
//            if (QUANTUM_TANK_CAPACITY.containsKey(metaMachineItem.getDefinition())) {
//                event.addCapability(
//                        GTCEu.id("fluid"),
//                        CapabilityProviderBuilder.create()
//                                .with(ForgeCapabilities.FLUID_HANDLER_ITEM, () -> new QuantumTankFluidHandlerItemStack(itemStack))
//                                .build());
//            }
//        }
//    }

}
