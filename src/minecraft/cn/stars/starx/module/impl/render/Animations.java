package cn.stars.starx.module.impl.render;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.PreMotionEvent;
import cn.stars.starx.event.impl.UpdateEvent;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "Animations", description = "Display special swing animations.", category = Category.RENDER)
public class Animations extends Module {
    private final NumberValue itemPositionY = new NumberValue("Item Position Y", this, 0.3, 0, 2, 0.1);
    private final BoolValue swordSwing = new BoolValue("Sword Swing", this, true);
    private final ModeValue swordMode = new ModeValue("Sword Mode", this, "1.7",
            "None" ,"1.7", "Smooth", "Stab", "Spin", "Leaked", "Old", "Exhibition", "Wood", "Swong", "Chill",
            "Komorebi", "Rhys", "Allah", "SlideDown", "Liquid");
    private final BoolValue foodSwing = new BoolValue("Food Swing", this, true);
    private final BoolValue potionSwing = new BoolValue("Drink Swing", this, true);
    private final BoolValue bowSwing = new BoolValue("Bow Swing", this, true);
    private final BoolValue interactWhileSwing = new BoolValue("Interact While Swing", this, false);
    private final BoolValue swingAnim = new BoolValue("Swing Animation", this, false);
    private final BoolValue anythingBlock = new BoolValue("Anything Block", this, false);
    private final BoolValue customHitColor = new BoolValue("Custom Hit Color",this , false);

    private final NumberValue chcRed = new NumberValue("Hit Color Red", this, 255, 0, 255, 1);
    private final NumberValue chcGreen = new NumberValue("Hit Color Green", this, 0, 0, 255, 1);
    private final NumberValue chcBlue = new NumberValue("Hit Color Blue", this, 0, 0, 255, 1);
    private final NumberValue chcAlpha = new NumberValue("Hit Color Alpha", this, 100, 0, 255, 1);

    @Override
    public void onUpdateAlwaysInGui() {
        swordMode.hidden = !swordSwing.isEnabled();
        chcRed.hidden = !customHitColor.isEnabled();
        chcGreen.hidden = !customHitColor.isEnabled();
        chcBlue.hidden = !customHitColor.isEnabled();
        chcAlpha.hidden = !customHitColor.isEnabled();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        RendererLivingEntity.dmgRed = (float) chcRed.getValue() / 255;
        RendererLivingEntity.dmgGreen = (float) chcGreen.getValue() / 255;
        RendererLivingEntity.dmgBlue = (float) chcBlue.getValue() / 255;
        RendererLivingEntity.dmgAlpha = (float) chcAlpha.getValue() / 255;
    }

}
