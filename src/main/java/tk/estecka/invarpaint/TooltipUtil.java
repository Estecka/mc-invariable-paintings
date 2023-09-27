package tk.estecka.invarpaint;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import tk.estecka.invarpaint.crafting.DyeCodeUtil;

public class TooltipUtil 
{
	static private final Text UNKNOWN_TEXT = Text.translatable("painting.unknown").formatted(Formatting.GRAY);
	static private final Text EMPTY_NOTICE = Text.literal(" (").append(Text.translatable("painting.empty")).append(")").formatted(Formatting.GRAY);

	static public void	AppendPaintingName(MutableText text, @Nullable String variantId){
		// I could just use translatable variables,
		// but this way is compatible with other languages
		if (variantId != null)
			text.append(
				Text.literal(" (")
					.append(Text.translatableWithFallback("painting."+variantId.replace(":",".")+".title", variantId))
					.append(")")
					.formatted(Formatting.YELLOW)
			);
		else
			text.append(EMPTY_NOTICE);

	}

	static public void	RemoveOriginalTooltip(List<Text> tooltip){
		tooltip.removeIf(text -> {
			TextContent textContent = text.getContent();
			if (textContent instanceof TranslatableTextContent) {
				String key = ((TranslatableTextContent) textContent).getKey();
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

	static public void AddCustomTooltip(List<Text> tooltip, String variantId, boolean advanced){
		Identifier id = Identifier.tryParse(variantId);
		Optional<PaintingVariant> variant = Registries.PAINTING_VARIANT.getOrEmpty(id);
		if (variant.isEmpty())
			tooltip.add(UNKNOWN_TEXT);
		else {
			tooltip.add(
				Text.translatable("painting.dimensions", variant.get().getWidth()/16, variant.get().getHeight()/16)
				.append(" ").append(Text.translatableWithFallback(id.toTranslationKey("painting", "author"), "").formatted(Formatting.GRAY))
			);

			if (advanced){
				int index = Registries.PAINTING_VARIANT.getRawId(variant.get());
				index = DyeCodeUtil.Var2Comb(index);
				short dyeMask = DyeCodeUtil.IndexToCombination(index, 8);
				tooltip.add(DyeCode(dyeMask));
			}
		}
	}

	static public Text DyeCode(short dyeMask){
		MutableText text = Text.literal("#");

		for (int i=15; 0<=i; --i)
		if (((dyeMask>>>i) & 1) != 0)
		{
			int dye = i;
			int color = IdToRgb(dye);
			text.append(
				Text.literal(String.format("%X", dye)).setStyle(Style.EMPTY.withColor(color))
			);
		}

		return text;
	}

	/**
	 * Fine-tuned colours for display on the tooltip.
	 */
	static public int IdToRgb(int id){
		switch (id){
			default:
			case 0x0: return 0xffffff;
			case 0x1: return 0xff8d19;
			case 0x2: return 0xf23df2;
			case 0x3: return 0x80d9ff;
			case 0x4: return 0xe6e62e;
			case 0x5: return 0x97ff32;
			case 0x6: return 0xffa6fe;
			case 0x7: return 0x717371;
			case 0x8: return 0xc8ccc7;
			case 0x9: return 0x00bfbc;
			case 0xa: return 0x8a2ee6;
			case 0xb: return 0x2e5bff;
			case 0xc: return 0x7f4000;
			case 0xd: return 0x00a601;
			case 0xe: return 0xcc2929;
			case 0xf: return 0x323331;
		}
	}

}
