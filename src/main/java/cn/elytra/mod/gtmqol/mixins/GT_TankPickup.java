package cn.elytra.mod.gtmqol.mixins;

import com.gregtechceu.gtceu.common.machine.storage.DrumMachine;
import com.gregtechceu.gtceu.common.machine.storage.QuantumTankMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { DrumMachine.class, QuantumTankMachine.class }, remap = false)
public class GT_TankPickup {

    @Inject(method = "savePickClone", at = @At("RETURN"), cancellable = true)
    private void gtmqol$overrideSavePickClone(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

}
