package tk.estecka.invarpaint.tooltip;

import java.util.List;
import java.util.Optional;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import tk.estecka.invarpaint.InvarpaintClient;
import tk.estecka.invarpaint.core.PaintStackUtil;

public class TooltipUtil
{
	static private final Text INVALID_TEXT = Text.translatable("painting.invalid").formatted(Formatting.RED);
	static private final Text EMPTY_NOTICE = Text.literal(" (").append(Text.translatable("painting.empty")).append(")").formatted(Formatting.GRAY);

	static public void	AppendPaintingName(MutableText text, ItemStack stack){
		// I could just use translatable variables,
		// but this way is compatible with other languages
		String variantName = PaintStackUtil.GetVariantName(stack);
		if (variantName != null)
			text.append(
				Text.literal(" (")
					.append(PaintStackUtil.TranslatableVariantName(variantName))
					.append(")")
					.formatted(Formatting.YELLOW)
			);
		else if (!PaintStackUtil.IsObfuscated(stack))
			text.append(EMPTY_NOTICE);

	}

	static public void	RemoveOriginalTooltip(List<Text> tooltip){
		tooltip.removeIf(text -> {
			TextContent textContent = text.getContent();
			if (textContent instanceof TranslatableTextContent translatable) {
				String key = translatable.getKey();
				return key.startsWith("painting.") 
					&& ( key.equals("painting.random") 
						|| key.equals("painting.dimensions") 
						|| key.endsWith(".title") 
						|| key.endsWith(".author") 
					)
				;
			}
			return false;
		});
	}

	static public void AddVariantTooltip(List<Text> tooltip, String variantName, boolean advanced){
		Identifier id = Identifier.tryParse(variantName);
		Optional<Registry<PaintingVariant>> registry = InvarpaintClient.GetPaintingRegitry();
		Optional<PaintingVariant> variant = registry.flatMap(r -> r.getOrEmpty(id));

		// In the event the registry would be absent, consider everything as valid, and print what can be known.
		if (registry.isPresent() && variant.isEmpty())
			tooltip.add(INVALID_TEXT);
		else if (id != null){
			MutableText authorLine = Text.translatableWithFallback(id.toTranslationKey("painting", "author"), "").formatted(Formatting.GRAY);
			if (variant.isPresent())
				authorLine = Text.translatable("painting.dimensions", variant.get().width(), variant.get().height())
					.append(" ")
					.append(authorLine)
					;

			tooltip.add(authorLine);
		}

		if (advanced)
			tooltip.add(Text.literal(variantName).formatted(Formatting.DARK_GRAY));
	}

}
