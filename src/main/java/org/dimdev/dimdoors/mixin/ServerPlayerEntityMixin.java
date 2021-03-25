package org.dimdev.dimdoors.mixin;

import org.dimdev.dimdoors.entity.stat.ModStats;
import org.dimdev.dimdoors.api.util.TeleportUtil;
import org.dimdev.dimdoors.world.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

@Mixin(value = ServerPlayerEntity.class, priority = 900)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin {
	public ServerPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
	public void checkDeathServer(DamageSource source, CallbackInfo ci) {
		this.doOnDeathStuff(source, ci);
		if (ci.isCancelled()) {
			if (ModDimensions.isPocketDimension(this.world)) {
				this.incrementStat(ModStats.DEATHS_IN_POCKETS);
			}
			this.incrementStat(ModStats.TIMES_SENT_TO_LIMBO);
			TeleportUtil.teleportRandom(this, ModDimensions.LIMBO_DIMENSION, 384);
		}
	}
}
