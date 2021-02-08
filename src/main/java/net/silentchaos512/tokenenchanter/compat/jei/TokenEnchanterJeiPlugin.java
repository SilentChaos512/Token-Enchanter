package net.silentchaos512.tokenenchanter.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterScreen;
import net.silentchaos512.tokenenchanter.item.EnchantedTokenItem;
import net.silentchaos512.tokenenchanter.setup.ModBlocks;
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
        reg.addRecipes(getRecipesOfType(ModRecipes.TOKEN_ENCHANTING_TYPE), TOKEN_ENCHANTING);
    }

    private static List<IRecipe<?>> getRecipesOfType(IRecipeType<?> recipeType) {
        World world = Objects.requireNonNull(Minecraft.getInstance().world);
        return world.getRecipeManager().getRecipes().stream()
                .filter(r -> r.getType() == recipeType)
                .collect(Collectors.toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.TOKEN_ENCHANTER), TOKEN_ENCHANTING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration reg) {
        reg.addRecipeClickArea(TokenEnchanterScreen.class, 102, 32, 24, 23, TOKEN_ENCHANTING);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration reg) {
        // Enchanted tokens
        reg.registerSubtypeInterpreter(ModItems.ENCHANTED_TOKEN.get(), stack -> {
            Enchantment enchantment = EnchantedTokenItem.getSingleEnchantment(stack);
            return enchantment != null ? enchantment.getName() : "none";
        });
        // XP crystals
        /*ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof IXpCrystalItem).forEach(item -> {
            reg.registerSubtypeInterpreter(item, stack -> {
                float levels = ((IXpCrystalItem) item).getLevels(stack);
                return String.valueOf((int) levels);
            });
        });*/
    }
}
