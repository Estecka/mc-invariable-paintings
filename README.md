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

### Crafting
Crafting of filled paintings is disabled by default, it is only available as an experiment. I don't necessarily recommend enabling it, and want to keep the "collectible" feel of the previous versions. This may trivialize obtaining new paintings.

All filled painting recipes require 1 Blank Painting, which now costs **1 Phantom Membrane** instead of 1 wool.

Two gamerules control whether and how paintings can be crafted:
- **`invarpaint.allowCrafting`** Allows you to craft new any painting, at the cost of **any 8 different Dyes**.
The colour of the dyes has no particular meaning. The correspondance between each combination and painting is random but deterministic; it will vary depending on the set of paintings you have installed.  
The number of possible combinations is 12,870. So long as you don't have this many paintings installed, all paintings variants can be crafted. All combinations will yield a painting, so multiple combination will give the same result.
- **`invarpaint.allowReplication`** Allows crafing replicas of already owned paintings, at the cost of **7 Glow Ink** instead of dyes.

## Painting item textures
This optional feature requires **[CIT Resewn](https://modrinth.com/mod/cit-resewn)**.

Allows filled paintings to have unique textures in the inventory, depending on their variant.

This is not strictly speaking a feature of InvarPaint. InvarPaint only provides resources used by CIT Resewn, but it does not add any of its own logic on top of it.

InvarPaint provides the CITs and textures for every vanilla variant. For modded variants, more can be added using a resource pack. See the sources for examples on how to make one, and refer to CIT resewn for additional documentation.


## Miscellaneous
### Server-side
- Placement of variant-locked paintings in tight spaces is more forgiving. (In vanilla, it would require targeting a very specific block.)
- Added a new loot function `lock_variant_randomly`, which can applied to painting items in loot tables.

### Client-side
- Fixes a vanilla bug whereby using variant-locked items in too small a space would consume the item without placing the painting. A warning message is shown instead.
- Makes the painting tooltip a bit more compact.


## Compatibility
Any mod that adds new paintings based on the vanilla system will be compatible, and their paintings will be obtainable through all existing  means.

Mods that bypass the vanilla system and implement their own, like _Custom Paintings_ or _Client Paintings_, will prevent InvarPaint from working properly. Amongst user-defined painting mods, [More Canvases v2](https://modrinth.com/mod/more-canvases) is the one that is compatible with InvarPaint.
