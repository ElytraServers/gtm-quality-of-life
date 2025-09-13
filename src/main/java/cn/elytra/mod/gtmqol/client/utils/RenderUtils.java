package cn.elytra.mod.gtmqol.client.utils;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class RenderUtils {

    private RenderUtils() {
    }

    public static void renderItem(GuiGraphics guiGraphics, ItemStack itemStack, int x, int y, float width,
        float height) {
        renderItem(guiGraphics, null, null, itemStack, x, y, 0, width, height, 0);
    }

    /**
     * Enhanced {@link GuiGraphics#renderItem(LivingEntity, Level, ItemStack, int, int, int, int)} where you can assign
     * the width and height of the rendered item.
     */
    public static void renderItem(GuiGraphics guiGraphics, @Nullable LivingEntity livingEntity, @Nullable Level level,
        ItemStack itemStack, int x, int y, int seed, float width, float height, int guiOffset) {
        if (itemStack.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        BakedModel bakedModel = mc.getItemRenderer().getModel(itemStack, level, livingEntity, seed);
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(
            (float) x + (width / 2),
            (float) y + (height / 2),
            (float) (/*150*/ 250 + (bakedModel.isGui3d() ? guiOffset : 0)));

        try {
            pose.mulPoseMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
            pose.scale(width, height, 16.0F);
            boolean flag = !bakedModel.usesBlockLight();
            if (flag) {
                Lighting.setupForFlatItems();
            }
            mc.getItemRenderer().render(
                itemStack,
                ItemDisplayContext.GUI,
                false,
                pose,
                guiGraphics.bufferSource(),
                15728880,
                OverlayTexture.NO_OVERLAY,
                bakedModel);
            guiGraphics.flush();
            if (flag) {
                Lighting.setupFor3DItems();
            }
        } catch (Throwable e) {
            CrashReport crash = CrashReport.forThrowable(e, "Rendering item");
            CrashReportCategory category = crash.addCategory("Item being rendered");
            category.setDetail("Item Type", () -> String.valueOf(itemStack.getItem()));
            category.setDetail(
                "Registry Name",
                () -> String.valueOf(ForgeRegistries.ITEMS.getKey(itemStack.getItem())));
            category.setDetail("Item Damage", () -> String.valueOf(itemStack.getDamageValue()));
            category.setDetail("Item NBT", () -> String.valueOf(itemStack.getTag()));
            category.setDetail("Item Foil", () -> String.valueOf(itemStack.hasFoil()));
            throw new ReportedException(crash);
        }

        pose.popPose();
    }

}
