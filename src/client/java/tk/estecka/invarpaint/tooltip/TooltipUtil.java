package tk.estecka.invarpaint.tooltip;

import java.util.List;
import java.util.Optional;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import tk.estecka.invarpaint.core.PaintStackUtil;

public class TooltipUtil
{
	static private final Text INVALID_TEXT = Text.translatable("painting.invalid").formatted(Formatting.GRAY);
	static private final Text EMPTY_NOTICE = Text.literal(" (").append(Text.translatable("painting.empty")).append(")").formatted(Formatting.GRAY);

	static public void	AppendPaintingName(MutableText text, ItemStack stack){
		// I could just use translatable variables,
		// but this way is compatible with other languages
		String variantId = PaintStackUtil.GetVariantId(stack);
		if (variantId != null)
			text.append(
				Text.literal(" (")
					.append(PaintStackUtil.TranslatableVariantName(variantId))
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

	static public void AddVariantTooltip(List<Text> tooltip, String variantId, boolean advanced){
		Identifier id = Identifier.tryParse(variantId);
		Optional<PaintingVariant> variant = Registries.PAINTING_VARIANT.getOrEmpty(id);
		if (variant.isEmpty())
			tooltip.add(INVALID_TEXT);
		else {
			tooltip.add(
				Text.translatable("painting.dimensions", variant.get().getWidth()/16, variant.get().getHeight()/16)
					.append(" ")
					.append(Text.translatableWithFallback(id.toTranslationKey("painting", "author"), "")
					.formatted(Formatting.GRAY))
			);
		}
	}

}
