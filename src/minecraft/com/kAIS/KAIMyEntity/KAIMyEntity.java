package com.kAIS.KAIMyEntity;

import com.kAIS.KAIMyEntity.register.KAIMyEntityRegisterClient;
import com.kAIS.KAIMyEntity.renderer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KAIMyEntity
{
    public static final String MODID = "kaimyentity";
    public static final String NAME = "KAIMyEntity";
    public static final String VERSION = "2.0.0-crossPlatform";

    public static Logger logger;

    public static void preInit() throws Exception {
        logger = LogManager.getLogger(NAME);
        logger.info("KAIMyEntity preInit begin...");

        logger.info("Renderer mode: OpenGL.");
        RenderTimer.Init();
        MMDModelManager.Init();
        MMDTextureManager.Init();
        MMDAnimManager.Init();
        KAIMyEntityRegisterClient.Regist();


        logger.info("KAIMyEntity preInit successful.");
    }
}
