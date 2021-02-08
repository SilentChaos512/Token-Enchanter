package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.item.Item;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.lib.registry.ItemRegistryObject;
import net.silentchaos512.tokenenchanter.item.EnchantedTokenItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public final class ModItems {
    private static final Collection<ItemRegistryObject<Item>> SIMPLE_MODEL_ITEMS = new ArrayList<>();

    public static final ItemRegistryObject<Item> GOLD_TOKEN = registerCraftingItem("gold_token");

    public static final ItemRegistryObject<Item> ENCHANTED_TOKEN = register("enchanted_token", () ->
            new EnchantedTokenItem(baseProps()));

    private ModItems() {}

    static void register() {}

    public static Collection<ItemRegistryObject<Item>> getSimpleModelItems() {
        return Collections.unmodifiableCollection(SIMPLE_MODEL_ITEMS);
    }

    private static <T extends Item> ItemRegistryObject<T> register(String name, Supplier<T> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }

    private static ItemRegistryObject<Item> registerCraftingItem(String name) {
        // Registers a generic, basic item with no special properties. Useful for items that are
        // used primarily for crafting recipes.
        ItemRegistryObject<Item> ret = register(name, () -> new Item(baseProps()));
        SIMPLE_MODEL_ITEMS.add(ret);
        return ret;
    }

    private static Item.Properties baseProps() {
        return new Item.Properties().group(TokenMod.ITEM_GROUP);
    }
}
