package net.silentchaos512.tokenenchanter.block.tokenenchanter;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.silentchaos512.lib.inventory.SlotOutputOnly;
import net.silentchaos512.lib.util.InventoryUtils;
import net.silentchaos512.tokenenchanter.setup.ModContainers;

import javax.annotation.Nonnull;

public class TokenEnchanterContainerMenu extends AbstractContainerMenu {
    private final TokenEnchanterBlockEntity tileEntity;
    private final ContainerData fields;

    public TokenEnchanterContainerMenu(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, new TokenEnchanterBlockEntity(), new SimpleContainerData(1));
    }

    @SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
    public TokenEnchanterContainerMenu(int id, Inventory playerInventory, TokenEnchanterBlockEntity tileEntity, ContainerData fields) {
        super(ModContainers.TOKEN_ENCHANTER.get(), id);

        this.tileEntity = tileEntity;
        this.fields = fields;
        addDataSlots(this.fields);

        // XP crystal
        this.addSlot(new Slot(this.tileEntity, 0, 22, 55) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return TokenEnchanterContainerMenu.this.tileEntity.canPlaceItem(0, stack);
            }
        });
        // Token
        this.addSlot(new Slot(this.tileEntity, 1, 22, 35));
        // Other ingredients
        this.addSlot(new Slot(this.tileEntity, 2, 48, 25));
        this.addSlot(new Slot(this.tileEntity, 3, 66, 25));
        this.addSlot(new Slot(this.tileEntity, 4, 84, 25));
        this.addSlot(new Slot(this.tileEntity, 5, 48, 43));
        this.addSlot(new Slot(this.tileEntity, 6, 66, 43));
        this.addSlot(new Slot(this.tileEntity, 7, 84, 43));
        // Output
        this.addSlot(new SlotOutputOnly(this.tileEntity, 8, 132, 35));

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);
    }

    public int getProgress() {
        return this.fields.get(0);
    }

    public int getProcessTime() {
        return TokenEnchanterBlockEntity.PROCESS_TIME;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.tileEntity.stillValid(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();

            final int playerStart = 9;
            final int playerEnd = playerStart + 36;

            if (slotIndex == 8) {
                // Transfer from output slot
                if (!this.moveItemStackTo(stack1, playerStart, playerEnd, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack1, stack);
            } else if (slotIndex > 8) {
                // Transfer from player
                if (this.tileEntity.canPlaceItem(0, stack1)) {
                    // XP crystal
                    if (!this.moveItemStackTo(stack1, 0, 1, false)) {
                        // Tokens and ingredients
                        if (!this.moveItemStackTo(stack1, 1, 8, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else if (this.tileEntity.canPlaceItem(1, stack1)) {
                    // Tokens and ingredients
                    if (!this.moveItemStackTo(stack1, 1, 8, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // Move from hotbar to backpack or vice versa
                    final int hotbarStart = playerStart + 27;
                    if (slotIndex < hotbarStart) {
                        if (!this.moveItemStackTo(stack1, hotbarStart, playerEnd, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (slotIndex < playerEnd && !this.moveItemStackTo(stack1, playerStart, hotbarStart, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(stack1, playerStart, playerEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack1);
        }

        return stack;
    }
}
