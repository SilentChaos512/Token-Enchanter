package net.silentchaos512.tokenenchanter.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterContainerMenu;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterContainerScreen;
import net.silentchaos512.tokenenchanter.item.EnchantedTokenItem;
import net.silentchaos512.tokenenchanter.item.XpCrystalItem;
import net.silentchaos512.tokenenchanter.setup.ModBlocks;
import net.silentchaos512.tokenenchanter.setup.ModContainers;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.tokenenchanter.setup.ModRecipes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class TokenEnchanterJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = TokenMod.getId("plugin");
    static final ResourceLocation TOKEN_ENCHANTING = TokenMod.getId("category/token_enchanting");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        IGuiHelper guiHelper = reg.getJeiHelpers().getGuiHelper();
        reg.addRecipeCategories(
                new TokenEnchantingRecipeCategoryJei(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        reg.addRecipes(TokenEnchantingRecipeCategoryJei.RECIPE_TYPE, getRecipesOfType(ModRecipes.TOKEN_ENCHANTING_TYPE.get()));
    }

    private static <T extends Recipe<?>> List<T> getRecipesOfType(RecipeType<T> recipeType) {
        Level world = Objects.requireNonNull(Minecraft.getInstance().level);
        return world.getRecipeManager().getRecipes().stream()
                .filter(r -> r.getType() == recipeType)
                .map(r -> (T) r)
                .collect(Collectors.toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.TOKEN_ENCHANTER), TokenEnchantingRecipeCategoryJei.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration reg) {
        reg.addRecipeClickArea(TokenEnchanterContainerScreen.class, 102, 32, 24, 23, TokenEnchantingRecipeCategoryJei.RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(TokenEnchanterContainerMenu.class, ModContainers.TOKEN_ENCHANTER.get(), TokenEnchantingRecipeCategoryJei.RECIPE_TYPE, 0, 8, 9, 36);
//        registration.addRecipeTransferHandler(new TokenEnchantingRecipeTransferHandler(), TOKEN_ENCHANTING);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration reg) {
        // Enchanted tokens
        reg.registerSubtypeInterpreter(ModItems.ENCHANTED_TOKEN.get(), (stack, context) -> {
            Enchantment enchantment = EnchantedTokenItem.getSingleEnchantment(stack);
            return enchantment != null ? enchantment.getDescriptionId() : "none";
        });
        // XP crystals
        ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof XpCrystalItem).forEach(item -> {
            reg.registerSubtypeInterpreter(item, (stack, context) -> {
                return String.valueOf(XpCrystalItem.getLevels(stack));
            });
        });

    }
}
