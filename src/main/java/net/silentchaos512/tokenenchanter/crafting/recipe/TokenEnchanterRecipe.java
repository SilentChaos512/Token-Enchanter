package net.silentchaos512.tokenenchanter.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.lib.collection.StackList;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.api.item.IXpCrystalItem;
import net.silentchaos512.tokenenchanter.api.item.IXpItem;
import net.silentchaos512.tokenenchanter.setup.ModRecipes;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class TokenEnchanterRecipe implements IRecipe<IInventory> {
    private final ResourceLocation id;
    private int levelCost;
    private ItemStack result;
    private Ingredient token;
    private final Map<Ingredient, Integer> ingredientMap = new LinkedHashMap<>();
    private boolean valid = true;

    public TokenEnchanterRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public int getLevelCost() {
        return levelCost;
    }

    public ItemStack getResult() {
        return result;
    }

    public Ingredient getToken() {
        return token;
    }

    public Map<Ingredient, Integer> getIngredientMap() {
        return Collections.unmodifiableMap(ingredientMap);
    }

    public void consumeIngredients(IInventory inv) {
        ItemStack stack = inv.getStackInSlot(0);
        if (stack.getItem() instanceof IXpCrystalItem) {
            IXpItem crystalItem = (IXpCrystalItem) stack.getItem();
            crystalItem.drainLevels(stack, this.levelCost);
        }

        consumeItems(inv, 1, 2, token, 1);
        ingredientMap.forEach(((ingredient, count) -> {
            consumeItems(inv, 2, inv.getSizeInventory() - 1, ingredient, count);
        }));
    }

    private static void consumeItems(IInventory inventory, int startIndex, int endIndex, Predicate<ItemStack> ingredient, int amount) {
        int amountLeft = amount;
        for (int i = startIndex; i < endIndex; ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                int toRemove = Math.min(amountLeft, stack.getCount());

                stack.shrink(toRemove);
                if (stack.isEmpty()) {
                    inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }

                amountLeft -= toRemove;
                if (amountLeft == 0) {
                    return;
                }
            }
        }
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        if (!valid) return false;

        // XP crystal with enough levels?
        if (!matchesXpCrystal(inv.getStackInSlot(0))) {
            return false;
        }

        // FIXME: Cleanup + Probably shouldn't use StackList in matches (speed issue)
        StackList list = StackList.from(inv);

        // Token?
        if (list.firstMatch(s -> token.test(s)).isEmpty()) return false;

        // Others?
        for (Map.Entry<Ingredient, Integer> entry : ingredientMap.entrySet()) {
            Ingredient ingredient = entry.getKey();
            int count = entry.getValue();

            int countInInv = 0;
            for (ItemStack stack : list) {
                if (ingredient.test(stack)) {
                    countInInv += stack.getCount();
                }
            }

            if (countInInv < count) {
                return false;
            }
        }

        return true;
    }

    private boolean matchesXpCrystal(ItemStack stack) {
        // Return true if item is an XP crystal with sufficient levels
        if (stack.getItem() instanceof IXpCrystalItem) {
            IXpItem crystalItem = (IXpCrystalItem) stack.getItem();
            float storedLevels = crystalItem.getLevels(stack);
            return storedLevels >= this.levelCost;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result.copy();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.TOKEN_ENCHANTING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.TOKEN_ENCHANTING_TYPE;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TokenEnchanterRecipe> {
        @Override
        public TokenEnchanterRecipe read(ResourceLocation recipeId, JsonObject json) {
            TokenEnchanterRecipe recipe = new TokenEnchanterRecipe(recipeId);

            // Chaos and processing time
            recipe.levelCost = JSONUtils.getInt(json, "level_cost", 1);

            // Ingredients
            JsonObject ingredientsJson = json.get("ingredients").getAsJsonObject();
            recipe.token = Ingredient.deserialize(ingredientsJson.get("token").getAsJsonObject());
            JsonArray othersArray = ingredientsJson.get("others").getAsJsonArray();
            for (JsonElement elem : othersArray) {
                Ingredient ingredient = Ingredient.deserialize(elem);
                int count = JSONUtils.getInt(elem.getAsJsonObject(), "count", 1);
                recipe.ingredientMap.put(ingredient, count);
            }

            // Result
            JsonObject resultJson = json.get("result").getAsJsonObject();
            recipe.result = deserializeItem(resultJson);

            // Enchantments (for tokens, and maybe other things)
            JsonElement elem = resultJson.get("enchantments");
            if (elem != null) {
                for (JsonElement elem1 : elem.getAsJsonArray()) {
                    JsonObject elemObj = elem1.getAsJsonObject();
                    String name = JSONUtils.getString(elemObj, "name");
                    Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(name));
                    if (enchantment == null) {
                        // Enchantment does not exist!
                        recipe.valid = false;
                    } else {
                        int level = JSONUtils.getInt(elemObj, "level", 1);
                        addEnchantment(recipe.result, enchantment, level);
                    }
                }
            }

            if (recipe.result.getItem() instanceof IXpItem) {
                // Infuse XP levels into certain items (XP bread, crystals, etc.)
                int amount = JSONUtils.getInt(resultJson, "infuse_levels", 0);
                if (amount > 0) {
                    IXpItem item = (IXpItem) recipe.result.getItem();
                    item.addLevels(recipe.result, amount);
                }
            }

            if (!recipe.valid) {
                logInvalidRecipe(recipe);
            }

            return recipe;
        }

        @Override
        public TokenEnchanterRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            TokenEnchanterRecipe recipe = new TokenEnchanterRecipe(recipeId);
            recipe.levelCost = buffer.readVarInt();
            recipe.result = buffer.readItemStack();
            recipe.token = Ingredient.read(buffer);
            int otherCount = buffer.readVarInt();
            for (int i = 0; i < otherCount; ++i) {
                Ingredient ingredient = Ingredient.read(buffer);
                int count = buffer.readVarInt();
                recipe.ingredientMap.put(ingredient, count);
            }
            return recipe;
        }

        @Override
        public void write(PacketBuffer buffer, TokenEnchanterRecipe recipe) {
            buffer.writeVarInt(recipe.levelCost);
            buffer.writeItemStack(recipe.result);
            recipe.token.write(buffer);
            buffer.writeVarInt(recipe.ingredientMap.size());
            recipe.ingredientMap.forEach((ingredient, count) -> {
                ingredient.write(buffer);
                buffer.writeVarInt(count);
            });
        }

        private static ItemStack deserializeItem(JsonObject json) {
            return ShapedRecipe.deserializeItem(json);
        }

        private static void addEnchantment(ItemStack stack, Enchantment enchantment, int level) {
            stack.addEnchantment(enchantment, level);
        }

        @SuppressWarnings("TypeMayBeWeakened")
        private static void logInvalidRecipe(TokenEnchanterRecipe recipe) {
            String msg = "Token enchanter recipe '{}' is invalid, enchantment does not exist";
            // Don't log built-in recipes for users (because they complain)
            if (TokenMod.MOD_ID.equals(recipe.getId().getNamespace())) {
                TokenMod.LOGGER.debug(msg, recipe.getId());
            } else {
                TokenMod.LOGGER.warn(msg, recipe.getId());
            }
        }
    }
}
