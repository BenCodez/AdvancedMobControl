package com.Ben12345rocks.AdvancedMobControl.VersionHandle;

import org.bukkit.entity.LivingEntity;

public interface AttributeHandle {
	public void setMaxHealth(LivingEntity entity, double health);
	public double getMaxHealth(LivingEntity entity);
}
