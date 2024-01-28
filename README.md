# Invariable Paintings

## Overview
Turns each painting variant into its own item.

**No new item type was added to the game**, variant-locked items are the same as the ones available in the vanilla creative menu.

When a painting is broken, it will drop the variant-locked item instead of the blank one.  
Blank paintings can no longer be placed in survival, but can be used as crafting ingredients.

### Dependencies
**[Patched](https://modrinth.com/mod/patched)** (server-side) is required in order to add items to some loot tables. However Invarpaint can still safely run without it.

Invarpaint is mostly optional on client. The worst a client without the mod may experience is a desyncing issue when trying to place a blank painting, or when trying to place a filled painting in space too that is too small.

## Obtaining paintings
### Trading
Filled paintings can be bought from **Master Shepherds** and **Wandering Traders**. Shepherds no longer sell variantless paintings. (This will not retroactively apply to existing shepherds).

### Looting
This features requires **[Patched](https://modrinth.com/mod/patched)**.

Filled paintings may be found inside of many naturally generated chests, most suspicious soils, and while fishing.  
The loot tables are provided as a built-in datapack which can be disabled.

### Crafting (experimental)
**Crafting of filled paintings is disabled by default,** it is only available as an experiment, and may receive significant changes. **If you enjoy paintings being rare collectibles, I don't recommend enabling crafting at all,** because it may considerably trivialize the process of completing a collection. For this reason, crafting was intentionally designed to feel obscure and a bit expensive.

There are several built-in datapacks that provide varying degrees of crafting freedom. More precise fine-tuning is possible by creating you own datapack. 

See [the wiki](https://github.com/Estecka/mc-invariable-paintings/wiki/Painting-Creation-Recipe) for the specifics on how crafting works, and how it can be configured.

#### General rules
- The cost of crafting blank paintings was increased to **1 Phantom Membrane** instead of 1 wool.
- Cloning is done by combining a blank painting with an already filled one; similarly to how banner cloning and book cloning works.
- Filling a painting is done by combining it with dyes. The colour of the dyes has no particular meaning. The correspondance between each combination and each variant is semi-randomly determined, based on set of paintings you have installed.
- The resulting painting is usually obfuscated until you take it out of the crafting table.

#### Datapack Presets:
None of the packs are enabled by default.
You can manage them in the datapack menu when creating a new world, or using the `/datapack` command in already created worlds.

These presets may receive significant changes in the future.

Except for Cloning, you should only need to enable one of these packs at a time.

- **Unbound:** The least restrictive crafting pack. Paintings can be crafted using any amount of dyes, using either a blank painting or an already filled one. This is the only pack were crafting results are not obfuscated. This already includes the cloning recipe.
- **Expensive:** Crafting costs 8 different dies, and only accept Blank paintings as ingredients.  
This pack can be used together with the cloning one.
- **Iterative:** Crafting only requires a single dye, thus only 16 different variants can be crafted from a blank canvas. More variants can be crafted by reusing filled paintings as ingredients. This will work better with more painting variants installed into the game.  
This pack can be used together with the cloning one.
- **Recycling:** The most restrictive crafting pack. Blank paintings cannot be crafted nor used as ingredients, making it impossible to create anything solely out of raw materials. Your only option for crafting one painting is to sacrifice another existing one. This will work better with cloning disabled. Crafting requires 8 different dyes.
- **Cloning:** Contains nothing but the cloning recipe, which is not included in most other presets. Using this pack alone will make paintings closer to banner templates or trimming templates: You can only find new paintings by exploring, but once you find one, you can reuse it indefinitely.


## Inventory Icons

Paintings in the inventory can have unique textures depending on their variant. 

Invarpaint provides icons for vanilla paintings, but it does not currently generate icons for modded paintings. Those should be provided via resource packs.

Custom icons are located at `/textures/<namespace>/item/painting/<variant>.png`, and matched to the variant with the corresponding id. Variants that lack a custom icon will fallback to a generic built-in one.

Older versions of the mods relied on CIT-resewn, but this is no longer necessary. CIT-resewn is still compatible, and old texture packs should still be able to override Invarpaint's textures. However, switching to Invarpaint's specialized cit logic should be preferred to improve performances.


## Miscellaneous tweaks
### Server-side
- Placement of variant-locked paintings in tight spaces is more forgiving. (Vanilla would require targeting some specific block.)
- Added a new loot function `lock_variant_randomly`, which can applied to painting items in loot tables.

### Client-side
- Fixes a vanilla bug whereby paiting items may sometimes appear to be consumed, without actually placing the painting.
- Shows a warning when trying to place a painting in too small a space.
- Slightly reworked the tooltip for painting items.


## Compatibility
Any mod that adds new paintings based on the vanilla system will be compatible, and their paintings will be obtainable through all existing  means.

Mods that bypass the vanilla system and implement their own, like _Custom Paintings_ or _Client Paintings_, are not compatible. Amongst user-defined painting mods, [More Canvases](https://modrinth.com/mod/more-canvases) was made to be compatible with Invariable Paintings.

Mods that give you a Painting selection GUI, or that lets you edit already placed paintings, are not necessarily incompatible on a technical level, but are conceptually incompatible with the idea of collectible paintings.
