package tk.estecka.invarpaint;

import org.jetbrains.annotations.Nullable;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradeOffers.Factory;
import net.minecraft.village.VillagerProfession;

public class SellPaintingFactory implements TradeOffers.Factory
{
	public static final int	NOVICE_LVL = 1;
	public static final int	APPRENTICE_LVL = 2;
	public static final int	JOURNEYMAN_LVL = 3;
	public static final int	EXPERT_LVL = 4;
	public static final int	MASTER_LVL = 5;

	public static void	RegisterPaintings(){
		InvariablePaintings.LOGGER.info("Adding locked paintings to trade pools");
		
		//This assumes that variantless painting is the only master trade available to Shepherds, and overwrites it.
		Int2ObjectMap<Factory[]> shepherd = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(VillagerProfession.SHEPHERD);
		shepherd.get(MASTER_LVL)[0] = new SellPaintingFactory();

		TradeOfferHelper.registerWanderingTraderOffers(APPRENTICE_LVL, list->list.add(new SellPaintingFactory()));
	}

	@Override
	@Nullable
	public TradeOffer	create(Entity entity, Random random){
		return new TradeOffer(
			new ItemStack(Items.EMERALD, 12),
			PaintStackUtil.CreateRandomVariant(random),
			12,
			30,
			0.05f
		);
	}
}
