package cn.elytra.mod.gtmqol.util;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class CapabilityProviderBuilder {
    public static CapabilityProviderBuilder create() {
        return new CapabilityProviderBuilder();
    }

    private final Reference2ObjectMap<Capability<?>, Supplier<?>> holder = new Reference2ObjectOpenHashMap<>();

    public <T> CapabilityProviderBuilder with(Capability<T> capability, Supplier<? extends T> supplier) {
        holder.put(capability, supplier);
        return this;
    }

    public <T> CapabilityProviderBuilder withValue(Capability<T> capability, T value) {
        return with(capability, () -> value);
    }

    public ICapabilityProvider build() {
        return new Impl(holder);
    }

    private record Impl(Map<Capability<?>, Supplier<?>> holder) implements ICapabilityProvider {

        @SuppressWarnings("unchecked")
        private <T> LazyOptional<T> getAndCast(Capability<T> capability, Supplier<? /* extends T */> supplier) {
            return capability.orEmpty(capability, LazyOptional.of(() -> /* checked */ (T) supplier.get()));
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
            Supplier<?> capSupplier = holder.get(capability);
            if (capSupplier == null) return LazyOptional.empty();
            return getAndCast(capability, capSupplier);
        }

        @Override
        public @NotNull String toString() {
            return "CapabilityProviderBuilder$build{ " + holder.size() + " capabilities }";
        }

        @Override
        public int hashCode() {
            return holder.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Impl other && holder.equals(other.holder);
        }
    }
}
