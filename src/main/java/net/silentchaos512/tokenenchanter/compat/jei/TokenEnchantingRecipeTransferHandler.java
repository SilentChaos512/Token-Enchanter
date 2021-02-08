package net.silentchaos512.tokenenchanter.compat.jei;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterContainer;

import javax.annotation.Nullable;

public class TokenEnchantingRecipeTransferHandler implements IRecipeTransferHandler<TokenEnchanterContainer> {
    @Override
    public Class<TokenEnchanterContainer> getContainerClass() {
        return TokenEnchanterContainer.class;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(TokenEnchanterContainer container, Object recipeIn, IRecipeLayout recipeLayout, PlayerEntity player, boolean maxTransfer, boolean doTransfer) {
        // FIXME
        return null;
    }
}
