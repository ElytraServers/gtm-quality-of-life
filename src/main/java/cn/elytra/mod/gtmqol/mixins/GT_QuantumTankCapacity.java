package cn.elytra.mod.gtmqol.mixins;

import cn.elytra.mod.gtmqol.common.Data;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.storage.QuantumTankMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = QuantumTankMachine.class, remap = false)
public class GT_QuantumTankCapacity {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gtmqol$storeQuantumTankCapacity(IMachineBlockEntity holder, int tier, long maxAmount, Object[] args, CallbackInfo ci) {
        Data.putQuantumTankCapacity((QuantumTankMachine) (Object) this, maxAmount);
    }

}
