package cn.stars.starx.module.impl.addons;

import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.NumberValue;

@ModuleInfo(name = "SkinLayers3D", description = "Extend your skin with extra layers", category = Category.ADDONS)
public class SkinLayers3D extends Module {
    private final NumberValue baseVoxelSize = new NumberValue("Voxel Size", this, 1.15, 1.01, 1.4, 0.01);
    private final NumberValue bodyVoxelSize = new NumberValue("Torso Voxel Width", this, 1.05, 1.01, 1.4, 0.01);
    private final NumberValue headVoxelSize = new NumberValue("Head Voxel Size", this, 1.18, 1.01, 1.25, 0.01);
    private final NumberValue renderDistance = new NumberValue("Level Of Detail Distance", this ,14, 5, 40, 1);
}
