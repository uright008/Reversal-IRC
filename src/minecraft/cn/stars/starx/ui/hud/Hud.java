package cn.stars.starx.ui.hud;

import cn.stars.starx.GameInstance;
import cn.stars.starx.StarX;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import cn.stars.starx.font.modern.FontManager;
import cn.stars.starx.font.modern.MFont;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.impl.hud.*;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.util.StarXLogger;
import cn.stars.starx.util.math.MathUtil;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.misc.ModuleInstance;
import cn.stars.starx.util.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;
import org.apache.commons.lang3.RandomUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Hud implements GameInstance {
    public static float ticks, ticksSinceClickgui;
    public static float positionOfLastModule;
    public static String key;
    static List<Object> modules;
    private static final TimeUtil timer2 = new TimeUtil();
    private static final TimeUtil timer = new TimeUtil();
    public static final KeystrokeUtil forward = new KeystrokeUtil();
    public static final KeystrokeUtil backward = new KeystrokeUtil();
    public static final KeystrokeUtil left = new KeystrokeUtil();
    public static final KeystrokeUtil right = new KeystrokeUtil();
    public static final KeystrokeUtil space = new KeystrokeUtil();
    public static MFont psm16 = FontManager.getPSM(16);
    public static MFont psm17 = FontManager.getPSM(17);
    public static MFont psb20 = FontManager.getPSB(20);
    public static MFont psm18 = FontManager.getPSM(18);
    static Keystrokes keystrokes;
    static BPSCounter bpsCounter;
    static Arraylist arraylist;
    static TextGui textGui;

    public static void renderKeyStrokes() {
        if (keystrokes.isEnabled()) {

            final Minecraft mc = Minecraft.getMinecraft();

            final int x = keystrokes.getX() + 35;
            final int y = keystrokes.getY();

            final int distanceBetweenButtons = 30;
            final int width = 26;

            forward.setUpKey(mc.gameSettings.keyBindForward);
            forward.updateAnimations();
            forward.drawButton(x, y, width);

            backward.setUpKey(mc.gameSettings.keyBindBack);
            backward.updateAnimations();
            backward.drawButton(x, y + distanceBetweenButtons, width);

            left.setUpKey(mc.gameSettings.keyBindLeft);
            left.updateAnimations();
            left.drawButton(x - distanceBetweenButtons, y + distanceBetweenButtons, width);

            right.setUpKey(mc.gameSettings.keyBindRight);
            right.updateAnimations();
            right.drawButton(x + distanceBetweenButtons, y + distanceBetweenButtons, width);

            space.setUpKey(mc.gameSettings.keyBindJump);
            space.updateAnimations();
            space.drawButton(x - 2, y + distanceBetweenButtons * 2, width);
        }
    }

    private static void renderBPS() {
        if (!bpsCounter.isEnabled()) return;

        final String mode = ModuleInstance.getMode("ClientSettings", "Theme").getMode();

        final double x = bpsCounter.getX(), y = bpsCounter.getY() + 15;
        final String bps = "BPS: " + MathUtil.round(mc.thePlayer.getSpeed(), 2);
        final String bps2 = "Speed: " + MathUtil.round(mc.thePlayer.getSpeed(), 2);
        switch (mode) {
            case "Minecraft Rainbow":
            case "Minecraft": {
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(bps, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }

            case "Never Lose":
            case "Comfort": {
                comfortaa.drawStringWithShadow(bps, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }

            case "StarX": {
                gs.drawStringWithShadow(bps2, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                gs.drawStringWithShadow("X:" + MathUtil.round(mc.thePlayer.posX, 1) + " Y:" + MathUtil.round(mc.thePlayer.posY, 1) + " Z:" + MathUtil.round(mc.thePlayer.posZ,1),(float) x, (float) y - 10f, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }

            case "Modern": {
                if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                    NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
                        psm16.drawString(bps2, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                        psm16.drawString("X:" + MathUtil.round(mc.thePlayer.posX, 1) + " Y:" + MathUtil.round(mc.thePlayer.posY, 1) + " Z:" + MathUtil.round(mc.thePlayer.posZ, 1), (float) x, (float) y - 8f, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                    });
                }
                psm16.drawString(bps2, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                psm16.drawString("X:" + MathUtil.round(mc.thePlayer.posX, 1) + " Y:" + MathUtil.round(mc.thePlayer.posY, 1) + " Z:" + MathUtil.round(mc.thePlayer.posZ, 1), (float) x, (float) y - 8f, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }

            case "Skeet": {
                skeetBig.drawStringWithShadow(bps, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }

            default: {
                CustomFont.drawStringWithShadow(bps, (float) x, (float) y, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
                break;
            }
        }
    }

    public static void onFadeOutline() {
        final ModeValue setting = ModuleInstance.getMode("ClientSettings", "Theme");
        final String mode = setting.getMode();

        if (modules == null || Minecraft.getMinecraft().gameSettings.showDebugInfo)
            return;

        for (final Object m : modules) {
            final Category c = ((Module) m).getModuleInfo().category();

            final int offsetY = 2;
            final int offsetX = 1;

            if (mode.equals("Never Lose")) {
                final double stringWidth = comfortaa.getWidth(((Module) m).getModuleInfo().name());
                RenderUtil.rect(((Module) m).renderX - offsetX, ((Module) m).renderY - offsetY, stringWidth + offsetX * 1.5, comfortaa.getHeight() + offsetY + 0.3, new Color(25, 25, 25, 245));
            }
        }
    }

    public static class ModuleComparator implements Comparator<Object> {
        @Override
        public int compare(final Object o1, final Object o2) {
            ModeValue setting = ModuleInstance.getMode("ClientSettings", "Theme");

            if (setting == null) return 1;

            final String mode = setting.getMode();
//
//            final String name = o1 instanceof Module ? ((Module) o1).getModuleInfo().name() : ((Script) o1).getName();
//            final String name2 = o2 instanceof Module ? ((Module) o2).getModuleInfo().name() : ((Script) o2).getName();

            final String name = ((Module) o1).getModuleInfo().name();
            final String name2 = ((Module) o2).getModuleInfo().name();

            switch (mode) {
                case "Minecraft Rainbow":
                case "Minecraft": {
                    return Float.compare(Minecraft.getMinecraft().fontRendererObj.getStringWidth(name2), Minecraft.getMinecraft().fontRendererObj.getStringWidth(name));
                }

                case "Never Lose":
                case "Comfort": {
                    return Float.compare(comfortaa.getWidth(name2), comfortaa.getWidth(name));
                }

                case "Skeet": {
                    return Float.compare(skeetBig.getWidth(name2), skeetBig.getWidth(name));
                }

                case "StarX": {
                    return Float.compare(gs.getWidth(name2), gs.getWidth(name));
                }

                case "Modern": {
                    String nameWithSuffix = ((Module) o1).hasSuffix() ? name + "  " + ((Module) o1).getSuffix() : name;
                    String nameWithSuffix2 = ((Module) o2).hasSuffix() ? name2 + "  " + ((Module) o2).getSuffix() : name2;
                    MFont psm = FontManager.getPSM(17);
                    return Float.compare(psm.getWidth(nameWithSuffix2), psm.getWidth(nameWithSuffix));
                }

                default: {
                    return Float.compare(CustomFont.getWidth(name2), CustomFont.getWidth(name));
                }
            }
        }
    }

    private static void renderArrayList() {
        if (!arraylist.isEnabled()) return;
        final String mode = ModuleInstance.getMode("ClientSettings", "Theme").getMode();

        final float offset = 6;

        final float arraylistX = arraylist.getX() + arraylist.getWidth();

        modules = new ArrayList<>();

        modules.addAll(StarX.INSTANCE.getModuleManager().getEnabledModules());

        modules.sort(new ModuleComparator());

        int moduleCount = 0;

        for (final Object n : modules) {

            final float posOnArraylist = offset + moduleCount * CustomFont.getHeight() * 1.2f;

            assert n instanceof Module;
            final String name = ((Module) n).getModuleInfo().name();

            float finalX = 0;
            final float speed = 6;

            final float renderX = ((Module) n).getRenderX();
            final float renderY = arraylist.getY() + ((Module) n).getRenderY();

            if (((Module) n).getModuleInfo().category().equals(Category.RENDER) && ModuleInstance.getBool("Arraylist", "No Render Modules").isEnabled())
                continue;

            switch (mode) {
                case "Rise":
                case "Rise Rainbow": {
                    finalX = arraylistX - CustomFont.getWidth(name);
                    double x = (double)(renderX + CustomFont.getWidth(name));
                    CustomFont.drawStringWithShadow(name, renderX, renderY, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));
             //       RenderUtil.rect(x, (double) renderY, x + 1, (double) renderY + CustomFont.getHeight(), ThemeUtil.getThemeColor(moduleCount, ThemeType.ARRAYLIST));
                    break;
                }

                case "Minecraft":
                case "Minecraft Rainbow": {
                    finalX = arraylistX - mc.fontRendererObj.getStringWidth(name);

                    mc.fontRendererObj.drawStringWithShadow(name, renderX, renderY, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));

                    break;
                }

                case "Comfort": {
                    finalX = arraylistX - comfortaa.getWidth(name);

                    comfortaa.drawStringWithShadow(name, renderX + 2, renderY, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));
                    break;
                }

                case "Never Lose": {
                    final int offsetY = 2;
                    final int offsetX = 1;

                    final double stringWidth = comfortaa.getWidth(name);
                    RenderUtil.rect(renderX - offsetX, renderY - offsetY, stringWidth + offsetX * 1.5, comfortaa.getHeight() + offsetY + 0.3, new Color(0, 0, 0, 120));
                    finalX = arraylistX - comfortaa.getWidth(name);

                    comfortaa.drawString(name, renderX, renderY, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));
                }
                break;

                case "StarX": {
                    final int offsetY = 2;
                    final int offsetX = 1;

                    final double stringWidth = gs.getWidth(name);
                    RenderUtil.rect(renderX - offsetX, renderY - offsetY + 0.5, stringWidth + offsetX * 1.5, gs.getHeight() + offsetY - 0.7, new Color(0, 0, 0, 60));
                    RenderUtil.roundedRect(renderX + stringWidth, renderY - offsetY + 0.5, 2, gs.getHeight() + offsetY - 0.6, 2.5, ColorUtil.liveColorBrighter(new Color(0,255,255), 1f));

                    finalX = arraylistX - gs.getWidth(name);

                    gs.drawString(name, renderX, renderY, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));
                }
                break;

                case "Modern": {
                    final int offsetY = 2;
                    final int offsetX = 1;

                    float fixedRenderX = renderX - (((Module) n).hasSuffix() ? psm17.getWidth("  " + ((Module) n).getSuffix()) : 0);

                    final double stringWidth = psm17.getWidth((((Module) n).hasSuffix() ? name + "  " + ((Module) n).getSuffix() : name));
                    final int mC = moduleCount;

                    if (ModuleInstance.getMode("ClientSettings", "Color Style").getMode().equals("Rainbow")) {
                        Runnable shadowRunnable = () -> {
                            RenderUtil.rect(fixedRenderX - offsetX - 1, renderY - offsetY + 0.2, stringWidth + offsetX * 1.5 + 1, 10.3 + offsetY - 1.5,
                                    ModuleInstance.getBool("Arraylist", "Glow Shadow").isEnabled() ? ThemeUtil.getThemeColor(mC, ThemeType.ARRAYLIST) : Color.BLACK);
                            RenderUtil.roundedRectangle(fixedRenderX + stringWidth, renderY - offsetY, 2, 10.3 + offsetY - 0.5, 2.5, ColorUtil.liveColorBrighter(new Color(0, 255, 255), 1f));
                        };

                        Runnable blurRunnable = () -> {
                            RenderUtil.rect(fixedRenderX - offsetX - 1, renderY - offsetY + 0.2, stringWidth + offsetX * 1.5 + 1, 10.3 + offsetY - 1.5, Color.BLACK);
                        };

                        RenderUtil.rect(fixedRenderX - offsetX - 1, renderY - offsetY + 0.2, stringWidth + offsetX * 1.5 + 1, 10.3 + offsetY - 1.5, new Color(0, 0, 0, 80));
                        RenderUtil.roundedRectangle(fixedRenderX + stringWidth, renderY - offsetY, 2, 10.3 + offsetY - 0.5, 2.5, ColorUtil.liveColorBrighter(new Color(0, 255, 255), 1f));

                        if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                            MODERN_BLOOM_RUNNABLES.add(shadowRunnable);
                        }

                        if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                            MODERN_BLUR_RUNNABLES.add(blurRunnable);
                        }

                        psm17.drawString(name, fixedRenderX - 1, renderY + 0.6, ThemeUtil.getThemeColorInt(mC, ThemeType.ARRAYLIST));
                        if (((Module) n).hasSuffix()) {
                            psm17.drawString(((Module) n).getSuffix(), fixedRenderX + psm17.getWidth("  " + name) - 2, renderY + 0.6, new Color(250, 250, 250, 200).getRGB());
                        }
                    } else {
                        Runnable shadowRunnable = () -> {
                            RenderUtil.rect(fixedRenderX - offsetX - 1, renderY - offsetY + 0.2, stringWidth + offsetX * 1.5 + 1, 10.3 + offsetY - 1.5,
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(5, mC * 25, Color.WHITE, Color.BLACK, true));
                            RenderUtil.roundedRectangle(fixedRenderX + stringWidth, renderY - offsetY, 2, 10.3 + offsetY - 0.5, 2.5, ColorUtils.INSTANCE.interpolateColorsBackAndForth(5, mC * 25, Color.WHITE, Color.BLACK, true));
                        };

                        Runnable blurRunnable = () -> {
                            RenderUtil.rect(fixedRenderX - offsetX - 1, renderY - offsetY + 0.2, stringWidth + offsetX * 1.5 + 1, 10.3 + offsetY - 1.5, Color.BLACK);
                        };

                        if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                            MODERN_BLOOM_RUNNABLES.add(shadowRunnable);
                        }

                        if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                            MODERN_BLUR_RUNNABLES.add(blurRunnable);
                        }

                        RenderUtil.rect(fixedRenderX - offsetX - 1, renderY - offsetY + 0.2, stringWidth + offsetX * 1.5 + 1, 10.3 + offsetY - 1.5, new Color(0, 0, 0, 150));
                        RenderUtil.roundedRectangle(fixedRenderX + stringWidth, renderY - offsetY, 2, 10.3 + offsetY - 0.5, 2.5, ColorUtils.INSTANCE.interpolateColorsBackAndForth(5, moduleCount * 25, Color.WHITE, Color.BLACK, true));
                        psm17.drawString(name, fixedRenderX - 1, renderY + 0.6, ColorUtils.INSTANCE.interpolateColorsBackAndForth(5, moduleCount * 25, Color.WHITE, Color.BLACK, true).getRGB());
                        if (((Module) n).hasSuffix()) {
                            psm17.drawString(((Module) n).getSuffix(), fixedRenderX + psm17.getWidth("  " + name) - 2, renderY + 0.6, new Color(250, 250, 250, 200).getRGB());
                        }
                    }

                    finalX = arraylistX - psm17.getWidth(name);
                }
                break;

                case "Skeet": {
                    finalX = arraylistX - skeetBig.getWidth(name);

                    skeetBig.drawStringWithShadow(name, renderX, renderY, ThemeUtil.getThemeColorInt(moduleCount, ThemeType.ARRAYLIST));
                }
                break;
            }

            moduleCount++;

            final String animationMode = ((ModeValue) Objects.requireNonNull(StarX.INSTANCE.getModuleManager().getSetting("ClientSettings", "List Animation"))).getMode();

            final Module m = ((Module) n);
            if (timer2.hasReached(1000 / 100)) {
                switch (animationMode) {
                    case "StarX":
                        m.renderX = (m.renderX * (speed - 1) + finalX) / speed;
                        m.renderY = (m.renderY * (speed - 1) + posOnArraylist) / speed;

                        break;
                    case "Slide":
                        m.renderX = (m.renderX * (speed - 1) + finalX) / speed;

                        if (m.renderY < positionOfLastModule) {
                            m.renderY = posOnArraylist;
                        } else {
                            m.renderY = (m.renderY * (speed - 1) + posOnArraylist) / (speed);
                        }
                        break;
                }
            }

            positionOfLastModule = posOnArraylist;

        }

        arraylist.setHeight(moduleCount * 12);

        // Resetting timer
        if (timer2.hasReached(1000 / 100)) {
            timer2.reset();
        }

        if (timer.hasReached(1000 / 60)) {
            timer.reset();

            if (mc.ingameGUI != null && !(mc.currentScreen instanceof GuiChat)) {
                if (ticksSinceClickgui <= 5)
                    ticksSinceClickgui++;
            } else {
                if (ticksSinceClickgui >= 1)
                    ticksSinceClickgui--;
            }

            forward.updateAnimations();
            backward.updateAnimations();
            left.updateAnimations();
            right.updateAnimations();
            space.updateAnimations();
        }
    }
    
    private static void renderClientName() {
        if (!textGui.isEnabled()) return;
        final String mode = ModuleInstance.getMode("ClientSettings", "Theme").getMode();
        final boolean useDefaultName = !ModuleInstance.getBool("TextGui", "Custom Name").isEnabled();

        final float offset;
        String name = StarX.NAME, customName = ThemeUtil.getCustomClientName();

        if (customName.isEmpty()) customName = "Use \".clientname <name>\" to set custom name.";
        switch (mode) {
            case "Rise": {
                if (useDefaultName) {
                    textGui.setWidth(70);
                    CustomFont.drawStringBigWithDropShadow(name, textGui.getX() + 1, textGui.getY(), ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                    offset = CustomFont.getWidthBig(name);
                    CustomFont.drawStringWithDropShadow(StarX.VERSION, textGui.getX() + offset, textGui.getY(), ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                } else {
                    textGui.setWidth((int) (20 + CustomFont.getWidthBig(customName)));
                    CustomFont.drawStringBigWithDropShadow(customName, textGui.getX() + 1, textGui.getY(), ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                }
                break;
            }

            case "Rise Rainbow": {
                if (useDefaultName) {
                    textGui.setWidth(70);
                    float off2 = 0;

                    for (int i = 0; i < name.length(); i++) {
                        final String character = String.valueOf(name.charAt(i));

                        CustomFont.drawStringBigWithDropShadow(character, textGui.getX() + 1 + off2, textGui.getY(), ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));

                        off2 += CustomFont.getWidthBig(character) - 2;
                    }

                    off2 = CustomFont.getWidthBig(name);
                    CustomFont.drawStringWithDropShadow(StarX.VERSION, textGui.getX() + off2, textGui.getY(), ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                } else {
                    textGui.setWidth((int) (20 + CustomFont.getWidthBig(customName)));
                    float off2 = 0;

                    for (int i = 0; i < customName.length(); i++) {
                        final String character = String.valueOf(customName.charAt(i));

                        CustomFont.drawStringBigWithDropShadow(character, textGui.getX() + 1 + off2, textGui.getY(), ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));

                        off2 += CustomFont.getWidthBig(character) - 2;
                    }

                }

                break;
            }

            case "Minecraft": {
                if (useDefaultName) {
                    textGui.setWidth(70);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(1.5F, 1.5F, 1.5F);
                    mc.fontRendererObj.drawStringWithShadow(name, textGui.getX() + 1, textGui.getY(), ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                    GlStateManager.popMatrix();

                    offset = CustomFont.getWidthBig(name);
                    mc.fontRendererObj.drawStringWithShadow(StarX.VERSION, textGui.getX() + offset + 7, textGui.getY(), ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                } else {
                    textGui.setWidth((int) (20 + mc.fontRendererObj.getStringWidth(customName) * 1.5));
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(1.5F, 1.5F, 1.5F);
                    mc.fontRendererObj.drawStringWithShadow(customName, textGui.getX() + 1, textGui.getY(), ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                    GlStateManager.popMatrix();
                }
                break;
            }

            case "StarX": {
                if (useDefaultName) {
                    textGui.setWidth(100);
                    gsTitle.drawStringWithShadow("S", textGui.getX() + 7, textGui.getY() + 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                    gsTitle.drawStringWithShadow("tarX [" + StarX.VERSION + "]", textGui.getX() + 5 + gsTitle.getWidth("S"), textGui.getY() + 4.9f, new Color(230, 230, 230, 200).getRGB());
                } else {
                    textGui.setWidth((int) (20 + gsTitle.getWidth(customName)));
                    // FOOLISH
                    gsTitle.drawStringWithShadow(String.valueOf(customName.charAt(0)), textGui.getX() + 7, textGui.getY() + 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                    // 从字符串第二个字开始获取
                    gsTitle.drawStringWithShadow(customName.substring(1), textGui.getX() + 5 + gsTitle.getWidth(String.valueOf(customName.charAt(0))), textGui.getY() + 4.9f, new Color(230, 230, 230, 200).getRGB());
                }
                break;
            }

            case "Modern": {
                int x = textGui.getX() + 5;
                int y = textGui.getY();
                float off = 0;
                String extraText = " | " + Minecraft.getDebugFPS() + " FPS | " + mc.getSession().getUsername();
                float extraWidth = psm18.getWidth(extraText);

                if (ModuleInstance.getMode("ClientSettings", "Color Style").getMode().equals("Rainbow")) {
                    if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                        MODERN_BLUR_RUNNABLES.add(() -> {
                            RoundedUtil.drawRound(x + 1, y + 1, 33 + extraWidth, 12, 4, Color.BLACK);
                        });
                    }

                    RoundedUtil.drawRound(x + 1, y + 1, 33 + extraWidth, 12, 4, new Color(0, 0, 0, 80));
                    RenderUtil.roundedOutlineRectangle(x, y, 35 + extraWidth, 14, 3, 1,
                            ModuleInstance.getBool("TextGui", "Rainbow").isEnabled() ? ThemeUtil.getThemeColor(ThemeType.LOGO) : new Color(250, 250, 250, 200));
                //    RoundedUtil.drawRoundOutline(x, y, 35 + extraWidth, 14, 3, 0.1f, new Color(0, 0, 0, 80),
                //            ModuleInstance.getBool("TextGui", "Rainbow").isEnabled() ? ThemeUtil.getThemeColor(ThemeType.LOGO) : new Color(250, 250, 250, 200));
                    psm18.drawString(extraText, x + 30.5, y + 4f, new Color(250, 250, 250, 200).getRGB());

                    if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            psm18.drawString(extraText, x + 30.5, y + 4f, ThemeUtil.getThemeColor(ThemeType.LOGO).getRGB());
                            RoundedUtil.drawRound(x + 1, y + 1, 33 + extraWidth, 12, 4,
                                    ModuleInstance.getBool("TextGui", "Rainbow").isEnabled() ? ThemeUtil.getThemeColor(ThemeType.LOGO) : new Color(250, 250, 250, 200));
                        });
                    }

                    textGui.setWidth((int) (44 + extraWidth));
                    for (int i = 0; i < name.length(); i++) {
                        final String character = String.valueOf(name.charAt(i));

                        final float off1 = off;
                        psb20.drawString(character, x + 4 + off1, y + 3.5,
                                ModuleInstance.getBool("TextGui", "Rainbow").isEnabled() ? ThemeUtil.getThemeColorInt(i, ThemeType.LOGO) : new Color(250, 250, 250, 200).getRGB());
                        off += psb20.getWidth(character);
                    }
                } else {

                    if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled()) {
                        MODERN_BLUR_RUNNABLES.add(() -> {
                            RoundedUtil.drawGradientRound(x - 0.5f, y - 0.5f, 76 + extraWidth, 15, 3,
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 1000, Color.WHITE, Color.BLACK, true),
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 2000, Color.WHITE, Color.BLACK, true),
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 4000, Color.WHITE, Color.BLACK, true),
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 3000, Color.WHITE, Color.BLACK, true));
                            RoundedUtil.drawRound(x, y, 75 + extraWidth, 14, 3, new Color(0, 0, 0, 220));
                        });
                    }

                    //    RoundedUtil.drawRound(x, y, 35 + extraWidth, 14, 4, new Color(0, 0, 0, 80));
                    RoundedUtil.drawGradientRound(x - 0.5f, y - 0.5f, 76 + extraWidth, 15, 3,
                            ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 1000, Color.WHITE, Color.BLACK, true),
                            ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 2000, Color.WHITE, Color.BLACK, true),
                            ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 4000, Color.WHITE, Color.BLACK, true),
                            ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 3000, Color.WHITE, Color.BLACK, true));
                    RoundedUtil.drawRound(x, y, 75 + extraWidth, 14, 3, new Color(0, 0, 0, 220));

                    psm18.drawString(extraText, x + 72, y + 4f, new Color(200, 200, 200, 240).getRGB());

                    if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled()) {
                        MODERN_BLOOM_RUNNABLES.add(() -> {
                            RoundedUtil.drawGradientRound(x - 0.5f, y - 0.5f, 76 + extraWidth, 15, 3,
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 1000, Color.WHITE, Color.BLACK, true),
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 2000, Color.WHITE, Color.BLACK, true),
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 4000, Color.WHITE, Color.BLACK, true),
                                    ColorUtils.INSTANCE.interpolateColorsBackAndForth(3, 3000, Color.WHITE, Color.BLACK, true));
                        });
                    }

                    textGui.setWidth((int) (84 + extraWidth));
                    name = "STARX CLIENT";

                    for (int i = 0; i < name.length(); i++) {
                        final String character = String.valueOf(name.charAt(i));

                        final float off1 = off;
                        psb20.drawString(character, x + 4 + off1, y + 3.5,
                                ColorUtils.INSTANCE.interpolateColorsBackAndForth(5, i * 10, Color.WHITE, Color.BLACK, true).getRGB());
                        off += psb20.getWidth(character);
                    }
                }
                break;
            }

            case "Minecraft Rainbow": {
                if (useDefaultName) {
                    textGui.setWidth(75);
                    float off = 0;

                    for (int i = 0; i < name.length(); i++) {
                        final String character = String.valueOf(name.charAt(i));

                        GlStateManager.pushMatrix();
                        GlStateManager.scale(1.5F, 1.5F, 1.5F);
                        mc.fontRendererObj.drawStringWithShadow(character, textGui.getX() + 1 + off, textGui.getY(), ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));
                        GlStateManager.popMatrix();

                        off += mc.fontRendererObj.getStringWidth(character);
                    }

                    off = CustomFont.getWidthBig(name);
                    mc.fontRendererObj.drawStringWithShadow(StarX.VERSION, off + 20, textGui.getY(), ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                } else {
                    textGui.setWidth((int) (20 + mc.fontRendererObj.getStringWidth(customName) * 1.5));
                    float off = 0;

                    for (int i = 0; i < customName.length(); i++) {
                        final String character = String.valueOf(customName.charAt(i));

                        GlStateManager.pushMatrix();
                        GlStateManager.scale(1.5F, 1.5F, 1.5F);
                        mc.fontRendererObj.drawStringWithShadow(character, textGui.getX() + 1 + off, textGui.getY(), ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));
                        GlStateManager.popMatrix();

                        off += mc.fontRendererObj.getStringWidth(character);
                    }
                }
                break;
            }

            case "Comfort": {
                if (useDefaultName) {
                    textGui.setWidth(70);
                    comfortaaBig.drawStringWithShadow(name, textGui.getX(), textGui.getY(), ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                    offset = CustomFont.getWidthBig(name);
                    comfortaa.drawStringWithShadow(StarX.VERSION, textGui.getX() + 3 + offset, textGui.getY() - 1, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                } else {
                    textGui.setWidth((int) (20 + comfortaaBig.getWidth(customName)));
                    comfortaaBig.drawStringWithShadow(customName, textGui.getX(), textGui.getY(), ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                }
                break;
            }

            case "Never Lose": {
                float x = textGui.getX() + 2;
                final float y = textGui.getY() - 1;
                final String clientName = useDefaultName ? "STARX" : customName.toUpperCase();
                final String ip = (mc.getCurrentServerData() == null ? "Singleplayer" : mc.getCurrentServerData().serverIP);
                String username = mc.getSession().getUsername();
                if (username == null) username = mc.thePlayer.getName();
                final float width = museo.getWidth(clientName) + eaves.getWidth(ip + Minecraft.getDebugFPS() + username);
                final int informationColor = new Color(255, 255, 255, 220).hashCode();
                textGui.setWidth((int) (width + 34));
                RenderUtil.roundedRect(x - 1, y - 1, width + 34, 12, 3, Color.black);


                RenderUtil.roundedRect(x - 1 - 4 / 2f, y - 1 - 4 / 2f, width + 34 + 4, 12 + 4, 6, new Color(0, 0, 0, 25));

                final Color themeColor = ThemeUtil.getThemeColor(ThemeType.GENERAL);
                final Color color = new Color(
                        themeColor.getRed(),
                        themeColor.getGreen(),
                        themeColor.getBlue(),
                        85
                );

                museo.drawString(clientName, x + 1, y, color.hashCode());
                museo.drawString(clientName, x + 2, y + 1, informationColor);

                x += museo.getWidth(clientName) + 5;
                eaves.drawString("|", x - 4, y + 2, new Color(255, 255, 255, 150).hashCode());
                eaves.drawString(username, x, y + 2, informationColor);

                x += eaves.getWidth(username) + 4;
                eaves.drawString("|", x - 4, y + 2, new Color(255, 255, 255, 150).hashCode());
                eaves.drawString(ip, x, y + 2, informationColor);

                x += eaves.getWidth(ip) + 4;
                eaves.drawString("|", x - 4, y + 2, new Color(255, 255, 255, 150).hashCode());
                eaves.drawString("FPS " + Minecraft.getDebugFPS(), x, y + 2, informationColor);
            }
            break;

            case "Skeet": {
                float x = textGui.getX() + 4;
                final float y = textGui.getY() + 1f;

                final String clientName = useDefaultName ? "starx" : customName.toLowerCase();
                final NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
                final int responseTime = info == null || mc.isSingleplayer() ? 0 : info.getResponseTime();

                String username = mc.getSession().getUsername();
                if (username == null) username = mc.thePlayer.getName();
                final float width = (skeet.getWidth(clientName) + skeet.getWidth(Minecraft.getDebugFPS() + " fps" + responseTime + "ms" + username)) + 10;
                final int informationColor = Color.WHITE.hashCode();
                textGui.setWidth((int) (width + 35) + 4);

                for (int i = 1; i <= 4; i++) {
                    RenderUtil.rect(x - 1.5 - i / 2f, y - 1.5 - i / 2f, width + 35 + i, 9 + i, true, Color.DARK_GRAY.darker());
                    RenderUtil.rect(x - 1 - i / 2f, y - 1 - i / 2f, width + 34 + i, 8 + i, true, Color.DARK_GRAY);
                }

                RenderUtil.rect(x - 1, y - 1, width + 34, 8, true, Color.DARK_GRAY.darker().darker());

                skeet.drawString(clientName, x + 2, y + 0.5f, informationColor);
                skeet.drawString(" sense", (x + 2) + skeet.getWidth(clientName) - 2, y + 0.5f, StarX.CLIENT_THEME_COLOR_BRIGHT);

                x += skeet.getWidth(clientName + "sense") + 15;
                skeet.drawString("|", x - 12, y + 0.5f, new Color(255, 255, 255, 150).hashCode());
                skeet.drawString(Minecraft.getDebugFPS() + " fps", x - 6, y + 0.5f, informationColor);

                x += skeet.getWidth(Minecraft.getDebugFPS() + " fps") + 6;
                skeet.drawString("|", x - 12, y + 0.5f, new Color(255, 255, 255, 150).hashCode());
                skeet.drawString(responseTime + "ms", x - 6, y + 0.5f, informationColor);

                x += skeet.getWidth(responseTime + "ms") + 6;
                skeet.drawString("|", x - 12, y + 0.5f, new Color(255, 255, 255, 150).hashCode());
                skeet.drawString(username, x - 6, y + 0.5f, informationColor);
            }
            break;
        }
    }

    public static void initializeModules() {
        try {
            keystrokes = (Keystrokes) ModuleInstance.getModule(Keystrokes.class);
            bpsCounter = (BPSCounter) ModuleInstance.getModule(BPSCounter.class);
            arraylist = (Arraylist) ModuleInstance.getModule(Arraylist.class);
            textGui = (TextGui) ModuleInstance.getModule(TextGui.class);
        } catch (NullPointerException e) {
            StarXLogger.fatal("Error while initialize modules for HUD.", e);
        }
    }

    public static void renderGameOverlay() {
        if (StarX.isDestructed || !ModuleInstance.getModule(HUD.class).isEnabled()) return;
        if (!ModuleInstance.getBool("HUD", "Display when debugging").isEnabled() && mc.gameSettings.showDebugInfo) return;
        renderKeyStrokes();
        renderBPS();
        renderClientName();
        renderArrayList();
    }
}
