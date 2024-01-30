# Minecraft Code Breaking Changes
### 1.19.4
Current master

### 1.20.0
- Crafting recipes and CrafitnScreenHandler receive a `RecipeInputInventory` instead of a `CraftingInventory`.
- Data pack format changed to 15. (Format range not supported in this version.) (Not really a breaking change.)

### 1.20.2
- Recipe constructors no longer take an ID
- Recipe `Serializer` must be backed up by a `Codec`
- Loot functions require a `Codec` instead of a `Serializer`
- Loot functions take conditions as a `List` instead of as an arrays.
- Trades should also be registered for the Trade Rebalance feature.
- CraftingScreenHandler receives recipes wrapped inside a `RecipeEntry`

### 1.20.3
- ~~Text serialization method moved elsewhere~~ (Yarn mapping change)
- Crafting category deserialization method was removed (Avoidable by using other methods)
- The field `CraftingRecipeCategory::CODEC` had its signature changed (Avoidable by getting it from elsewhere)
