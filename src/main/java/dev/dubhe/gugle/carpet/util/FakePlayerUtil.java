package dev.dubhe.gugle.carpet.util;

import carpet.fakes.ServerPlayerInterface;
import carpet.helpers.EntityPlayerActionPack;
import dev.dubhe.gugle.carpet.GcaExtension;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class FakePlayerUtil {

    public static void autoFish(Player player) {
        EntityPlayerActionPack ap = ((ServerPlayerInterface) player).getActionPack();
        long l = player.level().getGameTime();
        GcaExtension.PLAN_FUNCTION.add(Map.entry(l + 5, () -> ap.start(EntityPlayerActionPack.ActionType.USE, EntityPlayerActionPack.Action.once())));
        GcaExtension.PLAN_FUNCTION.add(Map.entry(l + 15, () -> ap.start(EntityPlayerActionPack.ActionType.USE, EntityPlayerActionPack.Action.once())));
    }

}
