package cn.stars.starx.module.impl.render;

import cn.stars.starx.event.impl.Render3DEvent;
import cn.stars.starx.event.impl.UpdateEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.RenderUtils;
import cn.stars.starx.util.render.ThemeType;
import cn.stars.starx.util.render.ThemeUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.CopyOnWriteArrayList;


@ModuleInfo(name = "JumpCircle", chineseName = "跳跃圆圈", description = "Draw a circle when you jump", chineseDescription = "在你跳跃时画圆圈", category = Category.RENDER)
public class JumpCircle extends Module {
    private final NumberValue radiusValue = new NumberValue("Radius", this, 3, 1, 5, 1);
    private final NumberValue widthValue = new NumberValue("Width", this, 0.5F, 0.1F, 50F, 1);
    private final NumberValue strengthValue = new NumberValue("Strength", this, 0.02F, 0.01F, 0.2F, 0.01f);
    private final CopyOnWriteArrayList<Circle> circles = new CopyOnWriteArrayList<>();
    private boolean lastOnGround;

    @Override
    public void onEnable(){
        lastOnGround = true;
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (mc.thePlayer.onGround && !lastOnGround) {
            circles.add(new Circle(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ, (float) widthValue.getValue()));
        }
        lastOnGround = mc.thePlayer.onGround;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (!circles.isEmpty()) {
            for (Circle circle : circles) {
                if (circle.add(strengthValue.getValue()) > radiusValue.getValue()) {
                    circles.remove(circle);
                    continue;
                }
                GL11.glPushMatrix();
                GL11.glTranslated(
                        circle.posX - mc.getRenderManager().renderPosX,
                        circle.posY - mc.getRenderManager().renderPosY,
                        circle.posZ - mc.getRenderManager().renderPosZ
                );
                GlStateManager.enableBlend();
                GlStateManager.enableLineSmooth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableDepth();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                GL11.glLineWidth(circle.width / 2f);
                GL11.glRotatef(90F, 1F, 0F, 0F);

                GL11.glBegin(GL11.GL_LINE_STRIP);

                for (int i = 0; i <= 360; i += 5) { // You can change circle accuracy  (60 - accuracy)
                    RenderUtils.color(ThemeUtil.getThemeColor(ThemeType.ARRAYLIST, i / 5f));
                    GL11.glVertex2f(
                            (float) (Math.cos(i * Math.PI / 180.0) * circle.radius),
                            (float) (Math.sin(i * Math.PI / 180.0) * circle.radius)
                    );
                }

                GL11.glEnd();

                GlStateManager.disableBlend();
                GlStateManager.enableTexture2D();
                GlStateManager.enableDepth();
                GlStateManager.disableLineSmooth();
                GL11.glPopMatrix();
            }
        }
    }

    static class Circle {
        public double posX, posY, posZ, lastTickPosX, lastTickPosY, lastTickPosZ;
        public float radius, width;
        Circle(double posX, double posY, double posZ, double lastTickPosX, double lastTickPosY, double lastTickPosZ, float width) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.lastTickPosX = lastTickPosX;
            this.lastTickPosY = lastTickPosY;
            this.lastTickPosZ = lastTickPosZ;
            this.width = width;
        }
        public double add(double radius) {
            this.radius += radius;
            return this.radius;
        }
    }
}