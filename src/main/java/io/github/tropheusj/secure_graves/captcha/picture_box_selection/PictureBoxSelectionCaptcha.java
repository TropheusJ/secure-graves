package io.github.tropheusj.secure_graves.captcha.picture_box_selection;

import io.github.tropheusj.secure_graves.block.SecureGraveScreen;
import io.github.tropheusj.secure_graves.captcha.Captcha;
import io.github.tropheusj.secure_graves.captcha.CaptchaInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record PictureBoxSelectionCaptcha(ResourceLocation texture, Component contents,
										 boolean[] correctValues) implements Captcha {
	@Override
	public CaptchaInstance newInstance(SecureGraveScreen screen) {
		return new PictureBoxSelectionCaptchaInstance(this, screen);
	}
}
