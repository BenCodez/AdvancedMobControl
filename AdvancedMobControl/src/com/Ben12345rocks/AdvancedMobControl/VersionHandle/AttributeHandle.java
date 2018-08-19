package com.Ben12345rocks.AdvancedMobControl.VersionHandle;

import org.bukkit.entity.LivingEntity;

public interface AttributeHandle {
	public double getMaxHealth(LivingEntity entity);

	public void setMaxHealth(LivingEntity entity, double health);
}
