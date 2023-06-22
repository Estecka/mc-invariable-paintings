# Invariable-Paintings

Turns paintings into collectibles, by making them harder to obtain, and have the paintings in you inventory remember which variant they are.

## Changes

### Server-side
- When a painting is broken, the dropped item will remember which variant it was. Those variant-locked items are the same as the ones available in the vanilla creative menu; the vanilla variantless painting still exists in the code, but is no longer obtainable in survival. If you were to obtain one, it would still drop a variant-locked item after being placed and broken.  
- Placement of variant-locked paintings in tight spaces is more forgiving.
- Paintings can no longer be crafted, but can be acquired through other means.
- Wandering Traders may sell paintings as their last trades. Master shepherd will sell variant-locked paintings instead of the variantless one. (This will not retroactively apply to existing master shepherds).
- _(Requires [Patched](https://modrinth.com/mod/patched))_ Paintings will be added to several loot tables: fishing, monster-rooms, buried treasures, shipwrecks, woodland mansions, pillager outposts, and stronghold libraries.  
The drop rate for these loots was chosen aphazardly, and is subject to be rebalanced.
- Added a new function `lock_variant_randomly`, which can applied to painting items in loot tables.

### Client-side
- Fixes a vanilla bug whereby using variant-locked items in too small a space would consume the item without placing the painting. A warning message is shown instead.
- _(Requires [CIT Resewn](https://modrinth.com/mod/cit-resewn))_ Makes the vanilla painting items have a unique texture for each variant.
- Makes the painting tooltip a bit more compact, and display the painting's title in the item name.


## Compatibility

This will not work properly with mods such as [Custom Paintings](https://modrinth.com/mod/custom-paintings-mod) or [Client Paintings](https://modrinth.com/mod/client-paintings), which bypass the vanilla variant system and implement their own.
On the other hand, mods that add new paintings in accordance with the vanilla system should not pose any problem, and their paintings should be available in the loot and trade pools.
