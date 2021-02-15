package net.silentchaos512.tokenenchanter.api.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.tokenenchanter.capability.XpStorageCapability;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.tokenenchanter.setup.ModRecipes;
import net.silentchaos512.tokenenchanter.setup.ModTags;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("WeakerAccess")
public class TokenEnchantingRecipeBuilder {
    private ResourceLocation name;
    private final Item result;
    private final int count;
    private final int levelCost;
    private Ingredient token;
    private final Map<Ingredient, Integer> ingredients = new LinkedHashMap<>();
    private final Map<Enchantment, Integer> enchantments = new LinkedHashMap<>();
    private int infuseLevels = 0;

    protected TokenEnchantingRecipeBuilder(IItemProvider result, int count, int levelCost) {
        this.result = result.asItem();
        this.count = count;
        this.levelCost = levelCost;
    }

    public static TokenEnchantingRecipeBuilder builder(IItemProvider result, int count, int levelCost) {
        return new TokenEnchantingRecipeBuilder(result, count, levelCost);
    }

    public static TokenEnchantingRecipeBuilder enchantedTokenBuilder(Enchantment enchantment, int enchantmentLevel, int count, int levelCost) {
        return builder(ModItems.ENCHANTED_TOKEN, count, levelCost)
                .enchantment(enchantment, enchantmentLevel)
                .token(ModTags.Items.TOKENS_GOLD);
    }

    public TokenEnchantingRecipeBuilder name(ResourceLocation name) {
        this.name = name;
        return this;
    }

    public TokenEnchantingRecipeBuilder enchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    public TokenEnchantingRecipeBuilder infuseLevels(int levels) {
        if (!new ItemStack(result).getCapability(XpStorageCapability.INSTANCE).isPresent()) {
            throw new IllegalStateException("Item '" + NameUtils.from(this.result) + "' has no XP storage capability");
        }
        this.infuseLevels = levels;
        return this;
    }

    public TokenEnchantingRecipeBuilder token(IItemProvider item) {
        return token(Ingredient.fromItems(item));
    }

    public TokenEnchantingRecipeBuilder token(ITag<Item> tag) {
        return token(Ingredient.fromTag(tag));
    }

    public TokenEnchantingRecipeBuilder token(Ingredient ingredient) {
        this.token = ingredient;
        return this;
    }

    public TokenEnchantingRecipeBuilder addIngredient(IItemProvider item, int count) {
        return addIngredient(Ingredient.fromItems(item), count);
    }

    public TokenEnchantingRecipeBuilder addIngredient(ITag<Item> tag, int count) {
        return addIngredient(Ingredient.fromTag(tag), count);
    }

    public TokenEnchantingRecipeBuilder addIngredient(Ingredient ingredient, int count) {
        this.ingredients.put(ingredient, count);
        return this;
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        ResourceLocation itemId = NameUtils.from(this.result);
        ResourceLocation id = name == null ? itemId : name;
        build(consumer, id);
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this));
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final TokenEnchantingRecipeBuilder builder;

        public Result(ResourceLocation id, TokenEnchantingRecipeBuilder builder) {
            this.id = id;
            this.builder = builder;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("level_cost", builder.levelCost);

            JsonObject ingredients = new JsonObject();
            ingredients.add("token", builder.token.serialize());
            JsonArray others = new JsonArray();

            // FIXME: Ingredients may be arrays...
            builder.ingredients.forEach((ing, count) -> {
                JsonObject j = ing.serialize().getAsJsonObject();
                j.addProperty("count", count);
                others.add(j);
            });
            ingredients.add("others", others);
            json.add("ingredients", ingredients);

            JsonObject result = new JsonObject();
            result.addProperty("item", NameUtils.from(builder.result).toString());
            if (builder.count > 1) {
                result.addProperty("count", builder.count);
            }
            if (!builder.enchantments.isEmpty()) {
                JsonArray array = new JsonArray();
                //noinspection OverlyLongLambda
                builder.enchantments.forEach((enchantment, level) -> {
                    JsonObject obj = new JsonObject();
                    obj.addProperty("name", NameUtils.from(enchantment).toString());
                    obj.addProperty("level", level);
                    array.add(obj);
                });
                result.add("enchantments", array);
            }
            if (builder.infuseLevels > 0) {
                result.addProperty("infuse_levels", builder.infuseLevels);
            }
            json.add("result", result);
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return ModRecipes.TOKEN_ENCHANTING.get();
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }
}
