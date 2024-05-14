# Invariable Paintings

## Overview
Turns each painting variant into its own item. When a painting is broken, it will drop the variant-locked item instead of the blank one.  
Blank paintings can still be obtained, but can no longer be placed in survival.

**No new item type was added to the game**, variant-locked items are the same as the ones available in the vanilla creative menu.


### Dependencies and Environment
Core functionalities are **fully server-side**.
[Patched](https://modrinth.com/mod/patched) is needed in order to add items to loot tables, but is otherwise optional.

**Client-side** is recommended but optional, containing only cosmetic changes.

## Obtaining paintings
### Trading
Filled paintings can be bought from **Master Shepherds** and **Wandering Traders**. Shepherds no longer sell variantless paintings, but will now require one to work with.

The elemental paintings are exclusive to the Wandering Trader, and few haphazardly chosen others are exclusive to the villagers.
Modded paintings will be available to both by default.

### Looting
This feature requires **[Patched](https://modrinth.com/mod/patched)**.

Filled paintings can be found inside of many naturally generated chests, suspicious soils, and while fishing.

Certain paintings are exclusive to some location; most notably, the Wither painting can only be found in the nether. Other paintings were spread about more haphazardly.
Modded paintings will be available in most locations by default.

The loot tables are provided as a built-in datapack which can be disabled. Without the datapack, all exclusive paintings will instead be available via trading.

### Crafting (deprecated)
Experimental crafting recipes were removed as of v2.0.
See [the old Readme](https://github.com/Estecka/mc-invariable-paintings/blob/1.4.0+1.20.2/README.md#crafting-experimental) for older versions of the mod.

## Inventory Icons
Painting items can have unique textures depending on their variant. 

This mod provides the icons for vanilla paintings, but it does not generate icons for modded paintings. Those can be added using a resource pack.

Custom icons will be searched for at `/textures/<namespace>/item/painting/<variant>.png`, based on the painting variant's ID.
Variants that lack a custom icon will fall back to a generic built-in one.


## Miscellaneous changes
### Server-side
- Adds a new loot function `invarpaint:lock_variant_randomly`.
- Placement of variant-locked paintings in tight spaces is more forgiving. (Vanilla would require targeting one specific block.)
- Shows a warning when trying to place a painting in a space that is too small.
- Fixes a vanilla bug whereby painting items may appear to be consumed, without actually placing the painting.

### Client-side
- Creative players can pick a painting's variant by holding Ctrl.
- Slightly reworked the tooltip for painting items.


## Compatibility
Any mod that adds new paintings based on the vanilla system will be compatible, and their paintings will be obtainable through all existing  means.

Mods that implement their own variant system are not compatible. Amongst user-defined painting mods, [More Canvases](https://modrinth.com/mod/more-canvases) was made to be compatible with Invariable Paintings.
