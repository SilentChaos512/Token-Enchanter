package net.silentchaos512.tokenenchanter.data.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.lib.data.recipe.LibRecipeProvider;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.api.data.TokenEnchantingRecipeBuilder;
import net.silentchaos512.tokenenchanter.setup.ModBlocks;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.tokenenchanter.setup.ModRecipes;
import net.silentchaos512.tokenenchanter.setup.ModTags;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ModRecipeProvider extends LibRecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn, TokenMod.MOD_ID);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerCustomRecipe(consumer, ModRecipes.APPLY_ENCHANTED_TOKEN.get());

        registerTokenEnchanting(consumer);

        shapedBuilder(ModItems.GOLD_TOKEN, 16)
                .patternLine("///")
                .patternLine("lel")
                .patternLine("///")
                .key('/', Tags.Items.INGOTS_GOLD)
                .key('l', Tags.Items.GEMS_LAPIS)
                .key('e', Tags.Items.GEMS_EMERALD)
                .build(consumer);

        TagEmptyCondition silverTagEmpty = new TagEmptyCondition(ModTags.Items.INGOTS_SILVER.getName());
        NotCondition silverTagExists = new NotCondition(silverTagEmpty);

        shapedBuilder(ModItems.SILVER_TOKEN, 16)
                .addExtraData(json -> writeConditions(json, silverTagExists))
                .patternLine("///")
                .patternLine("lel")
                .patternLine("///")
                .key('/', ModTags.Items.INGOTS_SILVER)
                .key('l', Tags.Items.GEMS_LAPIS)
                .key('e', Tags.Items.GEMS_EMERALD)
                .build(consumer);

        // Alternative silver token recipe, loaded when no silver ingots are tagged
        shapedBuilder(ModItems.SILVER_TOKEN, 8)
                .addExtraData(json -> writeConditions(json, silverTagEmpty))
                .patternLine("/n/")
                .patternLine("lel")
                .patternLine("/n/")
                .key('/', Tags.Items.INGOTS_IRON)
                .key('l', Tags.Items.GEMS_LAPIS)
                .key('e', Tags.Items.GEMS_EMERALD)
                .key('n', Tags.Items.NUGGETS_GOLD)
                .build(consumer, modId("silver_token_no_silver"));

        shapedBuilder(ModBlocks.TOKEN_ENCHANTER)
                .patternLine(" d ")
                .patternLine("/t/")
                .patternLine("o#o")
                .key('d', Tags.Items.GEMS_DIAMOND)
                .key('/', Tags.Items.INGOTS_GOLD)
                .key('t', ModItems.GOLD_TOKEN)
                .key('o', Tags.Items.OBSIDIAN)
                .key('#', Tags.Items.STORAGE_BLOCKS_LAPIS)
                .build(consumer);

        shapedBuilder(ModRecipes.SHAPED_XP_CRYSTAL.get(), ModItems.SMALL_XP_CRYSTAL)
                .patternLine("e")
                .patternLine("o")
                .patternLine("e")
                .key('e', Tags.Items.GEMS_EMERALD)
                .key('o', ModItems.GOLD_TOKEN)
                .build(consumer);

        shapedBuilder(ModRecipes.SHAPED_XP_CRYSTAL.get(), ModItems.XP_CRYSTAL)
                .patternLine(" c ")
                .patternLine("bbb")
                .patternLine(" c ")
                .key('c', ModItems.SMALL_XP_CRYSTAL)
                .key('b', Items.BLAZE_POWDER)
                .build(consumer);

        shapedBuilder(ModRecipes.SHAPED_XP_CRYSTAL.get(), ModItems.LARGE_XP_CRYSTAL)
                .patternLine(" c ")
                .patternLine("ene")
                .patternLine(" c ")
                .key('c', ModItems.XP_CRYSTAL)
                .key('e', Items.ENDER_EYE)
                .key('n', Tags.Items.NETHER_STARS)
                .build(consumer);
    }

    private static void registerTokenEnchanting(Consumer<IFinishedRecipe> consumer) {
        TokenEnchantingRecipeBuilder.builder(Items.GOLDEN_APPLE, 1, 2)
                .token(Items.APPLE)
                .addIngredient(Tags.Items.INGOTS_GOLD, 6)
                .build(consumer, TokenMod.getId("golden_apple"));

        TokenEnchantingRecipeBuilder.builder(ModItems.XP_BREAD, 1, 10)
                .infuseLevels(8)
                .token(Items.BREAD)
                .addIngredient(Items.HONEY_BOTTLE, 1)
                .build(consumer);

        enchantedToken(Enchantments.AQUA_AFFINITY, 2)
                .addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS, 1)
                .addIngredient(Items.COD, 5)
                .build(consumer);
        enchantedToken(Enchantments.BANE_OF_ARTHROPODS, 2)
                .addIngredient(Tags.Items.NUGGETS_IRON, 6)
                .addIngredient(Items.SPIDER_EYE, 6)
                .build(consumer);
        enchantedToken(Enchantments.BLAST_PROTECTION, 2)
                .addIngredient(Tags.Items.INGOTS_BRICK, 8)
                .addIngredient(Tags.Items.GUNPOWDER, 6)
                .build(consumer);
        enchantedToken(Enchantments.CHANNELING, 4)
                .addIngredient(Items.NAUTILUS_SHELL, 1)
                .addIngredient(Tags.Items.INGOTS_GOLD, 12)
                .addIngredient(Items.ENDER_EYE, 4)
                .build(consumer);
        enchantedToken(Enchantments.DEPTH_STRIDER, 6)
                .addIngredient(Items.WARPED_FUNGUS, 1)
                .addIngredient(Items.CLAY, 8)
                .build(consumer);
        enchantedToken(Enchantments.EFFICIENCY, 2)
                .addIngredient(Tags.Items.NUGGETS_GOLD, 4)
                .addIngredient(Tags.Items.DUSTS_REDSTONE, 9)
                .build(consumer);
        enchantedToken(Enchantments.FEATHER_FALLING, 3)
                .addIngredient(ItemTags.WOOL, 5)
                .addIngredient(Tags.Items.FEATHERS, 10)
                .addIngredient(Tags.Items.SLIMEBALLS, 2)
                .build(consumer);
        enchantedToken(Enchantments.FIRE_ASPECT, 5)
                .addIngredient(Items.MAGMA_CREAM, 6)
                .addIngredient(Tags.Items.NETHERRACK, 30)
                .build(consumer);
        enchantedToken(Enchantments.FIRE_PROTECTION, 2)
                .addIngredient(Items.SNOW_BLOCK, 5)
                .addIngredient(Tags.Items.INGOTS_NETHER_BRICK, 20)
                .build(consumer);
        enchantedToken(Enchantments.FLAME, 5)
                .addIngredient(Tags.Items.RODS_BLAZE, 3)
                .addIngredient(Tags.Items.NETHERRACK, 30)
                .addIngredient(Items.ARROW, 4)
                .build(consumer);
        enchantedToken(Enchantments.FORTUNE, 6)
                .addIngredient(Tags.Items.GEMS_DIAMOND, 3)
                .addIngredient(Tags.Items.DUSTS_GLOWSTONE, 16)
                .build(consumer);
        enchantedToken(Enchantments.FROST_WALKER, 32)
                .addIngredient(Items.BLUE_ICE, 4)
                .addIngredient(Tags.Items.GEMS_PRISMARINE, 16)
                .addIngredient(Items.PURPUR_BLOCK, 64)
                .addIngredient(Items.BLACKSTONE, 32)
                .addIngredient(Items.MAGMA_BLOCK, 32)
                .build(consumer);
        enchantedToken(Enchantments.IMPALING, 3)
                .addIngredient(Items.NAUTILUS_SHELL, 1)
                .addIngredient(Items.IRON_SWORD, 1)
                .addIngredient(Tags.Items.DUSTS_PRISMARINE, 4)
                .build(consumer);
        enchantedToken(Enchantments.INFINITY, 16)
                .addIngredient(Tags.Items.STORAGE_BLOCKS_EMERALD, 1)
                .addIngredient(Items.ENDER_EYE, 4)
                .addIngredient(Items.ARROW, 64)
                .build(consumer);
        enchantedToken(Enchantments.KNOCKBACK, 1)
                .addIngredient(Items.PISTON, 2)
                .addIngredient(Tags.Items.FEATHERS, 5)
                .build(consumer);
        enchantedToken(Enchantments.LOOTING, 8)
                .addIngredient(Tags.Items.GEMS_EMERALD, 2)
                .addIngredient(Tags.Items.STORAGE_BLOCKS_REDSTONE, 3)
                .build(consumer);
        enchantedToken(Enchantments.LOYALTY, 6)
                .addIngredient(Items.NAUTILUS_SHELL, 1)
                .addIngredient(Items.CHAIN, 12)
                .build(consumer);
        enchantedToken(Enchantments.LUCK_OF_THE_SEA, 2)
                .addIngredient(Items.COD, 7)
                .addIngredient(Items.KELP, 7)
                .addIngredient(Items.SEAGRASS, 7)
                .build(consumer);
        enchantedToken(Enchantments.LURE, 2)
                .addIngredient(Items.TRIPWIRE_HOOK, 4)
                .addIngredient(Items.SALMON, 6)
                .build(consumer);
        enchantedToken(Enchantments.MENDING, 32)
                .addIngredient(Tags.Items.INGOTS_NETHERITE, 1)
                .addIngredient(Items.APPLE, 64)
                .addIngredient(Items.TWISTING_VINES, 16)
                .addIngredient(Items.CRIMSON_FUNGUS, 4)
                .addIngredient(Items.CHORUS_FRUIT, 32)
                .build(consumer);
        enchantedToken(Enchantments.MULTISHOT, 16)
                .addIngredient(Items.CROSSBOW, 1)
                .addIngredient(Items.TRIPWIRE_HOOK, 12)
                .addIngredient(Tags.Items.ENDER_PEARLS, 4)
                .build(consumer);
        enchantedToken(Enchantments.PIERCING, 2)
                .addIngredient(Items.CROSSBOW, 1)
                .addIngredient(Items.IRON_BARS, 12)
                .addIngredient(Tags.Items.INGOTS_GOLD, 1)
                .build(consumer);
        enchantedToken(Enchantments.POWER, 2)
                .addIngredient(Items.ARROW, 10)
                .addIngredient(Tags.Items.STRING, 5)
                .addIngredient(Tags.Items.DUSTS_REDSTONE, 5)
                .build(consumer);
        enchantedToken(Enchantments.PROJECTILE_PROTECTION, 2)
                .addIngredient(Tags.Items.INGOTS_IRON, 4)
                .addIngredient(Items.CHAIN, 2)
                .addIngredient(Items.ARROW, 4)
                .build(consumer);
        enchantedToken(Enchantments.PROTECTION, 2)
                .addIngredient(Items.IRON_BARS, 4)
                .addIngredient(Tags.Items.INGOTS_BRICK, 4)
                .build(consumer);
        enchantedToken(Enchantments.PUNCH, 1)
                .addIngredient(Items.PISTON, 2)
                .addIngredient(Items.ARROW, 4)
                .build(consumer);
        enchantedToken(Enchantments.QUICK_CHARGE, 4)
                .addIngredient(Items.CROSSBOW, 1)
                .addIngredient(Tags.Items.INGOTS_GOLD, 2)
                .addIngredient(Tags.Items.DUSTS_GLOWSTONE, 16)
                .build(consumer);
        enchantedToken(Enchantments.RESPIRATION, 4)
                .addIngredient(Items.PUFFERFISH, 2)
                .addIngredient(Items.GLASS_BOTTLE, 9)
                .build(consumer);
        enchantedToken(Enchantments.RIPTIDE, 6)
                .addIngredient(Items.NAUTILUS_SHELL, 1)
                .addIngredient(Tags.Items.FEATHERS, 20)
                .addIngredient(Tags.Items.GEMS_LAPIS, 5)
                .build(consumer);
        enchantedToken(Enchantments.SHARPNESS, 3)
                .addIngredient(Tags.Items.INGOTS_IRON, 1)
                .addIngredient(Items.FLINT, 8)
                .build(consumer);
        enchantedToken(Enchantments.SILK_TOUCH, 12)
                .addIngredient(Tags.Items.STORAGE_BLOCKS_EMERALD, 1)
                .addIngredient(Tags.Items.RODS_BLAZE, 4)
                .build(consumer);
        enchantedToken(Enchantments.SMITE, 2)
                .addIngredient(Tags.Items.GEMS_LAPIS, 2)
                .addIngredient(Items.ROTTEN_FLESH, 15)
                .build(consumer);
        enchantedToken(Enchantments.SOUL_SPEED, 4)
                .addIngredient(Tags.Items.STORAGE_BLOCKS_QUARTZ, 1)
                .addIngredient(Items.SOUL_SOIL, 20)
                .build(consumer);
        enchantedToken(Enchantments.SWEEPING, 1)
                .addIngredient(Items.CHAIN, 2)
                .addIngredient(Items.SUGAR_CANE, 8)
                .build(consumer);
        enchantedToken(Enchantments.THORNS, 3)
                .addIngredient(Items.IRON_BARS, 12)
                .addIngredient(Items.ROSE_BUSH, 8)
                .build(consumer);
        enchantedToken(Enchantments.UNBREAKING, 1)
                .addIngredient(Tags.Items.INGOTS_IRON, 6)
                .addIngredient(Tags.Items.COBBLESTONE, 12)
                .build(consumer);

        createCursedTokenRecipe(consumer, Enchantments.BINDING_CURSE, Tags.Items.STRING);
        createCursedTokenRecipe(consumer, Enchantments.VANISHING_CURSE, Tags.Items.GUNPOWDER);

        for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS.getValues()) {
            // Check for vanilla enchantments with missing recipes
            ResourceLocation id = NameUtils.from(enchantment);
            if ("minecraft".equals(id.getNamespace()) && !ENCHANTED_TOKEN_RECIPES_CREATED.contains(enchantment)) {
                throw new NullPointerException("Missing enchanted token recipe for '" + id + "'");
            }
        }
    }

    private static final Set<Enchantment> ENCHANTED_TOKEN_RECIPES_CREATED = new HashSet<>();

    private static TokenEnchantingRecipeBuilder enchantedToken(Enchantment enchantment, int levelCost) {
        ENCHANTED_TOKEN_RECIPES_CREATED.add(enchantment);

        return TokenEnchantingRecipeBuilder.enchantedTokenBuilder(enchantment, 1, 1, levelCost)
                .name(getEnchantedTokenRecipeId(enchantment));
    }

    private static void createCursedTokenRecipe(Consumer<IFinishedRecipe> consumer, Enchantment enchantment, ITag<Item> ingredient) {
        ENCHANTED_TOKEN_RECIPES_CREATED.add(enchantment);

        TokenEnchantingRecipeBuilder.builder(ModItems.ENCHANTED_TOKEN, 1, 1)
                .enchantment(enchantment, 1)
                .token(ModTags.Items.TOKENS_SILVER)
                .addIngredient(ingredient, 4)
                .build(consumer, getEnchantedTokenRecipeId(enchantment));
    }

    private static ResourceLocation getEnchantedTokenRecipeId(Enchantment enchantment) {
        ResourceLocation enchantmentId = NameUtils.from(enchantment);
        return TokenMod.getId(String.format("enchanted_token/%s.%s", enchantmentId.getNamespace(), enchantmentId.getPath()));
    }

    protected void writeConditions(JsonObject json, ICondition... conditions) {
        if (conditions.length > 0) {
            JsonArray array = new JsonArray();
            for (ICondition condition : conditions) {
                array.add(CraftingHelper.serialize(condition));
            }
            json.add("conditions", array);
        }
    }
}
