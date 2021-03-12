package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.lib.crafting.recipe.ExtendedShapedRecipe;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.crafting.recipe.ApplyEnchantedTokenRecipe;
import net.silentchaos512.tokenenchanter.crafting.recipe.TokenEnchanterRecipe;
import net.silentchaos512.tokenenchanter.crafting.recipe.XpCrystalRecipe;

import java.util.function.Supplier;

public final class ModRecipes {
    public static final IRecipeType<TokenEnchanterRecipe> TOKEN_ENCHANTING_TYPE = IRecipeType.register(TokenMod.getId("token_enchanting").toString());

    public static final RegistryObject<IRecipeSerializer<?>> TOKEN_ENCHANTING = register("token_enchanting", TokenEnchanterRecipe.Serializer::new);

    public static final RegistryObject<IRecipeSerializer<?>> SHAPED_XP_CRYSTAL = register("shaped_xp_crystal", () ->
            ExtendedShapedRecipe.Serializer.basic(XpCrystalRecipe::new));

    public static final RegistryObject<SpecialRecipeSerializer<?>> APPLY_ENCHANTED_TOKEN = register("apply_enchanted_token", () ->
            new SpecialRecipeSerializer<>(ApplyEnchantedTokenRecipe::new));

    private ModRecipes() {}

    static void register() {}

    private static <T extends IRecipeSerializer<?>> RegistryObject<T> register(String name, Supplier<T> serializer) {
        return register(TokenMod.getId(name), serializer);
    }

    private static <T extends IRecipeSerializer<?>> RegistryObject<T> register(ResourceLocation id, Supplier<T> serializer) {
        return Registration.RECIPE_SERIALIZERS.register(id.getPath(), serializer);
    }
}
