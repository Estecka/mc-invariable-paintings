# Invariable Paintings

## Overview
Turns each painting variant into its own item.

**No new item type was added to the game**, variant-locked items are the same as the ones available in the vanilla creative menu.

When a painting is broken, it will drop the variant-locked item instead of the blank one.  
Blank paintings can still be crafted, but can no longer be placed.

### Dependencies
Some specific features depend on **[Patched](https://modrinth.com/mod/patched)** (server-side) and **[CIT Resewn](https://modrinth.com/mod/cit-resewn)** (client-side), but both are completely optional.


## Obtaining paintings
### Trading
Filled paintings can be bought from **Master Shepherds** and **Wandering Traders**. Shepherds no longer sell variantless paintings, this will not retroactively apply to existing shepherds.

### Looting
This optional features requires **[Patched](https://modrinth.com/mod/patched)**.

Filled paintings can be found while fishing, inside of most suspicious soils, and many naturally generated chests. The drop rate for these loots was chosen aphazardly, and is subject to be rebalanced.

### Replication
You can clone any filled painting, by placing it next to a blank one in a crafting grid.
This is disabled by default, and controlled by the gamerule `invarpaint.allowReplication`.

### Crafting (experimental)
**Crafting of filled paintings is disabled by default,** it is only available as an experiment, and may receive significant changes. I don't necessarily recommend enabling it, especially if you  enjoy the "collectible" aspect of the mod's previous versions. This may trivialize obtaining new paintings. For this reason, crafting was intentionally designed to feel obscure and a bit expensive.

The cost of crafting Blank paintings was increased to **1 Phantom Membrane**, instead of wool.

Crafting a filled painting costs **any 8 different Dyes**.
Two variants of this recipe exist, requiring either a Blank painting, or recycling a Filled painting. Those are respectively controlled by the gamerules `invarpaint.allowCreation` and `invarpaint.allowDerivation`.

The colour of the dyes has no particular meaning. The correspondance between each combination and painting is random but deterministic; it will only vary depending on the set of paintings you have installed.  
There are 12,870 possible combinations and all of them is guaranteed to yield a painting, thus many combinations will give the same result.

By default, the result of such crafting is obfuscated. You won't be able to see the resulting variant until you take the result out of the crafting table, and consume the ingredients. This is controlled by the gamerule `invarpaint.obfuscatedCrafting`.

## Painting item textures
This optional feature requires **[CIT Resewn](https://modrinth.com/mod/cit-resewn)**.

Allows filled paintings to have unique textures in the inventory, depending on their variant.

This is strictly speaking not a feature of InvarPaint; it provides a built-in resource pack that is used by CIT-Resewn, but does not add any of its own code on top of it.

InvarPaint only provides the CITs for vanilla variant. For modded variants, CITs should be provided in an external resource pack. See the sources for examples on how to make one, and refer to CIT resewn for additional documentation.

Having too many painting CITs (around hundreds of them) can put a toll on the game's performances, so the embedded vanilla CITs can be disabled in the resource-pack menu.



## Miscellaneous tweaks
### Server-side
- Placement of variant-locked paintings in tight spaces is more forgiving. (Vanilla would require targeting some specific block.)
- Added a new loot function `lock_variant_randomly`, which can applied to painting items in loot tables.

### Client-side
- Fixes a vanilla bug whereby paiting items may sometimes appear to be consummed, without actually placing a painting.
- Shows a warning when trying to place a painting in too small a space.
- Slightly reworked the tooltip for painting items.


## Compatibility
Any mod that adds new paintings based on the vanilla system will be compatible, and their paintings will be obtainable through all existing  means.

Mods that bypass the vanilla system and implement their own, like _Custom Paintings_ or _Client Paintings_, will prevent InvarPaint from working properly. Amongst user-defined painting mods, [More Canvases v2](https://modrinth.com/mod/more-canvases) is the one that is compatible with InvarPaint.
