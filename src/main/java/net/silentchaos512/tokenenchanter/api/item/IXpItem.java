package net.silentchaos512.tokenenchanter.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.MathHelper;

public interface IXpItem extends IItemProvider {
    String NBT_LEVELS = "StoredLevels";

    default ItemStack createWithLevels(int levels) {
        ItemStack ret = new ItemStack(this);
        addLevels(ret, levels);
        return ret;
    }

    default float getLevels(ItemStack stack) {
        return stack.getOrCreateTag().getFloat(NBT_LEVELS);
    }

    default float getMaxLevels(ItemStack stack) {
        return Integer.MAX_VALUE;
    }

    default void drainLevels(ItemStack stack, float levelsToDrain) {
        float newLevels = MathHelper.clamp(getLevels(stack) - levelsToDrain, 0, getMaxLevels(stack));
        stack.getOrCreateTag().putFloat(NBT_LEVELS, newLevels);
    }

    default void addLevels(ItemStack stack, float levelsToAdd) {
        drainLevels(stack, -levelsToAdd);
    }
}
