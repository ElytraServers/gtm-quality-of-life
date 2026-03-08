package cn.elytra.mod.gtmqol.mixins;

import com.gregtechceu.gtceu.api.item.component.IDurabilityBar;
import com.gregtechceu.gtceu.client.renderer.item.ToolChargeBarRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ToolChargeBarRenderer.class, remap = false)
public interface ToolChargeBarRendererAccessor {

    @Invoker("renderDurabilityBar")
    static boolean renderDurabilityBar(GuiGraphics graphics, ItemStack stack, IDurabilityBar manager, int xPosition, int yPosition) {
        throw new AssertionError();
    }

}
