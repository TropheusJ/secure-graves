package io.github.tropheusj.secure_graves;

import io.github.tropheusj.secure_graves.block.SecureGraveScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class SecureGravesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(SecureGraves.SECURE_GRAVE_MENU, SecureGraveScreen::new);
	}
}
