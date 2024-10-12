package cn.stars.starx.ui.clickgui.modern;

import cn.stars.starx.StarX;
import cn.stars.starx.font.FontManager;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.impl.addons.GuiSettings;
import cn.stars.starx.module.impl.addons.Optimization;
import cn.stars.starx.module.impl.addons.SkinLayers3D;
import cn.stars.starx.module.impl.hud.ClientSettings;
import cn.stars.starx.module.impl.hud.PostProcessing;
import cn.stars.starx.module.impl.render.ClickGui;
import cn.stars.starx.module.impl.render.HurtCam;
import cn.stars.starx.setting.Setting;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.ModeValue;
import cn.stars.starx.setting.impl.NoteValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.util.StarXLogger;
import cn.stars.starx.util.animation.advanced.Direction;
import cn.stars.starx.util.animation.advanced.impl.DecelerateAnimation;
import cn.stars.starx.util.animation.rise.Animation;
import cn.stars.starx.util.animation.rise.Easing;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.misc.ModuleInstance;
import cn.stars.starx.util.render.RenderUtil;
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

import static cn.stars.starx.GameInstance.MODERN_BLOOM_RUNNABLES;
import static cn.stars.starx.GameInstance.MODERN_BLUR_RUNNABLES;

public class ModernClickGUI extends GuiScreen {
    public Color backgroundColor = new Color(30,30,30, 220);
    public Animation scaleAnimation = new Animation(Easing.EASE_IN_CUBIC, 1000);
    private Animation animation = new Animation(Easing.LINEAR, 200);
    ScaledResolution sr;
    boolean isSearching = false;
    String searchString = "";
    TimeUtil searchTimer = new TimeUtil();
    Category selectedCategory = Category.COMBAT;
    private static float scrollAmount, lastScrollAmount, lastLastScrollAmount;
    private static float renderScrollAmount;
    Module firstModule;
    float lastModuleY;
    NumberValue selectedSlider;
    boolean hasEditedSliders = false;
    TimeUtil timer = new TimeUtil();
    private cn.stars.starx.util.animation.advanced.Animation windowAnim;

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
        if (ModuleInstance.getBool("PostProcessing", "Blur").isEnabled() && windowAnim.finished(Direction.FORWARDS)) {
            MODERN_BLUR_RUNNABLES.add(() -> {
                RenderUtil.roundedRectangle(x, y, 520, 320, 8, Color.BLACK);
            });
        }
        RenderUtil.roundedRectangle(x, y, 520, 320, 8, backgroundColor);

        // Client Name
        FontManager.getEaves(54).drawString("STARX", x + 18, y + 12, new Color(200,200,200,200).getRGB());

        // Line
        RenderUtil.rectangle(x + 115, y, 0.7, 320, new Color(200,200,200,100));
        RenderUtil.rectangle(x + 5, y + 62, 105, 0.7, new Color(200,200,200,100));

        // Shadow
        if (ModuleInstance.getBool("PostProcessing", "Bloom").isEnabled() && windowAnim.finished(Direction.FORWARDS)) {
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
            FontManager.getPSR(26).drawString("Ctrl+F", x + 8, y + 40, new Color(160, 160, 160, 160).getRGB());
            RenderUtil.roundedRectangle(x + 5, y + 35, 105, 20, 4, new Color(60, 60, 60, 160));
        }

        // Category
        int renderSelectY = y + 70;
        for (final Category category : Category.values()) {
            if (category == selectedCategory) {
                animation.run(255);
                RenderUtil.roundedGradientRectangle(x + 5, renderSelectY, 105, 20, 4,
                        new Color(100, 200, 255, 120), new Color(0,0,0,0), false);
                FontManager.getCur(26).drawString(getCategoryIcon(category), x + 8 + animation.getValue() / 80f, renderSelectY + 7, new Color(200,200,200, 250).getRGB());
                FontManager.getPSB(26).drawString(StringUtils.capitalize(category.name().toLowerCase()), x + 24 + animation.getValue() / 80f, renderSelectY + 6, new Color(200,200,200, 250).getRGB());
            } else {
            //    RenderUtil.roundedRectangle(x + 5, renderSelectY, 105, 20, 4, new Color(80, 80, 80, 180));
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
        float moduleY = y + 10 + renderScrollAmount;
        float settingX = x + 125;
        float settingY = y + 25 + renderScrollAmount;
        for (final Module m : StarX.moduleManager.getModuleList()) {
            if ((m.getModuleInfo().category() == selectedCategory && searchString.isEmpty()) || getRelevantModules(searchString).contains(m)) {
                m.guiX = moduleX;
                m.guiY = moduleY;
                if (firstModule == null) {
                    firstModule = m;
                }
                lastModuleY = Math.max(lastModuleY, m.guiY + m.sizeInGui);
                FontManager.getPSB(24).drawString(m.getModuleInfo().name(), m.guiX + 20, m.guiY + 6, isSpecialModule(m) ? new Color(240,240,10,250).getRGB() : m.isEnabled() ? new Color(240,240,240,250).getRGB() : new Color(160, 160, 160, 200).getRGB());
                FontManager.getPSR(16).drawString(m.getModuleInfo().chineseDescription().isEmpty() ? m.getModuleInfo().description() : m.getModuleInfo().chineseDescription(),
                        m.guiX + 20, m.guiY + 20 + (m.getModuleInfo().chineseDescription().isEmpty() ? 0 : 1), new Color(160, 160, 160, 160).getRGB());
                if (m.expanded) {
                    m.sizeInGui = 20;
                    settingY += m.sizeInGui;
                    if (m != firstModule) settingY += 15; // IDK why

                    for (final Setting setting : m.getSettings()) {

                        if (!setting.isHidden()) {
                            if (setting instanceof NoteValue) {
                                FontManager.getPSR(18).drawString(setting.name, setting.guiX, setting.guiY - 15, new Color(150,150,150,150).getRGB());
                                settingY += 12;
                                m.sizeInGui += 12;
                            }
                            if (setting instanceof BoolValue) {
                                FontManager.getPSR(18).drawString(setting.name, setting.guiX, setting.guiY - 15, new Color(200,200,200,200).getRGB());
                                RenderUtil.roundedOutlineRectangle(setting.guiX + 5 + FontManager.getPSR(18).width(setting.name), setting.guiY - 15.5, 8, 8, 4, 0.5,new Color(100, 200, 255, 200));
                                if (((BoolValue) setting).isEnabled())
                                    RenderUtil.roundedRectangle(setting.guiX + 6.5 + FontManager.getPSR(18).width(setting.name), setting.guiY - 14, 5, 5, 2.5, new Color(100, 200, 255, 250));
                                settingY += 12;
                                m.sizeInGui += 12;
                            }
                            if (setting instanceof NumberValue) {
                                FontManager.getPSR(18).drawString(setting.name, setting.guiX, setting.guiY - 15, new Color(200,200,200,200).getRGB());
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

                                RenderUtil.roundedRectangle(setting.guiX + fontWidth, setting.guiY - 13, 100, 2, 1, new Color(200,200,200,200));
                                RenderUtil.roundedRectangle(setting.guiX + fontWidth + settingValue.renderPercentage * 100, setting.guiY - 14.5, 5, 5, 2.5, new Color(100, 200, 255, 250));
                                FontManager.getPSR(18).drawString(value, setting.guiX + fontWidth + 109, setting.guiY - 15, new Color(240, 240, 240, 240).getRGB());
                                settingY += 12;
                                m.sizeInGui += 12;
                            }
                            if (setting instanceof ModeValue) {
                                FontManager.getPSR(18).drawString(setting.name, setting.guiX, setting.guiY - 15, new Color(200,200,200,200).getRGB());
                                FontManager.getPSR(18).drawString(((ModeValue) setting).getModes().get(((ModeValue) setting).index),
                                        setting.guiX + 5 + FontManager.getPSR(18).width(setting.name), setting.guiY - 15, new Color(240,240,240,240).getRGB());
                                settingY += 12;
                                m.sizeInGui += 12;
                            }
                            setting.guiX = settingX;
                            setting.guiY = settingY;
                        }

                    }
                } else {
                    m.sizeInGui = 20;
                }
                m.sizeInGui += 15;

                // Keybinding
                RenderUtil.roundedOutlineRectangle(m.guiX + 340 - FontManager.getPSR(16).width(Keyboard.getKeyName(m.getKeyBind())), m.guiY + 9,
                        4.5 + FontManager.getPSR(16).width(Keyboard.getKeyName(m.getKeyBind())), 12, 2, 0.7, new Color(160, 160, 160, 160));
                FontManager.getPSR(16).drawString(Keyboard.getKeyName(m.getKeyBind()), m.guiX + 342 - FontManager.getPSR(16).width(Keyboard.getKeyName(m.getKeyBind())),
                        m.guiY + 12.5, new Color(160,160,160,160).getRGB());

                if (!m.getSettings().isEmpty()) {
                    FontManager.getMi(20).drawString(m.expanded ? "h" : "i", m.guiX + 375, m.guiY + 14, new Color(160, 160, 160, 160).getRGB());
                }

                RenderUtil.roundedRectangle(m.guiX, m.guiY, 390, m.sizeInGui - 5, 4, new Color(80,80,80, (int) (60 + m.alphaAnimation.getValue())));
                RenderUtil.roundedRectangle(m.guiX + 8, m.guiY + 12, 6, 6, 3, m.isEnabled() ? new Color(50,255,50, 220) : new Color(160, 160, 160, 200));

                settingY = moduleY + m.sizeInGui;
                moduleY += m.sizeInGui;

            }
        }

        if (timer.hasReached(10)) {
            timer.reset();
            for (final Module m : StarX.moduleManager.getModuleList()) {
                m.alphaAnimation.run(m.isEnabled() ? 50 : 0);
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
            }

            if (firstModule != null && firstModule.guiY - y >= 20) {
                scrollAmount *= 0.86;
            }

            if (firstModule != null && lastModuleY - y - 320 < -40 && !(lastModuleY == firstModule.guiY)) {
                if (!(lastModuleY - firstModule.guiY < 280)) scrollAmount *= 0.99;
                else scrollAmount = -5;
            }

            renderScrollAmount = (float) (lastScrollAmount + (scrollAmount - lastScrollAmount) * mc.timer.renderPartialTicks);
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
        for (final Module m : StarX.moduleManager.getModuleList()) {
            if ((m.getModuleInfo().category() == selectedCategory && searchString.isEmpty()) || getRelevantModules(searchString).contains(m)) {
                if (RenderUtil.isHovered(moduleX, m.guiY, 520, 30, mouseX, mouseY)) {
                    if (m.guiY + FontManager.getPSR(20).height() < y || m.guiY + FontManager.getPSR(20).height() > y + 320) return;
                    if (mouseButton == 0) {
                        m.toggleModule();
                    }
                    else if (mouseButton == 1) m.expanded = !m.expanded;
                }
                for (final Setting setting : m.getSettings()) {
                    if (m.expanded) {
                        if (!setting.isHidden()) {
                            if (setting instanceof NoteValue) {
                                if (RenderUtil.isHovered(setting.guiX, setting.guiY - 15, x, 12, mouseX, mouseY)) {
                                }
                            }
                            if (setting instanceof BoolValue) {
                                if (RenderUtil.isHovered(setting.guiX, setting.guiY - 15, x, 12, mouseX, mouseY)) {
                                    ((BoolValue) setting).toggle();
                                }
                            }
                            if (setting instanceof NumberValue) {
                                if (RenderUtil.isHovered(setting.guiX, setting.guiY - 15, x, 12, mouseX, mouseY)) {
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
        animation = new Animation(Easing.LINEAR, 200);
        selectedCategory = category;
        scrollAmount = -5;
        renderScrollAmount = -5;
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
        int x = width / 2 - 260;
        int y = height / 2 - 160;
    /*    if (Mouse.hasWheel()) {
            if (firstModule == null || firstModule.guiY - y >= 20) {
                scrollAmount -= 10;
            } else if (lastModuleY - y - 320 < -40) {
                if (!(lastModuleY - firstModule.guiY < 280)) scrollAmount += 10;
                else scrollAmount = -5;
            } else {
                scrollAmount += Mouse.getDWheel() / 5;
            }
        } */
        lastLastScrollAmount = lastScrollAmount;
        lastScrollAmount = scrollAmount;

        final float wheel = Mouse.getDWheel();

        scrollAmount += wheel / (11f - ModuleInstance.getNumber("ClickGui", "Scroll Speed").getValue()) * 100;

        if (wheel == 0) {
            scrollAmount -= (lastLastScrollAmount - scrollAmount) * 0.6;
        }
    //    StarX.showMsg(Mouse.getDWheel() / 5);
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
                        StarXLogger.error("(ClickGUI) Unable to retrieve system clipboard flavor" + e);
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
        animation.reset();
        scaleAnimation.reset();
        scrollAmount = 0;
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

    public ArrayList<Module> getRelevantModules(final String search) {
        final ArrayList<Module> relevantModules = new ArrayList<>();

        if (search.isEmpty()) return relevantModules;

        for (final Module module : StarX.moduleManager.moduleList) {
            if (module.getModuleInfo().name().toLowerCase().replaceAll(" ", "")
                    .contains(search.toLowerCase().replaceAll(" ", ""))) {
                relevantModules.add(module);
            }
        }

        return relevantModules;
    }

    public boolean isSpecialModule(Module module) {
        if (module instanceof ClickGui || module instanceof PostProcessing || module instanceof ClientSettings || module instanceof SkinLayers3D || module instanceof GuiSettings || module instanceof HurtCam || module instanceof Optimization) {
            return true;
        }
        return false;
    }
}
