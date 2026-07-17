package dev.dubhe.gugle.carpet.entry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class RespawnDimensionTransition extends PortalInfo {
    private final ResourceKey<Level> dimension;

    public RespawnDimensionTransition(ResourceKey<Level> dimension, @Nullable Vec3 pos, float yRot, float xRot) {
        super(pos, Vec3.ZERO, yRot, xRot);
        this.dimension = dimension;
    }

    public float xRot() {
        return this.xRot;
    }

    public float yRot() {
        return this.yRot;
    }

    public Vec3 pos() {
        return this.pos;
    }

    public ResourceKey<Level> dimension() {
        return this.dimension;
    }
}
