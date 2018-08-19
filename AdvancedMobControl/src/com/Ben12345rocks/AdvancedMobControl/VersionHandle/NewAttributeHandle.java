package com.Ben12345rocks.AdvancedMobControl.VersionHandle;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class NewAttributeHandle implements AttributeHandle {

	public NewAttributeHandle() {

	}

	@Override
	public double getMaxHealth(LivingEntity entity) {
		return entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
	}

	@Override
	public void setMaxHealth(LivingEntity entity, double health) {
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
	}

}
