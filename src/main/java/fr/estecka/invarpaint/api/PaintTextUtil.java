package fr.estecka.invarpaint.api;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

public final class PaintTextUtil
{
	/**
	 * A translatable that includes  a default translation  as fallback. Used on
	 * the server-side, in order to produce modded text that is still legible to
	 * vanilla clients.
	 */
	static public MutableComponent ServersideTranslatable(String key, Object ... args){
		String fallback = Language.getInstance().getOrDefault(key);
		return Component.translatableWithFallback(key, fallback, args);
	}

	/**
	 * Handles missing translations more gracefully, by using the raw variant ID
	 * as fallback.
	 */
	static public MutableComponent TranslatableVariantName(Identifier variantId){
		return Component.translatableWithFallback(variantId.toLanguageKey("painting", "title"), variantId.toString());
	}
	static public MutableComponent TranslatableVariantName(String variantName){
		return Component.translatableWithFallback("painting."+variantName.replace(":",".")+".title", variantName);
	}
}
