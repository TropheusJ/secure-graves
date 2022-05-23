package io.github.tropheusj.secure_graves.captcha.picture_box_selection;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import io.github.tropheusj.secure_graves.SecureGraves;
import io.github.tropheusj.secure_graves.block.SecureGraveScreen;
import io.github.tropheusj.secure_graves.captcha.CaptchaInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PictureBoxSelectionCaptchaInstance implements CaptchaInstance {
	public static final ResourceLocation OVERLAY = SecureGraves.id("textures/captcha/picture_box_selection/overlay.png");
	public static final TranslatableComponent INSTRUCTIONS = new TranslatableComponent("secure_graves.picture_box_selection.instructions");
	public static final int IMAGE_SIDE_LENGTH = 200;
	public static final int BUFFER = 3;
	public static final int BUFFERED_LENGTH = IMAGE_SIDE_LENGTH - (BUFFER * 2);
	public static final int IMAGE_OFFSET = 75;
	public static final int OVERLAY_WIDTH = IMAGE_SIDE_LENGTH + (IMAGE_OFFSET * 2);
	public static final int TEXT_OFFSET_X = (int) (OVERLAY_WIDTH * 0.58);

	private final PictureBoxSelectionCaptcha captcha;
	private final List<PictureBoxSelectionButton> selectionBoxes = new ArrayList<>(4 * 4);

	public PictureBoxSelectionCaptchaInstance(PictureBoxSelectionCaptcha captcha, SecureGraveScreen screen) {
		this.captcha = captcha;
		initBoxes(screen);
	}

	private void initBoxes(SecureGraveScreen screen) {
		int xPos = (screen.width - IMAGE_SIDE_LENGTH) / 2 - IMAGE_OFFSET + BUFFER + 5;
		int yPos = ((screen.height - IMAGE_SIDE_LENGTH) / 2) + BUFFER + 4;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				int index = (y * 4) + x;
				boolean correct = captcha.correctValues()[index];
				PictureBoxSelectionButton box = new PictureBoxSelectionButton(correct, xPos, yPos);
				selectionBoxes.add(box);
				screen.addRenderableWidget(box);
				xPos += 50;
			}
			xPos -= 200;
			yPos += 50;
		}
	}

	@Override
	public boolean solved() {
		for (PictureBoxSelectionButton box : selectionBoxes) {
			if (!box.correct()) {
				return false;
			}
		}
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void render(PoseStack matrices, SecureGraveScreen screen, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (screen.width - IMAGE_SIDE_LENGTH) / 2 - IMAGE_OFFSET + BUFFER;
		int y = ((screen.height - IMAGE_SIDE_LENGTH) / 2) + BUFFER;
		matrices.pushPose();
		renderImage(matrices, screen, x, y);
		renderOverlay(matrices, x, y);
		renderText(matrices, screen, x, y);
		matrices.popPose();
	}

	@Environment(EnvType.CLIENT)
	private void renderImage(PoseStack matrices, SecureGraveScreen screen, int x, int y) {
		RenderSystem.setShaderTexture(0, captcha.texture());
		screen.blit(matrices, x + BUFFER, y + BUFFER, 0, 0, BUFFERED_LENGTH, BUFFERED_LENGTH);
	}

	@Environment(EnvType.CLIENT)
	private void renderOverlay(PoseStack matrices, int x, int y) {
		RenderSystem.setShaderTexture(0, OVERLAY);
		GuiComponent.blit(matrices, x, y, 0, 0, OVERLAY_WIDTH, IMAGE_SIDE_LENGTH, OVERLAY_WIDTH, IMAGE_SIDE_LENGTH);
	}

	@Environment(EnvType.CLIENT)
	private void renderText(PoseStack matrices, SecureGraveScreen screen, int overlayX, int overlayY) {
		Font font = Minecraft.getInstance().font;
		int x = overlayX + TEXT_OFFSET_X;
		int y = overlayY + 50;
		font.draw(matrices, INSTRUCTIONS, x, y, 0xFF000000);
		matrices.scale(1.5f, 1.5f, 0);
		x -= 80;
		y -= 10;
		Component contents = captcha.contents();
		int width = font.width(contents);
//		x += width / 4;
		font.draw(matrices, contents, x, y, 0xFF000000);
	}
}
