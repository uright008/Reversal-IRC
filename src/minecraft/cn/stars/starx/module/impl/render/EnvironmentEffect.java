package cn.stars.starx.module.impl.render;

import cn.stars.starx.event.impl.UpdateEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;

@ModuleInfo(name = "EnvironmentEffect", description = "Add particles to environment",
        chineseDescription = "给环境添加粒子效果", category = Category.RENDER)
public class EnvironmentEffect extends Module {
   /* private final BoolValue fireflies = new BoolValue("Fireflies", this, false);
    private final NumberValue ffCount = new NumberValue("Fireflies Count",this, 30, 20, 100, 1);
    private final NumberValue ffSize = new NumberValue("Fireflies Size",this, 1f, 0.1f, 2f, 0.1f);
    private final ModeValue mode = new ModeValue("Mode", this, "Snowflake", "Off", "Snowflake", "Stars", "Hearts", "Dollars", "Bloom");
    private final NumberValue count = new NumberValue("Count",this, 30, 20, 100, 1);
    private final NumberValue size = new NumberValue("Size",this, 30, 20, 100, 1);
    private final BoolValue physics = new BoolValue("Physics", this, false);

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
        fireFlies.removeIf(ParticleBase::tick);
        particles.removeIf(ParticleBase::tick);

        for (int i = fireFlies.size(); i < ffcount.getValue(); i++) {
            if (FireFlies.getValue().isEnabled())
                fireFlies.add(new FireFly(
                        (float) (mc.thePlayer.posX + MathUtility.random(-25f, 25f)),
                        (float) (mc.thePlayer.posY + MathUtility.random(2f, 15f)),
                        (float) (mc.thePlayer.posZ + MathUtility.random(-25f, 25f)),
                        MathUtility.random(-0.2f, 0.2f),
                        MathUtility.random(-0.1f, 0.1f),
                        MathUtility.random(-0.2f, 0.2f)));
        }

        for (int j = particles.size(); j < count.getValue(); j++) {
            boolean drop = physics.getValue() == Physics.Drop;
            if (mode.getValue() != Mode.Off)
                particles.add(new ParticleBase(
                        (float) (mc.thePlayer.posX + MathUtility.random(-48f, 48f)),
                        (float) (mc.thePlayer.posY + MathUtility.random(2, 48f)),
                        (float) (mc.thePlayer.posZ + MathUtility.random(-48f, 48f)),
                        drop ? 0 : MathUtility.random(-0.4f, 0.4f),
                        drop ? MathUtility.random(-0.2f, -0.05f) : MathUtility.random(-0.1f, 0.1f),
                        drop ? 0 : MathUtility.random(-0.4f, 0.4f)));
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
            age = (int) MathUtility.random(100, 300);
            maxAge = age;
        }

        public boolean tick() {
            if (mc.thePlayer.getDistanceSq(posX, posY, posZ) > 4096) age -= 8;
            else age--;

            if (age < 0)
                return true;

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
            switch (mode.getMode()) {
                case "Bloom" -> RenderSystem.setShaderTexture(0, firefly);
                case "Snowflake" -> RenderSystem.setShaderTexture(0, snowflake);
                case "Dollars" -> RenderSystem.setShaderTexture(0, dollar);
                case "Hearts" -> RenderSystem.setShaderTexture(0, heart);
                case "Stars" -> RenderSystem.setShaderTexture(0, star);
            }

            Camera camera = mc.gameRenderer.getCamera();
            Color color1 = lmode.getValue() == ColorMode.Sync ? HudEditor.getColor(age * 2) : color.getValue().getColorObject();
            Vec3d pos = Render3DEngine.interpolatePos(prevposX, prevposY, prevposZ, posX, posY, posZ);

            Matrix matrices = new MatrixStack();
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
            matrices.translate(pos.x, pos.y, pos.z);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

            Matrix4f matrix1 = matrices.peek().getPositionMatrix();

            bufferBuilder.(matrix1, 0, -size.getValue(), 0).texture(0f, 1f).color(Render2DEngine.injectAlpha(color1, (int) (255 * ((float) age / (float) maxAge))).getRGB()).next();
            bufferBuilder.vertex(matrix1, -size.getValue(), -size.getValue(), 0).texture(1f, 1f).color(Render2DEngine.injectAlpha(color1, (int) (255 * ((float) age / (float) maxAge))).getRGB()).next();
            bufferBuilder.vertex(matrix1, -size.getValue(), 0, 0).texture(1f, 0).color(Render2DEngine.injectAlpha(color1, (int) (255 * ((float) age / (float) maxAge))).getRGB()).next();
            bufferBuilder.vertex(matrix1, 0, 0, 0).texture(0, 0).color(Render2DEngine.injectAlpha(color1, (int) (255 * ((float) age / (float) maxAge))).getRGB()).next();
        }
    } */
}
