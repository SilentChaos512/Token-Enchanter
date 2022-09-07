package net.silentchaos512.tokenenchanter.api.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageCapability;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.tokenenchanter.setup.ModRecipes;
import net.silentchaos512.tokenenchanter.setup.ModTags;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
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

    protected TokenEnchantingRecipeBuilder(ItemLike result, int count, int levelCost) {
        this.result = result.asItem();
        this.count = count;
        this.levelCost = levelCost;
    }

    public static TokenEnchantingRecipeBuilder builder(ItemLike result, int count, int levelCost) {
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
            throw new IllegalStateException("Item '" + NameUtils.fromItem(this.result) + "' has no XP storage capability");
        }
        this.infuseLevels = levels;
        return this;
    }

    public TokenEnchantingRecipeBuilder token(ItemLike item) {
        return token(Ingredient.of(item));
    }

    public TokenEnchantingRecipeBuilder token(TagKey<Item> tag) {
        return token(Ingredient.of(tag));
    }

    public TokenEnchantingRecipeBuilder token(Ingredient ingredient) {
        this.token = ingredient;
        return this;
    }

    public TokenEnchantingRecipeBuilder addIngredient(ItemLike item, int count) {
        return addIngredient(Ingredient.of(item), count);
    }

    public TokenEnchantingRecipeBuilder addIngredient(TagKey<Item> tag, int count) {
        return addIngredient(Ingredient.of(tag), count);
    }

    public TokenEnchantingRecipeBuilder addIngredient(Ingredient ingredient, int count) {
        this.ingredients.put(ingredient, count);
        return this;
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        ResourceLocation itemId = NameUtils.fromItem(this.result);
        ResourceLocation id = name == null ? itemId : name;
        build(consumer, id);
    }

    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final TokenEnchantingRecipeBuilder builder;

        public Result(ResourceLocation id, TokenEnchantingRecipeBuilder builder) {
            this.id = id;
            this.builder = builder;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("level_cost", builder.levelCost);

            JsonObject ingredients = new JsonObject();
            ingredients.add("token", builder.token.toJson());
            JsonArray others = new JsonArray();

            // FIXME: Ingredients may be arrays...
            builder.ingredients.forEach((ing, count) -> {
                JsonObject j = ing.toJson().getAsJsonObject();
                j.addProperty("count", count);
                others.add(j);
            });
            ingredients.add("others", others);
            json.add("ingredients", ingredients);

            JsonObject result = new JsonObject();
            result.addProperty("item", NameUtils.fromItem(builder.result).toString());
            if (builder.count > 1) {
                result.addProperty("count", builder.count);
            }
            if (!builder.enchantments.isEmpty()) {
                JsonArray array = new JsonArray();
                //noinspection OverlyLongLambda
                builder.enchantments.forEach((enchantment, level) -> {
                    JsonObject obj = new JsonObject();
                    obj.addProperty("name", getEnchantmentId(enchantment).toString());
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

        private static ResourceLocation getEnchantmentId(Enchantment enchantment) {
            return Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(enchantment));
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.TOKEN_ENCHANTING.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
