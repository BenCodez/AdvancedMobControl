package com.Ben12345rocks.AdvancedMobControl.VersionHandle;

import org.bukkit.entity.LivingEntity;

public class OldAttributeHandle implements AttributeHandle{

	public OldAttributeHandle() {
	
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setMaxHealth(LivingEntity entity, double health) {
		entity.setMaxHealth(health);
	}

	@SuppressWarnings("deprecation")
	@Override
	public double getMaxHealth(LivingEntity entity) {
		return entity.getMaxHealth();
	}

}
