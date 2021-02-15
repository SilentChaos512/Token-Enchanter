package net.silentchaos512.tokenenchanter.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.tokenenchanter.api.xp.IXpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageItemImpl;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageCapability;
import net.silentchaos512.tokenenchanter.util.TextUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class XpFoodItem extends Item {
    public XpFoodItem(Properties properties) {
        super(properties);

        //noinspection OverridableMethodCallDuringObjectConstruction
        if (!isFood()) {
            throw new IllegalArgumentException("XpFoodItem must be a food");
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
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
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            float levels = stack.getCapability(XpStorageCapability.INSTANCE).map(IXpStorage::getLevels).orElse(0f);
            ((PlayerEntity) entityLiving).addExperienceLevel(Math.round(levels));
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        float levels = stack.getCapability(XpStorageCapability.INSTANCE).map(IXpStorage::getLevels).orElse(0f);
        tooltip.add(TextUtil.translate("item", "xp_food.levels", levels));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            items.add(createWithLevels(5));
            items.add(createWithLevels(10));
            items.add(createWithLevels(20));
            items.add(createWithLevels(30));
        }
    }

    public ItemStack createWithLevels(int levels) {
        ItemStack ret = new ItemStack(this);
        ret.getCapability(XpStorageCapability.INSTANCE).ifPresent(xp -> xp.setLevels(levels));
        return ret;
    }
}
