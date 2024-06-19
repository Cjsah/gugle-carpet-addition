package dev.dubhe.gugle.carpet.mixin;

import carpet.patches.EntityPlayerMPFake;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.gugle.carpet.GcaExtension;
import dev.dubhe.gugle.carpet.GcaSetting;
import dev.dubhe.gugle.carpet.api.tools.text.ComponentTranslate;
import dev.dubhe.gugle.carpet.tools.FakePlayerEnderChestContainer;
import dev.dubhe.gugle.carpet.tools.FakePlayerInventoryContainer;
import dev.dubhe.gugle.carpet.tools.FakePlayerInventoryMenu;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Player.class)
abstract class PlayerMixin {
    @Unique
    Player gca$self = (Player) (Object) this;

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        if (gca$self instanceof EntityPlayerMPFake fakePlayer && fakePlayer.isAlive()) {
            Map.Entry<FakePlayerInventoryContainer, FakePlayerEnderChestContainer> entry
                    = GcaExtension.fakePlayerInventoryContainerMap.get(gca$self);
            entry.getKey().tick();
            entry.getValue().tick();
        }
    }

    @WrapOperation(method = "interactOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"))
    private InteractionResult interactOn(Entity entity, Player player, InteractionHand hand, Operation<InteractionResult> original) {
        if (!(entity instanceof EntityPlayerMPFake fakePlayer)) {
            return original.call(entity, player, hand);
        }
        SimpleMenuProvider provider = null;
        if (player.isShiftKeyDown()) {
            if (GcaSetting.openFakePlayerEnderChest) {
                provider = new SimpleMenuProvider(
                        (i, inventory, p) -> ChestMenu.sixRows(
                                i, inventory,
                                GcaExtension.fakePlayerInventoryContainerMap.get(fakePlayer).getValue()
                        ),
                        ComponentTranslate.trans("gca.player.ender_chest", fakePlayer.getDisplayName())
                );
            } else {
                provider = new SimpleMenuProvider(
                        (i, inventory, p) -> ChestMenu.threeRows(
                                i, inventory,
                                GcaExtension.fakePlayerInventoryContainerMap.get(fakePlayer).getValue()
                        ),
                        ComponentTranslate.trans("gca.player.other_controller", fakePlayer.getDisplayName())
                );
            }
        } else if (GcaSetting.openFakePlayerInventory) {
            provider = new SimpleMenuProvider(
                    (i, inventory, p) -> new FakePlayerInventoryMenu(
                            i, inventory,
                            GcaExtension.fakePlayerInventoryContainerMap.get(fakePlayer).getKey()
                    ),
                    ComponentTranslate.trans("gca.player.inventory", fakePlayer.getDisplayName())
            );
        }
        if (provider != null) {
            player.openMenu(provider);
        }
        return InteractionResult.SUCCESS;
    }
}
