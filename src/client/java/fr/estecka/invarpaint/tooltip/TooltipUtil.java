package fr.estecka.invarpaint.tooltip;

import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import fr.estecka.invarpaint.InvarpaintClient;
import fr.estecka.invarpaint.api.PaintStackUtil;
import fr.estecka.invarpaint.api.PaintTextUtil;

public class TooltipUtil
{
	static private final Component INVALID_TEXT = Component.translatable("painting.invalid").withStyle(ChatFormatting.RED);
	static private final Component EMPTY_NOTICE = Component.literal(" (").append(Component.translatable("painting.empty")).append(")").withStyle(ChatFormatting.GRAY);

	static public MutableComponent AppendPaintingName(MutableComponent text, ItemStack stack){
		// I could just use translatable variables,
		// but this way is compatible with other languages
		Identifier variantId = PaintStackUtil.GetVariantId(stack);
		if (variantId != null)
			text.append(
				Component.literal(" (")
					.append(PaintTextUtil.TranslatableVariantName(variantId))
					.append(")")
					.withStyle(ChatFormatting.YELLOW)
			);
		else
			text.append(EMPTY_NOTICE);

		return text;
	}

	/**
	 * @deprecated Kept around for future reference if NoKebab is ever ported.
	 */
	@Deprecated
	static public void AddVariantTooltip(List<Component> tooltip, Identifier variantId, boolean advanced){
		Optional<Registry<PaintingVariant>> registry = InvarpaintClient.GetPaintingRegitry();
		Optional<PaintingVariant> variant = registry.flatMap(r -> r.getOptional(variantId));

		// In the event the registry would be absent, consider everything as valid, and print what can be known.
		if (registry.isPresent() && variant.isEmpty())
			tooltip.add(INVALID_TEXT);
		else if (variantId != null){
			MutableComponent authorLine = Component.translatableWithFallback(variantId.toLanguageKey("painting", "author"), "").withStyle(ChatFormatting.GRAY);
			if (variant.isPresent())
				authorLine = Component.translatable("painting.dimensions", variant.get().width(), variant.get().height())
					.append(" ")
					.append(authorLine)
					;

			tooltip.add(authorLine);
		}

		if (advanced)
			tooltip.add(Component.literal(variantId.toString()).withStyle(ChatFormatting.DARK_GRAY));
	}

}
