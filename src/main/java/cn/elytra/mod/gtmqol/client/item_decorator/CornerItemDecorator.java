package cn.elytra.mod.gtmqol.client.item_decorator;

import cn.elytra.mod.gtmqol.client.utils.RenderUtils;
import cn.elytra.mod.gtmqol.util.QualityUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;
import org.jetbrains.annotations.Nullable;

public abstract class CornerItemDecorator implements IItemDecorator {

    protected abstract @Nullable ItemStack getItemToRender(ItemStack containerItem);

    /**
     * Whether this renderer should work.
     *
     * @param shiftKeyPressed {@code true} when the Shift key is pressed
     * @return {@code true} to continue the rendering
     */
    protected boolean shouldRender(boolean shiftKeyPressed) {
        return true;
    }

    /**
     * The scaling of the item to be rendered, ranged from <code>(0.0, 1.0]</code>.
     */
    protected float getScaling() {
        return 0.75F;
    }

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack itemStack, int x, int y) {
        if(!shouldRender(Screen.hasShiftDown())) return false;

        ItemStack itemToRender = getItemToRender(itemStack);
        if (itemToRender == null) return false;

        try {
            float scaling = getScaling();
            if (scaling <= 0F || scaling > 1F) {
                QualityUtils.LOG.warn(
                    "Illegal scaling {} to render CornerItemDecorator, a float within range (0.0, 1.0] is required.",
                    scaling);
                scaling = 1.0F;
            }
            float size = 16 * scaling;
            RenderUtils.renderItem(guiGraphics, itemToRender, x + (16 - (int) size), y + (16 - (int) size), size, size);
        } catch (Exception e) {
            QualityUtils.LOG.warn("Exception occurred while rendering the CornerItemDecorator for {}", itemToRender);
        }

        return true;
    }
}
