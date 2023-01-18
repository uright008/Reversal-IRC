package cn.stars.starx.module.impl.world;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.BoolValue;
import cn.stars.starx.setting.impl.NumberValue;
import cn.stars.starx.ui.notification.NotificationType;
import cn.stars.starx.util.math.TimeUtil;
import cn.stars.starx.util.misc.HttpUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@ModuleInfo(name = "BanChecker", description = "Check the ban states on hypixel", category = Category.WORLD)
public class BanChecker extends Module {
    private static String API_PUNISHMENT = aB("68747470733a2f2f6170692e706c616e636b652e696f2f6879706978656c2f76312f70756e6973686d656e745374617473");

    public final BoolValue alertValue = new BoolValue("Alert", this,true);
    public final BoolValue serverCheckValue = new BoolValue("Server Check", this,true);
    public final NumberValue alertTimeValue = new NumberValue("Alert Time",this, 10, 1, 50,1);


    public static int WATCHDOG_BAN_LAST_MIN = 0;
    public static int LAST_TOTAL_STAFF = -1;
    public static int STAFF_BAN_LAST_MIN = 0;

    public BanChecker() {
        (new Thread("Hypixel-BanChecker") {
            public void run() {
                TimeUtil checkTimer = new TimeUtil();
                while (true) {
                    if (checkTimer.hasReached(60000L)) {
                        try {
                            String apiContent = HttpUtils.get(API_PUNISHMENT);
                            final JsonObject jsonObject = new JsonParser().parse(apiContent).getAsJsonObject();
                            if (jsonObject.get("success").getAsBoolean() && jsonObject.has("record")) {
                                JsonObject objectAPI = jsonObject.get("record").getAsJsonObject();
                                WATCHDOG_BAN_LAST_MIN = objectAPI.get("watchdog_lastMinute").getAsInt();
                                int staffBanTotal = objectAPI.get("staff_total").getAsInt();

                                if (staffBanTotal < LAST_TOTAL_STAFF)
                                    staffBanTotal = LAST_TOTAL_STAFF;

                                if (LAST_TOTAL_STAFF == -1)
                                    LAST_TOTAL_STAFF = staffBanTotal;
                                else {
                                    STAFF_BAN_LAST_MIN = staffBanTotal - LAST_TOTAL_STAFF;
                                    LAST_TOTAL_STAFF = staffBanTotal;
                                }

                                if (alertValue.isEnabled() && mc.thePlayer != null && (!serverCheckValue.isEnabled() || isOnHypixel()))
                                    if (STAFF_BAN_LAST_MIN > 0)
                                        registerNotification("Staffs banned " + STAFF_BAN_LAST_MIN + " players in the last minute!", "BanChecker", STAFF_BAN_LAST_MIN > 3 ? NotificationType.ERROR : NotificationType.WARNING, (long)alertTimeValue.getValue() * 1000);
                                    else
                                        registerNotification("Staffs didn't ban any player in the last minute.", "BanChecker", NotificationType.SUCCESS, (long)alertTimeValue.getValue() * 1000);

                                // watchdog ban doesnt matter, open an issue if you want to add it.
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            if (alertValue.isEnabled() && mc.thePlayer != null && (!serverCheckValue.isEnabled() || isOnHypixel()))
                                registerNotification("An error has occurred.", "BanChecker", NotificationType.ERROR, 1000L);
                        }
                        checkTimer.reset();
                    }
                }
            }
        }).start();
    }

    public boolean isOnHypixel() {
        return !mc.isIntegratedServerRunning() && mc.getCurrentServerData().serverIP.contains("hypixel.net");
    }

    public static String aB(String str) { // :trole:
        String result = new String();char[] charArray = str.toCharArray();for(int i = 0; i < charArray.length; i=i+2) {String st = ""+charArray[i]+""+charArray[i+1];char ch = (char)Integer.parseInt(st, 16);result = result + ch;};return result;
    }
}
