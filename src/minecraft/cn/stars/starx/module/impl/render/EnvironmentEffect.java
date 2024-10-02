package cn.stars.starx.module.impl.render;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.Render3DEvent;
import cn.stars.starx.event.impl.UpdateEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.render.ColorUtil;
import cn.stars.starx.util.render.ThemeType;
import cn.stars.starx.util.render.ThemeUtil;
import cn.stars.starx.util.world.BlockUtil;
import com.logisticscraft.occlusionculling.util.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

@ModuleInfo(name = "EnvironmentEffect", description = "Add particles to environment",
        chineseDescription = "给环境添加粒子效果", category = Category.RENDER)
public class EnvironmentEffect extends Module {
    private final BoolValue fireflies = new BoolValue("Fireflies", this, false);
    private final NumberValue ffCount = new NumberValue("Fireflies Count",this, 30, 20, 100, 1);
    private final NumberValue ffSize = new NumberValue("Fireflies Size",this, 1f, 0.1f, 2f, 0.1f);
    private final ModeValue mode = new ModeValue("Mode", this, "Snowflake", "Off", "Snowflake", "Stars", "Hearts", "Dollars", "Bloom");
    private final NumberValue count = new NumberValue("Count",this, 30, 1, 100, 1);
    private final NumberValue size = new NumberValue("Size",this, 30, 1, 100, 1);
    private final BoolValue physics = new BoolValue("Physics", this, false);

    public static final ResourceLocation star = new ResourceLocation("starx/images/particle/star.png");
    public static final ResourceLocation heart = new ResourceLocation("starx/images/particle/heart.png");
    public static final ResourceLocation dollar = new ResourceLocation("starx/images/particle/dollar.png");
    public static final ResourceLocation snowflake = new ResourceLocation("starx/images/particle/snowflake.png");
    public static final ResourceLocation firefly = new ResourceLocation("starx/images/particle/firefly.png");

    @Override
    public void onUpdateAlwaysInGui() {
        ffCount.hidden = !fireflies.isEnabled();
        ffSize.hidden = !fireflies.isEnabled();
        count.hidden = mode.getMode().equals("Off");
        size.hidden = mode.getMode().equals("Off");
    }

    private final ArrayList<ParticleBase> fireFlies = new ArrayList<>();
    private final ArrayList<ParticleBase> particles = new ArrayList<>();

    @Override
    public void onUpdate(UpdateEvent event) {
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        fireFlies.removeIf(ParticleBase::tick);
        particles.removeIf(ParticleBase::tick);

    /*    for (int i = fireFlies.size(); i < ffcount.getValue(); i++) {
            if (FireFlies.getValue().isEnabled())
                fireFlies.add(new FireFly(
                        (float) (mc.thePlayer.posX + random(-25f, 25f)),
                        (float) (mc.thePlayer.posY + random(2f, 15f)),
                        (float) (mc.thePlayer.posZ + random(-25f, 25f)),
                        random(-0.2f, 0.2f),
                        random(-0.1f, 0.1f),
                        random(-0.2f, 0.2f)));
        } */

        for (int j = particles.size(); j < count.getValue(); j++) {
            boolean drop = physics.isEnabled();
            if (!mode.getMode().equals("Off")) {
                particles.add(new ParticleBase(
                        (float) (mc.thePlayer.posX + random(-48f, 48f)),
                        (float) (mc.thePlayer.posY + random(2, 48f)),
                        (float) (mc.thePlayer.posZ + random(-48f, 48f)),
                        drop ? 0 : random(-0.4f, 0.4f),
                        drop ? random(-0.2f, -0.05f) : random(-0.1f, 0.1f),
                        drop ? 0 : random(-0.4f, 0.4f)));
            }
        }
    /*    if (fireflies.isEnabled()) {
            GL11.glPushMatrix();

            // 绑定纹理
            mc.getTextureManager().bindTexture(firefly);

            // 启用混合模式
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

            // 启用深度测试，禁用深度写入
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer buffer = tessellator.getWorldRenderer();

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            fireFlies.forEach(p -> p.render(buffer));
            tessellator.draw();

            // 恢复深度写入
            GL11.glDepthMask(true);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_BLEND);

            GL11.glPopMatrix();
        } */

        if (!mode.getMode().equals("Off")) {
            GL11.glPushMatrix();

            // 启用混合模式
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

            // 启用深度测试，禁用深度写入
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer buffer = tessellator.getWorldRenderer();

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            particles.forEach(p -> p.render(buffer));
            tessellator.draw();

            // 恢复深度写入
            GL11.glDepthMask(true);

            // 改回默认的混合函数
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_BLEND);

            GL11.glPopMatrix();
        }
    }

    public class ParticleBase {

        protected float prevposX, prevposY, prevposZ, posX, posY, posZ, motionX, motionY, motionZ;
        protected int age, maxAge;

        public ParticleBase(float posX, float posY, float posZ, float motionX, float motionY, float motionZ) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            prevposX = posX;
            prevposY = posY;
            prevposZ = posZ;
            this.motionX = motionX;
            this.motionY = motionY;
            this.motionZ = motionZ;
            age = (int) random(100, 300);
            maxAge = age;
        }

        public boolean tick() {
            if (mc.thePlayer.getDistanceSq(posX, posY, posZ) > 8192) {
                StarX.showMsg(mc.thePlayer.getDistanceSq(posX, posY, posZ));
                age -= 8;
            }
            else age--;

            if (age < 0) {
                return true;
            }

            prevposX = posX;
            prevposY = posY;
            prevposZ = posZ;

            posX += motionX;
            posY += motionY;
            posZ += motionZ;

            motionX *= 0.9f;
            if (physics.isEnabled())
                motionY *= 0.9f;
            motionZ *= 0.9f;

            motionY -= 0.001f;

            return false;
        }

        public void render(WorldRenderer bufferBuilder) {
            // 绑定纹理
            switch (mode.getMode()) {
                case "Bloom":
                    Minecraft.getMinecraft().getTextureManager().bindTexture(firefly);
                    break;
                case "Snowflake":
                    Minecraft.getMinecraft().getTextureManager().bindTexture(snowflake);
                    break;
                case "Dollars":
                    Minecraft.getMinecraft().getTextureManager().bindTexture(dollar);
                    break;
                case "Hearts":
                    Minecraft.getMinecraft().getTextureManager().bindTexture(heart);
                    break;
                case "Stars":
                    Minecraft.getMinecraft().getTextureManager().bindTexture(star);
                    break;
            }

            // 获取摄像机
            Entity camera = Minecraft.getMinecraft().getRenderViewEntity();
            float partialTicks = Minecraft.getMinecraft().timer.renderPartialTicks;
            double cameraX = camera.prevPosX + (camera.posX - camera.prevPosX) * partialTicks;
            double cameraY = camera.prevPosY + (camera.posY - camera.prevPosY) * partialTicks;
            double cameraZ = camera.prevPosZ + (camera.posZ - camera.prevPosZ) * partialTicks;

            // 计算位置
            Vec3d pos = BlockUtil.interpolatePos(prevposX, prevposY, prevposZ, posX, posY, posZ);

            // 保存当前矩阵状态
            GlStateManager.pushMatrix();

            // 平移和旋转
            GlStateManager.rotate(camera.rotationPitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(camera.rotationYaw + 180f, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(pos.x - cameraX, pos.y - cameraY, pos.z - cameraZ);
            GlStateManager.rotate(-camera.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(camera.rotationPitch, 1.0F, 0.0F, 0.0F);

            // 获取颜色
            Color color1 = ThemeUtil.getThemeColor(ThemeType.ARRAYLIST);
            int rgbaColor = ColorUtil.withAlpha(color1, (int) (255 * ((float) age / (float) maxAge))).getRGB();

            bufferBuilder.pos(0, -size.getValue(), 0).tex(0f, 1f).color((rgbaColor >> 16) & 0xFF, (rgbaColor >> 8) & 0xFF, rgbaColor & 0xFF, (rgbaColor >> 24) & 0xFF).endVertex();
            bufferBuilder.pos(-size.getValue(), -size.getValue(), 0).tex(1f, 1f).color((rgbaColor >> 16) & 0xFF, (rgbaColor >> 8) & 0xFF, rgbaColor & 0xFF, (rgbaColor >> 24) & 0xFF).endVertex();
            bufferBuilder.pos(-size.getValue(), 0, 0).tex(1f, 0).color((rgbaColor >> 16) & 0xFF, (rgbaColor >> 8) & 0xFF, rgbaColor & 0xFF, (rgbaColor >> 24) & 0xFF).endVertex();
            bufferBuilder.pos(0, 0, 0).tex(0, 0).color((rgbaColor >> 16) & 0xFF, (rgbaColor >> 8) & 0xFF, rgbaColor & 0xFF, (rgbaColor >> 24) & 0xFF).endVertex();

            // 恢复之前的矩阵状态
            GlStateManager.popMatrix();
        }
    }

    public static float random(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }
}
