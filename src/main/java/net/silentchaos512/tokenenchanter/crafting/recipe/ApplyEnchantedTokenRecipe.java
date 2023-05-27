package net.silentchaos512.tokenenchanter.crafting.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.silentchaos512.tokenenchanter.item.EnchantedTokenItem;
import net.silentchaos512.tokenenchanter.setup.ModRecipes;

import java.util.List;

public class ApplyEnchantedTokenRecipe extends CustomRecipe {
    public ApplyEnchantedTokenRecipe(ResourceLocation idIn, CraftingBookCategory category) {
        super(idIn, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        return !assemble(inv, null).isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack tool = ItemStack.EMPTY;
        List<ItemStack> tokens = NonNullList.create();

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof EnchantedTokenItem) {
                    // Any number of tokens
                    tokens.add(stack);
                } else {
                    // Only one tool
                    if (!tool.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    tool = stack;
                }
            }
        }

        if (tool.isEmpty() || tokens.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = tool.copy();
        result.setCount(1);
        for (ItemStack token : tokens) {
            result = EnchantedTokenItem.applyTokenToItem(token, result);
        }
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.APPLY_ENCHANTED_TOKEN.get();
    }
}
