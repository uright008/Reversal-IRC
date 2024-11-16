package cn.stars.reversal.util;

import cn.stars.reversal.GameInstance;
import cn.stars.reversal.RainyAPI;
import cn.stars.reversal.Reversal;
import cn.stars.reversal.module.impl.misc.CustomName;
import cn.stars.reversal.ui.curiosity.impl.CuriosityMainMenu;
import cn.stars.reversal.ui.gui.GuiLicense;
import cn.stars.reversal.ui.gui.GuiMainMenu;
import cn.stars.reversal.util.misc.ModuleInstance;
import net.minecraft.client.gui.GuiScreen;

public class Transformer implements GameInstance {
    public static GuiScreen transformMainMenu() {
        if (RainyAPI.isLicenseReviewed) {
            return new CuriosityMainMenu();
        } else {
            return new GuiLicense();
        }
    }

    public static String passStringWithCustomName(String string) {
        if (ModuleInstance.getModule(CustomName.class).isEnabled() && Reversal.customName != null) {
            return string.replace(mc.session.getUsername(), Reversal.customName.replace("&", "§"));
        }
        return string;
    }
}
