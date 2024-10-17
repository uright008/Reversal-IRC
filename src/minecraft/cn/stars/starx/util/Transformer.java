package cn.stars.starx.util;

import cn.stars.starx.GameInstance;
import cn.stars.starx.RainyAPI;
import cn.stars.starx.StarX;
import cn.stars.starx.module.impl.misc.CustomName;
import cn.stars.starx.ui.curiosity.impl.CuriosityMainMenu;
import cn.stars.starx.ui.gui.GuiLicense;
import cn.stars.starx.ui.gui.GuiMainMenu;
import cn.stars.starx.util.misc.ModuleInstance;
import net.minecraft.client.gui.GuiScreen;

public class Transformer implements GameInstance {
    public static GuiScreen transformMainMenu() {
        if (RainyAPI.isLicenseReviewed) {
            if (ModuleInstance.getBool("GuiSettings", "Curiosity Style").isEnabled()) {
                return new CuriosityMainMenu();
            }
            return new GuiMainMenu();
        } else {
            return new GuiLicense();
        }
    }

    public static String passStringWithCustomName(String string) {
        if (ModuleInstance.getModule(CustomName.class).isEnabled() && StarX.customName != null) {
            return string.replace(mc.session.getUsername(), StarX.customName.replace("&", "§"));
        }
        return string;
    }
}
