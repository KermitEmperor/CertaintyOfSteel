package net.kermir.certaintyofsteel.save;

import net.kermir.certaintyofsteel.CertaintyOfSteel;
import net.kermir.certaintyofsteel.player.android.AndroidPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class AndroidsSD extends SavedData {
    private HashMap<UUID, AndroidPlayer> androids;
    public static final String ID = "AndroidData";

    public AndroidsSD() {
        this.androids = new HashMap<>();
    }

    public boolean addAndroid(ServerPlayer player) {
        if (androids.containsKey(player.getUUID())) {
            return false;
        } else {
            androids.put(player.getUUID(), new AndroidPlayer());
            this.setDirty();
            return true;
        }
    };

    public boolean removeAndroid(ServerPlayer player) {
        if (androids.containsKey(player.getUUID())) {
            androids.remove(player.getUUID());
            this.setDirty();
            return true;
        } else {
            return false;
        }
    }

    public HashMap<UUID, AndroidPlayer> getAndroids() {
        return androids;
    }

    public @Nullable AndroidPlayer getAndroid(ServerPlayer player) {
        return androids.get(player.getUUID());
    }

    public @Nullable AndroidPlayer getAndroid(UUID uuid) {
        return androids.get(uuid);
    }

    //Saving

    public void deserialize(CompoundTag nbt) {
        androids.clear();

        CompoundTag androidlist = nbt.getCompound("AndroidList");

        for (String stringUUID : androidlist.getAllKeys()) {
            AndroidPlayer androidPlayer = new AndroidPlayer();

            androidPlayer.deserialize(androidlist.getCompound(stringUUID));

            androids.put(UUID.fromString(stringUUID), androidPlayer);
        }

    }

    public static AndroidsSD create() {
        return new AndroidsSD();
    }

    public static AndroidsSD load(CompoundTag nbt) {
        AndroidsSD data = create();

        data.deserialize(nbt);

        return data;
    }


    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        CertaintyOfSteel.LOGGER.info("Saving {} androids to disk", androids.keySet().size());

        CompoundTag androidlist = new CompoundTag();
        for (UUID uuid : androids.keySet()) {
            androidlist.put(uuid.toString(), androids.get(uuid).serialize());
        }

        nbt.put("AndroidList", androidlist);
        return nbt;
    }
}
