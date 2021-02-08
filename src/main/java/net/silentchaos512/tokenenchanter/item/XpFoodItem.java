package net.silentchaos512.tokenenchanter.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.silentchaos512.tokenenchanter.util.TextUtil;

import javax.annotation.Nullable;
import java.util.List;

public class XpFoodItem extends Item {
    private static final String NBT_LEVELS = "StoredLevels";

    public XpFoodItem(Properties properties) {
        super(properties);

        //noinspection OverridableMethodCallDuringObjectConstruction
        if (!isFood()) {
            throw new IllegalArgumentException("XpFoodItem must be a food");
        }
    }

    public ItemStack makeWithLevels(int levels) {
        ItemStack ret = new ItemStack(this);
        ret.getOrCreateTag().putInt(NBT_LEVELS, levels);
        return ret;
    }

    public int getLevels(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBT_LEVELS);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            ((PlayerEntity) entityLiving).addExperienceLevel(getLevels(stack));
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextUtil.translate("item", "xp_food.levels", getLevels(stack)));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            items.add(makeWithLevels(0));
            items.add(makeWithLevels(5));
            items.add(makeWithLevels(10));
            items.add(makeWithLevels(20));
            items.add(makeWithLevels(30));
        }
    }
}
