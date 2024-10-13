# Invariable Paintings

## Overview
Turns each painting variant into its own item. When a painting is broken, it will drop the variant-locked item instead of the blank one. No new item type was added to the game, variant-locked items work the same as the ones available in the vanilla creative menu.


### Environment
Core functionalities are **fully server-side**.

**Client-side is optional,** containing only minor cosmetic and QoL changes.
Vanilla clients will still need a separate **[resource pack](https://modrinth.com/resourcepacks/invarpaint-assets)** for CITs to work, and potentially additional assets for modded paintings.

### Dependencies
None of these dependencies are strictly required. The core mechanics can run without them, at the cost of some functionalities.
- [Server-side] **[Patched](https://modrinth.com/mod/patched)** is needed in order to add paintings to **loot tables** without completely overwritting them.
- [Client-side] **[Variants-CIT](https://modrinth.com/mod/variants-cit)** is an alternative to the server-sided `item_model` component. This mod is not required for painting CITs to work, but is helpful when working with modded paintings, using a resource format that is less redundant, and handling missing models more gracefully.


## Obtaining paintings
### Trading
Filled paintings can be bought from **Master Shepherds** and **Wandering Traders**. Shepherds no longer sell variantless paintings, but will now require one in their pricing.

The elemental paintings are exclusive to the Wandering Trader, and few haphazardly chosen others are exclusive to the villagers.
Modded paintings will be available to both by default.

### Looting
This feature requires **[Patched](https://modrinth.com/mod/patched)**.

Filled paintings can be found inside of many naturally generated chests, suspicious soils, and while fishing.
Some paintings can only be found in certain location; most notably, the Wither painting can only be found in the nether. Other paintings were spread about more haphazardly.
Modded paintings will be available in most locations by default.

The loot tables are provided as a built-in datapack which can be disabled. Without the datapack, all location-exclusive paintings will instead be available via trading.

## Inventory Icons
Painting items have their `item_model` component set based on their variant.
The corresponding item models must be placed at `/assets/<namespace>/models/item/painting/<variant>.json`, based on the painting variant's ID.

Being fully server-side, this logic will assign a model to _every single_ painting variant with no regards to available textures.
The mod's assets already includes CITs for all vanilla paintings, but for vanilla clients, you will need to create additional models for any paintings added via datapacks, even to simply give them a fallback texture.

_The client-sided CIT logic used in older verison of this mod has be relegated to [Variants-CIT](https://modrinth.com/mod/variants-cit)._

## Miscellaneous changes
### Server-side
- Adds a new loot function `invarpaint:lock_variant_randomly`.
- Placement of variant-locked paintings in tight spaces is more forgiving. (Vanilla would require targeting one specific block.)
- Shows a warning when trying to place a painting in a space that is too small.
- Fixes a vanilla bug whereby failing to place a painting causes an inventory desync, where the client believes it has consummed the painting.

### Client-side
- Creative players can pick a painting's variant by holding Ctrl.
- The paintings in the creative inventory now have their `item_model` component set.
- Slightly reworked the tooltip for painting items.
