# Invariable Paintings

## Overview
Turns each painting variant into its own item.

**No new item type was added to the game**, variant-locked items are the same as the ones available in the vanilla creative menu.

When a painting is broken, it will drop the variant-locked item instead of the blank one.  
Blank paintings can no longer be placed in survival, but can be used as crafting ingredients.

### Dependencies
Some specific features depend on **[Patched](https://modrinth.com/mod/patched)** (server-side) and **[CIT Resewn](https://modrinth.com/mod/cit-resewn)** (client-side), but both are completely optional.

The mod is mostly optional on client. The worst a client without the mod may experience is a desyncing issue when trying to place a blank painting, or when trying to place a filled painting in too small a space.

## Obtaining paintings
### Trading
Filled paintings can be bought from **Master Shepherds** and **Wandering Traders**. Shepherds no longer sell variantless paintings, this will not retroactively apply to existing shepherds.

### Looting
This features requires **[Patched](https://modrinth.com/mod/patched)**.

Filled paintings may be found inside of many naturally generated chests, most suspicious soils, and while fishing.
f
### Crafting (experimental)
**Crafting of filled paintings is disabled by default,** it is only available as an experiment, and may receive significant changes. **If you enjoy the idea of paintings being rare collectibles, I don't recommend enabling crafting at all,** because it may considerably trivialize the process of completing a collection. For this reason, crafting was intentionally designed to feel obscure and a bit expensive.

There are several built-in datapacks that provide varying degrees of crafting freedom. More precise fine-tuning is possible by creating you own datapack.

#### Generalities
The cost of crafting Blank paintings was increased to **1 Phantom Membrane**, instead of wool.

Cloning is done by combining a blank painting to an already filled one; similarly to banner cloning or book cloning.

Crafting is done by applying one or several different dyes to a painting, either blank or filled. The recipe can be configured to use a specific amount of dyes, or a specific type of canvas. Results can be obfuscated, meaning you won't be able to see the resulting variant until you take it out of the crafting table and consume the ingredients.

The colour of the dyes has no particular meaning. The correspondance between each combination and each variant is random, but deterministic; it will only vary depending on the set of paintings you have installed.

Crafting will behave differently depending on how many dyes the recipe is configured to use:
- If the amount of possible dye combinations exceeds the amount of painting variants, (e.g, using 8 dyes), then multiple similar combinations will result in the same variant.
- If the amount painting variants exceeds the amount of possible dye combinations (e.g, using only 1 dye at once), then the variant of the ingredient painting will also affect the result. Only a limited set of paintings can be crafted from a blank canvas, but more can be crafted from different filled paintings.


#### Datapack Presets:

None of the packs are enabled by default.
You can manage them in the datapack menu when creating a new world, or using the `/datapack` command in already created worlds.

These presets may receive significant changes in the future.

Except for "Cloning", you should only need enable one if these at a time.

- **Unbound:** The least restrictive crafting pack. Paintings can be crafted using any amount of dyes, using either a blank painting or an already filled one. This is the only pack were crafting results are not obfuscated. This already includes the cloning recipe.
- **Expensive:** Crafting costs 8 different dies, and only accept Blank paintings as ingredients.
This pack can be used together with the cloning one.
- **Iterative:** Crafting only requires a single dye, thus only 16 different variants can be crafted from a blank canvas. More variants can be crafted by reusing filled paintings as ingredients. This will work even better if you have about a dozen of modded variants installed.
This pack can be used together with the cloning one.
- **Recycling:** The most restrictive crafting pack. Blank paintings cannot be crafted nor used as ingredients, making it impossible to create anything solely out of raw materials. Your only option for crafting one painting is to sacrifice another existing one; **this will work better with cloning disabled.** Crafting requires 8 different dyes.
- **Cloning:** Contains nothing but the cloning recipe, which is not included in most other presets. Using this pack alone own will make paintings will closer to banner templates or trimming templates: You can only find new paintings by exploring, but once you find one, you can reuse it indefinitely.

## Inventory painting textures
This feature requires **[CIT Resewn](https://modrinth.com/mod/cit-resewn)**.

Allows filled paintings to have unique textures in the inventory, depending on their variant.

This is, strictly speaking, not a feature of Invariable-Paintings. It provides a built-in resource pack that is then used by CIT-Resewn, but does not add any of its own code on top of it.

Invariable-Paintings only provides the CITs for the vanilla variants. For modded variants, CITs should be provided in an external resource pack. See [the sources](https://github.com/Estecka/mc-invariable-paintings/tree/HEAD/src/client/resources/resourcepacks/vanilla-cit) for an example on how to make one, and refer to [CIT resewn's documentation](https://citresewn.shcm.io/) for additional information.

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

Mods that bypass the vanilla system and implement their own, like _Custom Paintings_ or _Client Paintings_, are not compatible. Amongst the user-defined painting mods I know of, [More Canvases v2](https://modrinth.com/mod/more-canvases) is the one that is compatible with Invariable Paintings.

Mods that give you a Painting selection GUI, or that lets you edit already placed paintings, are not necessarily incompatible on a technical level, but are conceptually incompatible with the idea of collectible paintings.
