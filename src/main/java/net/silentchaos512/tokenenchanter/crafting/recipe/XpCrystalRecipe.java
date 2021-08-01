package net.silentchaos512.tokenenchanter.crafting.recipe;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.lib.crafting.recipe.ExtendedShapedRecipe;
import net.silentchaos512.tokenenchanter.api.xp.IXpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageCapability;
import net.silentchaos512.tokenenchanter.setup.ModRecipes;

public class XpCrystalRecipe extends ExtendedShapedRecipe {
    public XpCrystalRecipe(ShapedRecipe recipe) {
        super(recipe);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SHAPED_XP_CRYSTAL.get();
    }

    @Override
    public boolean matches(CraftingContainer craftingInventory, Level world) {
        return getBaseRecipe().matches(craftingInventory, world);
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingInventory) {
        ItemStack ret = getBaseRecipe().assemble(craftingInventory);
        LazyOptional<IXpStorage> retCap = ret.getCapability(XpStorageCapability.INSTANCE);

        if (retCap.isPresent()) {
            // Get sum of the stored levels on all crystal, to preserve them
            float storedLevels = 0;
            for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
                ItemStack stack = craftingInventory.getItem(i);
                IXpStorage xp = stack.getCapability(XpStorageCapability.INSTANCE).orElse(XpStorage.INVALID);
                if (xp.canDrain()) {
                    storedLevels += xp.getLevels();
                }
            }

            final float toAdd = storedLevels;
            retCap.ifPresent(xp -> xp.addLevels(toAdd));
        }

        return ret;
    }
}
