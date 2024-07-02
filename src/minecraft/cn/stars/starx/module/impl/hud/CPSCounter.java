package cn.stars.starx.module.impl.hud;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.ClickEvent;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.event.impl.Shader3DEvent;
import cn.stars.starx.event.impl.TickEvent;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import cn.stars.starx.font.modern.FontManager;
import cn.stars.starx.font.modern.MFont;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.util.misc.ModuleInstance;
import cn.stars.starx.util.render.*;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "CPSCounter", description = "Show your CPS on screen",
        chineseDescription = "显示你的点击速度", category = Category.HUD)
public class CPSCounter extends Module {
    private final BoolValue displayOnClick = new BoolValue("Display On Click", this, false);
    private final BoolValue rainbow = new BoolValue("Rainbow", this, false);
    private final BoolValue outline = new BoolValue("Background", this, true);
    public CPSCounter() {
        setCanBeEdited(true);
        setWidth(100);
        setHeight(20);
        this.Lclicks = new ArrayList<>();
        this.Rclicks = new ArrayList<>();
    }
    private final List<Long> Lclicks;
    private final List<Long> Rclicks;
    MFont psm = FontManager.getPSM(18);
    MFont icon = FontManager.getMi(24);

    @Override
    public void onRender2D(Render2DEvent event) {
        if (!getModule("HUD").isEnabled()) return;
        if (!ModuleInstance.getBool("HUD", "Display when debugging").isEnabled() && mc.gameSettings.showDebugInfo)
            return;
        if (displayOnClick.isEnabled() && (Lclicks.isEmpty() && Rclicks.isEmpty())) return;
        String cpsString = Lclicks.size() + " CPS | " + Rclicks.size() + " CPS";
        Color color = rainbow.isEnabled() ? ThemeUtil.getThemeColor(ThemeType.LOGO) : new Color(250, 250, 250, 200);


        if (outline.isEnabled()) {
            if (ModuleInstance.getMode("ClientSettings", "Color Style").getMode().equals("Rainbow")) {
                RoundedUtil.drawRound(getX() + 2, getY() - 1, 19 + psm.getWidth(cpsString), psm.getHeight() + 3, 4, new Color(0, 0, 0, 80));
                RenderUtil.roundedOutlineRectangle(getX() + 1, getY() - 2, 21 + psm.getWidth(cpsString), psm.getHeight() + 5, 3, 1, color);

                if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                    MODERN_BLOOM_RUNNABLES.add(() -> {
                        RoundedUtil.drawRound(getX() + 2, getY() - 1, 19 + psm.getWidth(cpsString), psm.getHeight() + 3, 4, ColorUtil.withAlpha(color, 255));
                    });
                }

                if (canBlur()) {
                    MODERN_BLUR_RUNNABLES.add(() -> {
                        RoundedUtil.drawRound(getX() + 2, getY() - 1, 19 + psm.getWidth(cpsString), psm.getHeight() + 3, 4, Color.BLACK);
                    });
                }
            } else {
                RoundedUtil.drawGradientRound(getX() + 0.5f, getY() - 2.5f, 22 + psm.getWidth(cpsString), psm.getHeight() + 6, 4,
                        ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 1000, Color.WHITE, Color.BLACK, true),
                        ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 2000, Color.WHITE, Color.BLACK, true),
                        ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 4000, Color.WHITE, Color.BLACK, true),
                        ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 3000, Color.WHITE, Color.BLACK, true));
                RoundedUtil.drawRound(getX() + 1, getY() - 2, 21 + psm.getWidth(cpsString), psm.getHeight() + 5, 4, new Color(0, 0, 0, 220));

                if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                    MODERN_BLOOM_RUNNABLES.add(() -> {
                        RoundedUtil.drawGradientRound(getX() + 0.5f, getY() - 2.5f, 22 + psm.getWidth(cpsString), psm.getHeight() + 6, 4,
                                ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 1000, Color.WHITE, Color.BLACK, true),
                                ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 2000, Color.WHITE, Color.BLACK, true),
                                ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 4000, Color.WHITE, Color.BLACK, true),
                                ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 3000, Color.WHITE, Color.BLACK, true));
                    });
                }

                if (canBlur()) {
                    MODERN_BLUR_RUNNABLES.add(() -> {
                        RoundedUtil.drawGradientRound(getX() + 0.5f, getY() - 2.5f, 22 + psm.getWidth(cpsString), psm.getHeight() + 6, 4,
                                ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 1000, Color.WHITE, Color.BLACK, true),
                                ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 2000, Color.WHITE, Color.BLACK, true),
                                ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 4000, Color.WHITE, Color.BLACK, true),
                                ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 3000, Color.WHITE, Color.BLACK, true));
                    });
                }
            }
            icon.drawString("P", getX() + 4, getY() + 2, new Color(250, 250, 250, 200).getRGB());
            psm.drawString(cpsString, getX() + 17, getY() + 2.5f, new Color(250, 250, 250, 200).getRGB());
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
