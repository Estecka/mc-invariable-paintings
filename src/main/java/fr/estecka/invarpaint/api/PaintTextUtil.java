package fr.estecka.invarpaint.api;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;

public final class PaintTextUtil
{
	/**
	 * A translatable that includes  a default translation as fallabcks. Used on
	 * the server-side, in order to produce modded text that is still legible to
	 * vanilla clients.
	 */
	static public MutableText ServersideTranslatable(String key, Object ... args){
		String fallback = Language.getInstance().get(key);
		return Text.translatableWithFallback(key, fallback, args);
	}

	/**
	 * Handles missing translations more gracefully, by using the raw variant ID
	 * as fallback.
	 */
	static public MutableText TranslatableVariantName(Identifier variantId){
		return Text.translatableWithFallback(variantId.toTranslationKey("painting", "title"), variantId.toString());
	}
	static public MutableText TranslatableVariantName(String variantName){
		return Text.translatableWithFallback("painting."+variantName.replace(":",".")+".title", variantName);
	}
}
