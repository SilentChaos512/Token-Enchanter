package net.silentchaos512.tokenenchanter.data.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.world.World;
import net.silentchaos512.lib.crafting.recipe.ExtendedShapedRecipe;
import net.silentchaos512.tokenenchanter.api.item.IXpCrystalItem;
import net.silentchaos512.tokenenchanter.setup.ModRecipes;

public class XpCrystalRecipe extends ExtendedShapedRecipe {
    public XpCrystalRecipe(ShapedRecipe recipe) {
        super(recipe);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.SHAPED_XP_CRYSTAL.get();
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        return getBaseRecipe().matches(craftingInventory, world);
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory craftingInventory) {
        ItemStack ret = getBaseRecipe().getCraftingResult(craftingInventory);

        if (ret.getItem() instanceof IXpCrystalItem) {
            // Get sum of the stored levels on all crystal, to preserve them
            int storedLevels = 0;
            for (int i = 0; i < craftingInventory.getSizeInventory(); ++i) {
                ItemStack stack = craftingInventory.getStackInSlot(i);
                if (stack.getItem() instanceof IXpCrystalItem) {
                    storedLevels += ((IXpCrystalItem) stack.getItem()).getLevels(stack);
                }
            }

            ((IXpCrystalItem) ret.getItem()).addLevels(ret, storedLevels);
        }

        return ret;
    }
}
