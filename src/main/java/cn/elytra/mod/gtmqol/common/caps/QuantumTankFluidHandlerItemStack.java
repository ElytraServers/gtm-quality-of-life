package cn.elytra.mod.gtmqol.common.caps;

import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.common.machine.storage.QuantumTankMachine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

/// A special [IFluidHandlerItem] implementation for [QuantumTankMachine].
public final class QuantumTankFluidHandlerItemStack implements IFluidHandlerItem {

    public static final String TAG_STORED = "stored";
    public static final String TAG_AMOUNT = "storedAmount";
    public static final String TAG_CAPACITY = "maxAmount";

    public final ItemStack container;

    public QuantumTankFluidHandlerItemStack(ItemStack container) {
        this.container = container;
    }

    public void setFluid(FluidStack fluidStack) {
        CompoundTag tag = this.container.getOrCreateTag();
        tag.put(TAG_STORED, fluidStack.writeToNBT(new CompoundTag()));
        tag.putLong(TAG_AMOUNT, fluidStack.getAmount());
    }

    @Override
    public @NotNull ItemStack getContainer() {
        return this.container;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        CompoundTag tag = this.container.getTag();
        if (tag == null) return FluidStack.EMPTY;
        FluidStack stored = FluidStack.loadFluidStackFromNBT(tag.getCompound(TAG_STORED));
        if (stored.isEmpty()) return FluidStack.EMPTY;
        stored.setAmount(Ints.saturatedCast(tag.getLong(TAG_AMOUNT)));
        return stored;
    }

    @Override
    public int getTankCapacity(int i) {
        CompoundTag tag = this.container.getTag();
        if (tag == null) return 0;
        return Ints.saturatedCast(tag.getLong(TAG_CAPACITY));
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return true;
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        if (this.container.getCount() == 1 && !fluidStack.isEmpty()) {
            FluidStack contained = this.getFluidInTank(0);
            if (contained.isEmpty()) {
                int fillAmount = Math.min(this.getTankCapacity(0), fluidStack.getAmount());
                if (fluidAction.execute()) {
                    FluidStack filled = fluidStack.copy();
                    filled.setAmount(fillAmount);
                    this.setFluid(filled);
                }
                return fillAmount;
            } else if (contained.isFluidEqual(fluidStack)) {
                int fillAmount = Math.min(this.getTankCapacity(0) - contained.getAmount(), fluidStack.getAmount());
                if (fluidAction.execute() && fillAmount > 0) {
                    contained.grow(fillAmount);
                    this.setFluid(contained);
                }
                return fillAmount;
            }
        }
        return 0;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return this.container.getCount() == 1
                && !fluidStack.isEmpty()
                && fluidStack.isFluidEqual(this.getFluidInTank(0))
                ? this.drain(fluidStack.getAmount(), fluidAction)
                : FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
        if (this.container.getCount() == 1 && i > 0) {
            FluidStack contained = getFluidInTank(0);
            if (!contained.isEmpty()) {
                int drainAmount = Math.min(contained.getAmount(), i);
                FluidStack drained = contained.copy();
                drained.setAmount(drainAmount);
                if (fluidAction.execute()) {
                    contained.shrink(drainAmount);
                    if (contained.isEmpty()) {
                        this.setFluid(FluidStack.EMPTY);
                    } else {
                        this.setFluid(contained);
                    }
                }
                return drained;
            }
        }
        return FluidStack.EMPTY;
    }
}
