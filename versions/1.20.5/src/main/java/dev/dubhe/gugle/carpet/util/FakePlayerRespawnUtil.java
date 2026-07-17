package dev.dubhe.gugle.carpet.util;

import carpet.patches.EntityPlayerMPFake;
import dev.dubhe.gugle.carpet.GcaSetting;
import dev.dubhe.gugle.carpet.entry.RespawnDimensionTransition;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class FakePlayerRespawnUtil {

    public static RespawnDimensionTransition getRespawnPosition(EntityPlayerMPFake player) {
        float xRot = player.getXRot();
        float yRot = player.getYRot();

        if ("spawn".equals(GcaSetting.fakePlayerAutoRespawn)) {
            ServerLevel serverLevel = player.server.getLevel(player.getRespawnDimension());
            BlockPos respawnPos = player.getRespawnPosition();

            Optional<Vec3> optional;
            if (serverLevel != null && respawnPos != null) {
                optional = Player.findRespawnPositionAndUseSpawnBlock(
                    serverLevel,
                    respawnPos,
                    player.getRespawnAngle(),
                    player.isRespawnForced(),
                    false
                );
            } else {
                optional = Optional.empty();
            }

            ServerLevel level = optional.isPresent() ? serverLevel : player.server.overworld();
            Vec3 pos = optional.orElse(null);

            return new RespawnDimensionTransition(level.dimension(), pos, yRot, xRot);
        }

        ResourceKey<Level> dimension = player.level().dimension();
        return new RespawnDimensionTransition(dimension, player.position(), yRot, xRot);
    }

}
