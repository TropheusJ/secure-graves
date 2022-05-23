package io.github.tropheusj.secure_graves.captcha;

import com.mojang.blaze3d.vertex.PoseStack;

import io.github.tropheusj.secure_graves.block.SecureGraveScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * A captcha instance is an actual solvable thing linked to a Captcha
 */
public interface CaptchaInstance {
	boolean solved();

	Captcha captcha();

	@Environment(EnvType.CLIENT)
	void render(PoseStack matrices, SecureGraveScreen screen, float partialTicks, int mouseX, int mouseY);
}
