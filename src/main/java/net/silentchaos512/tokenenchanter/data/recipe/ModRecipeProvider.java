package net.silentchaos512.tokenenchanter.data.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.silentchaos512.lib.data.ExtendedShapedRecipeBuilder;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.api.data.TokenEnchantingRecipeBuilder;
import net.silentchaos512.tokenenchanter.setup.ModBlocks;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.tokenenchanter.setup.ModRecipes;
import net.silentchaos512.tokenenchanter.setup.ModTags;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerTokenEnchanting(consumer);

        ExtendedShapedRecipeBuilder.vanillaBuilder(ModItems.GOLD_TOKEN, 12)
                .patternLine("///")
                .patternLine("lel")
                .patternLine("///")
                .key('/', Tags.Items.INGOTS_GOLD)
                .key('l', Tags.Items.GEMS_LAPIS)
                .key('e', Tags.Items.GEMS_EMERALD)
                .build(consumer);

        ExtendedShapedRecipeBuilder.vanillaBuilder(ModBlocks.TOKEN_ENCHANTER)
                .patternLine(" d ")
                .patternLine("/o/")
                .patternLine("/#/")
                .key('d', Tags.Items.GEMS_DIAMOND)
                .key('/', Tags.Items.INGOTS_GOLD)
                .key('o', ModItems.GOLD_TOKEN)
                .key('#', Tags.Items.STORAGE_BLOCKS_LAPIS)
                .build(consumer);

        ExtendedShapedRecipeBuilder.builder(ModRecipes.SHAPED_XP_CRYSTAL.get(), ModItems.SMALL_XP_CRYSTAL)
                .patternLine("e")
                .patternLine("o")
                .patternLine("e")
                .key('e', Tags.Items.GEMS_EMERALD)
                .key('o', ModItems.GOLD_TOKEN)
                .build(consumer);
        ExtendedShapedRecipeBuilder.builder(ModRecipes.SHAPED_XP_CRYSTAL.get(), ModItems.XP_CRYSTAL)
                .patternLine(" c ")
                .patternLine("bbb")
                .patternLine(" c ")
                .key('c', ModItems.SMALL_XP_CRYSTAL)
                .key('b', Items.BLAZE_POWDER)
                .build(consumer);
    }

    private static void registerTokenEnchanting(Consumer<IFinishedRecipe> consumer) {
        TokenEnchantingRecipeBuilder.builder(Items.GOLDEN_APPLE, 1, 1)
                .token(Items.APPLE)
                .addIngredient(Tags.Items.INGOTS_GOLD, 4)
                .build(consumer, TokenMod.getId("token_enchanting/apple_test"));

        enchantedToken(Enchantments.EFFICIENCY, 1)
                .addIngredient(Tags.Items.NUGGETS_GOLD, 4)
                .addIngredient(Tags.Items.DUSTS_REDSTONE, 9)
                .build(consumer);
        enchantedToken(Enchantments.SILK_TOUCH, 3)
                .addIngredient(Tags.Items.STORAGE_BLOCKS_EMERALD, 1)
                .addIngredient(Tags.Items.RODS_BLAZE, 4)
                .build(consumer);
        enchantedToken(Enchantments.UNBREAKING, 1)
                .addIngredient(Tags.Items.INGOTS_IRON, 8)
                .build(consumer);
    }

    private static TokenEnchantingRecipeBuilder enchantedToken(Enchantment enchantment, int levelCost) {
        ResourceLocation id = NameUtils.from(enchantment);
        return TokenEnchantingRecipeBuilder.builder(ModItems.ENCHANTED_TOKEN, 1, levelCost)
                .enchantment(enchantment, 1)
                .name(TokenMod.getId(String.format("token_enchanting/%s.%s", id.getNamespace(), id.getPath())))
                .token(ModTags.Items.TOKENS_GOLD);
    }
}
