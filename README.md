# Invariable Paintings

## Overview
Turns each painting variant into its own item. When a painting is broken, it will drop the variant-locked item instead of the blank one. No new item type was added to the game, variant-locked items work the same as the ones available in the vanilla creative menu.

Empty paintings can no longer be placed, but filled ones can be found in various places.


### Environment:
Core functionalities are **fully server-side**.

**Client-side is strictly optional,** containing only minor cosmetic and QoL changes.
Vanilla clients only need a **[resource pack](https://modrinth.com/resourcepack/invarpaint-cit)** for CITs to work.

### Optional dependencies:
- [Server-side] **[Patched](https://modrinth.com/mod/patched)** is needed in order to **add paintings to loot tables**, without completely overwritting them.
- [Client-side] **[Variants-CIT](https://modrinth.com/mod/variants-cit)** is an alternative to the server-sided `item_model` component. This mod is not required at all for painting CITs to work, but **it is helpful when working with modded paintings**: it uses a resource format that is less redundant than vanilla, and it handles missing models more gracefully.


## Obtaining paintings
### Trading
Filled paintings can be bought from **Master Shepherds** and **Wandering Traders**. Shepherds no longer sell variantless paintings, but will now require one in their pricing.

The elemental paintings are exclusive to the Wandering Trader, and a few haphazardly chosen others are exclusive to the villagers.
Modded paintings will be available to both by default.

### Looting
This feature requires **[Patched](https://modrinth.com/mod/patched)**.

Filled paintings can be found inside many naturally generated chests, suspicious soils, and while fishing.
Some paintings are exclusive to certain location; for example, the Wither painting can only be found in the nether.
Modded paintings will be available in most locations by default.

The loot tables are provided as a built-in datapack which can be disabled. Without the datapack, all location-exclusive paintings will instead be available via trading.

## Inventory Icons
Painting items have their `item_model` component set based on their variant.
The corresponding item models must be placed at `/assets/<namespace>/models/item/painting/<variant>.json`, based on the painting variant's ID.

Being fully server-side, this logic will assign a unique model to _every single_ painting variant with no regards to available textures. Modded painting will appear as missing models unless you provide additional models for them, even if it's simply to give them a fallback texture.

_The client-sided CIT logic used in older verison of this mod has been relegated to [Variants-CIT](https://modrinth.com/mod/variants-cit)._

## Miscellaneous changes
### Server-side
- Adds a new loot function `invarpaint:lock_variant_randomly`.
- Placement of variant-locked paintings in tight spaces is more forgiving. (Vanilla would require targeting one specific block.)
- Shows a warning when trying to place a painting in a space that is too small.
- Fixes [MC-257133](https://bugs.mojang.com/browse/MC-257133), whereby failing to place a painting causes an inventory desync, causing the client to believes it has consummed the item.

### Client-side
- Creative players can pick a painting's variant by holding Ctrl.
- The paintings in the creative inventory now have their `item_model` component set.
- Slightly reworked the tooltip for painting items.
