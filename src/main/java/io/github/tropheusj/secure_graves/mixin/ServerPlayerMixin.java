package io.github.tropheusj.secure_graves.mixin;

import io.github.tropheusj.secure_graves.SecureGraves;
import io.github.tropheusj.secure_graves.ServerLevelExtensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
	@Shadow
	public abstract ServerLevel getLevel();

	@Inject(
			method = "die",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerPlayer;dropAllDeathLoot(Lnet/minecraft/world/damagesource/DamageSource;)V",
					shift = Shift.BEFORE
			)
	)
	private void secure_graves$beforeDrop(DamageSource cause, CallbackInfo ci) {
		if (getLevel() instanceof ServerLevelExtensions ex) {
			ex.startCapturingDrops();
		}
	}

	@Inject(
			method = "die",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerPlayer;dropAllDeathLoot(Lnet/minecraft/world/damagesource/DamageSource;)V",
					shift = Shift.AFTER
			)
	)
	private void secure_graves$afterDrop(DamageSource cause, CallbackInfo ci) {
		if (getLevel() instanceof ServerLevelExtensions ex) {
			List<Entity> captured = ex.finishCapturingDrops();
			SecureGraves.handleGrave((ServerPlayer) (Object) this, captured);
		}
	}
}
