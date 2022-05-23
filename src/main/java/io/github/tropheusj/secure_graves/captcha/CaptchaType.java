package io.github.tropheusj.secure_graves.captcha;

import com.google.gson.JsonObject;

import io.github.tropheusj.secure_graves.SecureGraves;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

/**
 * A type of captcha, ex. select boxes containing image
 */
public abstract class CaptchaType {
	public static final Registry<CaptchaType> REGISTRY = FabricRegistryBuilder
			.createSimple(CaptchaType.class, SecureGraves.id("captcha_types"))
			.attribute(RegistryAttribute.SYNCED)
			.attribute(RegistryAttribute.MODDED)
			.buildAndRegister();

	public static CaptchaType register(CaptchaType type) {
		return Registry.register(REGISTRY, type.id, type);
	}

	public final ResourceLocation id;

	public CaptchaType(ResourceLocation id) {
		this.id = id;
	}

	public abstract Captcha deserialize(ResourceLocation id, JsonObject jsonObject);
}
