package net.silentchaos512.tokenenchanter.block.tokenenchanter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;
import net.silentchaos512.tokenenchanter.api.xp.IXpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageCapability;
import net.silentchaos512.tokenenchanter.crafting.recipe.TokenEnchanterRecipe;
import net.silentchaos512.tokenenchanter.setup.ModBlockEntityTypes;
import net.silentchaos512.tokenenchanter.setup.ModBlocks;
import net.silentchaos512.tokenenchanter.setup.ModRecipes;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class TokenEnchanterBlockEntity extends LockableSidedInventoryTileEntity {
    public static final int PROCESS_TIME = 50;

    private static final int INVENTORY_SIZE = 2 + 6 + 1;
    private static final int[] SLOTS_INPUT = IntStream.range(1, 8).toArray();
    private static final int[] SLOTS_OUTPUT = {8};
    private static final int[] SLOTS_ALL = IntStream.range(0, 9).toArray();

    private int progress;

    @SuppressWarnings("OverlyComplexAnonymousInnerClass")
    private final ContainerData fields = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return TokenEnchanterBlockEntity.this.progress;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    TokenEnchanterBlockEntity.this.progress = value;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public TokenEnchanterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.TOKEN_ENCHANTER.get(), INVENTORY_SIZE, pos, state);
    }

    TokenEnchanterBlockEntity() {
        super(ModBlockEntityTypes.TOKEN_ENCHANTER.get(), INVENTORY_SIZE, BlockPos.ZERO, ModBlocks.TOKEN_ENCHANTER.asBlockState());
    }

    @Nullable
    private TokenEnchanterRecipe getRecipe() {
        if (level == null) return null;
        return level.getRecipeManager().getRecipeFor(ModRecipes.TOKEN_ENCHANTING_TYPE.get(), this, level).orElse(null);
    }

    @SuppressWarnings("TypeMayBeWeakened")
    private ItemStack getCraftingResult(TokenEnchanterRecipe recipe) {
        return recipe.assemble(this, null);
    }

    private void consumeIngredients(TokenEnchanterRecipe recipe) {
        recipe.consumeIngredients(this);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TokenEnchanterBlockEntity blockEntity) {
        TokenEnchanterRecipe recipe = blockEntity.getRecipe();
        if (recipe != null && blockEntity.canMachineRun(recipe)) {
            // Process
            ++blockEntity.progress;

            if (blockEntity.progress >= PROCESS_TIME) {
                // Create result
                blockEntity.storeResultItem(blockEntity.getCraftingResult(recipe));
                blockEntity.consumeIngredients(recipe);
                blockEntity.progress = 0;
            }
        } else {
            blockEntity.progress = 0;
        }
    }

    private boolean canMachineRun(TokenEnchanterRecipe recipe) {
        return level != null && recipe.matches(this, this.level) && hasRoomForOutputItem(getCraftingResult(recipe));
    }

    private boolean hasRoomForOutputItem(ItemStack stack) {
        return canItemsStack(stack, getItem(INVENTORY_SIZE - 1));
    }

    private static boolean canItemsStack(ItemStack a, ItemStack b) {
        // Determine if the item stacks can be merged
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }

    private void storeResultItem(ItemStack stack) {
        // Merge the item into any output slot it can fit in
        ItemStack output = getItem(INVENTORY_SIZE - 1);
        if (canItemsStack(stack, output)) {
            if (output.isEmpty()) {
                setItem(INVENTORY_SIZE - 1, stack);
            } else {
                output.setCount(output.getCount() + stack.getCount());
            }
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.tokenenchanter.token_enchanter");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return new TokenEnchanterContainerMenu(id, playerInventory, this, this.fields);
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
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return canPlaceItem(index, itemStackIn);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == INVENTORY_SIZE - 1;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (index == 0) {
            IXpStorage xp = stack.getCapability(XpStorageCapability.INSTANCE).orElse(XpStorage.INVALID);
            return xp.canDrain();
        }
        return index < INVENTORY_SIZE - 1;
    }
}
