package io.github.tropheusj.secure_graves.captcha.picture_box_selection;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.github.tropheusj.secure_graves.SecureGraves;
import io.github.tropheusj.secure_graves.captcha.Captcha;
import io.github.tropheusj.secure_graves.captcha.CaptchaType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.ChatFormatting.*;

public class PictureBoxSelectionCaptchaType extends CaptchaType {
	public static final CaptchaType INSTANCE =
			register(new PictureBoxSelectionCaptchaType(SecureGraves.id("picture_box_selection")));

	public PictureBoxSelectionCaptchaType(ResourceLocation id) {
		super(id);
	}

	@Override
	public Captcha deserialize(ResourceLocation id, JsonObject jsonObject) {
		String translationKey = jsonObject.get("contents").getAsString();
		ResourceLocation texture = new ResourceLocation(jsonObject.get("texture").getAsString());
		boolean[] correctValues = new boolean[4 * 4];
		JsonArray correctValuesJson = jsonObject.get("correct_values").getAsJsonArray();
		for (int row = 0; row < 4; row++) {
			String values = correctValuesJson.get(row).getAsString();
			byte[] data = values.getBytes();
			for (int column = 0; column < 4; column++) {
				byte character = data[column];
				int index = row * 4 + column;
				if (character == 'O')
					correctValues[index] = false;
				else if (character == 'X')
					correctValues[index] = true;
				else throw new IllegalArgumentException(String.format(
						"Invalid character in Captcha [%s]: '%s' (row %s, index %s)",
						id, character, row, column));
			}
		}
		return new PictureBoxSelectionCaptcha(
				texture,
				new TranslatableComponent(translationKey).withStyle(DARK_AQUA, BOLD, ITALIC),
				correctValues
		);
	}
}
