package net.kermir.certaintyofsteel.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class AndroidMenu extends AbstractContainerMenu {

    public AndroidMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv);
    }

    public AndroidMenu(int pContainerId, Inventory inv) {
        super(MenuTypeRegistires.ANDROID_MENU.get(), pContainerId);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }
}
