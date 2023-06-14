# Invariable-Paintings

Turns paintings into collectibles, by making them harder to obtain, and have the paintings in you inventory remember which variant they are.

## Changes

- When a painting is broken, the dropped item will remember which variant it was; those are the same as the paintings available in the vanilla creative menu. The vanilla variantless painting still exists in the code, but is no longer obtainable in survival. If you were to obtain one, it would still drop a variant-locked item after being placed and broken.  
- Paintings can no longer be crafted, but can be acquired through other means.
- Master Sheperds and Wandering Traders may sell paintings as their last trades.
Master sheperd sell variant-locked paintings instead of the variantless one. (This will not retroactively apply to existing master sheperds).
- If using the mod [Patched](https://modrinth.com/mod/patched), paintings will also be added to several loot tables: fishing, monster-rooms, buried treasures, shipwrecks, woodland mansions, pillager outposts, and stronghold libraries  
The drop rate for these loots was chosen hapazardly, and is subject to be rebalanced.
- Added a new loot function to be used in loot tables, which randomizes a painting's variant:
```json
{
	"type": "item",
	"name": "minecraft:painting",
	"functions": [
		{
			"function": "lock_variant_randomly"
		}
	]
}
```

## Compatibility

This will not work properly with mods such as [Custom Paintings](https://modrinth.com/mod/custom-paintings-mod) or [Client Paintings](https://modrinth.com/mod/client-paintings), which bypass the vanilla variant system and implement their own.
On the other hand, mods that add new paintings in accordance with the vanilla system should not pose any problem, and their paintings should be available in the loot and trade pools.

## Versions

- Minecraft 1.20.1
- Fabric Loader 0.14.21 or above
- Fabric API 0.79.0 or above
- Optionally: Patched 1.1.0 or above
