package fr.estecka.invarpaint.core;

import net.fabricmc.fabric.api.event.player.PlayerPickItemEvents.PickItemFromEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.item.ItemStack;
import fr.estecka.invarpaint.api.PaintStackUtil;

public class PaintingPickEvent
implements PickItemFromEntity
{
	@Override
	public ItemStack onPickItemFromEntity(ServerPlayer player, Entity entity, boolean requestIncludeData){
		if (requestIncludeData && entity instanceof Painting)
			return PaintStackUtil.CreateVariant(entity);
		else
			return null;
	}
}
