package cn.stars.starx.ui.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import cn.stars.starx.StarX;
import cn.stars.starx.font.CustomFont;
import cn.stars.starx.font.TTFFontRenderer;
import cn.stars.starx.util.animation.normal.Animation;
import cn.stars.starx.util.animation.normal.Direction;
import cn.stars.starx.util.animation.normal.impl.EaseBackIn;
import cn.stars.starx.util.animation.simple.SimpleAnimation;
import cn.stars.starx.util.render.*;
import cn.stars.starx.util.shader.round.RoundedUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class GuiScreenshotViewer extends GuiScreen{

    private ArrayList<String> screenshots = new ArrayList<String>();

    private File screenshotDir;

    private int prevScreenshots;

    private Animation introAnimation;
    private boolean close;

    private ResourceLocation image;
    private String selectedImage;
    private String prevSelectedImage;
    private int selected = 0;

    private float translate = 0;
    private SimpleAnimation changeAnimation = new SimpleAnimation(0.0F);

    private TTFFontRenderer icon = CustomFont.FONT_MANAGER.getFont("Check 24");

    @Override
    public void initGui() {
        close = false;
        screenshotDir = new File(mc.mcDataDir, "screenshots");

        if(!screenshotDir.exists()) {
            screenshotDir.mkdir();
        }

        introAnimation = new EaseBackIn(400, 1, 2);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution sr = new ScaledResolution(mc);

        int addX = 170;
        int addY = 110;
        int x = sr.getScaledWidth() / 2 - addX;
        int y = sr.getScaledHeight() / 2 - addY;

        this.loadScreenshot();

        if(close) {
            introAnimation.setDirection(Direction.BACKWARDS);
            if(introAnimation.isDone(Direction.BACKWARDS)) {
                mc.displayGuiScreen(null);
            }
        }

        GlUtils.startScale(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, (float) introAnimation.getValue());

        //Draw background
        RoundedUtils.drawRound(x, y, addX * 2, addY * 2, 6, ColorUtil.getBackgroundColor(2));

        if(!screenshots.isEmpty()) {
            selectedImage = screenshots.get(selected);

            if(prevSelectedImage != selectedImage) {

                prevSelectedImage = selectedImage;

                try {
                    BufferedImage t = ImageIO.read(new File(screenshotDir, selectedImage));
                    DynamicTexture nibt = new DynamicTexture(t);

                    image = mc.getTextureManager().getDynamicTextureLocation("Image", nibt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(image != null) {

                changeAnimation.setAnimation(translate, 18);

                CustomFont.drawString(selectedImage, x + 20, y + 10, ColorUtil.getFontColor(2).getRGB());
                mc.getTextureManager().bindTexture(image);
                GlStateManager.enableBlend();

                StencilUtils.initStencilToWrite();
                RoundedUtils.drawRound(x, y, addX * 2, addY * 2, 6, ColorUtil.getBackgroundColor(2));
                StencilUtils.readStencilBuffer(1);

                GlUtils.startTranslate(0, changeAnimation.getValue());
                RoundedUtils.drawRoundTextured(x + 20, y + 30, 300, 180, 6, 1.0F);
                GlUtils.stopTranslate();

                StencilUtils.uninitStencilBuffer();

                icon.drawString("h", x + 3, y + 107, ColorUtil.getFontColor(2).getRGB());
                icon.drawString("k", (x + addX * 2 ) - 15, y + 107, ColorUtil.getFontColor(2).getRGB());

                RoundedUtils.drawRound((x + addX * 2) - 70, y + 4, 50, 20, 6, ColorUtil.getBackgroundColor(4));
                CustomFont.drawString("Open", (x + addX * 2) - 57, y + 9, ColorUtil.getFontColor(2).getRGB());

                RoundedUtils.drawRound((x + addX * 2) - 130, y + 4, 50, 20, 6, ColorUtil.getBackgroundColor(4));
                CustomFont.drawString("Delete", (x + addX * 2) - 117, y + 9, ColorUtil.getFontColor(2).getRGB());

            }
        }else {
            CustomFont.drawString("You haven't taken screenshots yet! (F2)", x + 20, y + 9, ColorUtil.getFontColor(2).getRGB());
        }

        icon.drawString("p", x + addX + addX - 15, y + 5, ColorUtil.getFontColor(2).getRGB());

        GlUtils.stopScale();

        if(translate == 90) {
            translate = 0;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        ScaledResolution sr = new ScaledResolution(mc);

        int addX = 170;
        int addY = 110;
        int x = sr.getScaledWidth() / 2 - addX;
        int y = sr.getScaledHeight() / 2 - addY;

        if(mouseButton == 0) {

            // Left
            if(RenderUtils.isInside(mouseX, mouseY, x + 2, y + 106, 15, 15)) {
                if(selected != 0) {
                    translate = 90;
                    selected--;
                }
            }

            // Right
            if(RenderUtils.isInside(mouseX, mouseY, (x + addX * 2 ) - 17, y + 106, 15, 15)) {
                if(selected < screenshots.size() - 1) {
                    translate = 90;
                    selected++;
                }
            }

            // Close
            if(RenderUtils.isInside(mouseX, mouseY, x + addX + addX - 15, y + 5, 15, 15)) {
                close = true;
            }

            // Open
            if(RenderUtils.isInside(mouseX, mouseY, (x + addX * 2) - 70, y + 4, 50, 20) && !screenshots.isEmpty()) {
                Desktop desktop = Desktop.getDesktop();

                try {
                    desktop.browse(new File(screenshotDir, screenshots.get(selected)).toURI());
                } catch (IOException e) {
                    StarX.INSTANCE.showMsg("Failed to open file.Check your file dictionary name,there should not be Chinese or Space!");
                    e.printStackTrace();
                }
            }

            // Delete
            if(RenderUtils.isInside(mouseX, mouseY, (x + addX * 2) - 130, y + 4, 50, 20) && !screenshots.isEmpty()) {
                File file = new File(screenshotDir, screenshots.get(selected));

                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if(keyCode == 1) {
            close = true;
        }
    }


    private void loadScreenshot() {
        if(prevScreenshots != screenshotDir.listFiles().length) {

            prevScreenshots = screenshotDir.listFiles().length;

            screenshots.clear();

            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File file, String str){
                    if (str.endsWith("png")){
                        return true;
                    }else{
                        return false;
                    }
                }
            };

            File fileArray[] = screenshotDir.listFiles(filter);

            for(File f : fileArray) {
                screenshots.add(f.getName());
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }
}