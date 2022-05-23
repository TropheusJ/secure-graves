package io.github.tropheusj.secure_graves.captcha;

import io.github.tropheusj.secure_graves.block.SecureGraveScreen;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A captcha, ex. select all boxes containing a cow
 */
public interface Captcha {
	Map<ResourceLocation, Captcha> ALL = new HashMap<>();
	Random R = new Random();

	CaptchaInstance newInstance(SecureGraveScreen screen);

	static Captcha random() {
		int index = 0;
		int target = R.nextInt(ALL.size());
		for (Captcha c : ALL.values()) {
			if (index == target) {
				return c;
			}
			index++;
		}
		throw new RuntimeException("What");
	}
}
