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
	static public final int	NOVICE_LVL = 1;
	static public final int	APPRENTICE_LVL = 2;
	static public final int	JOURNEYMAN_LVL = 3;
	static public final int	EXPERT_LVL = 4;
	static public final int	MASTER_LVL = 5;

	static private final SellPaintingFactory SHEPHERD_TRADE = new SellPaintingFactory(new ItemStack(Items.EMERALD, 24), new ItemStack(Items.PAINTING));
	static private final SellPaintingFactory WANDERER_TRADE = new SellPaintingFactory(new ItemStack(Items.EMERALD, 24), null);
	static private final int MAX_USE = 3;
	static private final int XP_GAIN = 30;

	private final ItemStack priceLeft, priceRight;

	public static void	Register(){
		InvariablePaintings.LOGGER.info("Adding locked paintings to trade pools");
		
		//This assumes that variantless painting is the only master trade available to Shepherds, and overwrites it.
		Int2ObjectMap<Factory[]> shepherd = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(VillagerProfession.SHEPHERD);
		shepherd.get(MASTER_LVL)[0] = SHEPHERD_TRADE;

		TradeOfferHelper.registerWanderingTraderOffers(APPRENTICE_LVL, list->list.add(WANDERER_TRADE));
	}

	public SellPaintingFactory(@Nullable ItemStack price1, @Nullable ItemStack price2){
		this.priceLeft  = (price1 == null) ? ItemStack.EMPTY : price1;
		this.priceRight = (price2 == null) ? ItemStack.EMPTY : price2;
	}

	@Override
	public TradeOffer	create(Entity entity, Random random){
		return new TradeOffer(
			this.priceLeft.copy(),
			this.priceRight.copy(),
			PaintStackUtil.CreateRandomVariant(random),
			MAX_USE,
			XP_GAIN,
			0.05f
		);
	}
}
