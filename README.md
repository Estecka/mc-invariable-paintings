# Invariable Paintings

## Overview
Turns each painting variant into its own item. When a painting is broken, it will drop the variant-locked item instead of the blank one. No new item type was added to the game, variant-locked items work the same as the ones available in the vanilla creative menu.

Empty paintings can no longer be placed, but filled ones can be found in various places.


### Environment:
Core functionalities are **fully server-side**.

**Client-side is strictly optional,** containing only minor cosmetic changes to the tooltip.
Vanilla clients only need a **[resource pack](https://modrinth.com/resourcepack/invarpaint-cit)** for CITs to work.

### Optional dependencies:
- [Server-side] **[Patched](https://modrinth.com/mod/patched)** is needed in order to **add paintings to loot tables** without completely overwritting them. Without it, paintings will only be available via trading.
- [Client-side] **[Variants-CIT](https://modrinth.com/mod/variants-cit)** is an alternative to vanilla CIT system. This mod is not required at all for painting CITs to work, but **it is helpful when working with modded paintings**: it uses a resource format that is less redundant than vanilla, and it handles missing models more gracefully.


## Obtaining paintings
The built-in data packs contain several tags at `/data/invarpaint/tags/painting_variant/exclusive*.json`, which dictate where each variant can be found. Variants that are absent from the global exclusive tag can be found everywhere.

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
### Pure vanilla
The associated texture pack uses purely vanilla mechanics to display the correct painting, and requires no action be taken. However, this pack will only work for vanilla paintings; modded paintings will use a generic texture.

### Vanilla clients with modded painting
There are two ways to support modded paintings on vanilla clients:

#### Stateless
The vanilla format for CITs unfortunately needs all variants listed in a single monolithic file. In order to add custom painting textures, you will need to directly modify the provided resource pack, so that `/assets/minecraft/items/painting.json` references your custom models.

#### `item_model` component
This feature can be enabled with the command: `/invarpaint config server.item_model true`.

This will set the `item_model` component of a variant to `<namespace>:painting/<path>`, based on the painting's ID. You can provide your own models in a separate pack, but painting variants with no custom model will show up as a missing models.

### Non-vanilla clients with modded paintings.
If keeping your clients vanilla is not a concern, an easier way to handle paintings is to have them install [Variants-CIT](https://modrinth.com/mod/variants-cit). You'll only need to provide custom textures at `/assets/<namespace>/textures/item/painting/<variant>.png`, and can do so in a separate pack. The provided pack is still required, as it contains the configuration file required to make this mod work with paintings.
With this, painting variants that lack a custom texture will use a built-in generic texture.

## Miscellaneous changes
### Server-side
- Creative players can pick a painting's variant by holding Ctrl.
- Adds a new loot function `invarpaint:lock_variant_randomly`.
- Placement of variant-locked paintings in tight spaces is more forgiving. (Vanilla would require targeting one specific block.)
- Shows a warning when trying to place a painting in a space that is too small.
- Fixes [MC-257133](https://bugs.mojang.com/browse/MC-257133), whereby failing to place a painting causes an inventory desync, causing the client to believe it has consummed the item.

### Client-side
- Slightly reworked the tooltip for painting items. The painting title appears in the item name, and the author appears on the same line as the size.
