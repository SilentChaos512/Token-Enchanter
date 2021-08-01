package net.silentchaos512.tokenenchanter.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.tokenenchanter.api.xp.IXpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageCapability;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageItemImpl;
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
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
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
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 16;
    }

    private static int getPlayerLevel(LivingEntity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            return player.getAbilities().instabuild ? Integer.MAX_VALUE : player.experienceLevel;
        }
        return 0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        IXpStorage xp = stack.getCapability(XpStorageCapability.INSTANCE).orElse(XpStorage.INVALID);
        int fillAmount = getFillAmount(stack);

        if (xp.getLevels() < xp.getCapacity()) {
            if (getPlayerLevel(playerIn) >= fillAmount) {
                playerIn.startUsingItem(handIn);
                return InteractionResultHolder.consume(stack);
            } else {
                Component msg = TextUtil.translate("item", "xp_crystal.not_enough_levels", fillAmount);
                playerIn.displayClientMessage(msg, true);
                playerIn.getCooldowns().addCooldown(this, 10);
                return InteractionResultHolder.fail(stack);
            }
        }

        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        int playerLevels = getPlayerLevel(entityLiving);
        int fillAmount = getFillAmount(stack);

        if (playerLevels >= fillAmount && entityLiving instanceof Player) {
            Player player = (Player) entityLiving;
            player.giveExperienceLevels(-fillAmount);

            ItemStack ret = stack.copy();
            ret.getCapability(XpStorageCapability.INSTANCE).ifPresent(xp -> xp.addLevels(fillAmount));
            return ret;
        }

        return super.finishUsingItem(stack, worldIn, entityLiving);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        IXpStorage xp = stack.getCapability(XpStorageCapability.INSTANCE).orElse(XpStorage.INVALID);
        float levels = xp.getLevels();
        if (levels <= 0.1f) {
            tooltip.add(TextUtil.translate("item", "xp_crystal.hint").plainCopy().withStyle(ChatFormatting.ITALIC));
        }

        String levelsFormatted = String.format("%.1f", levels);
        String max = String.valueOf(xp.getCapacity());
        tooltip.add(TextUtil.translate("item", "xp_crystal.levels", levelsFormatted, max));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            ItemStack empty = new ItemStack(this);
            items.add(empty);

            ItemStack full = empty.copy();
            full.getCapability(XpStorageCapability.INSTANCE).ifPresent(xp -> xp.addLevels(xp.getCapacity()));
            items.add(full);
        }
    }
}
