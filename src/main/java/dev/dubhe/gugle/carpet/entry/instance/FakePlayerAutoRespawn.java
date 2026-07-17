package dev.dubhe.gugle.carpet.entry.instance;

import carpet.fakes.ServerPlayerInterface;
import carpet.helpers.EntityPlayerActionPack;
import carpet.patches.EntityPlayerMPFake;
import com.mojang.authlib.GameProfile;
import dev.dubhe.gugle.carpet.GcaSetting;
import dev.dubhe.gugle.carpet.commands.BotCommand;
import dev.dubhe.gugle.carpet.entry.BotActionInfo;
import dev.dubhe.gugle.carpet.entry.BotInfo;
import dev.dubhe.gugle.carpet.entry.RespawnDimensionTransition;
import dev.dubhe.gugle.carpet.util.BotSpawnUtil;
import dev.dubhe.gugle.carpet.util.FakePlayerRespawnUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.world.phys.Vec2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FakePlayerAutoRespawn {
    private final MinecraftServer server;
    private final Map<UUID, EntityPlayerActionPack> diedFakePlayers = new HashMap<>();

    public FakePlayerAutoRespawn(MinecraftServer server) {
        this.server = server;
    }

    public void onFakePlayerDied(UUID uuid, EntityPlayerActionPack actionPack) {
        if ("false".equals(GcaSetting.fakePlayerAutoRespawn)) return;
        this.diedFakePlayers.put(uuid, actionPack);
    }

    public void onFakePlayerSpawned(UUID uuid) {
        this.diedFakePlayers.remove(uuid);
    }

    public void tryRespawn(EntityPlayerMPFake player) {
        EntityPlayerActionPack pack = this.diedFakePlayers.remove(player.getUUID());
        if (pack == null || "false".equals(GcaSetting.fakePlayerAutoRespawn)) return;

        EntityPlayerActionPack actionPack = ((ServerPlayerInterface) player).getActionPack();
        GameProfile profile = player.getGameProfile();
        String name = profile.getName();

        BotInfo respawnBot = getRespawnBotInfo(player, this.server, name);

        this.server.tell(new TickTask(this.server.getTickCount() + 1, () -> {
            BotSpawnUtil.spawnBot(this.server, null, respawnBot, profile, GcaSetting.fakePlayerReloadAction, actionPack);
        }));
    }

    private static BotInfo getRespawnBotInfo(EntityPlayerMPFake player, MinecraftServer server, String name) {
        if ("setting".equals(GcaSetting.fakePlayerAutoRespawn)) {
            BotInfo botInfo = BotCommand.getBotInfo(server, name);
            if (botInfo != null) return botInfo;
        }

        RespawnDimensionTransition transition = FakePlayerRespawnUtil.getRespawnPosition(player);
        Vec2 facing = new Vec2(transition.xRot(), transition.yRot());

        return new BotInfo(
            name,
            "",
            transition.pos(),
            facing,
            transition.dimension(),
            player.gameMode.getGameModeForPlayer(),
            player.getAbilities().flying,
            BotActionInfo.EMPTY,
            List.of()
        );
    }
}
