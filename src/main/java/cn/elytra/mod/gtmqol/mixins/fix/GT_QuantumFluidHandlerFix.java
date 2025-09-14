package cn.elytra.mod.gtmqol.mixins.fix;

import com.gregtechceu.gtceu.api.misc.forge.QuantumFluidHandlerItemStack;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(
    require = {
        // this bug has been fixed in 7.2.0-snapshot by ME :)
        // but we need to handle this for the latest release.
        // see https://github.com/GregTechCEu/GregTech-Modern/pull/3715
        @Condition(value = "gtceu", versionPredicates = ">=7.1.0 <7.2.0")
    }
)
@Mixin(value = QuantumFluidHandlerItemStack.class, remap = false)
public class GT_QuantumFluidHandlerFix {

    @WrapWithCondition(method = "getFluid", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraftforge/fluids/FluidStack;setAmount(I)V"))
    private boolean gtmqol$fixSetAmountToEmptyFluidStack(FluidStack instance, int amount) {
        return !instance.isEmpty();
    }

}
