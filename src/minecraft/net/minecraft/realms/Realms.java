package net.minecraft.realms;

import java.net.Proxy;
import net.minecraft.client.Minecraft;

public class Realms
{
    public static Proxy getProxy()
    {
        return Minecraft.getMinecraft().getProxy();
    }

    public static long currentTimeMillis()
    {
        return Minecraft.getSystemTime();
    }

    public static String getUUID()
    {
        return Minecraft.getMinecraft().getSession().getPlayerID();
    }

    public static String getName()
    {
        return Minecraft.getMinecraft().getSession().getUsername();
    }

    public static void setScreen(RealmsScreen p_setScreen_0_)
    {
        Minecraft.getMinecraft().displayGuiScreen(p_setScreen_0_.getProxy());
    }

    public static void setConnectedToRealms(boolean p_setConnectedToRealms_0_)
    {
        Minecraft.getMinecraft().setConnectedToRealms(p_setConnectedToRealms_0_);
    }

    public static void clearResourcePack()
    {
        Minecraft.getMinecraft().getResourcePackRepository().clearResourcePack();
    }

}
