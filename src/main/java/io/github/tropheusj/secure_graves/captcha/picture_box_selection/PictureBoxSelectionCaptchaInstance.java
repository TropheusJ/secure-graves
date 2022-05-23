package io.github.tropheusj.secure_graves.captcha.picture_box_selection;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import io.github.tropheusj.secure_graves.SecureGraves;
import io.github.tropheusj.secure_graves.block.SecureGraveScreen;
import io.github.tropheusj.secure_graves.captcha.Captcha;
import io.github.tropheusj.secure_graves.captcha.CaptchaInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.ChatFormatting.*;

public class PictureBoxSelectionCaptchaInstance implements CaptchaInstance {
	public static final ResourceLocation OVERLAY = SecureGraves.id("textures/captcha/picture_box_selection/overlay.png");
	public static final ResourceLocation REFRESH = SecureGraves.id("textures/captcha/refresh.png");
	public static final TranslatableComponent INSTRUCTIONS = new TranslatableComponent("secure_graves.picture_box_selection.instructions");
	public static final Component SUBMIT = new TranslatableComponent("secure_graves.captcha.submit");
	public static final Component WATERMARK = new TranslatableComponent("secure_graves.captcha.watermark").withStyle(ITALIC, GRAY);
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
		initButtons(screen);
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

	public void initButtons(SecureGraveScreen screen) {
		int x = (screen.width - IMAGE_SIDE_LENGTH) / 2 - IMAGE_OFFSET + BUFFER + TEXT_OFFSET_X;
		int y = ((screen.height - IMAGE_SIDE_LENGTH) / 2) + BUFFER + 115;
		screen.addRenderableWidget(new ImageButton(x, y, 20, 20, 0, 0, 20, REFRESH, 20, 40, b -> screen.refresh()));
		x += 40;
		screen.addRenderableWidget(new Button(x, y, 80, 20, SUBMIT, b -> screen.submit()));
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

	@Override
	public Captcha captcha() {
		return this.captcha;
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
		y += 20;
		Component contents = captcha.contents();
		font.draw(matrices, contents, x, y, 0xFF000000);
		y += 70;
		x -= 3;
		font.draw(matrices, WATERMARK, x, y, 0xFF4E4E4E);
	}
}
