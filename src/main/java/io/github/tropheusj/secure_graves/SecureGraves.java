package io.github.tropheusj.secure_graves;

import io.github.tropheusj.secure_graves.block.SecureGraveBlock;
import io.github.tropheusj.secure_graves.block.SecureGraveBlockEntity;
import io.github.tropheusj.secure_graves.block.SecureGraveMenu;
import io.github.tropheusj.secure_graves.captcha.CaptchaLoader;
import io.github.tropheusj.secure_graves.captcha.picture_box_selection.PictureBoxSelectionCaptchaType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SecureGraves implements ModInitializer {
	public static final String ID = "secure_graves";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static Block SECURE_GRAVE;
	public static BlockEntityType<SecureGraveBlockEntity> SECURE_GRAVE_BE_TYPE;
	public static MenuType<SecureGraveMenu> SECURE_GRAVE_MENU;

	@Override
	public void onInitialize() {
		SECURE_GRAVE = Registry.register(Registry.BLOCK, id("secure_grave"),
				new SecureGraveBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));
		SECURE_GRAVE_BE_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("secure_grave"),
				FabricBlockEntityTypeBuilder.create(SecureGraveBlockEntity::new, SECURE_GRAVE).build());
		SECURE_GRAVE_MENU = Registry.register(Registry.MENU, id("secure_grave"),
				new MenuType<>(SecureGraveMenu::new));
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(CaptchaLoader.INSTANCE);
		new PictureBoxSelectionCaptchaType(null);
	}

	public static void handleGrave(ServerPlayer player, List<Entity> capturedDrops) {
		ServerLevel level = player.getLevel();
		BlockPos pos = player.blockPosition();
		level.setBlockAndUpdate(pos, SECURE_GRAVE.defaultBlockState());
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof SecureGraveBlockEntity grave) {
			grave.storeLoot(capturedDrops);
		}
	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
}
