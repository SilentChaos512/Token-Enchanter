package net.silentchaos512.tokenenchanter.data.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.lib.crafting.recipe.ExtendedShapedRecipe;
import net.silentchaos512.tokenenchanter.api.xp.IXpStorage;
import net.silentchaos512.tokenenchanter.capability.XpStorageCapability;
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
        LazyOptional<IXpStorage> retCap = ret.getCapability(XpStorageCapability.INSTANCE);

        if (retCap.isPresent()) {
            // Get sum of the stored levels on all crystal, to preserve them
            float storedLevels = 0;
            for (int i = 0; i < craftingInventory.getSizeInventory(); ++i) {
                ItemStack stack = craftingInventory.getStackInSlot(i);
                LazyOptional<IXpStorage> lazy = stack.getCapability(XpStorageCapability.INSTANCE);
                storedLevels += lazy.map(IXpStorage::getLevels).orElse(0f);
            }

            final float toAdd = storedLevels;
            retCap.ifPresent(xp -> xp.addLevels(toAdd));
        }

        return ret;
    }
}
