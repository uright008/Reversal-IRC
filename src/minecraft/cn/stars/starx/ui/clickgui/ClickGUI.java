package cn.stars.starx.ui.clickgui;

import cn.stars.starx.StarX;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.impl.render.ClickGui;
import cn.stars.starx.module.impl.render.GuiAnimation;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NoteValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.math.MathUtil;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.render.InGameBlurUtil;
import cn.stars.starx.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Year;
import java.util.List;
import java.util.*;

public final class ClickGUI extends GuiScreen /*implements ClickGUIType */ {

    private float x, y, size;

    //Font
    private static final TTFFontRenderer icon = CustomFont.FONT_MANAGER.getFont("Icon 18");
    private static final TTFFontRenderer icon2 = CustomFont.FONT_MANAGER.getFont("Icon2 18");
    private static final TTFFontRenderer gs = CustomFont.FONT_MANAGER.getFont("GoogleSans 40");

    private float width = 320;
    private static float height = 260;

    private float categoryWidth;
    private static float categoryHeight;

    private Category selectedCat = Category.COMBAT;

    private float moduleWidth;
    private float moduleHeight;

    private float offset;
    private float heightOffset;

    private final String separator = File.separator;

    private static float scrollAmount, lastScrollAmount, lastLastScrollAmount;
    private static float renderScrollAmount;

    private final TimeUtil timer = new TimeUtil();
    private final TimeUtil timer2 = new TimeUtil();

    public static NumberValue selectedSlider;

    public static float firstModulePosition;
    public static float lastModulePosition;

    private boolean holdingGui, resizingGui;
    private float holdingOffsetX, holdingOffsetY;

    private float renderSelectY;

    private boolean hasEditedSliders;

    private List<String> configs;

    public boolean draggingRadar;
    public int oldMouseX;
    public int oldMouseY;

    Color colorModules;
    Color colorCategory;
    Color colorTop;
    Color selectedCatColor;
    Color booleanColor1;
    Color booleanColor2;
    Color settingColor3;
    Color opacityColor;

    int test;
    int customHue;

//    List<OnlineScriptHandler.OnlineScript> scriptList;
//    private ScriptState scriptState = ScriptState.NONE;

    private final static TTFFontRenderer fontBig = CustomFont.FONT_MANAGER.getFont("Light 19");
    private final static TTFFontRenderer fontLarge = CustomFont.FONT_MANAGER.getFont("Light 24");
    private final static TTFFontRenderer fontExtraLarge = CustomFont.FONT_MANAGER.getFont("Light 36");

    double gap;

    double positionXOfScript;
    double positionYOfScript;
    double widthOfScript;
    double heightOfScript;

    public boolean blockScriptEditorOpen;

    public ClickGUI() {
       /* try {
            AuthGUI.getClipboardString();
        } catch (final Throwable t) {
            while (5476 < 295728735) {

            }
        } */
    }

    @Override
    public void initGui() {
        size = GuiAnimation.startingSizeValue;

        holdingGui = false;

        resizingGui = false;

        for (final Module m : StarX.INSTANCE.getModuleManager().getModuleList()) {
            for (final Setting s : m.getSettings()) {
                if (s instanceof NumberValue) {
                    final NumberValue NumberValue = ((NumberValue) s);
                    NumberValue.renderPercentage = Math.random();
                }
            }
        }

        hasEditedSliders = false;
        blockScriptEditorOpen = false;

        final RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        final List<String> arguments = runtimeMxBean.getInputArguments();

        int i = 0;
        for (final Object s : arguments.toArray()) {
            i++;
            //Rise.addChatMessage(s + " " + i);
        }
        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.pushMatrix();

        final boolean blur = ((BoolValue) Objects.requireNonNull(StarX.INSTANCE.getModuleManager().getSetting("ClickGui", "Blur"))).isEnabled();

        if (blur) {
            InGameBlurUtil.postBlur(5,2);
        }

        final ScaledResolution sr = new ScaledResolution(mc);

        final boolean canPopUp = Objects.requireNonNull(StarX.INSTANCE.getModuleManager().getModule("GuiAnimation")).isEnabled() && GuiAnimation.clickGuiValue;

        if (canPopUp) {
            size = (float) MathUtil.lerp(size, 1, GuiAnimation.speedValue / (Minecraft.getDebugFPS() / 20D));

            GlStateManager.translate((x - x * size) + (width / 2F - width / 2F * size), (y - y * size) + (height / 2F - height / 2F * size), 0);
            GlStateManager.scale(size, size, 1);
        } else {
            size = 1;
        }

        if (resizingGui) {

            width = 320;
            height = 260;

            while (width < mouseX - x) {
                width += 1;
            }

            while (height < mouseY - y) {
                height += 1;
            }
        }

        categoryWidth = 70;
        categoryHeight = 20;

        colorModules = new Color(39, 42, 48, 255);
        colorCategory = new Color(38, 39, 44, 255);
        colorTop = new Color(39, 42, 49, 255);
        selectedCatColor = new Color(68, 134, 240, 255);
        booleanColor1 = new Color(60, 90, 135, 255);
        booleanColor2 = new Color(68, 134, 240, 255);
        settingColor3 = new Color(70, 100, 145, 255);
        opacityColor = new Color(38, 39, 44, 220);

        final ModeValue theme = (ModeValue) StarX.INSTANCE.getModuleManager().getSetting("ClickGui", "Theme");

        customHue = 0;

        switch (Objects.requireNonNull(theme).getMode()) {
            case "Rural Amethyst":
                customHue = 265;
                break;

            case "Alyssum Pink":
                customHue = 330;
                break;

            case "Sweet Grape Vine":
                customHue = 130;
                selectedCatColor = new Color(25, 91, 197, 255);
                break;

            case "Orchid Aqua":
                customHue = 200;
                break;

            case "Disco":
                customHue = (test++);
                if (test > 359) test = 0;
                break;
        }

        if (!theme.is("Deep Blue")) {
            colorModules = changeHue(colorModules, customHue / 360f);
            colorCategory = changeHue(colorCategory, customHue / 360f);
            colorTop = changeHue(colorTop, customHue / 360f);
            selectedCatColor = changeHue(selectedCatColor, customHue / 360f);
            booleanColor1 = changeHue(booleanColor1, customHue / 360f);
            booleanColor2 = changeHue(booleanColor2, customHue / 360f);
            settingColor3 = changeHue(settingColor3, customHue / 360f);
            opacityColor = changeHue(opacityColor, customHue / 360f);
        }

        if (((BoolValue) Objects.requireNonNull(StarX.INSTANCE.getModuleManager().getSetting("ClickGui", "Transparency"))).isEnabled()) {
            colorCategory = new Color(opacityColor.getRed(), opacityColor.getGreen(), opacityColor.getBlue(), 220);
        }

        // Background
        RenderUtil.roundedRectCustom(x + categoryWidth, y + categoryHeight, width - categoryWidth, height - categoryHeight, 10, colorModules, false, false, false, true);

        // Category background
        RenderUtil.roundedRectCustom(x, y, categoryWidth, height, 10, colorCategory, true, false, true, false);

        // Above
        RenderUtil.roundedRectCustom(x + categoryWidth, y, width - categoryWidth, categoryHeight, 10, colorTop, false, true, false, false);

        //Logo
        CustomFont.drawStringBig(StarX.NAME, x + 8, y + 1f, new Color(237, 237, 237).getRGB());
        //CustomFont.drawString(Rise.CLIENT_VERSION, x + 46, y + 3.0, new Color(237, 237, 237).getRGB());

        // Handle the selected category.
        int i = 0;
        for (final Category category : Category.values()) {
            if (category == selectedCat) {
                if (timer2.hasReached(1000 / 120)) {
                    timer2.reset();
                    renderSelectY = MathUtil.lerp(renderSelectY, categoryHeight * (i + 1), 0.15F);
                }

                RenderUtil.rect(x, y + renderSelectY, categoryWidth, categoryHeight, selectedCatColor);
            }

            ++i;
        }

        int amount = 0;
        for (final Category c : Category.values()) {

            final Color color = new Color(237, 237, 237);

            switch (c) {
                case COMBAT: {
                    icon.drawString("a", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 - 3.5F, color.hashCode());
                    break;
                }
                case MOVEMENT: {
                    icon.drawString("b", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 - 3.5F, color.hashCode());
                    break;
                }
                case PLAYER: {
                    icon.drawString("c", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 - 3.5F, color.hashCode());
                    break;
                }
                case RENDER: {
                    icon2.drawString("D", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 - 3.5F, color.hashCode());
                    break;
                }
                case WORLD: {
                    icon.drawString("f", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 - 3.5F, color.hashCode());
                    break;
                }
                case ADDONS: {
                    icon.drawString("e", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 - 3.5F, color.hashCode());
                    break;
                }
                case MISC: {
                    icon2.drawString("H", x + 5, y + categoryHeight * (amount + 1) + categoryHeight / 2 - 3.5F, color.hashCode());
                    break;
                }
            }


            CustomFont.drawString(StringUtils.capitalize(c.name().toLowerCase()), x + 18, y + categoryHeight * (amount + 1) + categoryHeight / 2 - 4.5, color.hashCode());

            ++amount;
        }

        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor((x - (categoryWidth - categoryWidth * size)) + categoryWidth, (y + (height / 2F) - (height / 2F) * size) + categoryHeight * size, width - categoryWidth * size, ((height - categoryHeight) * size));

        moduleWidth = width - categoryWidth;
        moduleHeight = 20;
        offset = 5;

        //Modules
        heightOffset = 0;
        firstModulePosition = 9999999;
        amount = 0;

        switch (selectedCat) {

            default:
                for (final Module m : StarX.INSTANCE.getModuleManager().getModuleList()) {
                    if (m.isHidden()) continue;
                    m.sizeInGui = moduleHeight;

                    final Category c = m.getModuleInfo().category();

                    if (c == selectedCat) {


                            if (firstModulePosition == 9999999)
                                firstModulePosition = categoryHeight + heightOffset + offset * amount + renderScrollAmount;

                            //Settings
                            if (m.expanded) {
                                m.sizeInGui = categoryHeight;

                                for (final Setting s : m.getSettings()) {

                                    if (!s.isHidden()) {

                                        final float fontWidth = CustomFont.getWidth(s.name) + 5;

                                        final float settingsX = x + categoryWidth + offset + 4;
                                        final float settingsY = y + categoryHeight + heightOffset + +offset * amount + m.sizeInGui + renderScrollAmount;

                                        if (s instanceof NoteValue) {
                                            CustomFont.drawString(s.name, settingsX, settingsY, new Color(237, 237, 237, 150).getRGB());
                                        } else {
                                            CustomFont.drawString(s.name, settingsX, settingsY, new Color(237, 237, 237).getRGB());
                                        }

                                        if (s instanceof BoolValue) {
                                            RenderUtil.circle(settingsX + fontWidth, settingsY + 1.5, 7, false, booleanColor1);

                                            if (((BoolValue) s).isEnabled()) {
                                                RenderUtil.circle(settingsX + fontWidth + 1.25, settingsY + 1.5 + 1.25, 4.5, true, booleanColor2);
                                            }
                                        }

                                        if (s instanceof NumberValue) {
                                            final NumberValue NumberValue = ((NumberValue) s);

                                            if (selectedSlider == s) {

                                                final double percent = (double) (mouseX - (settingsX + fontWidth)) / (double) (100);
                                                double value = NumberValue.minimum - percent * (NumberValue.minimum - NumberValue.maximum);

                                                if (value > NumberValue.maximum) value = NumberValue.maximum;
                                                if (value < NumberValue.minimum) value = NumberValue.minimum;

                                                NumberValue.value = value;

                                                if (NumberValue.getIncrement() != 0)
                                                    NumberValue.value = round(value, (float) NumberValue.increment);
                                                else NumberValue.value = value;

                                                hasEditedSliders = true;
                                            }

                                            NumberValue.percentage = (((NumberValue) s).value - ((NumberValue) s).minimum) / (((NumberValue) s).maximum - ((NumberValue) s).minimum);

                                            RenderUtil.rect(settingsX + fontWidth, settingsY + 3.5, 100, 2, booleanColor1);
                                            RenderUtil.roundedRect(settingsX + fontWidth + NumberValue.renderPercentage * 100, settingsY + 2, 5, 5, 5, settingColor3);

                                            String value = String.valueOf((float) round(NumberValue.value, (float) NumberValue.increment));

                                            if (NumberValue.increment == 1) {
                                                value = value.replace(".0", "");
                                            }

                                            if (NumberValue.getReplacements() != null) {
                                                for (final String replacement : NumberValue.getReplacements()) {
                                                    final String[] split = replacement.split("-");
                                                    value = value.replace(split[0], split[1]);
                                                }
                                            }

                                            CustomFont.drawString(value, settingsX + fontWidth + 109, settingsY, new Color(237, 237, 237, 235).hashCode());
                                        }

                                        if (s instanceof ModeValue) {
                                            CustomFont.drawString(((ModeValue) s).getModes().get(((ModeValue) s).index), settingsX + fontWidth, settingsY, new Color(237, 237, 237, 255).getRGB());
                                        }

                                        m.sizeInGui += 12;
                                        updateRainbow(theme);
                                    }
                                }
                            }

                            final float startModuleRenderY = y + categoryHeight;
                            final float moduleRenderY = startModuleRenderY + heightOffset + offset * amount + renderScrollAmount;

                            if (moduleRenderY > startModuleRenderY - m.sizeInGui) {
                                if (moduleRenderY < startModuleRenderY + height) {
                                    renderModule(x + categoryWidth + offset, moduleRenderY, moduleWidth - offset * 2, m.sizeInGui, m);
                                }
                            }
                            lastModulePosition = categoryHeight + heightOffset + offset * amount + renderScrollAmount + m.sizeInGui;

                            heightOffset += m.sizeInGui;

                            amount++;

                            if (m.isEnabled()) updateRainbow(theme);
                        }
                }
                break;
        }


        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        //Selected category
        final String categoryName = StringUtils.capitalize(selectedCat.name().toLowerCase());
        CustomFont.drawString(categoryName, x + categoryWidth + 3, y + 5.5, new Color(237, 237, 237).getRGB());

        if (timer.hasReached(1000 / 100)) {
            timer.reset();

            for (final Module m : StarX.INSTANCE.getModuleManager().getModuleList()) {
                for (final Setting s : m.getSettings()) {
                    if (s instanceof NumberValue) {
                        final NumberValue NumberValue = ((NumberValue) s);

                        if (hasEditedSliders) {
                            NumberValue.renderPercentage = (NumberValue.renderPercentage + NumberValue.percentage) / 2;
                        } else {
                            NumberValue.renderPercentage = (NumberValue.renderPercentage * 4 + NumberValue.percentage) / 5;
                        }

                    }
                }

                //Grey out
                if (!(m.isEnabled() || m.isExpanded() && m.settings.size() > 0)) {
                    m.clickGuiOpacity += 6;

                    if (m.clickGuiOpacity > 90) {
                        m.clickGuiOpacity = 90;
                    }
                } else {
                    m.clickGuiOpacity -= 6;

                    if (m.clickGuiOpacity < 1) {
                        m.clickGuiOpacity = 1;
                    }
                }

                heightOffset = 0;
                amount = 0;
                for (final Module module : StarX.INSTANCE.getModuleManager().getModuleList()) {

                    if (module.getModuleInfo().category() == selectedCat) {
                        if (mouseOver(x + categoryWidth, y + categoryHeight + heightOffset + offset * amount + renderScrollAmount, moduleWidth, moduleHeight, mouseX, mouseY)) {
                            module.descOpacityInGui += 0.1;
                        } else {
                            module.descOpacityInGui -= 0.1;
                        }

                        module.descOpacityInGui = Math.max(0, Math.min(255, module.descOpacityInGui));

                        heightOffset += module.sizeInGui;
                        amount++;
                    }
                }
            }

            if (firstModulePosition > categoryHeight && lastModulePosition > height - categoryHeight) {
                scrollAmount *= 0.8;
            }

            if (lastModulePosition < height - categoryHeight && firstModulePosition < categoryHeight) {
                scrollAmount += ((height - categoryHeight) - lastModulePosition) * 0.14;
            }
        }

        renderScrollAmount = (float) (lastScrollAmount + (scrollAmount - lastScrollAmount) * mc.timer.renderPartialTicks * ClickGui.speedValue);

        if (holdingGui) {
            x = mouseX + holdingOffsetX;
            y = mouseY + holdingOffsetY;
        }


      /*  if (draggingRadar) {
            int x = mouseX + oldMouseX;
            int y = mouseY + oldMouseY;

            if (mouseX < (sr.getScaledWidth() / 2f) + 17.5 && mouseX > (sr.getScaledWidth() / 2f) - 17.5) {
                x = sr.getScaledWidth() / 2 - 35;
                RenderUtil.rect(sr.getScaledWidth() / 2f - 0.5, 0, 0.5, sr.getScaledHeight(), Color.GREEN);
            }

            if (mouseY < (sr.getScaledHeight() / 2f) + 17.5 && mouseY > (sr.getScaledHeight() / 2f) - 17.5) {
                y = sr.getScaledHeight() / 2 - 35;
                RenderUtil.rect(0, sr.getScaledHeight() / 2f - 0.5, sr.getScaledWidth(), 0.5, Color.RED);
            }

//            if(y > (sr.getScaledHeight() / 2) - 35 || y < (sr.getScaledHeight() / 2) + 35)
//                y = sr.getScaledHeight() / 2;

            if (x >= sr.getScaledWidth() - 70) {
                x = sr.getScaledWidth() - 70;
            } else if (x <= 0) {
                x = 0;
            }

            if (y >= sr.getScaledHeight() - 70) {
                y = sr.getScaledHeight() - 70;
            } else if (y <= 0) {
                y = 0;
            }


            ((NumberValue) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Radar", "Radar X"))).setValue(x);
            ((NumberValue) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("Radar", "Radar Y"))).setValue(y);
        } */

        GlStateManager.popMatrix();
    }

//    public void drawScript(final OnlineScriptHandler.OnlineScript script, final double x, final double y, final double width, final double height) {
//        if (script != null) {
//            RenderUtil.roundedRect(x, y, width, height, 9, new Color(255, 255, 255, 10));
//
//            final String scriptName = StringUtils.capitalize(script.getName());
//            final String scriptAuthor = script.getAuthor();
//
//            fontBig.drawString(scriptName, (float) x + 5, (float) y + 5, new Color(237, 237, 237, 237).getRGB());
//            CustomFont.drawString("by " + scriptAuthor, (float) x + 5, (float) y + 16, new Color(237, 237, 237, 177).getRGB());
//
//            RenderUtil.imageCentered(new ResourceLocation("rise/icon/downloadIcon.png"), (int) (x + width - 10), (float) (y + height - 10), 14 / 1.2f, 13 / 1.2f);
//        }
//    }

    @Override
    public void updateScreen()
    {
        updateScroll();
    }

    public static Color changeHue(Color c, final float hue) {

        // Get saturation and brightness.
        final float[] hsbVals = new float[3];
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbVals);

        // Pass .5 (= 180 degrees) as HUE
        c = new Color(Color.HSBtoRGB(hue, hsbVals[1], hsbVals[2]));

        return c;
    }

    public static void updateScroll() {
        lastLastScrollAmount = lastScrollAmount;
        lastScrollAmount = scrollAmount;

        if (lastModulePosition - renderScrollAmount > height - categoryHeight) {

            final float wheel = Mouse.getDWheel();

            scrollAmount += wheel / 10.0F;

            if (wheel == 0) {
                scrollAmount -= (lastLastScrollAmount - scrollAmount) * 0.6;
            }

        } else {
            scrollAmount = 0;
        }
    }

    public void renderModule(final float x, final float y, final float width, final float height, final Module m) {
        //Module background
        RenderUtil.roundedRect(x, y, width, height, 5, new Color(255, 255, 255, 10));

        //Module name
        CustomFont.drawString(m.getModuleInfo().name(), x + 4, y + 6, ((m.isEnabled()) ? booleanColor2 : new Color(237, 237, 237)).getRGB());

        //Switch
        if (!m.getModuleInfo().name().equals("Interface")) {
            RenderUtil.roundedRect(x + width - 15, y + 8, 10, 5, 5, new Color(255, 255, 255, 255));
            RenderUtil.circle(x + width - ((m.isEnabled()) ? 10 : 17), y + 7, 7, booleanColor1);
        }

        if (m.clickGuiOpacity != 1)
            RenderUtil.roundedRect(x, y, width, height, 5, new Color(39, 42, 48, Math.round(m.clickGuiOpacity)));

        //Module description
        if (m.descOpacityInGui > 1)
            CustomFont.drawStringSmall(m.getModuleInfo().description(), x + (CustomFont.getWidth(m.getModuleInfo().name())) + 6, y + 8, new Color(175, 175, 175, Math.round(m.descOpacityInGui)).getRGB());

    }

//    public void renderScript(final float x, final float y, final float width, final float height, final Script script) {
//        //Module background
//        RenderUtil.roundedRect(x, y, width, height, 5, new Color(255, 255, 255, 10));
//
//        //Module name
//        CustomFont.drawString(script.getName(), x + 4, y + 6, ((script.isEnabled()) ? booleanColor2 : new Color(237, 237, 237)).getRGB());
//
//        //Switch
//        RenderUtil.roundedRect(x + width - 15, y + 8, 10, 5, 5, new Color(255, 255, 255, 255));
//        RenderUtil.circle(x + width - ((script.isEnabled()) ? 10 : 17), y + 7, 7, booleanColor1);
//
//        if (script.clickGuiOpacity != 1)
//            RenderUtil.roundedRect(x, y, width, height, 5, new Color(39, 42, 48, Math.round(script.clickGuiOpacity)));
//    }

    public void renderModule(final float x, final float y, final float width, final float height, final String n) {
        //Module background
        RenderUtil.roundedRect(x, y, width, height, 5, new Color(255, 255, 255, 10));

        //Module name
        CustomFont.drawString(n, x + 4, y + 6, booleanColor2.hashCode());
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        final int grabSize = 10;

//        if (blockScriptEditorOpen) {
//            GuiBlockScript.mouseClicked(mouseX, mouseY, button);
//            return;
//        }

        if (mouseOver(x + width - grabSize + 3, y + height - grabSize + 3, grabSize, grabSize, mouseX, mouseY)) {
            resizingGui = true;
            return;
        }

        int amount = 0;
        for (final Category c : Category.values()) {
            if (mouseOver(x, y + categoryHeight * (amount + 1), categoryWidth, categoryHeight, mouseX, mouseY) && selectedCat != c) {

//                if (c == Category.STORE) {
//                    scriptState = ScriptState.NONE;
//                    scriptList = null;
//
//                    if (button == 1) {
//                        blockScriptEditorOpen = true;
//                        GuiBlockScript.onInit();
//                    }
//                }

                selectedCat = c;

                scrollAmount = 0;
                renderScrollAmount = 0;
                lastScrollAmount = -30;
                lastLastScrollAmount = -30;

                scrollAmount = 0;

                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));

                for (final Module m : StarX.INSTANCE.getModuleManager().getModuleList()) {
                    for (final Setting s : m.getSettings()) {
                        if (s instanceof NumberValue) {
                            final NumberValue NumberValue = ((NumberValue) s);
                            NumberValue.renderPercentage = Math.random();
                        }
                    }
                }

            }

            ++amount;
        }

        //Modules
        heightOffset = 0;
        amount = 0;

        if (mouseOver(x + categoryWidth, y + categoryHeight, width - categoryWidth, height - categoryHeight, mouseX, mouseY)) {
            for (final Module m : StarX.INSTANCE.getModuleManager().getModuleList()) {
                if (m.isHidden()) continue;
                m.sizeInGui = moduleHeight;

                if (m.expanded) {
                    m.sizeInGui = categoryHeight;

                    for (final Setting ignored : m.getSettings()) {
                        m.sizeInGui += 12;
                    }
                }

                if (m.getModuleInfo().category() == selectedCat) {
                    if (m.expanded) {
                        m.sizeInGui = categoryHeight;

                        for (final Setting s : m.getSettings()) {

                            if (!s.isHidden()) {

                                final float settingsX = x + categoryWidth + offset + 4;
                                final float settingsY = y + categoryHeight + heightOffset + offset * amount + m.sizeInGui + renderScrollAmount;

                                if (mouseOver(settingsX, settingsY, width - categoryWidth - offset * 3, 11, mouseX, mouseY)) {
                                    if (s instanceof BoolValue) {
                                        if (button == 0) ((BoolValue) s).toggle();
                                    }
                                    if (s instanceof NumberValue) {
                                        if (button == 0) selectedSlider = (NumberValue) s;
                                    }
                                    if (s instanceof ModeValue) {
                                        if (button == 0) ((ModeValue) s).cycle(true);
                                        if (button == 1) ((ModeValue) s).cycle(false);
                                    }
                                }

                                m.sizeInGui += 12;
                            }
                        }
                    }

                    if (mouseOver(x + categoryWidth, y + categoryHeight + heightOffset + offset * amount + renderScrollAmount, moduleWidth, moduleHeight, mouseX, mouseY)) {
                        if (button == 0 && !m.getModuleInfo().name().equals("ClickGui")) {
                            m.toggleModule();
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        }
                        if (button == 1) {
                            m.expanded = !m.expanded;
                        }
                    }

                    heightOffset += m.sizeInGui;

                    amount++;
                }
            }

//            if (selectedCat == Category.SCRIPTS) {
//                amount = 0;
//                for (final Script script : Rise.INSTANCE.getScriptManager().getScripts()) {
//                    if (mouseOver(x + categoryWidth, y + categoryHeight + moduleHeight * amount + offset * amount + renderScrollAmount, moduleWidth, moduleHeight, mouseX, mouseY)) {
//                        if (button == 0) {
//                            script.toggleScript();
//                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
//                        }
//                        if (button == 1) {
//                            script.expanded = !script.expanded;
//                        }
//                    }
//
//                    heightOffset += 12;
//                    ++amount;
//                }
//            }
        }

        if (mouseOver(x, y, width, categoryHeight, mouseX, mouseY)) {
            holdingGui = true;
            holdingOffsetX = x - mouseX;
            holdingOffsetY = y - mouseY;
        }

     //   final int radarX = (int) ((NumberValue) Objects.requireNonNull(Solitary.INSTANCE.getModuleManager().getSetting("Radar", "Radar X"))).getValue();
     //   final int radarY = (int) ((NumberValue) Objects.requireNonNull(Solitary.INSTANCE.getModuleManager().getSetting("Radar", "Radar Y"))).getValue();

     //   if (mouseOver(radarX, radarY, 70, 70, mouseX, mouseY)) {
     //       oldMouseX = radarX - mouseX;
     //       oldMouseY = radarY - mouseY;
     //       draggingRadar = true;
     //   }

    }

    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        selectedSlider = null;
        holdingGui = resizingGui = false;
        draggingRadar = false;
        //if (blockScriptEditorOpen) GuiBlockScript.releasedMouseButton();
        super.mouseReleased(mouseX, mouseY, state);
    }

    public boolean mouseOver(final float posX, final float posY, final float width, final float height, final float mouseX, final float mouseY) {
        return mouseX > posX && mouseX < posX + width && mouseY > posY && mouseY < posY + height;
    }

    @Override
    public void onGuiClosed() {
        selectedSlider = null;
        holdingGui = resizingGui = false;
        Objects.requireNonNull(StarX.INSTANCE.getModuleManager().getModule("ClickGui")).toggleModule();
    }

//    private static double round(final double value, final int places) {
//        if (places < 0) throw new IllegalArgumentException();
//
//        BigDecimal bd = new BigDecimal(Double.toString(value));
//        bd = bd.setScale(places, RoundingMode.HALF_UP);
//        return bd.doubleValue();
//    }

    private static double round(final double value, final float places) {
        if (places < 0) throw new IllegalArgumentException();

        final double precision = 1 / places;
        return Math.round(value * precision) / precision;
    }

    private long days(final String date) {
        // creating the date 1 with sample input date.
        final Date date1 = new Date(Year.now().getValue(), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.DATE));

        // creating the date 2 with sample input date.
        final String[] split = date.split("/");

        final Date date2 = new Date(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0]));

        // getting milliseconds for both dates
        final long date1InMs = date1.getTime();
        final long date2InMs = date2.getTime();

        // getting the diff between two dates.
        long timeDiff = 0;
        if (date1InMs > date2InMs) {
            timeDiff = date1InMs - date2InMs;
        } else {
            timeDiff = date2InMs - date1InMs;
        }

        // print diff in days
        return (int) (timeDiff / (1000 * 60 * 60 * 24));
    }

    private void updateRainbow(final ModeValue theme) {
        if (theme.is("Disco")) {
            customHue = customHue > 360 ? 0 : customHue + 9;
            colorModules = changeHue(colorModules, customHue / 360f);
            booleanColor1 = changeHue(booleanColor1, customHue / 360f);
            booleanColor2 = changeHue(booleanColor2, customHue / 360f);
            settingColor3 = changeHue(settingColor3, customHue / 360f);
        }
    }
}
