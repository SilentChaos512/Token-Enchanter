package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.RegistryObject;
import net.silentchaos512.lib.crafting.recipe.ExtendedShapedRecipe;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.crafting.recipe.ApplyEnchantedTokenRecipe;
import net.silentchaos512.tokenenchanter.crafting.recipe.TokenEnchanterRecipe;
import net.silentchaos512.tokenenchanter.crafting.recipe.XpCrystalRecipe;

import java.util.function.Supplier;

public final class ModRecipes {
    public static final RegistryObject<RecipeType<TokenEnchanterRecipe>> TOKEN_ENCHANTING_TYPE = registerType("token_enchanting");

    public static final RegistryObject<RecipeSerializer<?>> TOKEN_ENCHANTING = register("token_enchanting", TokenEnchanterRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<?>> SHAPED_XP_CRYSTAL = register("shaped_xp_crystal", () ->
            ExtendedShapedRecipe.Serializer.basic(XpCrystalRecipe::new));

    public static final RegistryObject<SimpleRecipeSerializer<?>> APPLY_ENCHANTED_TOKEN = register("apply_enchanted_token", () ->
            new SimpleRecipeSerializer<>(ApplyEnchantedTokenRecipe::new));

    private ModRecipes() {}

    static void register() {}

    private static <T extends RecipeSerializer<?>> RegistryObject<T> register(String name, Supplier<T> serializer) {
        return register(TokenMod.getId(name), serializer);
    }

    private static <T extends RecipeSerializer<?>> RegistryObject<T> register(ResourceLocation id, Supplier<T> serializer) {
        return Registration.RECIPE_SERIALIZERS.register(id.getPath(), serializer);
    }

    private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> registerType(final String name) {
        return Registration.RECIPE_TYPES.register(name, () -> new RecipeType<T>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }
}
