package net.silentchaos512.tokenenchanter.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageCapability;
import net.silentchaos512.tokenenchanter.setup.ModLoot;

public class FillXpItemFunction extends LootItemConditionalFunction {
    public static final Serializer SERIALIZER = new Serializer();

    private final NumberProvider levels;

    public FillXpItemFunction(LootItemCondition[] conditionsIn, NumberProvider levels) {
        super(conditionsIn);
        this.levels = levels;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (stack.getCapability(XpStorageCapability.INSTANCE).isPresent()) {
            ItemStack ret = stack.copy();
            ret.getCapability(XpStorageCapability.INSTANCE).ifPresent(xp ->
                    xp.addLevels(this.levels.getInt(context)));
            return ret;
        }
        return stack;
    }

    public static LootItemConditionalFunction.Builder<?> builder(int levels) {
        return builder(ConstantValue.exactly(levels));
    }

    public static LootItemConditionalFunction.Builder<?> builder(NumberProvider levels) {
        return simpleBuilder(conditions -> new FillXpItemFunction(conditions, levels));
    }

    @Override
    public LootItemFunctionType getType() {
        return ModLoot.FILL_XP_ITEM;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<FillXpItemFunction> {
        @Override
        public void serialize(JsonObject json, FillXpItemFunction function, JsonSerializationContext context) {
            json.add("levels", context.serialize(function.levels));
        }

        @Override
        public FillXpItemFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditionsIn) {
            NumberProvider levels = context.deserialize(json.get("levels"), NumberProvider.class);
            return new FillXpItemFunction(conditionsIn, levels);
        }
    }
}
