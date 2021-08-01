package net.silentchaos512.tokenenchanter.api.xp;

import net.minecraft.world.item.ItemStack;

public class XpStorageItemImpl extends XpStorage {
    private static final String NBT_LEVELS = "StoredLevels";

    private final ItemStack stack;

    public XpStorageItemImpl(ItemStack stack, int capacity, boolean canDrain) {
        super(capacity, canDrain);
        this.stack = stack;
    }

    @Override
    public float getLevels() {
        return stack.getOrCreateTag().getFloat(NBT_LEVELS);
    }

    @Override
    public void setLevels(float levels) {
        stack.getOrCreateTag().putFloat(NBT_LEVELS, levels);
    }
}
