package io.github.tropheusj.secure_graves.captcha.picture_box_selection;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.github.tropheusj.secure_graves.SecureGraves;
import io.github.tropheusj.secure_graves.captcha.Captcha;
import io.github.tropheusj.secure_graves.captcha.CaptchaType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class PictureBoxSelectionCaptchaType extends CaptchaType {
	public static final CaptchaType INSTANCE =
			register(new PictureBoxSelectionCaptchaType(SecureGraves.id("picture_box_selection")));

	public PictureBoxSelectionCaptchaType(ResourceLocation id) {
		super(id);
	}

	@Override
	public Captcha deserialize(JsonObject jsonObject) {
		String translationKey = jsonObject.get("contents").getAsString();
		ResourceLocation texture = new ResourceLocation(jsonObject.get("texture").getAsString());
		boolean[] correctValues = new boolean[4 * 4];
		JsonArray correctValuesArray = jsonObject.get("correct_values").getAsJsonArray();
		for (int y = 0; y < 4; y++) {
			JsonArray subArray = correctValuesArray.get(y).getAsJsonArray();
			for (int x = 0; x < 4; x++) {
				correctValues[y * x] = subArray.get(x).getAsBoolean();
			}
		}
		return new PictureBoxSelectionCaptcha(
				texture,
				new TranslatableComponent(translationKey).withStyle(
						ChatFormatting.GOLD,
						ChatFormatting.BOLD,
						ChatFormatting.ITALIC
				),
				correctValues
		);
	}
}
