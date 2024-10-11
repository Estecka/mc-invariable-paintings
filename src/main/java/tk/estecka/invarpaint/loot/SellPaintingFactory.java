package tk.estecka.invarpaint.loot;

import java.util.List;
import java.util.Optional;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
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

	static private final int MAX_USE = 1;
	static private final int XP_GAIN = 30;

	public static void	Register(){
		InvarpaintMod.LOGGER.info("Adding locked paintings to trade pools");

		final PoolIdentifier COMMON              = PoolIdentifier.Parse("!#invarpaint:exclusive").getOrThrow();
		final PoolIdentifier VILLAGER_EXCLUSIVES = PoolIdentifier.Parse("#invarpaint:exclusive/villager").getOrThrow();
		final PoolIdentifier WANDERER_EXCLUSIVES = PoolIdentifier.Parse("#invarpaint:exclusive/wanderer").getOrThrow();

		final SellPaintingFactory SHEPHERD_TRADE  = new SellPaintingFactory(24, true,  List.of(COMMON, VILLAGER_EXCLUSIVES));
		final SellPaintingFactory WANDERER_TRADE1 = new SellPaintingFactory(24, false, List.of(COMMON));
		final SellPaintingFactory WANDERER_TRADE2 = new SellPaintingFactory(24, false, List.of(WANDERER_EXCLUSIVES));

		//This assumes that variantless painting is the only master trade available to Shepherds, and overwrites it.
		Int2ObjectMap<Factory[]> shepherd = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(VillagerProfession.SHEPHERD);
		shepherd.get(MASTER_LVL)[0] = SHEPHERD_TRADE;

		TradeOfferHelper.registerWanderingTraderOffers(APPRENTICE_LVL, list->{
			list.add(WANDERER_TRADE1);
			list.add(WANDERER_TRADE2);
		});
		TradeOfferHelper.registerRebalancedWanderingTraderOffers(builder->builder.addOffersToPool(
			TradeOfferHelper.WanderingTraderOffersBuilder.SELL_SPECIAL_ITEMS_POOL,
			WANDERER_TRADE1,
			WANDERER_TRADE2
		));
	}


	private final TradedItem priceLeft;
	private final Optional<TradedItem> priceRight;
	private final List<PoolIdentifier> pool;

	public SellPaintingFactory(TradedItem price1, Optional<TradedItem> price2, List<PoolIdentifier> pool){
		this.priceLeft  = price1;
		this.priceRight = price2;
		this.pool = pool;
	}

	public SellPaintingFactory(int emeralds, boolean canvas, List<PoolIdentifier> pool){
		this(
			new TradedItem(Items.EMERALD, emeralds),
			Optional.ofNullable(canvas ? new TradedItem(Items.PAINTING) : null),
			pool
		);
	}

	@Override
	public TradeOffer	create(Entity entity, Random random){
		var registry = entity.getWorld().getRegistryManager().getOrThrow(RegistryKeys.PAINTING_VARIANT);
		Identifier variant = PoolIdentifier.GetRandom(this.pool, random, registry);

		if (variant == null)
			return null;

		return new TradeOffer(
			this.priceLeft,
			this.priceRight,
			PaintStackUtil.CreateVariant(variant),
			MAX_USE,
			XP_GAIN,
			0.05f
		);
	}
}
