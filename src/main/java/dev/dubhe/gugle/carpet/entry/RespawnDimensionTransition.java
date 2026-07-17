package dev.dubhe.gugle.carpet.entry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

public class RespawnDimensionTransition {
    private final DimensionTransition transition;

    public RespawnDimensionTransition(DimensionTransition transition) {
        this.transition = transition;
    }

    public float xRot() {
        return this.transition.xRot();
    }

    public float yRot() {
        return this.transition.yRot();
    }

    public Vec3 pos() {
        return this.transition.pos();
    }

    public ResourceKey<Level> dimension() {
        return this.transition.newLevel().dimension();
    }
}
