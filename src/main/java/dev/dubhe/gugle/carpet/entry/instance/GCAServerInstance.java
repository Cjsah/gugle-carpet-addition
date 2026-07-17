package dev.dubhe.gugle.carpet.entry.instance;

import dev.dubhe.gugle.carpet.tools.player.FakePlayerAutoReplaceTool;
import dev.dubhe.gugle.carpet.tools.player.FakePlayerResident;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;

public class GCAServerInstance {
    private final MinecraftServer server;
    @Nullable
    private FakePlayerResident resident = null;
    private boolean initiating = false;

    public GCAServerInstance(MinecraftServer server) {
        this.server = server;
    }

    public void startLoadLevel() {
        this.resident = new FakePlayerResident(this.server);
    }

    public void completeLoadLevel() {
        if (this.resident != null) {
            this.resident.load();
        }
    }

    public void setInitiating(boolean initiating) {
        this.initiating = initiating;
    }

    public void stopServer() {
        FakePlayerAutoReplaceTool.clear();
        if (this.resident == null || this.server.isSingleplayer()) return;
        this.resident.save();
    }

    public void startSaveLevel() {
        if (this.resident != null && !this.initiating) this.resident.save();
    }

    public void saveResident() {
        if (this.resident != null) this.resident.save();
    }
}
