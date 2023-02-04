package cn.stars.addons.skinlayers3d;

import cn.stars.starx.util.misc.ModuleInstance;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.entity.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.*;
import com.google.common.collect.*;
import net.minecraft.item.*;
import net.minecraft.client.renderer.*;

public class HeadLayerFeatureRenderer implements LayerRenderer<AbstractClientPlayer>
{
    private final Set<Item> hideHeadLayers;
    private final boolean thinArms;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private final RenderPlayer playerRenderer;

    public HeadLayerFeatureRenderer(RenderPlayer playerRenderer) {
        this.hideHeadLayers = Sets.newHashSet(Items.skull);
        this.thinArms = ((PlayerEntityModelAccessor)playerRenderer).hasThinArms();
        this.playerRenderer = playerRenderer;
    }

    public void doRenderLayer(AbstractClientPlayer player, float nameFloat1, float nameFloat2, float nameFloat3, float partialTicks, float nameFloat5, float nameFloat6, float nameFloat7) {
        if (!player.hasSkin() || player.isInvisible() || !player.isWearing(EnumPlayerModelParts.HAT)) {
            return;
        }
        if (HeadLayerFeatureRenderer.mc.thePlayer.getPositionVector().squareDistanceTo(player.getPositionVector()) >
                ModuleInstance.getNumber("SkinLayers3D", "Level Of Detail Distance").getInt() * ModuleInstance.getNumber("SkinLayers3D", "Level Of Detail Distance").getInt()) {
            return;
        }
        final ItemStack itemStack = player.getEquipmentInSlot(1);
        if (itemStack != null && this.hideHeadLayers.contains(itemStack.getItem())) {
            return;
        }
        if (((PlayerSettings) player).getHeadLayers() == null && !this.setupModel(player, (PlayerSettings) player)) {
            return;
        }
        this.renderCustomHelmet((PlayerSettings) player, player);
    }

    private boolean setupModel(final AbstractClientPlayer abstractClientPlayerEntity, final PlayerSettings settings) {
        if (!SkinUtil.hasCustomSkin(abstractClientPlayerEntity)) {
            return false;
        }
        SkinUtil.setup3dLayers(abstractClientPlayerEntity, settings, this.thinArms, null);
        return true;
    }

    public void renderCustomHelmet(final PlayerSettings settings, final AbstractClientPlayer abstractClientPlayer) {
        if (settings.getHeadLayers() == null) {
            return;
        }
        if (this.playerRenderer.getMainModel().bipedHead.isHidden) {
            return;
        }
        float voxelSize = ModuleInstance.getNumber("SkinLayers3D", "Head Voxel Size").getFloat();
        GlStateManager.pushMatrix();
        if (abstractClientPlayer.isSneaking()) {
            GlStateManager.translate(0.0f, 0.2f, 0.0f);
        }
        this.playerRenderer.getMainModel().bipedHead.postRender(0.0625f);
        GlStateManager.scale(0.0625, 0.0625, 0.0625);
        GlStateManager.scale(voxelSize, voxelSize, voxelSize);
        boolean tintRed = abstractClientPlayer.hurtTime > 0 || abstractClientPlayer.deathTime > 0;
        settings.getHeadLayers().render(tintRed);
        GlStateManager.popMatrix();
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
