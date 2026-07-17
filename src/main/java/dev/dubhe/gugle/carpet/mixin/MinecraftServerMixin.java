package dev.dubhe.gugle.carpet.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.gugle.carpet.entry.instance.GCAServerInstance;
import dev.dubhe.gugle.carpet.fakes.GCAServerInterface;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements GCAServerInterface {

    @Unique
    private GCAServerInstance gca$instance = null;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initGCAInstance(CallbackInfo ci) {
        this.gca$instance = new GCAServerInstance((MinecraftServer) (Object) this);
    }

    @Inject(method = "loadLevel", at = @At("HEAD"))
    public void initResident(CallbackInfo ci) {
        this.gca$instance.startLoadLevel();
    }

    @Inject(method = "loadLevel", at = @At("RETURN"))
    public void spawnResident(CallbackInfo ci) {
        this.gca$instance.completeLoadLevel();
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    public void saveResidentPoint1(CallbackInfo ci) {
        this.gca$instance.stopServer();
    }

    @WrapOperation(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;initServer()Z"))
    private boolean init(MinecraftServer instance, Operation<Boolean> original) {
        this.gca$instance.setInitiating(true);
        boolean result = original.call(instance);
        this.gca$instance.setInitiating(false);
        return result;
    }

    @Inject(method = "saveEverything", at = @At("HEAD"))
    public void saveResidentPoint2(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir) {
        this.gca$instance.startSaveLevel();
    }

    @Override
    public GCAServerInstance gca$getGCAInstance() {
        return this.gca$instance;
    }
}
