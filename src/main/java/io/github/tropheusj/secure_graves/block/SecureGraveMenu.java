package io.github.tropheusj.secure_graves.block;

import io.github.tropheusj.secure_graves.SecureGraves;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;

public class SecureGraveMenu extends AbstractContainerMenu {
	private final ContainerLevelAccess levelAccess;

	public SecureGraveMenu(int i, Inventory inventory) {
		this(i, inventory, ContainerLevelAccess.NULL);
	}

	public SecureGraveMenu(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
		super(SecureGraves.SECURE_GRAVE_MENU, i);
		this.levelAccess = containerLevelAccess;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(levelAccess, player, SecureGraves.SECURE_GRAVE);
	}
}
