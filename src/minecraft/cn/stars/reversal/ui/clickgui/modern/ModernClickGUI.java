package cn.stars.reversal.ui.clickgui.modern;

import cn.stars.reversal.RainyAPI;
import cn.stars.reversal.Reversal;
import cn.stars.reversal.font.FontManager;
import cn.stars.reversal.module.Category;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.module.impl.addons.Optimization;
import cn.stars.reversal.module.impl.addons.SkinLayers3D;
import cn.stars.reversal.module.impl.hud.ClientSettings;
import cn.stars.reversal.module.impl.hud.PostProcessing;
import cn.stars.reversal.module.impl.render.ClickGui;
import cn.stars.reversal.module.impl.render.HurtCam;
import cn.stars.reversal.value.Value;
import cn.stars.reversal.value.impl.BoolValue;
import cn.stars.reversal.value.impl.ModeValue;
import cn.stars.reversal.value.impl.NoteValue;
import cn.stars.reversal.value.impl.NumberValue;
import cn.stars.reversal.util.ReversalLogger;
import cn.stars.reversal.util.animation.advanced.Direction;
import cn.stars.reversal.util.animation.advanced.impl.DecelerateAnimation;
import cn.stars.reversal.util.animation.rise.Animation;
import cn.stars.reversal.util.animation.rise.Easing;
import cn.stars.reversal.util.math.TimeUtil;
import cn.stars.reversal.util.misc.ModuleInstance;
import cn.stars.reversal.util.render.ColorUtil;
import cn.stars.reversal.util.render.RenderUtil;
import cn.stars.reversal.util.render.ThemeType;
import cn.stars.reversal.util.render.ThemeUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.util.ArrayList;

import static cn.stars.reversal.GameInstance.MODERN_BLOOM_RUNNABLES;
import static cn.stars.reversal.GameInstance.MODERN_BLUR_RUNNABLES;

public class ModernClickGUI extends GuiScreen {
    public Color backgroundColor = new Color(20,20,20,250);
    public Animation scaleAnimation = new Animation(Easing.EASE_OUT_EXPO, 1000);
    private Animation sideAnimation = new Animation(Easing.EASE_OUT_EXPO, 400);
    ScaledResolution sr;
    boolean isSearching = false;
    String searchString = "";
    TimeUtil searchTimer = new TimeUtil();
    Category selectedCategory = Category.COMBAT;
    private static float scrollAmount;
    Module firstModule;
    float lastModuleY;
    NumberValue selectedSlider;
    boolean hasEditedSliders = false;
    TimeUtil timer = new TimeUtil();
    private cn.stars.reversal.util.animation.advanced.Animation windowAnim;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (windowAnim.finished(Direction.BACKWARDS)) mc.displayGuiScreen(null);

        sr = new ScaledResolution(mc);
        int x = width / 2 - 260;
        int y = height / 2 - 160;
    //    scaleAnimation.run(1);
    //    GlUtils.startScale(x, y, (float) scaleAnimation.getValue());

        RenderUtil.scaleStart(x + 260, y + 160, windowAnim.getOutput().floatValue());

        // Background
        if (ModuleInstance.getModule(PostProcessing.class).blur.enabled && windowAnim.finished(Direction.FORWARDS)) {
            MODERN_BLUR_RUNNABLES.add(() -> {
                RenderUtil.roundedRectangle(x, y, 520, 320, 2, Color.BLACK);
            });
        }
        RenderUtil.roundedRectangle(x, y, 520, 320, 2, backgroundColor);

        // Client Name
        RenderUtil.roundedRectangle(x + 6, y + 10.5, 2, 16, 1, ThemeUtil.getThemeColor(ThemeType.ARRAYLIST));
        FontManager.getRegular(36).drawString("REVERSAL", x + 14, y + 12, new Color(200,200,200,200).getRGB());

        // Line
        RenderUtil.rectangle(x + 115, y, 0.7, 320, new Color(200,200,200,100));
        RenderUtil.rectangle(x + 5, y + 62, 105, 0.7, new Color(200,200,200,100));

        // Shadow
        if (ModuleInstance.getModule(PostProcessing.class).bloom.enabled && windowAnim.finished(Direction.FORWARDS)) {
            MODERN_BLOOM_RUNNABLES.add(() -> {
                RenderUtil.roundedRectangle(x, y, 520, 320, 8, backgroundColor);
            });
        }

        // Search
        if (isSearching) {
            FontManager.getPSR(26).drawString(searchString,x + 8, y + 40, new Color(200,200,200,250).getRGB());
            RenderUtil.roundedRectangle(x + 5, y + 35, 105, 20, 4, new Color(60, 60, 60, 160));
            RenderUtil.roundedOutlineRectangle(x + 5, y + 35, 105, 20, 4, 1, new Color(160, 160, 160, 160));
            if (searchTimer.hasReached(500L)) {
                RenderUtil.rectangle(x + FontManager.getPSR(26).getWidth(searchString) + 9, y + 38, 1, 14, new Color(160, 160, 160, 160));
            }
            if (searchTimer.hasReached(1000L)) searchTimer.reset();
        } else if (!searchString.isEmpty() && !getRelevantModules(searchString).isEmpty()) {
            FontManager.getPSR(26).drawString(searchString,x + 8, y + 40, new Color(160, 160, 160, 160).getRGB());
            RenderUtil.roundedRectangle(x + 5, y + 35, 105, 20, 4, new Color(60, 60, 60, 160));
        }  else {
            FontManager.getPSR(26).drawString("Search...", x + 8, y + 40, new Color(160, 160, 160, 160).getRGB());
            RenderUtil.roundedRectangle(x + 5, y + 35, 105, 20, 4, new Color(60, 60, 60, 160));
        }

        // Category
        int renderSelectY = y + 70;
        for (final Category category : Category.values()) {
            if (category == selectedCategory) {
                sideAnimation.run(renderSelectY);
                RenderUtil.roundedRectangle(x + 5, sideAnimation.getValue(), 100, 20, 4, ColorUtil.withAlpha(ThemeUtil.getThemeColor(ThemeType.ARRAYLIST), 100));
                FontManager.getCur(26).drawString(getCategoryIcon(category), x + 8, renderSelectY + 7, new Color(200,200,200, 250).getRGB());
                FontManager.getPSB(26).drawString(StringUtils.capitalize(category.name().toLowerCase()), x + 24, renderSelectY + 6, new Color(200,200,200, 250).getRGB());
            } else {
                if (RenderUtil.isHovered(x + 5, renderSelectY, 100, 20, mouseX, mouseY)) {
                    category.alphaAnimation.run(80);
                    //    RenderUtil.roundedRectangle(x + 5, renderSelectY, 105, 20, 4, new Color(80, 80, 80, 180));
                } else {
                    category.alphaAnimation.run(0);
                }
                RenderUtil.roundedRectangle(x + 5, renderSelectY, 100, 20, 4, new Color(50,50,50,(int)category.alphaAnimation.getValue()));
                FontManager.getCur(26).drawString(getCategoryIcon(category), x + 8, renderSelectY + 7, new Color(160, 160, 160, 200).getRGB());
                FontManager.getPSB(22).drawString(StringUtils.capitalize(category.name().toLowerCase()), x + 24, renderSelectY + 6.5, new Color(160, 160, 160, 200).getRGB());
            }
            renderSelectY += 25;
        }

        // Module
        firstModule = null;
        lastModuleY = 0;
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(x, y, 520, 320);
        float moduleX = x + 120;
        float moduleY = y + 10 + scrollAmount;
        float settingX = x + 125;
        float settingY = y + 25 + scrollAmount;
        for (final Module m : Reversal.moduleManager.getModuleList()) {
            if ((m.getModuleInfo().category() == selectedCategory && searchString.isEmpty()) || getRelevantModules(searchString).contains(m)) {
                m.guiX = moduleX;
                m.guiY = moduleY;
                if (firstModule == null) {
                    firstModule = m;
                }
                lastModuleY = Math.max(lastModuleY, m.guiY + m.sizeInGui);
                if (m.yAnimation.getValue() >= y && m.yAnimation.getValue() + m.sizeAnimation.getValue() <= y + 320) {
                    RenderUtil.scissor(m.guiX - 1, m.yAnimation.getValue(), 391, m.sizeAnimation.getValue() - 5);
                } else {
                    RenderUtil.scissor(x, y, 520, 320);
                }
                FontManager.getRegularBold(24).drawString(canUseChinese(m) ? m.getModuleInfo().chineseName() : m.getModuleInfo().name(), m.guiX + 20, m.yAnimation.getValue() + 6 + (canUseChinese(m) ? 1 : 0), RainyAPI.isSpecialModule(m) ? new Color(240,240,10,250).getRGB() : m.isEnabled() ? new Color(240,240,240,250).getRGB() : new Color(160, 160, 160, 200).getRGB());
                FontManager.getPSR(16).drawString(canUseChinese(m) ? m.getModuleInfo().chineseDescription() : m.getModuleInfo().description(),
                        m.guiX + 20, m.yAnimation.getValue() + 20 + (canUseChinese(m) ? 1 : 0), new Color(160, 160, 160, 160).getRGB());
                if (m.expanded || (!m.sizeAnimation.isFinished() && m.yAnimation.isFinished())) {
                    m.sizeInGui = 20;
                    settingY += m.sizeInGui;
                    if (m != firstModule) settingY += 15; // IDK why

                    for (final Value setting : m.getSettings()) {
                        if (!setting.isHidden()) {
                            if (setting instanceof NoteValue) {
                                FontManager.getPSR(18).drawString(setting.name, setting.guiX, setting.yAnimation.getValue() - 15, new Color(150, 150, 150, 150).getRGB());
                                settingY += 12;
                                m.sizeInGui += 12;
                            }
                            if (setting instanceof BoolValue) {
                                FontManager.getPSR(18).drawString(setting.name, setting.guiX, setting.yAnimation.getValue() - 15, new Color(200, 200, 200, 200).getRGB());
                                RenderUtil.roundedOutlineRectangle(setting.guiX + 5 + FontManager.getPSR(18).width(setting.name), setting.yAnimation.getValue() - 15.5, 8, 8, 4, 0.5, new Color(100, 200, 255, 200));
                                if (((BoolValue) setting).isEnabled())
                                    RenderUtil.roundedRectangle(setting.guiX + 6.5 + FontManager.getPSR(18).width(setting.name), setting.yAnimation.getValue() - 14, 5, 5, 2.5, new Color(100, 200, 255, 250));
                                settingY += 12;
                                m.sizeInGui += 12;
                            }
                            if (setting instanceof NumberValue) {
                                FontManager.getPSR(18).drawString(setting.name, setting.guiX, setting.yAnimation.getValue() - 15, new Color(200, 200, 200, 200).getRGB());
                                NumberValue settingValue = (NumberValue) setting;
                                float fontWidth = FontManager.getPSR(18).getWidth(setting.name) + 5;
                                if (selectedSlider == setting) {

                                    final double percent = (mouseX - (setting.guiX + fontWidth)) / (double) (100);
                                    double value = settingValue.minimum - percent * (settingValue.minimum - settingValue.maximum);

                                    if (value > settingValue.maximum) value = settingValue.maximum;
                                    if (value < settingValue.minimum) value = settingValue.minimum;

                                    settingValue.value = value;

                                    if (settingValue.getIncrement() != 0)
                                        selectedSlider.value = round(value, (float) settingValue.increment);
                                    else settingValue.value = value;

                                    hasEditedSliders = true;

                                }

                                settingValue.percentage = (((NumberValue) setting).value - ((NumberValue) setting).minimum) / (((NumberValue) setting).maximum - ((NumberValue) setting).minimum);

                                String value = String.valueOf((float) round(settingValue.value, (float) settingValue.increment));

                                if (settingValue.increment == 1) {
                                    value = value.replace(".0", "");
                                }

                                if (settingValue.getReplacements() != null) {
                                    for (final String replacement : settingValue.getReplacements()) {
                                        final String[] split = replacement.split("-");
                                        value = value.replace(split[0], split[1]);
                                    }
                                }

                                RenderUtil.roundedRectangle(setting.guiX + fontWidth, setting.yAnimation.getValue() - 13, 100, 2, 1, new Color(200, 200, 200, 200));
                                RenderUtil.roundedRectangle(setting.guiX + fontWidth + settingValue.renderPercentage * 100, setting.yAnimation.getValue() - 14.5, 5, 5, 2.5, new Color(100, 200, 255, 250));
                                FontManager.getPSR(18).drawString(value, setting.guiX + fontWidth + 109, setting.yAnimation.getValue() - 15, new Color(240, 240, 240, 240).getRGB());
                                settingY += 12;
                                m.sizeInGui += 12;
                            }
                            if (setting instanceof ModeValue) {
                                FontManager.getPSR(18).drawString(setting.name, setting.guiX, setting.yAnimation.getValue() - 15, new Color(200, 200, 200, 200).getRGB());
                                FontManager.getPSR(18).drawString(((ModeValue) setting).getModes().get(((ModeValue) setting).index),
                                        setting.guiX + 5 + FontManager.getPSR(18).width(setting.name), setting.yAnimation.getValue() - 15, new Color(240, 240, 240, 240).getRGB());
                                settingY += 12;
                                m.sizeInGui += 12;
                            }
                            setting.guiX = settingX;
                            setting.guiY = settingY;
                            setting.yAnimation.run(setting.guiY);
                        }
                    }
                }
                if (!m.expanded) {
                    m.sizeInGui = 20;
                }
                m.sizeInGui += 15;
                m.sizeAnimation.run(m.sizeInGui);
                m.yAnimation.run(m.guiY);

                // Keybinding
                RenderUtil.roundedOutlineRectangle(m.guiX + 340 - FontManager.getPSR(16).width(Keyboard.getKeyName(m.getKeyBind())), m.yAnimation.getValue() + 9,
                        4.5 + FontManager.getPSR(16).width(Keyboard.getKeyName(m.getKeyBind())), 12, 2, 0.7, new Color(160, 160, 160, 160));
                FontManager.getPSR(16).drawString(Keyboard.getKeyName(m.getKeyBind()), m.guiX + 342 - FontManager.getPSR(16).width(Keyboard.getKeyName(m.getKeyBind())),
                        m.yAnimation.getValue() + 12.5, new Color(160,160,160,160).getRGB());

                if (!m.getSettings().isEmpty()) {
                    FontManager.getIcon(20).drawString(m.expanded ? "h" : "i", m.guiX + 375, m.yAnimation.getValue() + 14, new Color(160, 160, 160, 160).getRGB());
                }

            //    RenderUtil.roundedRectangle(m.guiX - 0.5, m.yAnimation.getValue() + 10, 1, 10, 1, ThemeUtil.getThemeColor(ThemeType.ARRAYLIST));
                RenderUtil.roundedRectangle(m.guiX, m.yAnimation.getValue(), 390, m.sizeAnimation.getValue() - 5, 3, new Color(80,80,80, (int) (60 + m.alphaAnimation.getValue())));
                RenderUtil.roundedRectangle(m.guiX + 8, m.yAnimation.getValue() + 12, 6, 6, 3, m.isEnabled() ? new Color(50,255,50, 220) : new Color(160, 160, 160, 200));

                settingY = moduleY + m.sizeInGui;
                moduleY += m.sizeInGui;

            }
        }
        if (timer.hasReached(10)) {
            timer.reset();
            for (final Module m : Reversal.moduleManager.getModuleList()) {
                if (m.isEnabled()) {
                    m.alphaAnimation.run(80);
                } else if (RenderUtil.isHovered(m.guiX, m.yAnimation.getValue(), 390, m.sizeAnimation.getValue() - 5, mouseX, mouseY)) {
                    m.alphaAnimation.run(40);
                } else {
                    m.alphaAnimation.run(0);
                }
                for (final Value s : m.getSettings()) {
                    if (s instanceof NumberValue) {
                        final NumberValue NumberValue = ((NumberValue) s);

                        if (hasEditedSliders) {
                            NumberValue.renderPercentage = (NumberValue.renderPercentage + NumberValue.percentage) / 2;
                        } else {
                            NumberValue.renderPercentage = (NumberValue.renderPercentage * 4 + NumberValue.percentage) / 5;
                        }

                    }
                }
            }

            if (firstModule != null && firstModule.guiY - y >= 20) {
                scrollAmount *= 0.86;
            }

            if (firstModule != null && lastModuleY - y - 320 < -40 && !(lastModuleY == firstModule.guiY)) {
                if (!(lastModuleY - firstModule.guiY < 280)) scrollAmount *= 0.99f;
                else scrollAmount = -5;
            }
        }

        GlStateManager.popMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    //    GlUtils.stopScale();

        RenderUtil.scaleEnd();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int x = width / 2 - 260;
        int y = height / 2 - 160;
        if (RenderUtil.isHovered(x + 5, y + 35, 105, 20, mouseX, mouseY)) {
            isSearching = true;
        } else {
            isSearching = false;
        }

        if (RenderUtil.isHovered(x + 5, y + 70, 105, 20, mouseX, mouseY)) {
            setSelectedCategory(Category.COMBAT);
        }
        if (RenderUtil.isHovered(x + 5, y + 95, 105, 20, mouseX, mouseY)) {
            setSelectedCategory(Category.MOVEMENT);
        }
        if (RenderUtil.isHovered(x + 5, y + 120, 105, 20, mouseX, mouseY)) {
            setSelectedCategory(Category.PLAYER);
        }
        if (RenderUtil.isHovered(x + 5, y + 145, 105, 20, mouseX, mouseY)) {
            setSelectedCategory(Category.RENDER);
        }
        if (RenderUtil.isHovered(x + 5, y + 170, 105, 20, mouseX, mouseY)) {
            setSelectedCategory(Category.MISC);
        }
        if (RenderUtil.isHovered(x + 5, y + 195, 105, 20, mouseX, mouseY)) {
            setSelectedCategory(Category.WORLD);
        }
        if (RenderUtil.isHovered(x + 5, y + 220, 105, 20, mouseX, mouseY)) {
            setSelectedCategory(Category.HUD);
        }
        if (RenderUtil.isHovered(x + 5, y + 245, 105, 20, mouseX, mouseY)) {
            setSelectedCategory(Category.ADDONS);
        }

        float moduleX = x + 120;
        float moduleY = y + 5;
        for (final Module m : Reversal.moduleManager.getModuleList()) {
            if ((m.getModuleInfo().category() == selectedCategory && searchString.isEmpty()) || getRelevantModules(searchString).contains(m)) {
                if (RenderUtil.isHovered(moduleX, m.guiY, 520, 30, mouseX, mouseY)) {
                    if (m.guiY + FontManager.getPSR(20).height() < y || m.guiY + FontManager.getPSR(20).height() > y + 320) return;
                    if (mouseButton == 0) {
                        m.toggleModule();
                    }
                    else if (mouseButton == 1) m.expanded = !m.expanded;
                }
                for (final Value setting : m.getSettings()) {
                    if (m.expanded) {
                        if (!setting.isHidden()) {
                            if (setting instanceof NoteValue) {
                                if (RenderUtil.isHovered(setting.guiX, setting.guiY - 15, x, 12, mouseX, mouseY)) {
                                }
                            }
                            if (setting instanceof BoolValue) {
                                if (RenderUtil.isHovered(setting.guiX, setting.guiY - 15, x, 12, mouseX, mouseY) && mouseButton == 0) {
                                    ((BoolValue) setting).toggle();
                                }
                            }
                            if (setting instanceof NumberValue) {
                                if (RenderUtil.isHovered(setting.guiX, setting.guiY - 15, x, 12, mouseX, mouseY) && mouseButton == 0) {
                                    selectedSlider = (NumberValue) setting;
                                }
                            }
                            if (setting instanceof ModeValue) {
                                if (RenderUtil.isHovered(setting.guiX, setting.guiY - 15, x, 12, mouseX, mouseY)) {
                                    if (mouseButton == 0) ((ModeValue) setting).cycle(true);
                                    if (mouseButton == 1) ((ModeValue) setting).cycle(false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setSelectedCategory(Category category) {
        if (selectedCategory == category) return;
        selectedCategory = category;
        scrollAmount = -5;
        mc.getSoundHandler().playButtonPress();
    }

    private String getCategoryIcon(Category c) {
        switch (c) {
            case COMBAT: {
                return "A";
            }
            case MOVEMENT: {
                return "B";
            }
            case PLAYER: {
                return "C";
            }
            case RENDER: {
                return "D";
            }
            case MISC: {
                return "E";
            }
            case WORLD: {
                return "F";
            }
            case HUD: {
                return "G";
            }
            case ADDONS: {
                return "H";
            }
        }
        return "A";
    }

    @Override
    public void updateScreen() {
        final float wheel = Mouse.getDWheel();

        scrollAmount += wheel / (11f - ModuleInstance.getModule(ClickGui.class).scrollSpeed.getValue()) * 200;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (isCtrlKeyDown() && keyCode == Keyboard.KEY_F) {
            isSearching = true;
        }
        if (keyCode == Keyboard.KEY_ESCAPE && windowAnim.getDirection() == Direction.FORWARDS) {
            windowAnim.changeDirection();
            Keyboard.enableRepeatEvents(false);
        }
        if (isSearching) {
            // Paste
            if (isKeyComboCtrlV(keyCode)) {
                DataFlavor flavor = DataFlavor.stringFlavor;
                if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(flavor))
                    try {
                        searchString = searchString + Toolkit.getDefaultToolkit().getSystemClipboard().getData(flavor);
                    } catch (Exception e) {
                        ReversalLogger.error("(ClickGUI) Unable to retrieve system clipboard flavor" + e);
                    }
            }
            // Type
            if (Character.isDigit(typedChar) || Character.isLetter(typedChar) || Character.isIdeographic(typedChar)) {
                if (FontManager.getPSR(26).getWidth(searchString) < 95) searchString = searchString + typedChar;
            } else if (keyCode == Keyboard.KEY_BACK && !searchString.isEmpty()) {
                searchString = searchString.substring(0, searchString.length() - 1);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        windowAnim = new DecelerateAnimation(100, 1d);
        hasEditedSliders = false;
        sideAnimation.reset();
        scaleAnimation.reset();
    }

    @Override
    public void onGuiClosed() {
        searchString = "";
        isSearching = false;
        Keyboard.enableRepeatEvents(false);
        selectedSlider = null;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        selectedSlider = null;
    }

    private static double round(final double value, final float places) {
        if (places < 0) throw new IllegalArgumentException();

        final double precision = 1 / places;
        return Math.round(value * precision) / precision;
    }

    public boolean canUseChinese(Module module) {
        if (ModuleInstance.getModule(ClientSettings.class).chinese.isEnabled()) {
            return !module.getModuleInfo().chineseDescription().isEmpty() && !module.getModuleInfo().chineseName().isEmpty();
        }
        return false;
    }

    public ArrayList<Module> getRelevantModules(final String search) {
        final ArrayList<Module> relevantModules = new ArrayList<>();

        if (search.isEmpty()) return relevantModules;

        for (final Module module : Reversal.moduleManager.moduleList) {
            if (module.getModuleInfo().name().toLowerCase().replaceAll(" ", "")
                    .contains(search.toLowerCase().replaceAll(" ", ""))) {
                relevantModules.add(module);
            }
        }

        return relevantModules;
    }
}
