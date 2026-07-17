package dev.dubhe.gugle.carpet.util;

import carpet.patches.EntityPlayerMPFake;
import dev.dubhe.gugle.carpet.GcaSetting;
import dev.dubhe.gugle.carpet.entry.RespawnDimensionTransition;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

public class FakePlayerRespawnUtil {

    public static RespawnDimensionTransition getRespawnPosition(EntityPlayerMPFake player) {
        return new RespawnDimensionTransition(getRespawnPositionNoWrap(player));
    }

    private static DimensionTransition getRespawnPositionNoWrap(EntityPlayerMPFake player) {
        if ("spawn".equals(GcaSetting.fakePlayerAutoRespawn)) {
            return player.findRespawnPositionAndUseSpawnBlock(false, DimensionTransition.DO_NOTHING);
        }

        ServerLevel level = (ServerLevel) player.level();
        float xRot = player.getXRot();
        float yRot = player.getYRot();

        return new DimensionTransition(level, player.position(), Vec3.ZERO, yRot, xRot, DimensionTransition.DO_NOTHING);
    }

}
