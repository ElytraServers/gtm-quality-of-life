package cn.elytra.mod.gtmqol.client.utils;

import cn.elytra.mod.gtmqol.util.CapabilityProviderBuilder;
import cn.elytra.mod.gtmqol.common.Data;
import cn.elytra.mod.gtmqol.common.caps.QuantumTankFluidHandlerItemStack;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ClientOnlyCapability {

    private static final List<Function<ItemStack, ICapabilityProvider>> CAP_PROVIDERS = new ArrayList<>();

    public static void register(Function<ItemStack, ICapabilityProvider> provider) {
        CAP_PROVIDERS.add(provider);
    }

    public static <T> LazyOptional<T> getCapability(Capability<T> capability, ItemStack itemStack) {
        for (Function<ItemStack, ICapabilityProvider> providerFunction : CAP_PROVIDERS) {
            ICapabilityProvider provider = providerFunction.apply(itemStack);
            if (provider == null) continue;
            LazyOptional<T> cap = provider.getCapability(capability);
            if (cap.isPresent()) return cap.cast();
        }
        return LazyOptional.empty();
    }

    static {
        // quantum tanks
        register(i -> {
            if (i.getItem() instanceof MetaMachineItem metaMachineItem && Data.QUANTUM_TANK_CAPACITY.containsKey(metaMachineItem.getDefinition())) {
                return CapabilityProviderBuilder.create().withValue(ForgeCapabilities.FLUID_HANDLER_ITEM, new QuantumTankFluidHandlerItemStack(i)).build();
            }
            return null;
        });
    }
}
