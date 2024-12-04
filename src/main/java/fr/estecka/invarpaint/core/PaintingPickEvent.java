package fr.estecka.invarpaint.core;

import net.fabricmc.fabric.api.event.player.PlayerPickItemEvents.PickItemFromEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import fr.estecka.invarpaint.api.PaintStackUtil;

public class PaintingPickEvent
implements PickItemFromEntity
{
	@Override
	public ItemStack onPickItemFromEntity(ServerPlayerEntity player, Entity entity, boolean requestIncludeData){
		if (requestIncludeData && entity instanceof PaintingEntity)
			return PaintStackUtil.CreateVariant(entity);
		else
			return null;
	}
}
