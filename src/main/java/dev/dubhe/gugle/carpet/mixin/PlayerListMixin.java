package dev.dubhe.gugle.carpet.mixin;

import carpet.patches.EntityPlayerMPFake;
import dev.dubhe.gugle.carpet.fakes.GCAServerInterface;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#if MC > 12001
import net.minecraft.server.network.CommonListenerCookie;
//#endif

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    private void onFakePlayerSpawned(Connection connection, ServerPlayer serverPlayer,
                                     //#if MC > 12001
                                     CommonListenerCookie commonListenerCookie,
                                     //#endif
                                     CallbackInfo ci) {
        if (serverPlayer instanceof EntityPlayerMPFake) {
            ((GCAServerInterface)serverPlayer.level().getServer()).gca$getGCAInstance()
                .getAutoRespawn().onFakePlayerSpawned(serverPlayer.getUUID());
        }
    }
}
