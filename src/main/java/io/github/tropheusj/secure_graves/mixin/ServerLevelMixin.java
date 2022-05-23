package io.github.tropheusj.secure_graves.mixin;

import io.github.tropheusj.secure_graves.ServerLevelExtensions;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements ServerLevelExtensions {
	@Unique
	private List<Entity> secure_graves$capturedDrops = null;

	@Inject(method = "addEntity", cancellable = true, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;addNewEntity(Lnet/minecraft/world/level/entity/EntityAccess;)Z"))
	private void secure_graves$captureDrops(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (secure_graves$capturedDrops != null && (entity instanceof ItemEntity || entity instanceof ExperienceOrb)) {
			secure_graves$capturedDrops.add(entity);
			cir.setReturnValue(true);
		}
	}

	@Override
	public void startCapturingDrops() {
		secure_graves$capturedDrops = new ArrayList<>();
	}

	@Override
	public List<Entity> finishCapturingDrops() {
		List<Entity> drops = secure_graves$capturedDrops;
		secure_graves$capturedDrops = null;
		return drops;
	}
}
