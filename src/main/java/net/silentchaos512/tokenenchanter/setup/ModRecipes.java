package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.lib.crafting.recipe.ExtendedShapedRecipe;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.crafting.recipe.TokenEnchanterRecipe;
import net.silentchaos512.tokenenchanter.data.recipe.XpCrystalRecipe;

import java.util.function.Supplier;

public final class ModRecipes {
    public static final IRecipeType<TokenEnchanterRecipe> TOKEN_ENCHANTING_TYPE = IRecipeType.register(TokenMod.getId("token_enchanting").toString());

    public static final RegistryObject<IRecipeSerializer<?>> TOKEN_ENCHANTING = register("token_enchanting", TokenEnchanterRecipe.Serializer::new);

    public static final RegistryObject<IRecipeSerializer<?>> SHAPED_XP_CRYSTAL = register("shaped_xp_crystal", () ->
            ExtendedShapedRecipe.Serializer.basic(XpCrystalRecipe::new));

    private ModRecipes() {}

    static void register() {}

    private static RegistryObject<IRecipeSerializer<?>> register(String name, Supplier<IRecipeSerializer<?>> serializer) {
        return register(TokenMod.getId(name), serializer);
    }

    private static RegistryObject<IRecipeSerializer<?>> register(ResourceLocation id, Supplier<IRecipeSerializer<?>> serializer) {
        return Registration.RECIPE_SERIALIZERS.register(id.getPath(), serializer);
    }
}
