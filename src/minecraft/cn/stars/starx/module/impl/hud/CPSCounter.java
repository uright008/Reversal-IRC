package cn.stars.starx.module.impl.hud;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.ClickEvent;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.event.impl.TickEvent;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.ThemeType;
import cn.stars.starx.util.render.ThemeUtil;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "CPSCounter", description = "Show your CPS on screen", category = Category.HUD)
public class CPSCounter extends Module {
    private final BoolValue rainbow = new BoolValue("Rainbow", this, false);
    private final BoolValue shadow = new BoolValue("Shadow", this, true);
    private final BoolValue outline = new BoolValue("Outline", this, true);
    public CPSCounter() {
        setCanBeEdited(true);
        setWidth(100);
        setHeight(20);
        this.Lclicks = new ArrayList<>();
        this.Rclicks = new ArrayList<>();
    }
    private final List<Long> Lclicks;
    private final List<Long> Rclicks;

    @Override
    public void onRender2D(Render2DEvent event) {
        if (!getModule("HUD").isEnabled()) return;
        TTFFontRenderer psm = CustomFont.FONT_MANAGER.getFont("PSM 18");
        TTFFontRenderer icon = CustomFont.FONT_MANAGER.getFont("Mi 24");
        String cpsString = Lclicks.size() + " CPS | " + Rclicks.size() + " CPS";
        Color color = rainbow.isEnabled() ? ThemeUtil.getThemeColor(ThemeType.LOGO) : new Color(250,250,250,200);


        if (outline.isEnabled()) {
            RenderUtil.roundedRectangle(getX() + 1, getY() - 2, 21 + psm.getWidth(cpsString), psm.getHeight() + 5, 4, new Color(0,0,0, 80));
            RenderUtil.roundedOutlineRectangle(getX() + 1, getY() - 2, 21 + psm.getWidth(cpsString), psm.getHeight() + 5, 3, 1, color);

            if (shadow.isEnabled()) {
                NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
                    RenderUtil.roundedOutlineRectangle(getX() + 1, getY() - 2, 21 + psm.getWidth(cpsString), psm.getHeight() + 5, 3, 1, color);
                });
            }
        }
        icon.drawString("P", getX() + 4, getY(), color.getRGB());
        psm.drawString(cpsString, getX() + 18, getY() + 0.5f, color.getRGB());
        if (shadow.isEnabled()) {
            NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
                icon.drawString("P", getX() + 4, getY(), color.getRGB());
                psm.drawString(cpsString, getX() + 18, getY() + 0.5f, color.getRGB());
            });
        }

    }

    @Override
    public void onTick(TickEvent event) {
        this.Lclicks.removeIf(l -> l < System.currentTimeMillis() - 1000L);
        this.Rclicks.removeIf(t -> t < System.currentTimeMillis() - 1000L);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getType() == ClickEvent.ClickType.LEFT) {
            this.Lclicks.add(System.currentTimeMillis());
        }
        if (event.getType() == ClickEvent.ClickType.RIGHT) {
            this.Rclicks.add(System.currentTimeMillis());
        }
    }
}
