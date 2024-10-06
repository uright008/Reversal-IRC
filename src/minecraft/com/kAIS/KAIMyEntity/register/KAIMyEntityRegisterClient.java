package com.kAIS.KAIMyEntity.register;

import com.kAIS.KAIMyEntity.KAIMyEntity;
import net.minecraft.client.Minecraft;

import java.io.File;

public class KAIMyEntityRegisterClient
{
    public static void Regist()
    {

        File modelDir = new File(Minecraft.getMinecraft().mcDataDir, "KAIMyEntity");
        File[] allDir = modelDir.listFiles();
        if (allDir != null)
        {
            for (File i : allDir)
            {
                if (!i.getName().equals("EntityPlayer"))
                {
                    String mcEntityName = i.getName();
                    try
                    {
                        Class mcEntityClass = Class.forName(mcEntityName);
                    //    RenderingRegistry.registerEntityRenderingHandler(mcEntityClass, new KAIMyEntityRenderFactory<>(mcEntityName));
                    }
                    catch (Exception e)
                    {
                        KAIMyEntity.logger.info(String.format("Cannot regist entity renderer: %s", mcEntityName));
                    }
                }
            }
        }
    }

 /*   public static void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        //Renderer Create time: When 3rd view. If use shader then when world entered.
        //Renderer Render time: WHen 3rd view.

        if (event.getEntityPlayer() == null)
            return;
        if (KAIMyEntityRendererPlayer.GetInst() == null)
        {
            KAIMyEntityRendererPlayer.Init(event.getRenderer().getRenderManager());
        }
        event.setCanceled(true);

        float f = event.getEntity().prevRotationYaw + (event.getEntity().rotationYaw - event.getEntity().prevRotationYaw) * event.getPartialRenderTick();
        KAIMyEntityRendererPlayer.GetInst().doRender(event.getEntityPlayer(), event.getX(), event.getY(), event.getZ(), f, event.getPartialRenderTick());
    } */

 /*   @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event)
    {
        if (keyResetPhysics.isKeyDown())
        {
            if (KAIMyEntityRendererPlayer.GetInst() != null)
            {
                KAIMyEntityRendererPlayer.GetInst().ResetPhysics(Minecraft.getMinecraft().player);
                KAIMyEntityRegisterCommon.channel.sendToServer(new KAIMyEntityNetworkPack(2, Minecraft.getMinecraft().player.getUniqueID(), 0));
            }
        }
        if (keyReloadModels.isKeyDown())
        {
            MMDModelManager.ReloadModel();
        }
        if (keyCustomAnim1.isKeyDown())
        {
            if (KAIMyEntityRendererPlayer.GetInst() != null)
            {
                KAIMyEntityRendererPlayer.GetInst().CustomAnim(Minecraft.getMinecraft().player, "1");
                KAIMyEntityRegisterCommon.channel.sendToServer(new KAIMyEntityNetworkPack(1, Minecraft.getMinecraft().player.getUniqueID(), 1));
            }
        }
        if (keyCustomAnim2.isKeyDown())
        {
            if (KAIMyEntityRendererPlayer.GetInst() != null)
            {
                KAIMyEntityRendererPlayer.GetInst().CustomAnim(Minecraft.getMinecraft().player, "2");
                KAIMyEntityRegisterCommon.channel.sendToServer(new KAIMyEntityNetworkPack(1, Minecraft.getMinecraft().player.getUniqueID(), 2));
            }
        }
        if (keyCustomAnim3.isKeyDown())
        {
            if (KAIMyEntityRendererPlayer.GetInst() != null)
            {
                KAIMyEntityRendererPlayer.GetInst().CustomAnim(Minecraft.getMinecraft().player, "3");
                KAIMyEntityRegisterCommon.channel.sendToServer(new KAIMyEntityNetworkPack(1, Minecraft.getMinecraft().player.getUniqueID(), 3));
            }
        }
        if (keyCustomAnim4.isKeyDown())
        {
            if (KAIMyEntityRendererPlayer.GetInst() != null)
            {
                KAIMyEntityRendererPlayer.GetInst().CustomAnim(Minecraft.getMinecraft().player, "4");
                KAIMyEntityRegisterCommon.channel.sendToServer(new KAIMyEntityNetworkPack(1, Minecraft.getMinecraft().player.getUniqueID(), 4));
            }
        }
    }

    static KeyBinding keyResetPhysics = new KeyBinding("key.resetPhysics", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_K, "key.title");
    static KeyBinding keyReloadModels = new KeyBinding("key.reloadModels", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_NUMPAD1, "key.title");
    static KeyBinding keyCustomAnim1 = new KeyBinding("key.customAnim1", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_V, "key.title");
    static KeyBinding keyCustomAnim2 = new KeyBinding("key.customAnim2", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_B, "key.title");
    static KeyBinding keyCustomAnim3 = new KeyBinding("key.customAnim3", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_N, "key.title");
    static KeyBinding keyCustomAnim4 = new KeyBinding("key.customAnim4", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_M, "key.title"); */
}
