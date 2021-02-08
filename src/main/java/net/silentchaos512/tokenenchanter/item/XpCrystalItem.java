package net.silentchaos512.tokenenchanter.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.silentchaos512.tokenenchanter.api.item.IXpCrystalItem;
import net.silentchaos512.tokenenchanter.util.TextUtil;

import javax.annotation.Nullable;
import java.util.List;

public class XpCrystalItem extends Item implements IXpCrystalItem {
    private static final String NBT_LEVELS = "StoredLevels";

    private final int maxLevels;

    public XpCrystalItem(int maxLevels, Properties properties) {
        super(properties);
        this.maxLevels = maxLevels;
    }

    @Override
    public float getLevels(ItemStack stack) {
        return stack.getOrCreateTag().getFloat(NBT_LEVELS);
    }

    @Override
    public float getMaxLevels(ItemStack stack) {
        return maxLevels;
    }

    @Override
    public ItemStack drainLevels(ItemStack stack, float levelsToDrain) {
        float newLevels = MathHelper.clamp(getLevels(stack) - levelsToDrain, 0, getMaxLevels(stack));
        ItemStack ret = stack.copy();
        ret.getOrCreateTag().putFloat(NBT_LEVELS, newLevels);
        return ret;
    }

    public int getFillAmount(ItemStack stack) {
        return (int) getMaxLevels(stack) / 5;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        float levels = getLevels(stack);
        float max = getMaxLevels(stack);
        return (max - levels) / max;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 16;
    }

    protected static int getPlayerLevel(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            return player.abilities.isCreativeMode ? Integer.MAX_VALUE : player.experienceLevel;
        }
        return 0;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (getLevels(stack) < getMaxLevels(stack)) {
            int fillAmount = getFillAmount(stack);

            if (getPlayerLevel(playerIn) >= fillAmount) {
                playerIn.setActiveHand(handIn);
                return ActionResult.resultConsume(stack);
            } else {
                ITextComponent msg = TextUtil.translate("item", "xp_crystal.not_enough_levels", fillAmount);
                playerIn.sendStatusMessage(msg, true);
                playerIn.getCooldownTracker().setCooldown(this, 10);
                return ActionResult.resultFail(stack);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        int playerLevels = getPlayerLevel(entityLiving);
        int fillAmount = getFillAmount(stack);

        if (playerLevels >= fillAmount && entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            player.addExperienceLevel(-fillAmount);
            return addLevels(stack, fillAmount);
        }

        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        float levels = getLevels(stack);
        if (levels <= 0.1f) {
            tooltip.add(TextUtil.translate("item", "xp_crystal.hint").copyRaw().mergeStyle(TextFormatting.ITALIC));
        }

        String levelsFormatted = String.format("%.1f", levels);
        String max = String.valueOf((int) getMaxLevels(stack));
        tooltip.add(TextUtil.translate("item", "xp_crystal.levels", levelsFormatted, max));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            ItemStack empty = new ItemStack(this);
            items.add(empty);
            ItemStack full = addLevels(empty, getMaxLevels(empty));
            items.add(full);
        }
    }
}
