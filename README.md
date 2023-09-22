# Invariable Paintings

## Overview
Turns paintings into collectibles.

When a painting is broken, the dropped item will remember which variant it was, and will only ever allow this variant to be placed. Those variant-locked items are the same as the ones available in the vanilla creative menu, so no new item type was added to the game.

Paintings are no longer craftable and must be obtained through other means, such as trading, fishing and chest loots.


## All Changes
### Server-side
- Broken paintings drop a variant-locked item instead of variantless ones.
- Placement of variant-locked paintings in tight spaces is more forgiving.
- Paintings are no longer craftables.
- Wandering Traders and Master Shepherds can sell variant-locked paintings. Shepherds no longer sell variantless paintings. (This will not retroactively apply to existing master shepherds).
- _(Requires [Patched](https://modrinth.com/mod/patched))_ Paintings will be added to several loot tables: fishing, monster-rooms, buried treasures, shipwrecks, woodland mansions, pillager outposts, and stronghold libraries.  
The drop rate for these loots was chosen aphazardly, and is subject to be rebalanced.
- Added a new function `lock_variant_randomly`, which can applied to painting items in loot tables.

### Client-side
- Fixes a vanilla bug whereby using variant-locked items in too small a space would consume the item without placing the painting. A warning message is shown instead.
- _(Requires [CIT Resewn](https://modrinth.com/mod/cit-resewn))_ Provides unique inventory icon for every vanilla variant. Icons for modded variants can be provided via a resource pack. (See the sources for examples, and CIT resewn for documentation.)
- Makes the painting tooltip a bit more compact, and display the painting's title in the item name.


## Compatibility
Any other mods that add new paintings in accordance with the vanilla system should be compatible, and their paintings should be available in the loot and trade pools.

Mods that bypass the vanilla system and implement their own, like _Custom Paintings_ or _Client Paintings_, will prevent InvarPaint from working properly. [More Canvases v2](https://modrinth.com/mod/more-canvases) is their equivalent that respects the vanilla variant system.
