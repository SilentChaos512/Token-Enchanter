# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.4.0] - 2022-09-06
- Updated for Minecraft 1.19.x

## [1.3.1] - 2022-04-18
### Fixed
- Crash caused by a typo in mods.toml [#5]

## [1.3.0] - 2022-04-17
- Updated for Minecraft 1.18.2

## [1.2.0] - 2021-12-10
- Updated for Minecraft 1.18.x

## [1.1.2] - 2021-09-04
- JEI support is back!

## [1.1.1] - 2021-08-04
### Fixed
- Crash with Forge 37.0.24 (updating Forge _not_ required)
- Error with simple dungeon loot table injector

## [1.1.0] - 2021-08-01
- Updated for Minecraft 1.17.1

## [1.0.5] - 2021-07-22
### Changed
- Updated to official Mojang mappings and Silent Lib 4.10.x (required)

## [1.0.4] - 2021-03-28
### Added
- Enchanted tokens can now be applied to books to create enchanted books [#2]

## [1.0.3] - 2021-03-11
### Added
- Silver tokens (has an alternative recipe if silver ingots are not available)
- Recipes for curse tokens (because why not?)
- Unique model for curse enchantment tokens
### Changed
- Tweaked token textures
- Reorder token types in JEI

## [1.0.2] - 2021-02-15
### Changed
- (API) Rework `IXpItem` and `IXpCrystalItem` into a capability so that mods can add XP crystal/food items without adding a required dependency
### Fixed
- XP crystals taking more levels from the player than they can store when too full
- Invalid items going into the token enchanter's XP crystal slot

## [1.0.1] - 2021-02-08
### Fixed
- Unable to apply enchanted tokens to anything (missing recipe)

## [1.0.0] - 2021-02-08
### Added
- Large XP Crystal (stores 100 levels, expensive to craft)
- Token enchanter will now process recipes
- Recipes for all vanilla enchanted tokens
- Recipe to craft XP bread (extracts levels from crystals, but loses 20%)
- JEI recipe transfer handler (incomplete, does not properly recognize item counts)

## [0.1.0] - 2021-02-08
- Initial alpha build, not fully working yet (some items work, recipes do not)
### Added
- Most of the things :)
