package tk.estecka.invarpaint;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public class SellPaintingFactory implements TradeOffers.Factory
{
	public static final int	NOVICE_LVL = 1;
	public static final int	APPRENTICE_LVL = 2;
	public static final int	JOURNEYMAN_LVL = 3;
	public static final int	EXPERT_LVL = 4;
	public static final int	MASTER_LVL = 5;

	public static void	RegisterPaintings(){
		Int2ObjectMap<TradeOffers.Factory[]> shepherd = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(VillagerProfession.SHEPHERD);
		Int2ObjectMap<TradeOffers.Factory[]> wanderer = TradeOffers.WANDERING_TRADER_TRADES;

		InvariablePaintings.LOGGER.info("Replacing villager trades");

		//This line assumes that variantless paintings are the only trade available for master Shepherds, and overwrites them.
		shepherd.get(MASTER_LVL)[0] = new SellPaintingFactory();
	
		TradeOffers.Factory[] old = wanderer.get(APPRENTICE_LVL);
		TradeOffers.Factory[] neo = new TradeOffers.Factory[old.length+1];
		for (int i=0; i<old.length; i++)
			neo[i] = old[i];
		neo[old.length] = new SellPaintingFactory();
		wanderer.put(APPRENTICE_LVL, neo);
	}

	@Override
	@Nullable
	public TradeOffer	create(Entity entity, Random random){
		return new TradeOffer(
			new ItemStack(Items.EMERALD, 12),
			PaintStackCreator.CreateRandomVariant(random),
			12,
			30,
			0.05f
		);
	}
}
