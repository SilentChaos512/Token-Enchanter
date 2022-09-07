package net.silentchaos512.tokenenchanter.item;

import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.lib.event.ClientTicks;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.tokenenchanter.TokenMod;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.*;

public class EnchantedTokenItem extends Item {
    public enum Icon {
        ANY,
        TOOL,
        SWORD,
        FISHING_ROD,
        TRIDENT,
        BOW,
        CROSSBOW,
        ARMOR,
        CURSE,
        UNKNOWN;

        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public static final ResourceLocation MODEL_INDEX = TokenMod.getId("model_index");

    private static final Map<Enchantment, Integer> OUTLINE_COLOR_MAP = new HashMap<>();
    private static final Map<String, Icon> MODELS_BY_TYPE = new HashMap<>();

    static {
        MODELS_BY_TYPE.put(EnchantmentCategory.VANISHABLE.toString(), Icon.ANY);
        MODELS_BY_TYPE.put(EnchantmentCategory.BREAKABLE.toString(), Icon.ANY);
        MODELS_BY_TYPE.put(EnchantmentCategory.ARMOR.toString(), Icon.ARMOR);
        MODELS_BY_TYPE.put(EnchantmentCategory.ARMOR_CHEST.toString(), Icon.ARMOR);
        MODELS_BY_TYPE.put(EnchantmentCategory.ARMOR_FEET.toString(), Icon.ARMOR);
        MODELS_BY_TYPE.put(EnchantmentCategory.ARMOR_HEAD.toString(), Icon.ARMOR);
        MODELS_BY_TYPE.put(EnchantmentCategory.ARMOR_LEGS.toString(), Icon.ARMOR);
        MODELS_BY_TYPE.put(EnchantmentCategory.WEARABLE.toString(), Icon.ARMOR);
        MODELS_BY_TYPE.put(EnchantmentCategory.BOW.toString(), Icon.BOW);
        MODELS_BY_TYPE.put(EnchantmentCategory.CROSSBOW.toString(), Icon.CROSSBOW);
        MODELS_BY_TYPE.put(EnchantmentCategory.DIGGER.toString(), Icon.TOOL);
        MODELS_BY_TYPE.put(EnchantmentCategory.FISHING_ROD.toString(), Icon.FISHING_ROD);
        MODELS_BY_TYPE.put(EnchantmentCategory.TRIDENT.toString(), Icon.TRIDENT);
        MODELS_BY_TYPE.put(EnchantmentCategory.WEAPON.toString(), Icon.SWORD);

        if (TokenMod.isDevBuild()) {
            TokenMod.LOGGER.info("Checking enchantment type icons...");
            boolean allGood = true;

            for (EnchantmentCategory type : EnchantmentCategory.values()) {
                if (!MODELS_BY_TYPE.containsKey(type.toString())) {
                    TokenMod.LOGGER.fatal("Missing icon for type: {}", type);
                    allGood = false;
                }
            }

            if (allGood) {
                TokenMod.LOGGER.info("All good!");
            }
        }
    }

    public EnchantedTokenItem(Properties properties) {
        super(properties);
    }

    //region ItemStack construction

    public ItemStack construct(Enchantment enchantment, int level) {
        return construct(new EnchantmentInstance(enchantment, level));
    }

    public ItemStack construct(EnchantmentInstance... enchantments) {
        ItemStack stack = new ItemStack(this);
        for (EnchantmentInstance data : enchantments) {
            addEnchantment(stack, data);
        }
        return stack;
    }

    public static void addEnchantment(ItemStack stack, Enchantment enchantment, int level) {
        addEnchantment(stack, new EnchantmentInstance(enchantment, level));
    }

    private static void addEnchantment(ItemStack stack, EnchantmentInstance data) {
        stack.enchant(data.enchantment, data.level);
    }

    //endregion

    //region Crafting

    public static ItemStack applyTokenToItem(ItemStack token, ItemStack stack) {
        if (stack.isEmpty()) {
            return stack;
        }

        if (stack.getItem() == Items.BOOK || stack.getItem() == Items.ENCHANTED_BOOK) {
            return applyTokenToBook(token, stack);
        }
        return applyTokenToTool(token, stack);
    }

    public static ItemStack applyTokenToBook(ItemStack token, ItemStack book) {
        ItemStack ret = book.getItem() == Items.BOOK ? new ItemStack(Items.ENCHANTED_BOOK) : book.copy();

        // Enchantments on token
        Map<Enchantment, Integer> enchantmentsOnToken = EnchantmentHelper.getEnchantments(token);
        if (enchantmentsOnToken.isEmpty()) {
            return ItemStack.EMPTY;
        }

        // Enchantments on book
        Map<Enchantment, Integer> enchantmentsOnBook = EnchantmentHelper.getEnchantments(ret);

        // Appy enchantments to new copy of book
        if (!mergeEnchantmentLists(enchantmentsOnToken, enchantmentsOnBook)) {
            return ItemStack.EMPTY;
        }
        EnchantmentHelper.setEnchantments(enchantmentsOnToken, ret);
        return ret;
    }

    public static ItemStack applyTokenToTool(ItemStack token, ItemStack tool) {
        if (token.isEmpty() || tool.isEmpty()) {
            return ItemStack.EMPTY;
        }

        // Enchantments on token
        Map<Enchantment, Integer> enchantmentsOnToken = EnchantmentHelper.getEnchantments(token);
        if (enchantmentsOnToken.isEmpty()) {
            return ItemStack.EMPTY;
        }

        // Enchantments on tool
        Map<Enchantment, Integer> enchantmentsOnTool = EnchantmentHelper.getEnchantments(tool);

        // Make sure all enchantments can apply to the tool
        for (Map.Entry<Enchantment, Integer> entry : enchantmentsOnToken.entrySet()) {
            Enchantment ench = entry.getKey();
            // Valid for tool?
            if (!ench.canEnchant(tool)) {
                return ItemStack.EMPTY;
            }

            // Does new enchantment conflict with any existing ones?
            for (Enchantment enchTool : enchantmentsOnTool.keySet()) {
                if (!ench.equals(enchTool) && !ench.isCompatibleWith(enchTool)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        // Appy enchantments to new copy of tool
        if (!mergeEnchantmentLists(enchantmentsOnToken, enchantmentsOnTool)) {
            return ItemStack.EMPTY;
        }
        ItemStack ret = tool.copy();
        EnchantmentHelper.setEnchantments(enchantmentsOnToken, ret);
        return ret;
    }

    private static boolean mergeEnchantmentLists(Map<Enchantment, Integer> first, Map<Enchantment, Integer> second) {
        // Add enchantments from second list to first...
        for (Enchantment enchantment : second.keySet()) {
            int newLevel;
            int level = newLevel = second.get(enchantment);
            // If first list contains the enchantment, try increasing the level.
            if (first.containsKey(enchantment)) {
                newLevel = first.get(enchantment) + level;
                // Level too high?
                if (newLevel > enchantment.getMaxLevel()) {
                    return false;
                }
            }
            first.put(enchantment, newLevel);
        }

        return true;
    }

    //endregion

    //region Item overrides


    @Override
    public Component getName(ItemStack stack) {
        Enchantment enchantment = getSingleEnchantment(stack);
        if (enchantment != null) {
            MutableComponent enchantmentName = enchantment.getFullname(1).plainCopy()
                    .withStyle(this.getRarity(stack).getStyleModifier());
            return Component.translatable(this.getDescriptionId(stack) + ".single", enchantmentName);
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);

        if (enchantments.size() == 1) {
            Enchantment enchantment = enchantments.keySet().iterator().next();
            list.add(subText("maxLevel", enchantment.getMaxLevel()));
            list.add(subText("mod", getModName(enchantment)));

            // Debug info
            if (flag.isAdvanced()) {
                ResourceLocation registryName = NameUtils.fromEnchantment(enchantment);
                list.add(Component.literal(registryName.toString())
                        .withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.UNCOMMON;
    }

    @SuppressWarnings("TypeMayBeWeakened")
    private static String getModName(Enchantment enchantment) {
        return ModList.get().getModContainerById(NameUtils.fromEnchantment(enchantment).getNamespace())
                .map(c -> c.getModInfo().getDisplayName())
                .orElse("Unknown Mod");
    }

    private Component subText(String key, Object... formatArgs) {
        ResourceLocation id = NameUtils.fromItem(this);
        String fullKey = String.format("item.%s.%s.%s", id.getNamespace(), id.getPath(), key);
        return Component.translatable(fullKey, formatArgs);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (!allowedIn(group)) return;

        List<ItemStack> tokens = NonNullList.create();
        for (Enchantment enchantment : ForgeRegistries.ENCHANTMENTS) {
            tokens.add(construct(enchantment, 1));
        }

        // Sort by type, then enchantment name
        tokens.sort(EnchantedTokenItem::compareEnchantmentNames);
        items.addAll(tokens);
    }

    private static int compareEnchantmentNames(ItemStack o1, ItemStack o2) {
        // First compare icon names (group together enchantments of one type)
        int k = Integer.compare(getModelIcon(o1).ordinal(), getModelIcon(o2).ordinal());
        if (k == 0) {
            Enchantment e1 = getSingleEnchantment(o1);
            Enchantment e2 = getSingleEnchantment(o2);
            if (e1 != null && e2 != null) {
                // If this crashes the enchantment is at fault, nothing should be done about it.
                Component name1 = getEnchantmentDisplayName(e1);
                Component name2 = getEnchantmentDisplayName(e2);
                return name1.getString().compareTo(name2.getString());
            }
        }
        return k;
    }

    private static Component getEnchantmentDisplayName(Enchantment enchantment) {
        // Report on broken mods
        try {
            return enchantment.getFullname(1);
        } catch (Throwable ex) {
            CrashReport report = CrashReport.forThrowable(ex, "Enchantment threw an exception when getting display name. This is not Token Enchanter's fault!");
            CrashReportCategory cat = report.addCategory("Enchantment");
            cat.setDetail("ID", NameUtils.fromEnchantment(enchantment));
            cat.setDetail("Mod Name", getModName(enchantment));
            throw new ReportedException(report);
        }
    }

    @Nullable
    public static Enchantment getSingleEnchantment(ItemStack token) {
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(token);
        if (map.size() != 1) return null;
        return map.keySet().iterator().next();
    }

    //endregion

    //region Rendering

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    private static final float OUTLINE_PULSATE_SPEED = 1f / (3f * (float) Math.PI);

    public static int getItemColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 1) return 0xFFFFFF;

        int baseColor = getOutlineColor(stack);

        int j = (int) (160 * Mth.sin(ClientTicks.ticksInGame() * OUTLINE_PULSATE_SPEED));
        j = Mth.clamp(j, 0, 255);
        int r = (baseColor >> 16) & 255;
        r = Mth.clamp(r + j, 0, 255);
        int g = (baseColor >> 8) & 255;
        g = Mth.clamp(g + j, 0, 255);
        int b = baseColor & 255;
        b = Mth.clamp(b + j, 0, 255);
        return (r << 16) | (g << 8) | b;
    }

    private static int getOutlineColor(ItemStack stack) {
        Enchantment enchantment = getSingleEnchantment(stack);
        if (enchantment != null) {
            return OUTLINE_COLOR_MAP.computeIfAbsent(enchantment, e -> {
                int hash = NameUtils.fromEnchantment(e).hashCode();
                float hue = ((hash + 32 * OUTLINE_COLOR_MAP.size()) % 1024) / 1024f;
                return Color.getHSBColor(hue, 1f, 1f).getRGB();
            });
        }
        return 0x8040CC;
    }

    public static float getModel(ItemStack stack, ClientLevel world, LivingEntity entity, int var4) {
        return getModelIcon(stack).ordinal();
    }

    private static Icon getModelIcon(ItemStack stack) {
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        if (map.isEmpty()) return Icon.UNKNOWN;

        Enchantment enchantment = map.keySet().iterator().next();
        if (enchantment.isCurse()) return Icon.CURSE;

        EnchantmentCategory type = enchantment.category;
        if (type == null) return Icon.UNKNOWN;

        return MODELS_BY_TYPE.getOrDefault(type.toString(), Icon.UNKNOWN);
    }

    //endregion
}
