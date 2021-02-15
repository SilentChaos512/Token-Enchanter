# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
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
