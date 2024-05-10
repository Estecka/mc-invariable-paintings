# Minecraft Code Breaking Changes
### 1.19.4
Current master

### 1.20.0
#### No Workaround :
- Crafting recipes and CrafitnScreenHandler receive a `RecipeInputInventory` instead of a `CraftingInventory`.
- Data pack format changed to 15. (Format range not supported in this version.) (Not really a breaking change.)

### 1.20.2
#### No Workaround :
- Recipe constructors no longer take an ID
- Recipe `Serializer` must be backed up by a `Codec`
- Loot functions require a `Codec` instead of a `Serializer`
- Loot functions take conditions as a `List` instead of as an arrays.
- Trades should also be registered for the Trade Rebalance feature.
- CraftingScreenHandler receives recipes wrapped inside a `RecipeEntry`

### 1.20.3
#### Worked Around :
- `Text.Serializer::toJson` was renamed to `Text.Serialization::toJsonString`. (Yarn Mapping change)
- `CraftingRecipeCategory::CODEC::byId` was removed. In packets, use `readEnumConstant` instead.
- The field `CraftingRecipeCategory::CODEC` had its signature changed; obtain it via `StringIdentifiable::createCodec` instead.

### 1.20.5
- **Item componentization fundamentally changes the structure of variant-locked paintings.**
- `PaintingEntity::VARIANT_NBT_KEY` was removed. Use literal instead.
- `Codecs.createStrictOptionalFieldCodec` was removed; use `ConditionalLootFunction::addConditionsField` instead. (Formerly known as `method_53344`)
- `LootFunctionType` now has a type parameter.
- The constructor of `LootFunctionType` now takes a `MapCodec`.
- `DecorationItem::appendTooltip` now takes a `Item$TooltipContext` argument instead of a world.
- The old `TooltipContext` was renamed to `TooltipType`. (Yarn Mapping only ?)
