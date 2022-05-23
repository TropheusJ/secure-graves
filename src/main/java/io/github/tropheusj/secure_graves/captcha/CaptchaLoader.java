package io.github.tropheusj.secure_graves.captcha;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;

import io.github.tropheusj.secure_graves.SecureGraves;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class CaptchaLoader extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
	public static final ResourceLocation ID = SecureGraves.id("captcha_loader");
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static final CaptchaLoader INSTANCE = new CaptchaLoader();

	private CaptchaLoader() {
		super(GSON, "captchas");
	}

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
		Captcha.ALL.clear();
		resources.forEach((id, element) -> {
			JsonObject obj = element.getAsJsonObject();
			ResourceLocation type = new ResourceLocation(obj.get("type").getAsString());
			CaptchaType cType = CaptchaType.REGISTRY.get(type);
			if (cType == null)
				throw new IllegalArgumentException("Type [" + type + "] does not exist!");
			Captcha captcha = cType.deserialize(obj);
			if (captcha == null)
				throw new IllegalArgumentException("Failed to deserialize captcha of type " + type);
			Captcha.ALL.put(id, captcha);
		});
		SecureGraves.LOGGER.info("Successfully reloaded {} captcha(s).", resources.size());
	}
}
