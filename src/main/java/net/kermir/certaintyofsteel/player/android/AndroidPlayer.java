package net.kermir.certaintyofsteel.player.android;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class AndroidPlayer {
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();

        return nbt;
    }

    public void deserialize(CompoundTag nbt) {

    }
}
