package dev.dubhe.gugle.carpet.util;

import carpet.fakes.ServerPlayerInterface;
import carpet.helpers.EntityPlayerActionPack;
import dev.dubhe.gugle.carpet.GcaExtension;
import dev.dubhe.gugle.carpet.api.tools.text.ComponentHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class FakePlayerUtil {

    @SuppressWarnings("resource")
    public static void autoFish(Player player) {
        EntityPlayerActionPack ap = ((ServerPlayerInterface) player).getActionPack();
        long l = player.level().getGameTime();
        GcaExtension.PLAN_FUNCTION.add(Map.entry(l + 5, () -> ap.start(EntityPlayerActionPack.ActionType.USE, EntityPlayerActionPack.Action.once())));
        GcaExtension.PLAN_FUNCTION.add(Map.entry(l + 15, () -> ap.start(EntityPlayerActionPack.ActionType.USE, EntityPlayerActionPack.Action.once())));
    }

    public static void sendToolDamaged(ServerPlayer player, ItemStack stack) {
        broadcastSystemMessage(player, "gca.tool.damaged", stack);
    }

    public static void sendRestockFailed(ServerPlayer player, ItemStack stack) {
        broadcastSystemMessage(player, "gca.tool.restock.failed", stack);
    }

    public static void sendRestockFailed(ServerPlayer player, Item item) {
        broadcastSystemMessage(player, "gca.tool.restock.failed", item.getDefaultInstance());
    }

    @SuppressWarnings("resource")
    private static void broadcastSystemMessage(ServerPlayer player, String key, ItemStack stack) {
        Component playerName = player.getDisplayName();
        Component itemName = stack.getDisplayName();

        Component msg = ComponentHelper.tr(key, playerName, itemName);

        player.level().getServer().getPlayerList().broadcastSystemMessage(msg, false);
    }
}
