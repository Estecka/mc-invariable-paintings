package tk.estecka.invarpaint.loot;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;
import net.minecraft.village.TradeOffers.Factory;
import net.minecraft.village.VillagerProfession;
import tk.estecka.invarpaint.InvarpaintMod;
import tk.estecka.invarpaint.core.PaintStackUtil;

public class SellPaintingFactory
implements TradeOffers.Factory
{
	static public final int	NOVICE_LVL = 1;
	static public final int	APPRENTICE_LVL = 2;
	static public final int	JOURNEYMAN_LVL = 3;
	static public final int	EXPERT_LVL = 4;
	static public final int	MASTER_LVL = 5;

	static private final SellPaintingFactory SHEPHERD_TRADE = new SellPaintingFactory(new TradedItem(Items.EMERALD, 24), new TradedItem(Items.PAINTING));
	static private final SellPaintingFactory WANDERER_TRADE = new SellPaintingFactory(new TradedItem(Items.EMERALD, 24), null);
	static private final int MAX_USE = 3;
	static private final int XP_GAIN = 30;

	private final TradedItem priceLeft;
	private final Optional<TradedItem> priceRight;

	public static void	Register(){
		InvarpaintMod.LOGGER.info("Adding locked paintings to trade pools");
		
		//This assumes that variantless painting is the only master trade available to Shepherds, and overwrites it.
		Int2ObjectMap<Factory[]> shepherd = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(VillagerProfession.SHEPHERD);
		shepherd.get(MASTER_LVL)[0] = SHEPHERD_TRADE;

		TradeOfferHelper.registerWanderingTraderOffers(APPRENTICE_LVL, list->list.add(WANDERER_TRADE));
		TradeOfferHelper.registerRebalancedWanderingTraderOffers(builder->builder.addOffersToPool(TradeOfferHelper.WanderingTraderOffersBuilder.SELL_SPECIAL_ITEMS_POOL, WANDERER_TRADE));
	}

	public SellPaintingFactory(@NotNull TradedItem price1, @Nullable TradedItem price2){
		this.priceLeft  = price1;
		this.priceRight = (price2 == null) ? Optional.empty() : Optional.of(price2);
	}

	@Override
	public TradeOffer	create(Entity entity, Random random){
		return new TradeOffer(
			this.priceLeft,
			this.priceRight,
			PaintStackUtil.CreateRandomVariant(random),
			MAX_USE,
			XP_GAIN,
			0.05f
		);
	}
}
