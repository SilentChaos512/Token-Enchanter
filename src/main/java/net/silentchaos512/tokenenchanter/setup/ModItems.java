package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.lib.registry.ItemRegistryObject;
import net.silentchaos512.tokenenchanter.item.EnchantedTokenItem;
import net.silentchaos512.tokenenchanter.item.XpCrystalItem;
import net.silentchaos512.tokenenchanter.item.XpFoodItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public final class ModItems {
    private static final Collection<ItemRegistryObject<? extends Item>> SIMPLE_MODEL_ITEMS = new ArrayList<>();

    public static final ItemRegistryObject<Item> GOLD_TOKEN = registerCraftingItem("gold_token");

    public static final ItemRegistryObject<XpCrystalItem> SMALL_XP_CRYSTAL = registerSimpleModel("small_xp_crystal", () ->
            new XpCrystalItem(10, unstackableProps()));
    public static final ItemRegistryObject<XpCrystalItem> XP_CRYSTAL = registerSimpleModel("xp_crystal", () ->
            new XpCrystalItem(30, unstackableProps()));
    public static final ItemRegistryObject<XpCrystalItem> LARGE_XP_CRYSTAL = registerSimpleModel("large_xp_crystal", () ->
            new XpCrystalItem(100, unstackableProps()));

    public static final ItemRegistryObject<XpFoodItem> XP_BREAD = registerSimpleModel("xp_bread", () ->
            new XpFoodItem(baseProps()
                    .food(new Food.Builder().hunger(6).saturation(1.2f).setAlwaysEdible().build())));

    public static final ItemRegistryObject<EnchantedTokenItem> ENCHANTED_TOKEN = register("enchanted_token", () ->
            new EnchantedTokenItem(baseProps()));

    private ModItems() {}

    static void register() {}

    public static Collection<ItemRegistryObject<? extends Item>> getSimpleModelItems() {
        return Collections.unmodifiableCollection(SIMPLE_MODEL_ITEMS);
    }

    private static <T extends Item> ItemRegistryObject<T> register(String name, Supplier<T> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }

    private static <T extends Item> ItemRegistryObject<T> registerSimpleModel(String name, Supplier<T> item) {
        ItemRegistryObject<T> ret = register(name, item);
        SIMPLE_MODEL_ITEMS.add(ret);
        return ret;
    }

    private static ItemRegistryObject<Item> registerCraftingItem(String name) {
        // Registers a generic, basic item with no special properties. Useful for items that are
        // used primarily for crafting recipes.
        return registerSimpleModel(name, () -> new Item(baseProps()));
    }

    private static Item.Properties baseProps() {
        return new Item.Properties().group(TokenMod.ITEM_GROUP);
    }

    private static Item.Properties unstackableProps() {
        return baseProps().maxStackSize(1);
    }
}
