package net.silentchaos512.tokenenchanter.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.tokenenchanter.api.xp.IXpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageItemImpl;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageCapability;
import net.silentchaos512.tokenenchanter.util.TextUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class XpCrystalItem extends Item {
    private final int maxLevels;

    public XpCrystalItem(int maxLevels, Properties properties) {
        super(properties);
        this.maxLevels = maxLevels;
    }

    private static int getFillAmount(ItemStack stack) {
        IXpStorage xp = stack.getCapability(XpStorageCapability.INSTANCE).orElse(XpStorage.INVALID);
        int normalAmount = xp.getCapacity() / 5;
        int freeSpace = (int) (xp.getCapacity() - xp.getLevels());
        return Math.min(freeSpace, normalAmount);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        IXpStorage xp = stack.getCapability(XpStorageCapability.INSTANCE).orElse(XpStorage.INVALID);
        float levels = xp.getLevels();
        float max = xp.getCapacity();
        if (max == 0f) {
            return 1.0;
        }
        return (max - levels) / max;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ICapabilityProvider() {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                if (cap == XpStorageCapability.INSTANCE) {
                    return LazyOptional.of(() -> new XpStorageItemImpl(stack, XpCrystalItem.this.maxLevels, true)).cast();
                }
                return LazyOptional.empty();
            }
        };
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

    private static int getPlayerLevel(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            return player.abilities.isCreativeMode ? Integer.MAX_VALUE : player.experienceLevel;
        }
        return 0;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        IXpStorage xp = stack.getCapability(XpStorageCapability.INSTANCE).orElse(XpStorage.INVALID);
        int fillAmount = getFillAmount(stack);

        if (xp.getLevels() < xp.getCapacity()) {
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

            ItemStack ret = stack.copy();
            ret.getCapability(XpStorageCapability.INSTANCE).ifPresent(xp -> xp.addLevels(fillAmount));
            return ret;
        }

        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        IXpStorage xp = stack.getCapability(XpStorageCapability.INSTANCE).orElse(XpStorage.INVALID);
        float levels = xp.getLevels();
        if (levels <= 0.1f) {
            tooltip.add(TextUtil.translate("item", "xp_crystal.hint").copyRaw().mergeStyle(TextFormatting.ITALIC));
        }

        String levelsFormatted = String.format("%.1f", levels);
        String max = String.valueOf(xp.getCapacity());
        tooltip.add(TextUtil.translate("item", "xp_crystal.levels", levelsFormatted, max));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            ItemStack empty = new ItemStack(this);
            items.add(empty);

            ItemStack full = empty.copy();
            full.getCapability(XpStorageCapability.INSTANCE).ifPresent(xp -> xp.addLevels(xp.getCapacity()));
            items.add(full);
        }
    }
}
