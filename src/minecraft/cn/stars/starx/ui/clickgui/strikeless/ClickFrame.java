package cn.stars.starx.ui.clickgui.strikeless;

import cn.stars.starx.GameInstance;
import cn.stars.starx.StarX;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.impl.render.ClickGui;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.*;
import cn.stars.starx.util.render.RenderUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.time.Year;
import java.util.List;
import java.util.*;

public class ClickFrame implements GameInstance {

    private static TTFFontRenderer comfortaaBig = CustomFont.FONT_MANAGER.getFont("Comfortaa 27");
    private static TTFFontRenderer comfortaaNormal = CustomFont.FONT_MANAGER.getFont("Comfortaa 18");
    private static TTFFontRenderer gssmall = CustomFont.FONT_MANAGER.getFont("GoogleSans 15");
    private static TTFFontRenderer check = CustomFont.FONT_MANAGER.getFont("Check 18");

    public static float entryWidth = 132;

    public static float entryHeight = 22;

    public static int maximumEntries = 15;

    public static float scale = 0.8F;

    private static List<String> configs;

    private final Category category;

    private final List<Module> modules;

    private float frameX, frameY, mouseX, mouseY, dragX, dragY;

    private float scrollVertical, lastScrollVertical;

    private float previousHeight;

    private boolean expanded, dragged;

    private boolean leftClick, rightClick;

    private final List<Module> expandedModuleIndices;

    private int customHue;

    private Color background, backgroundDarker, backgroundDarkest, accent, accentDarker, accentDarkest, shadow;

    public ClickFrame(final Category category, final float frameX, final float frameY) {
        this.modules = StarX.INSTANCE.getModuleManager().getModulesByCategory(category);
        this.expandedModuleIndices = new ArrayList<>();

        this.category = category;
        this.frameX = frameX;
        this.frameY = frameY;
    }

    public void draw(final StrikeGUI parent, final int mouseX, final int mouseY) {
        /*
         * Advanced Rise(R) Client(tm) color utility systems incorporated corporation limited
         */

        /* Setting objects used to calculate size of gui */
        scale = (float) this.getNumber(ClickGui.class, "Scale");
        entryWidth = 132 * scale;
        entryHeight = 22 * scale;
        comfortaaBig = CustomFont.FONT_MANAGER.getFont("Comfortaa " + (int) (27 * scale));
        comfortaaNormal = CustomFont.FONT_MANAGER.getFont("Comfortaa " + (int) (18 * scale));

        accent = new Color(65, 68, 217);
        accentDarker = new Color(46, 48, 153);
        accentDarkest = new Color(34, 35, 115);
        background = new Color(38, 35, 41);
        backgroundDarker = new Color(27, 24, 30);
        backgroundDarkest = new Color(15, 12, 18);

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
                break;

            case "Orchid Aqua":
                customHue = 200;
                break;

            case "Disco":
                theme.cycle(true);
                StarX.INSTANCE.getNotificationManager().registerNotification("Disco Theme doesn't work with Dropdown ClickGui.");
                break;
        }

        if (!theme.is("Deep Blue")) {
            backgroundDarkest = changeHue(backgroundDarkest, customHue / 360f);
            backgroundDarker = changeHue(backgroundDarker, customHue / 360f);
            background = changeHue(background, customHue / 360f);
            accentDarkest = changeHue(accentDarkest, customHue / 360f);
            accentDarker = changeHue(accentDarker, customHue / 360f);
            accent = changeHue(accent, customHue / 360f);
        }

        shadow = new Color(backgroundDarker.getRed(), backgroundDarker.getGreen(), backgroundDarker.getBlue(), 100);

        /*
         * Scrolling
         */
        if (mouseX >= frameX + StrikeGUI.scrollHorizontal && mouseX <= frameX + StrikeGUI.scrollHorizontal + entryWidth && !GuiInventory.isCtrlKeyDown()) {
            final float partialTicks = mc == null || mc.timer == null ? 1.0F : mc.timer.renderPartialTicks;

            final float lastLastScrollHorizontal = lastScrollVertical;
            lastScrollVertical = scrollVertical;
            final float wheel = Mouse.getDWheel();
            scrollVertical += wheel / 10.0F;
            if (wheel == 0) scrollVertical -= (lastLastScrollHorizontal - scrollVertical) * 0.6 * partialTicks;

            final float minScroll = maximumEntries * entryHeight - previousHeight;
            if (scrollVertical < minScroll) scrollVertical = minScroll;
            if (scrollVertical > 0.0F) scrollVertical = 0.0F;
        }

        /*
         * Now the ClickGUI itself
         */
        final float textOffsetBig = gsTitle.getHeight() / 2.0F;
        final float textOffsetNormal = gs.getHeight() / 2.0F;

        this.mouseX = mouseX;
        this.mouseY = mouseY;

        if (dragged) {
            this.frameX += this.mouseX - this.dragX;
            this.frameY += this.mouseY - this.dragY;
            this.dragX = this.mouseX;
            this.dragY = this.mouseY;
        }

        final float frameX = this.frameX + StrikeGUI.scrollHorizontal;
        final float frameY = this.frameY;

        // Draw the top bar
        RenderUtil.rect(frameX, frameY, entryWidth, entryHeight, backgroundDarker);
        gsTitle.drawCenteredString(
                StringUtils.capitalize(category.name().toLowerCase(Locale.ENGLISH)),
                frameX + entryWidth / 2.0F, frameY + entryHeight / 2.0F - textOffsetBig, Color.WHITE.getRGB()
        );

        if (expanded) {
            float currentY = frameY;

            RenderUtil.enable(GL11.GL_SCISSOR_TEST);
            RenderUtil.scissor(frameX, currentY + entryHeight, entryWidth, maximumEntries * entryHeight);

            for (final Module module : modules) {
                if (module.isHidden()) continue;
                currentY += entryHeight;

                float moduleY = currentY + scrollVertical;

                final Color backgroundColor = module.isEnabled()
                        ? getAccentColor(frameX, moduleY, entryWidth, entryHeight)
                        : getBackgroundColor(frameX, moduleY, entryWidth, entryHeight, false);

                RenderUtil.rect(frameX, moduleY, entryWidth, entryHeight, backgroundColor);

                float titleY = moduleY + entryHeight / 2.0F - textOffsetNormal;
                gs.drawString(module.getModuleInfo().name(), frameX + 4.0F, titleY, Color.WHITE.getRGB());

                // Draw the settings
                final List<Setting> settings = module.getSettings();
                if (settings.size() > 0) {
                    final boolean settingsExpanded = expandedModuleIndices.contains(module);
                    gs.drawString(settingsExpanded ? "-" : "+", frameX + entryWidth - 8.0F, titleY, Color.WHITE.getRGB());

                    if (settingsExpanded) {
                        boolean isFirst = true;
                        for (final Setting setting : settings) {
                            if (setting.isHidden()) continue;

                            moduleY += entryHeight;
                            titleY += entryHeight;

                            // Thank you rise base. very nice.
                            if (setting instanceof BoolValue) {
                                final Color settingColor = ((BoolValue) setting).isEnabled()
                                        ? getAccentColor(frameX, moduleY, entryWidth, entryHeight)
                                        : getBackgroundColor(frameX, moduleY, entryWidth, entryHeight, true);
                                RenderUtil.rect(frameX, moduleY, entryWidth, entryHeight, settingColor);

                                if (((BoolValue) setting).isEnabled())
                                    check.drawString("o" ,frameX + 2.0F, titleY + 1f, Color.WHITE.getRGB());
                                else
                                    check.drawString("p" ,frameX + 2.5F, titleY + 1f, Color.WHITE.getRGB());
                                gs.drawString(setting.getName(), frameX + 13.0F, titleY, Color.WHITE.getRGB());
                            } else if (setting instanceof ModeValue) {
                                RenderUtil.rect(frameX, moduleY, entryWidth, entryHeight, getBackgroundColor(frameX, moduleY, entryWidth, entryHeight, true));

                                final ModeValue arraySetting = (ModeValue) setting;
                                check.drawString("b" ,frameX + 2.0F, titleY + 1f, Color.WHITE.getRGB());
                                gs.drawString(arraySetting.getName() + ": " + arraySetting.getMode(), frameX + 13.0F, titleY, Color.WHITE.getRGB());
                            } else if (setting instanceof NumberValue) {
                                final NumberValue NumberValue = (NumberValue) setting;

                                final double fromZeroValue = NumberValue.getValue() - Math.abs(NumberValue.getMinimum());
                                final float normalizedValue = (float) (fromZeroValue / (float) (NumberValue.getMaximum() - NumberValue.getMinimum()));
                                final float screenSpaceValue = normalizedValue * entryWidth;

                                RenderUtil.rect(frameX, moduleY, screenSpaceValue, entryHeight, getAccentColor(frameX, moduleY, entryWidth, entryHeight));
                                final float backgroundWidth = entryWidth - screenSpaceValue;

                                if (backgroundWidth > 0.0F) {
                                    final Color color = getBackgroundColor(frameX, moduleY, entryWidth, entryHeight, true);
                                    RenderUtil.rect(frameX + screenSpaceValue, moduleY, backgroundWidth, entryHeight, color);
                                }

                                check.drawString("g" ,frameX + 2.0F, titleY + 1f, Color.WHITE.getRGB());
                                gs.drawString(
                                        NumberValue.getName() + ": " + getTruncatedDouble(NumberValue),
                                        frameX + 13.0F, titleY, Color.WHITE.getRGB()
                                );
                            } else if (setting instanceof TextValue) {
                                RenderUtil.rect(frameX, moduleY, entryWidth, entryHeight, backgroundDarker);
                                check.drawString("r" ,frameX + 2.0F, titleY + 1f, Color.WHITE.getRGB());
                                gs.drawString(setting.getName(), frameX + 14.0F, titleY, new Color(217, 217, 217, 150).getRGB());
                            }

                            if (isFirst) {
                                isFirst = false;
                                RenderUtil.gradient(frameX, moduleY, entryWidth, 10, shadow, new Color(0, 0, 0, 0));
                            }
                        }

                        RenderUtil.gradient(frameX, moduleY + entryHeight - 10, entryWidth, 10, new Color(0, 0, 0, 0), shadow);
                    }
                }

                currentY = moduleY - scrollVertical;
            }

            previousHeight = currentY - frameY;
            RenderUtil.disable(GL11.GL_SCISSOR_TEST);
        }
    }

    public void drawDescriptions(final int mouseX, final int mouseY, final float partialTicks) {
        if (expanded) {
            final float frameX = this.frameX + StrikeGUI.scrollHorizontal;
            float currentY = frameY;

            if (mouseY < currentY + entryHeight || mouseY > currentY + entryHeight + maximumEntries * entryHeight)
                return;

            for (final Module module : modules) {
                if (module.isHidden()) continue;

                currentY += entryHeight;
                final float moduleY = currentY + scrollVertical;

                if (isHovering(frameX, moduleY, entryWidth, entryHeight)) {
                    final String description = module.getModuleInfo().description();
                    final float descriptionWidth = gssmall.getWidth(description);
                    RenderUtil.rect(mouseX + 4, mouseY - 6 - entryHeight / 2.0F, descriptionWidth + 6.0F, entryHeight / 2.0F + 2.0F, backgroundDarker);
                    RenderUtil.rect(mouseX + 5, mouseY - 5 - entryHeight / 2.0F, descriptionWidth + 4.0F, entryHeight / 2.0F, background);
                    gssmall.drawString(description, mouseX + 7, mouseY - 6 - entryHeight / 2.0F + 2.0F, Color.WHITE.getRGB());
                }

                final List<Setting> settings = module.getSettings();
                if (settings.size() > 0) {
                    final boolean settingsExpanded = expandedModuleIndices.contains(module);
                    if (settingsExpanded) {
                        for (final Setting setting : settings) {
                            if (setting.isHidden()) continue;
                            currentY += entryHeight;
                        }
                    }
                }
            }
        }
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        switch (mouseButton) {
            case 0: {
                leftClick = true;
                break;
            }
            case 1: {
                rightClick = true;
                break;
            }
        }

        final float frameX = this.frameX + StrikeGUI.scrollHorizontal;

        if (isHovering(frameX, frameY, entryWidth, entryHeight)) {
            if (leftClick) {
                dragged = true;
                dragX = mouseX;
                dragY = mouseY;
            }
        }
    }

    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        final float frameX = this.frameX + StrikeGUI.scrollHorizontal;

        if (leftClick) dragged = false;
        else if (isHovering(frameX, frameY, entryWidth, entryHeight)) expanded = !expanded;

        if (expanded) {
            float currentY = frameY;

            float moduleY = currentY + scrollVertical;

            for (final Module module : modules) {
                if (module.isHidden()) continue;

                moduleY += entryHeight;

                if (isHovering(frameX, moduleY, entryWidth, entryHeight)) {
                    switch (mouseButton) {
                        case 0:
                            module.toggleModule();
                            break;
                        case 1: {
                            if (expandedModuleIndices.contains(module)) expandedModuleIndices.remove(module);
                            else expandedModuleIndices.add(module);
                            break;
                        }
                    }
                }

                if (expandedModuleIndices.contains(module)) {
                    for (final Setting setting : module.getSettings()) {
                        if (setting.isHidden()) continue;

                        moduleY += entryHeight;

                        if (isHovering(frameX, moduleY, entryWidth, entryHeight)) {
                            if (setting instanceof BoolValue) {
                                ((BoolValue) setting).toggle();
                            } else if (setting instanceof ModeValue) {
                                final ModeValue ModeValue = (ModeValue) setting;
                                switch (mouseButton) {
                                    case 0: {
                                        ModeValue.cycle(true);
                                        break;
                                    }
                                    case 1: {
                                        ModeValue.cycle(false);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                currentY = moduleY - scrollVertical;
            }
        }

        switch (mouseButton) {
            case 0: {
                leftClick = false;
                break;
            }
            case 1: {
                rightClick = false;
                break;
            }
        }
    }

    public void mouseClickMove(final int mouseX, final int mouseY, final int mouseButton, final long timeSinceLastClick) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        if (expanded) {
            final float frameX = this.frameX + StrikeGUI.scrollHorizontal;
            float currentY = frameY + scrollVertical;

            for (final Module module : modules) {
                if (module.isHidden()) continue;

                currentY += entryHeight;

                if (expandedModuleIndices.contains(module)) {
                    for (final Setting setting : module.getSettings()) {
                        if (setting.isHidden()) continue;

                        currentY += entryHeight;

                        if (isHovering(frameX, currentY, entryWidth, entryHeight)) {
                            if (setting instanceof NumberValue) {
                                final NumberValue NumberValue = (NumberValue) setting;

                                final float mouseRelative = mouseX - frameX;
                                final float mouseNormalized = mouseRelative / entryWidth;
                                final double range = NumberValue.getMaximum() - NumberValue.getMinimum();
                                final double value = (mouseNormalized * range) + NumberValue.getMinimum();

                                if (NumberValue.getIncrement() != 0)
                                    NumberValue.value = round(value, (float) NumberValue.increment);
                                else NumberValue.value = value;
                            }
                        }
                    }
                }
            }
        }
    }

    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }

    private boolean isHovering(final float x, final float y, final float width, final float height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private Color getBackgroundColor(final float x, final float y, final float width, final float height, final boolean setting) {
        Color color = isHovering(x, y, width, height) ? ((leftClick || rightClick) ? backgroundDarkest : backgroundDarker) : background;
        if (setting) color = color.darker();
        return color;
    }

    private Color getAccentColor(final float x, final float y, final float width, final float height) {
        return isHovering(x, y, width, height) ? ((leftClick || rightClick) ? accentDarkest : accentDarker) : accent;
    }

    private static double round(final double value, final float places) {
        if (places < 0) throw new IllegalArgumentException();

        final double precision = 1 / places;
        return Math.round(value * precision) / precision;
    }

    public static Color changeHue(Color c, final float hue) {

        // Get saturation and brightness.
        final float[] hsbVals = new float[3];
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbVals);

        // Pass .5 (= 180 degrees) as HUE
        c = new Color(Color.HSBtoRGB(hue, hsbVals[1], hsbVals[2]));

        return c;
    }

    private static String getTruncatedDouble(final NumberValue setting) {
        String str = String.valueOf((float) round(setting.value, (float) setting.increment));

        if (setting.increment == 1) {
            str = str.replace(".0", "");
        }

        if (setting.getReplacements() != null) {
            for (final String replacement : setting.getReplacements()) {
                final String[] split = replacement.split("-");
                str = str.replace(split[0], split[1]);
            }
        }

        return str;
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
}
