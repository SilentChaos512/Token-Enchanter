package net.silentchaos512.tokenenchanter.block.tokenenchanter;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;
import net.silentchaos512.tokenenchanter.crafting.recipe.TokenEnchanterRecipe;
import net.silentchaos512.tokenenchanter.setup.ModRecipes;
import net.silentchaos512.tokenenchanter.setup.ModTileEntities;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class TokenEnchanterTileEntity extends LockableSidedInventoryTileEntity {
    private static final int INVENTORY_SIZE = 1 + 6 + 1;
    private static final int[] SLOTS_INPUT = IntStream.range(0, 7).toArray();
    private static final int[] SLOTS_OUTPUT = {7};
    private static final int[] SLOTS_ALL = IntStream.range(0, 8).toArray();

    protected final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            return 0;
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int size() {
            return 1;
        }
    };

    public TokenEnchanterTileEntity() {
        super(ModTileEntities.TOKEN_ENCHANTER.get(), INVENTORY_SIZE);
    }

    protected int[] getOutputSlots() {
        //noinspection AssignmentOrReturnOfFieldWithMutableType
        return SLOTS_OUTPUT;
    }

    @Nullable
    protected TokenEnchanterRecipe getRecipe() {
        if (world == null) return null;
        return world.getRecipeManager().getRecipe(ModRecipes.TOKEN_ENCHANTING_TYPE, this, world).orElse(null);
    }

    protected ItemStack getCraftingResult(TokenEnchanterRecipe recipe) {
        return recipe.getCraftingResult(this);
    }

    protected void consumeIngredients(TokenEnchanterRecipe recipe) {
        recipe.consumeIngredients(this);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.tokenenchanter.token_enchanter");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new TokenEnchanterContainer(id, playerInventory, this, this.fields);
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    public int[] getSlotsForFace(Direction side) {
        switch (side) {
            case UP:
                return SLOTS_INPUT;
            case DOWN:
                return SLOTS_ALL;
            default:
                return SLOTS_ALL;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index < INVENTORY_SIZE - 1;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == INVENTORY_SIZE - 1;
    }
}
