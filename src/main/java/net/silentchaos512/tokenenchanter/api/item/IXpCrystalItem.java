package net.silentchaos512.tokenenchanter.api.item;

import net.minecraft.item.ItemStack;

public interface IXpCrystalItem extends IXpItem {
    @Override
    float getMaxLevels(ItemStack stack);

    int getFillAmount(ItemStack stack);
}
