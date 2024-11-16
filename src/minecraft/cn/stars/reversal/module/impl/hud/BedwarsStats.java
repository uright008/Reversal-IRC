package cn.stars.reversal.module.impl.hud;

import cn.stars.reversal.Reversal;
import cn.stars.reversal.event.impl.ClickEvent;
import cn.stars.reversal.event.impl.Render2DEvent;
import cn.stars.reversal.event.impl.Shader3DEvent;
import cn.stars.reversal.font.FontManager;
import cn.stars.reversal.font.MFont;
import cn.stars.reversal.module.Category;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.module.ModuleInfo;
import cn.stars.reversal.value.impl.BoolValue;
import cn.stars.reversal.value.impl.ModeValue;
import cn.stars.reversal.util.math.MathUtil;
import cn.stars.reversal.util.render.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.ArrayList;

@ModuleInfo(name = "BedwarsStats", chineseName = "起床数据", description = "Show your bedwars stats",
        chineseDescription = "显示你的起床战争数据", category = Category.HUD)
public class BedwarsStats extends Module {
    private final ModeValue mode = new ModeValue("Mode", this, "Simple", "Simple", "Modern", "ThunderHack");
    private final BoolValue rainbow = new BoolValue("Rainbow", this, false);
    private final MFont icon = FontManager.getIcon(24);
    private Status currentStatus = Status.WAITING;
    private final ArrayList<EntityPlayer> teammateList = new ArrayList<>();

    public BedwarsStats() {
        setCanBeEdited(true);
        setWidth(180);
        setHeight(100);
        setX(100);
        setY(100);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int x = getX() + 4;
        int y = getY() + 4;
        RenderUtil.rect(x - 2, y - 4, 148, 24, new Color(0, 0, 0, 80));

        // 顶部
        regular20Bold.drawString("Bedwars Stats", x + 15, y, new Color(250, 250, 250, 200).getRGB());
        icon.drawString("I", x, y - 0.3f, new Color(250, 250, 250, 200).getRGB());

        regular16.drawString(">  Current", x + 2, y + 11, new Color(250, 250, 250, 200).getRGB());
        regular16.drawString(currentStatus.name(), x + 145 - regular16.width(currentStatus.name()), y + 11, new Color(250, 250, 250, 200).getRGB());

        if (currentStatus == Status.IN_GAME) {
            RenderUtil.rect(x - 2, y + 20, 148, 10, new Color(0, 0, 0, 80));
            regular16.drawString(">  Teammates", x + 2, y + 21, new Color(250, 250, 250, 200).getRGB());
            int i = 0;
            for (EntityPlayer entityPlayer : teammateList) {
                if (entityPlayer != null) {
                    RenderUtil.rect(x - 2, y + 30 + i, 148, 10, new Color(0, 0, 0, 80));
                    regular16.drawString(entityPlayer.getName(), x + 5, y + 31 + i, new Color(220, 220, 220, 200).getRGB());
                    regular16.drawString(MathUtil.round(mc.thePlayer.getDistanceToEntity(entityPlayer), 1) + "m", x + 145 - regular16.width(MathUtil.round(mc.thePlayer.getDistanceToEntity(entityPlayer), 1) + "m"), y + 31 + i, new Color(250, 250, 250, 200).getRGB());
                    i = i + 10;
                }
            }
        }
    }

    @Override
    public void onShader3D(Shader3DEvent event) {
        int x = getX() + 4;
        int y = getY() + 4;
        RenderUtil.rect(x - 2, y - 4, 148, 24, Color.BLACK);

        if (currentStatus == Status.IN_GAME) {
            RenderUtil.rect(x - 2, y + 20, 148, 10, Color.BLACK);
            int i = 0;
            for (EntityPlayer entityPlayer : teammateList) {
                RenderUtil.rect(x - 2, y + 30 + i, 148, 10, Color.BLACK);
                i = i + 10;
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getType() == ClickEvent.ClickType.MIDDLE) {
            currentStatus = Status.WAITING;
            teammateList.clear();
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityPlayer && entity != mc.thePlayer) {
                    if (mc.thePlayer.getDistanceToEntity(entity) <= 6 && teammateList.size() < 3) {
                        teammateList.add((EntityPlayer) entity);
                        Reversal.showMsg("Add entity to list: " + entity.getName());
                    }
                }
            }
            if (!teammateList.isEmpty()) currentStatus = Status.IN_GAME;
        }
    }

    enum Status {
        WAITING,
        IN_GAME,
        GAME_ENDED
    }
}
