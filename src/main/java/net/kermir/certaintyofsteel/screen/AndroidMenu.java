package net.kermir.certaintyofsteel.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

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
