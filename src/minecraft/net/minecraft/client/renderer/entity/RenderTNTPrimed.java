package net.minecraft.client.renderer.entity;

import cn.stars.reversal.module.impl.render.TNTTimer;
import cn.stars.reversal.util.misc.ModuleInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;

public class RenderTNTPrimed extends Render<EntityTNTPrimed>
{
    private static DecimalFormat timeFormatter = new DecimalFormat("0.00");
    public RenderTNTPrimed(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    public void doRender(EntityTNTPrimed entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + 0.5F, (float)z);

        if ((float)entity.fuse - partialTicks + 1.0F < 10.0F)
        {
            float f = 1.0F - ((float)entity.fuse - partialTicks + 1.0F) / 10.0F;
            f = MathHelper.clamp_float(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            GlStateManager.scale(f1, f1, f1);
        }

        float f2 = (1.0F - ((float)entity.fuse - partialTicks + 1.0F) / 100.0F) * 0.8F;
        this.bindEntityTexture(entity);
        GlStateManager.translate(-0.5F, -0.5F, 0.5F);
        blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), entity.getBrightness(partialTicks));
        GlStateManager.translate(0.0F, 0.0F, 1.0F);

        if (entity.fuse / 5 % 2 == 0)
        {
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0F, 1.0F, 1.0F, f2);
            GlStateManager.doPolygonOffset(-3.0F, -3.0F);
            GlStateManager.enablePolygonOffset();
            blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0F);
            GlStateManager.doPolygonOffset(0.0F, 0.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }

        GlStateManager.popMatrix();

        if (ModuleInstance.getModule(TNTTimer.class).isEnabled()) {
            int fuseTimer = entity.fuse;

            if (fuseTimer >= 1) {
                double distance = entity.getDistanceSqToEntity(getRenderManager().livingPlayer);

                if (distance <= 4096.0D) {
                    float number = ((float) fuseTimer - partialTicks) / 20.0F;
                    String time = timeFormatter.format((double) number);
                    FontRenderer fontrenderer = getFontRendererFromRenderManager();

                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float) x + 0.0F, (float) y + entity.height + 0.5F, (float) z);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                    byte xMultiplier = 1;

                    if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
                        xMultiplier = -1;
                    }

                    float scale = 0.02666667F;

                    GlStateManager.rotate(getRenderManager().playerViewX * (float) xMultiplier, 1.0F, 0.0F, 0.0F);
                    GlStateManager.scale(-scale, -scale, scale);
                    GlStateManager.disableLighting();
                    GlStateManager.depthMask(false);
                    GlStateManager.disableDepth();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    int stringWidth = fontrenderer.getStringWidth(time) >> 1;
                    float green = Math.min((float) fuseTimer / (80.0F), 1.0F);
                    Color color = new Color(1.0F - green, green, 0.0F);

                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.disableTexture2D();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldrenderer.pos((double) (-stringWidth - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double) (-stringWidth - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double) (stringWidth + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((double) (stringWidth + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                    fontrenderer.drawString(time, -fontrenderer.getStringWidth(time) >> 1, 0, color.getRGB());
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.popMatrix();
                }

            }
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityTNTPrimed entity)
    {
        return TextureMap.locationBlocksTexture;
    }
}
