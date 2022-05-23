package io.github.tropheusj.secure_graves.block;

import com.mojang.blaze3d.vertex.PoseStack;

import io.github.tropheusj.secure_graves.captcha.Captcha;
import io.github.tropheusj.secure_graves.captcha.CaptchaInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class SecureGraveScreen extends AbstractContainerScreen<SecureGraveMenu> {
	private CaptchaInstance captcha;

	public SecureGraveScreen(SecureGraveMenu abstractContainerMenu, Inventory inventory, Component component) {
		super(abstractContainerMenu, inventory, component);
	}

	@Override
	protected void init() {
		this.imageWidth = 512;
		this.imageHeight = 512;
		super.init();
		this.captcha = Captcha.random().newInstance(this);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
		renderBackground(poseStack);
		this.captcha.render(poseStack, this, partialTick, mouseX, mouseY);
	}

	public int leftPos() {
		return this.leftPos;
	}

	public int topPos() {
		return this.topPos;
	}

	public int imageWidth() {
		return this.imageWidth;
	}

	public int imageHeight() {
		return this.imageHeight;
	}

	@Override
	public <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T widget) {
		return super.addRenderableWidget(widget);
	}
}
