package net.silentchaos512.tokenenchanter.api.item;

import net.minecraft.item.ItemStack;

public interface IXpCrystalItem {
    float getLevels(ItemStack stack);

    float getMaxLevels(ItemStack stack);

    ItemStack drainLevels(ItemStack stack, float levelsToDrain);

    default ItemStack addLevels(ItemStack stack, float levelsToAdd) {
        return drainLevels(stack, -levelsToAdd);
    }
}
