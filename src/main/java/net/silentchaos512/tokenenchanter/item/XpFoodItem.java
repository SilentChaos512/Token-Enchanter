package net.silentchaos512.tokenenchanter.item;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.tokenenchanter.api.xp.IXpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageCapability;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageItemImpl;
import net.silentchaos512.tokenenchanter.util.TextUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class XpFoodItem extends Item implements HasSubItems {
    public XpFoodItem(Properties properties) {
        super(properties);

        //noinspection OverridableMethodCallDuringObjectConstruction
        if (!isEdible()) {
            throw new IllegalArgumentException("XpFoodItem must be a food");
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                if (cap == XpStorageCapability.INSTANCE) {
                    return LazyOptional.of(() -> new XpStorageItemImpl(stack, Integer.MAX_VALUE, false)).cast();
                }
                return LazyOptional.empty();
            }
        };
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof Player) {
            float levels = stack.getCapability(XpStorageCapability.INSTANCE).map(IXpStorage::getLevels).orElse(0f);
            ((Player) entityLiving).giveExperienceLevels(Math.round(levels));
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        float levels = stack.getCapability(XpStorageCapability.INSTANCE).map(IXpStorage::getLevels).orElse(0f);
        tooltip.add(TextUtil.translate("item", "xp_food.levels", levels));
    }

    @Override
    public List<ItemStack> getSubItems() {
        NonNullList<ItemStack> items = NonNullList.create();
        items.add(createWithLevels(5));
        items.add(createWithLevels(10));
        items.add(createWithLevels(20));
        items.add(createWithLevels(30));
        return items;
    }

    public ItemStack createWithLevels(int levels) {
        ItemStack ret = new ItemStack(this);
        ret.getCapability(XpStorageCapability.INSTANCE).ifPresent(xp -> xp.setLevels(levels));
        return ret;
    }
}
