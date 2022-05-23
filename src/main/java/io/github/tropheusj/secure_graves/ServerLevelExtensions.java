package io.github.tropheusj.secure_graves;

import net.minecraft.world.entity.Entity;

import java.util.List;

public interface ServerLevelExtensions {
	void startCapturingDrops();
	List<Entity> finishCapturingDrops();
}
