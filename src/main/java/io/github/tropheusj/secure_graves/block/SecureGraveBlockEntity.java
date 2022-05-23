package io.github.tropheusj.secure_graves.block;

import io.github.tropheusj.secure_graves.SecureGraves;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SecureGraveBlockEntity extends BlockEntity implements MenuProvider {
	public SecureGraveBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SecureGraves.SECURE_GRAVE_BE_TYPE, blockPos, blockState);
	}

	@Override
	public Component getDisplayName() {
		return TextComponent.EMPTY;
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return new SecureGraveMenu(i, inventory, ContainerLevelAccess.create(level, worldPosition));
	}

	public void storeLoot(List<Entity> capturedDrops) {

	}
}
