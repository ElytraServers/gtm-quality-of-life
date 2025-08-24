package cn.elytra.mod.gtmqol.mixins;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IAutoOutputFluid;
import com.gregtechceu.gtceu.common.machine.storage.DrumMachine;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(value = DrumMachine.class, remap = false)
public abstract class GT_DrumOutput extends MetaMachine implements IAutoOutputFluid {

    @Unique
    @Persisted
    protected boolean gtmqol$allowInputFromOutputSideFluids;

    public GT_DrumOutput(IMachineBlockEntity holder, boolean gtmqol$allowInputFromOutputSideFluids) {
        super(holder);
        this.gtmqol$allowInputFromOutputSideFluids = gtmqol$allowInputFromOutputSideFluids;
    }

    @Override
    public boolean isAllowInputFromOutputSideFluids() {
        return gtmqol$allowInputFromOutputSideFluids;
    }

    @Override
    public void setAllowInputFromOutputSideFluids(boolean allow) {
        gtmqol$allowInputFromOutputSideFluids = allow;
    }

    @Override
    protected @NotNull InteractionResult onSoftMalletClick(@NotNull Player playerIn, @NotNull InteractionHand hand,
        @NotNull Direction gridSide, @NotNull BlockHitResult hitResult) {
        if (!isRemote()) {
            if (!playerIn.isShiftKeyDown()) {
                setAllowInputFromOutputSideFluids(!isAllowInputFromOutputSideFluids());
                playerIn.sendSystemMessage(Component.translatable("gtceu.machine.basic.input_from_output_side." + (
                    isAllowInputFromOutputSideFluids()
                        ? "allow"
                        : "disallow")).append(Component.translatable("gtceu.creative.tank.fluid")));
            }
            return InteractionResult.SUCCESS;
        }
        return super.onSoftMalletClick(playerIn, hand, gridSide, hitResult);
    }

    @Inject(method = "sideTips", at = @At("RETURN"), cancellable = true)
    private void gtmqol$softMalletSideTips(Player player, BlockPos pos, BlockState state, Set<GTToolType> toolTypes,
        Direction side, CallbackInfoReturnable<ResourceTexture> cir) {
        if(toolTypes.contains(GTToolType.SOFT_MALLET)) {
            if(side == Direction.DOWN) {
                cir.setReturnValue(GuiTextures.TOOL_ALLOW_INPUT);
            }
        }
    }
}
