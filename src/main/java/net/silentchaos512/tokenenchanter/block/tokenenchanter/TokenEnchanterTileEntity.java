package net.silentchaos512.tokenenchanter.block.tokenenchanter;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;
import net.silentchaos512.tokenenchanter.capability.XpStorageCapability;
import net.silentchaos512.tokenenchanter.crafting.recipe.TokenEnchanterRecipe;
import net.silentchaos512.tokenenchanter.setup.ModRecipes;
import net.silentchaos512.tokenenchanter.setup.ModTileEntities;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class TokenEnchanterTileEntity extends LockableSidedInventoryTileEntity implements ITickableTileEntity {
    public static final int PROCESS_TIME = 50;

    private static final int INVENTORY_SIZE = 2 + 6 + 1;
    private static final int[] SLOTS_INPUT = IntStream.range(1, 8).toArray();
    private static final int[] SLOTS_OUTPUT = {8};
    private static final int[] SLOTS_ALL = IntStream.range(0, 9).toArray();

    private int progress;

    @SuppressWarnings("OverlyComplexAnonymousInnerClass")
    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return TokenEnchanterTileEntity.this.progress;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    TokenEnchanterTileEntity.this.progress = value;
            }
        }

        @Override
        public int size() {
            return 1;
        }
    };

    public TokenEnchanterTileEntity() {
        super(ModTileEntities.TOKEN_ENCHANTER.get(), INVENTORY_SIZE);
    }

    @Nullable
    private TokenEnchanterRecipe getRecipe() {
        if (world == null) return null;
        return world.getRecipeManager().getRecipe(ModRecipes.TOKEN_ENCHANTING_TYPE, this, world).orElse(null);
    }

    @SuppressWarnings("TypeMayBeWeakened")
    private ItemStack getCraftingResult(TokenEnchanterRecipe recipe) {
        return recipe.getCraftingResult(this);
    }

    private void consumeIngredients(TokenEnchanterRecipe recipe) {
        recipe.consumeIngredients(this);
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        TokenEnchanterRecipe recipe = getRecipe();
        if (recipe != null && canMachineRun(recipe)) {
            // Process
            ++progress;

            if (progress >= PROCESS_TIME) {
                // Create result
                storeResultItem(getCraftingResult(recipe));
                consumeIngredients(recipe);
                progress = 0;
            }
        } else {
            progress = 0;
        }
    }

    private boolean canMachineRun(TokenEnchanterRecipe recipe) {
        return world != null && recipe.matches(this, this.world) && hasRoomForOutputItem(getCraftingResult(recipe));
    }

    private boolean hasRoomForOutputItem(ItemStack stack) {
        return canItemsStack(stack, getStackInSlot(INVENTORY_SIZE - 1));
    }

    private static boolean canItemsStack(ItemStack a, ItemStack b) {
        // Determine if the item stacks can be merged
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }

    private void storeResultItem(ItemStack stack) {
        // Merge the item into any output slot it can fit in
        ItemStack output = getStackInSlot(INVENTORY_SIZE - 1);
        if (canItemsStack(stack, output)) {
            if (output.isEmpty()) {
                setInventorySlotContents(INVENTORY_SIZE - 1, stack);
            } else {
                output.setCount(output.getCount() + stack.getCount());
            }
        }
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
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == INVENTORY_SIZE - 1;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0) {
            return stack.getCapability(XpStorageCapability.INSTANCE).isPresent();
        }
        return index < INVENTORY_SIZE - 1;
    }
}
