package net.silentchaos512.tokenenchanter.data.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.api.data.TokenEnchantingRecipeBuilder;
import net.silentchaos512.tokenenchanter.setup.ModBlocks;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.lib.data.ExtendedShapedRecipeBuilder;

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
    }

    private static void registerTokenEnchanting(Consumer<IFinishedRecipe> consumer) {
        TokenEnchantingRecipeBuilder.builder(Items.GOLDEN_APPLE, 1, 1)
                .token(Items.APPLE)
                .addIngredient(Tags.Items.INGOTS_GOLD, 4)
                .build(consumer, TokenMod.getId("token_enchanting/apple_test"));
    }
}
