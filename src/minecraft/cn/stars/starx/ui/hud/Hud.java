package cn.stars.starx.ui.hud;

import cn.stars.starx.GameInstance;
import cn.stars.starx.StarX;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.impl.render.ClientSettings;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.math.MathUtil;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.render.KeystrokeUtil;
import cn.stars.starx.util.render.RenderUtil;
import cn.stars.starx.util.render.ThemeType;
import cn.stars.starx.util.render.ThemeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Hud implements GameInstance {
    public static float ticks, ticksSinceClickgui;
    public static float positionOfLastModule;
    public static String key, lastKey;
    static List<Object> modules;
    private static final TimeUtil timer2 = new TimeUtil();
    private final Vec3 lastPos = new Vec3(0, 0, 0);
    private final double bpsSpeed = 0;
    private static final TimeUtil timer = new TimeUtil();
    public static final KeystrokeUtil forward = new KeystrokeUtil();
    public static final KeystrokeUtil backward = new KeystrokeUtil();
    public static final KeystrokeUtil left = new KeystrokeUtil();
    public static final KeystrokeUtil right = new KeystrokeUtil();
    public static final KeystrokeUtil space = new KeystrokeUtil();

    static boolean getBoolean2(final Class<? extends Module> clazz, final String name) {
        for (final Setting setting : getModule2(clazz).getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return ((BoolValue) setting).isEnabled();
            }
        }

        return false;
    }

    static String getMode2(final Class<? extends Module> clazz, final String name) {
        for (final Setting setting : getModule2(clazz).getSettings()) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return ((ModeValue) setting).getMode();
            }
        }

        return null;
    }

    static Module getModule2(final Class<? extends Module> clazz) {
        for (final Module module : StarX.INSTANCE.getModuleManager().getModuleList()) {
            if (module.getClass() == clazz) {
                return module;
            }
        }

        return null;
    }

    public static void renderKeyStrokes() {
        if (((BoolValue) Objects.requireNonNull(
                StarX.INSTANCE.getModuleManager().getSetting("ClientSettings", "Keystrokes"))).isEnabled()) {

            final Minecraft mc = Minecraft.getMinecraft();
            final ScaledResolution SR = new ScaledResolution(mc);

            final float xPercentage = (float) ((NumberValue) Objects.requireNonNull(StarX.INSTANCE.getModuleManager().getSetting("ClientSettings", "KeystrokesX"))).getValue();
            final float yPercentage = (float) ((NumberValue) Objects.requireNonNull(StarX.INSTANCE.getModuleManager().getSetting("ClientSettings", "KeystrokesY"))).getValue();

            final int x = (int) (SR.getScaledWidth() * xPercentage) / 100;
            final int y = (int) (SR.getScaledHeight() * yPercentage) / 100;

            final int distanceBetweenButtons = 34;
            final int width = 30;

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
            space.drawButton(x, y + distanceBetweenButtons * 2, width);
        }
    }

    private static void renderBPS() {
        final boolean enabled = getBoolean2(ClientSettings.class, "BPS Counter");
        if (!enabled) return;

        final String mode = getMode2(ClientSettings.class, "Theme");
        final ScaledResolution sr = new ScaledResolution(mc);

        final double x = 2, y = sr.getScaledHeight() - 10;
        final String bps = "BPS: " + MathUtil.round(((Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * mc.timer.timerSpeed) * 20), 2);
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
        final ModeValue setting = ((ModeValue) Objects.requireNonNull(StarX.INSTANCE.getModuleManager().getSetting("ClientSettings", "Theme")));
        final String mode = setting.getMode();

        if (modules == null || Minecraft.getMinecraft().gameSettings.showDebugInfo)
            return;

        for (final Object m : modules) {
            final Category c = ((Module) m).getModuleInfo().category();

            final int offsetY = 2;
            final int offsetX = 1;

            if (true) {
                assert m instanceof Module;

                if (mode.equals("Never Lose")) {
                    final double stringWidth = comfortaa.getWidth(((Module) m).getModuleInfo().name());
                    RenderUtil.rect(((Module) m).renderX - offsetX, ((Module) m).renderY - offsetY, stringWidth + offsetX * 1.5, comfortaa.getHeight() + offsetY + 0.3, new Color(25, 25, 25, 245));
                }
            }
        }
    }

    public static class ModuleComparator implements Comparator<Object> {
        @Override
        public int compare(final Object o1, final Object o2) {
            ModeValue setting = null;
            try {
                setting = ((ModeValue) Objects.requireNonNull(StarX.INSTANCE.getModuleManager().getSetting("ClientSettings", "Theme")));
            } catch (final Exception ignored) {
            }

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

                default: {
                    return Float.compare(CustomFont.getWidth(name2), CustomFont.getWidth(name));
                }
            }
        }
    }

    private static void renderArrayList() {
        final String mode = getMode2(ClientSettings.class, "Theme");

        final ScaledResolution SR = new ScaledResolution(mc);

        final float offset = 6;

        final float arraylistX = SR.getScaledWidth() - offset;

        modules = new ArrayList<>();

        modules.addAll(StarX.INSTANCE.getModuleManager().getEnabledModules());

        modules.sort(new ModuleComparator());

        int moduleCount = 0;

        for (final Object n : modules) {
            final Category c = ((Module) n).getModuleInfo().category();

            if (true) {
                final float posOnArraylist = offset + moduleCount * CustomFont.getHeight() * 1.2f;

                assert n instanceof Module;
                final String name = ((Module) n).getModuleInfo().name();

                float finalX = 0;
                final float speed = 6;

                final float renderX = ((Module) n).getRenderX();
                final float renderY = ((Module) n).getRenderY();

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

        }

        //Resetting timer
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
        {
            final String mode = getMode2(ClientSettings.class, "Theme");
            final boolean module = getBoolean2(ClientSettings.class, "Logo");

            final float offset;
            final String name = StarX.NAME, customName = ThemeUtil.getCustomClientName();

            final boolean shouldRenderCustomClientName = !(customName.isEmpty() || customName.equals(" "));

            if (!module) return;

            switch (mode) {
                case "Rise": {
                    CustomFont.drawStringBigWithDropShadow(name, 2, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                    offset = CustomFont.getWidthBig(name);
                    CustomFont.drawStringWithDropShadow(StarX.VERSION, 1 + offset, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                    if (shouldRenderCustomClientName)
                        CustomFont.drawStringWithDropShadow(customName, 1 + offset, 12, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                    break;
                }

                case "Rise Rainbow":
                    float off2 = 0;

                    for (int i = 0; i < name.length(); i++) {
                        final String character = String.valueOf(name.charAt(i));

                        CustomFont.drawStringBigWithDropShadow(character, 2 + off2, 5, ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));

                        off2 += CustomFont.getWidthBig(character) - 2;
                    }

                    off2 = CustomFont.getWidthBig(name);
                    CustomFont.drawStringWithDropShadow(StarX.VERSION, 1 + off2, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                    if (shouldRenderCustomClientName)
                        CustomFont.drawStringWithDropShadow(customName, 1 + off2, 12, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                    break;

                case "Minecraft": {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(1.5F, 1.5F, 1.5F);
                    mc.fontRendererObj.drawStringWithShadow(name, 2, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                    GlStateManager.popMatrix();

                    offset = CustomFont.getWidthBig(name);
                    mc.fontRendererObj.drawStringWithShadow(StarX.VERSION, offset - 10, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                    if (shouldRenderCustomClientName)
                        mc.fontRendererObj.drawStringWithShadow(customName, 1 + offset, 13, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                    break;
                }

                case "Minecraft Rainbow": {
                    float off = 0;

                    for (int i = 0; i < name.length(); i++) {
                        final String character = String.valueOf(name.charAt(i));

                        GlStateManager.pushMatrix();
                        GlStateManager.scale(1.5F, 1.5F, 1.5F);
                        mc.fontRendererObj.drawStringWithShadow(character, 2 + off, 5, ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));
                        GlStateManager.popMatrix();

                        off += mc.fontRendererObj.getStringWidth(character);
                    }

                    off = CustomFont.getWidthBig(name);
                    mc.fontRendererObj.drawStringWithShadow(StarX.VERSION, off - 10, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                    if (shouldRenderCustomClientName) {
                        for (int i = 0; i < customName.length(); i++) {
                            final String character = String.valueOf(customName.charAt(i));
                            mc.fontRendererObj.drawStringWithShadow(character, 1 + off, 13, ThemeUtil.getThemeColorInt(i, ThemeType.LOGO));
                            off += mc.fontRendererObj.getStringWidth(character);
                        }
                    }
                    break;
                }

                case "Comfort": {
                    comfortaaBig.drawStringWithShadow(name, 1, 5, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                    offset = CustomFont.getWidthBig(name);
                    comfortaa.drawStringWithShadow(StarX.VERSION, 4 + offset, 4, ThemeUtil.getThemeColorInt(ThemeType.LOGO));

                    if (shouldRenderCustomClientName)
                        comfortaa.drawStringWithShadow(customName, 1 + offset, 12, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
                    break;
                }

                case "Never Lose": {
                    float x = 3;
                    final float y = 4;
                    final String clientName = "STARX";
                    final String ip = (mc.getCurrentServerData() == null ? "Singleplayer" : mc.getCurrentServerData().serverIP);
                    String username = mc.getSession().getUsername();
                    if (username == null) username = mc.thePlayer.getName();
                    final float width = museo.getWidth(clientName) + eaves.getWidth(ip + Minecraft.getDebugFPS() + username);
                    final int informationColor = new Color(255, 255, 255, 220).hashCode();

                    RenderUtil.roundedRect(x - 1, y - 1, width + 34, 12, 3, Color.black);


                    RenderUtil.roundedRect(x - 1 - 4 / 2f, y - 1 - 4 / 2f, width + 34 + 4, 12 + 4, 6, new Color(0, 0, 0, 25));

                    final Color themeColor = ThemeUtil.getThemeColor(ThemeType.GENERAL);
                    final Color color = new Color(
                            themeColor.getRed(),
                            themeColor.getGreen(),
                            themeColor.getBlue(),
                            85
                    );

                    museo.drawString(clientName, x - 1, y + 1, color.hashCode());
                    museo.drawString(clientName, x, y + 2, informationColor);

                    x += museo.getHeight(clientName) + 30;
                    eaves.drawString("|", x - 4, y + 2, new Color(255, 255, 255, 150).hashCode());
                    eaves.drawString(username, x, y + 2, informationColor);

                    x += eaves.getWidth(username) + 4;
                    eaves.drawString("|", x - 4, y + 2, new Color(255, 255, 255, 150).hashCode());
                    eaves.drawString(ip, x, y + 2, informationColor);

                    x += eaves.getWidth(ip) + 4;
                    eaves.drawString("|", x - 4, y + 2, new Color(255, 255, 255, 150).hashCode());
                    eaves.drawString("FPS " + Minecraft.getDebugFPS(), x, y + 2, informationColor);

                    if (shouldRenderCustomClientName) {
                        final float customWidth = eaves.getWidth(customName) + 2;
                        final float customY = 18;
                        final float customX = 3;

                        RenderUtil.roundedRect(customX - 1, customY - 1, customWidth, 12, 3, Color.black);

                        RenderUtil.roundedRect(customX - 1 - 4 / 2f, customY - 1 - 4 / 2f, customWidth + 4, 12 + 4, 6, new Color(0, 0, 0, 25));

                        eaves.drawString(customName, customX, customY + 2, informationColor);
                    }
                }
                break;

                case "Skeet": {
                    float x = 5;
                    final float y = 6f;

                    final String clientName = "starx";
                    final NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
                    final int responseTime = info == null || mc.isSingleplayer() ? 0 : info.getResponseTime();

                    String username = mc.getSession().getUsername();
                    if (username == null) username = mc.thePlayer.getName();
                    final float width = (skeet.getWidth(clientName) + skeet.getWidth(Minecraft.getDebugFPS() + " fps" + responseTime + "ms" + username)) + 10;
                    final int informationColor = Color.WHITE.hashCode();

                    for (int i = 1; i <= 4; i++) {
                        RenderUtil.rect(x - 1.5 - i / 2f, y - 1.5 - i / 2f, width + 35 + i, 9 + i, true, Color.DARK_GRAY.darker());
                        RenderUtil.rect(x - 1 - i / 2f, y - 1 - i / 2f, width + 34 + i, 8 + i, true, Color.DARK_GRAY);
                    }

                    RenderUtil.rect(x - 1, y - 1, width + 34, 8, true, Color.DARK_GRAY.darker().darker());

                    skeet.drawString(clientName, x + 2, y + 0.5f, informationColor);
                    skeet.drawString("sense", (x + 2) + skeet.getWidth("starx") - 2, y + 0.5f, StarX.CLIENT_THEME_COLOR_BRIGHT);

                    x += skeet.getHeight(clientName) + 43;
                    skeet.drawString("|", x - 12, y + 0.5f, new Color(255, 255, 255, 150).hashCode());
                    skeet.drawString(Minecraft.getDebugFPS() + " fps", x - 6, y + 0.5f, informationColor);

                    x += skeet.getWidth(Minecraft.getDebugFPS() + " fps") + 6;
                    skeet.drawString("|", x - 12, y + 0.5f, new Color(255, 255, 255, 150).hashCode());
                    skeet.drawString(responseTime + "ms", x - 6, y + 0.5f, informationColor);

                    x += skeet.getWidth(responseTime + "ms") + 6;
                    skeet.drawString("|", x - 12, y + 0.5f, new Color(255, 255, 255, 150).hashCode());
                    skeet.drawString(username, x - 6, y + 0.5f, informationColor);

                    if (shouldRenderCustomClientName) {
                        final float customWidth = skeet.getWidth(customName) + 4;
                        final float customX = 5;
                        final float customY = 20;

                        for (int i = 1; i <= 4; i++) {
                            RenderUtil.rect(customX - 1.5 - i / 2f, customY - 1.5 - i / 2f, customWidth + 1 + i, 9 + i, true, Color.DARK_GRAY.darker());
                            RenderUtil.rect(customX - 1 - i / 2f, customY - 1 - i / 2f, customWidth + i, 8 + i, true, Color.DARK_GRAY);
                        }

                        RenderUtil.rect(customX - 1, customY - 1, customWidth, 8, true, Color.DARK_GRAY.darker().darker());

                        skeet.drawString(customName, customX + 2, customY + 0.5f, informationColor);
                    }
                }
                break;
            }
        }
    }


    public static void renderGameOverlay(float partialTicks) {
        renderKeyStrokes();
        renderBPS();
        renderClientName();
        renderArrayList();
    }
}
