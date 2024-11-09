package cn.stars.addons.optimization.entityculling;

import cn.stars.reversal.util.misc.ModuleInstance;

public class EntityCullingMod extends EntityCullingModBase {


    public EntityCullingMod() {
    }

    @Override
    public void initModloader() {
    }

    public void doClientTick() {
        if (ModuleInstance.getBool("Optimization", "Entity Culling").isEnabled()) this.clientTick();
    }

    public void doWorldTick() {
        if (ModuleInstance.getBool("Optimization", "Entity Culling").isEnabled()) this.worldTick();
    }

}