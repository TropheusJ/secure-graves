package io.github.tropheusj.secure_graves.captcha.picture_box_selection;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import io.github.tropheusj.secure_graves.SecureGraves;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class PictureBoxSelectionButton extends Button {
	public static final ResourceLocation SELECTED = SecureGraves.id("textures/captcha/picture_box_selection/box_selected.png");
	public boolean enabled = false;
	public final boolean correctValue;

	public PictureBoxSelectionButton(boolean correctValue, int topLeftX, int topLeftY) {
		super(topLeftX, topLeftY, 40, 40, TextComponent.EMPTY, PictureBoxSelectionButton::click);
		this.correctValue = correctValue;
	}

	@Override
	public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		if (enabled) {
			RenderSystem.setShaderTexture(0, SELECTED);
			blit(poseStack, x, y, 0, 0, 40, 40, 40, 40);
		}
	}

	public void toggle() {
		enabled = !enabled;
	}

	public boolean correct() {
		return enabled == correctValue;
	}

	public static void click(Button b) {
		if (b instanceof PictureBoxSelectionButton box) {
			box.toggle();
		}
	}
}
