# v1
## 1.0
- Initial release

## 1.1
- Fixed a vanilla client bug, whereby placing a variant-locked painting too close to a wall would seemingly consume the item without placing the painting.

## 1.2
### 1.2.0
- Added unique item textures for vanilla variants.
- Added custom painting placing logic, ensuring placing a variant-locked will never fail so long as there is enough space nearby.
### 1.2.1
- Added painting trades to the rebalanced Wandering Trader
- Added missing credits
### 1.2.2
- Added paintings to the loot table of most Suspicious soils.

## 1.3
### 1.3.0
- Added some experimental crafting recipe for paintings.
- Added a default CIT for filled paintings.
- Vanilla CITs can be disabled in the resource-pack menu.
- The tooltip handles missing translation variables more gracefully
### 1.3.1
- Crafting recipe are now fully data-driven. The corresponding gamerules have been disused.
- Added a dye count option to the crafting recipes.
- Added partitioned crafting logic, allowing the crafting of any variant, even using short combinations of dyes.
- Added several recipe preset datapacks.
- Moved the loot tables to a datapack, which can be disabled.
### 1.3.2
- Updated version dependencies

## 1.4
- Added a specialized and more performant CIT logic for painting items. CIT-Resewn is no longer a dependency.
- Painting variants can be picked with Ctrl
- Increased the price of paintings trades.
- "Recycling" preset no longer forbids blank canvas crafting.
- `lock_variant_randomly` is now also available under the `invarpaint` namespace. Marked the old id as deprecated.


# v2
## 2.0.0
Additions:
- Some vanilla variants are now exclusive to specific sources. Added paintings to a few more locations.
- `lock_variant_randomly` may now specify a pool of variants to pull from.
- The inventory-desync fix is now fully server-side.
- The warning for placing a painting in a smaller space is now translated server-side.

Removals:
- Vanilla CITs are no longer an optional resource pack. (They are back into the core assets.)
- Removed the deprecated name `minecraft:lock_variant_randomly`.
- Removed crafting

## 2.1.0
- Ported to MC 1.21
- CITs are now generated based on available textures, instead of using the painting registry.
- Shuffled a little the repartion of painting loot, including the new paintings.
- Added CITs for the new painting
