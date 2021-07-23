package net.silentchaos512.tokenenchanter.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageCapability;
import net.silentchaos512.tokenenchanter.setup.ModLoot;

public class FillXpItemFunction extends LootFunction {
    public static final Serializer SERIALIZER = new Serializer();

    private final IRandomRange levels;

    public FillXpItemFunction(ILootCondition[] conditionsIn, IRandomRange levels) {
        super(conditionsIn);
        this.levels = levels;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (stack.getCapability(XpStorageCapability.INSTANCE).isPresent()) {
            ItemStack ret = stack.copy();
            ret.getCapability(XpStorageCapability.INSTANCE).ifPresent(xp ->
                    xp.addLevels(this.levels.getInt(context.getRandom())));
            return ret;
        }
        return stack;
    }

    public static LootFunction.Builder<?> builder(int levels) {
        return builder(ConstantRange.exactly(levels));
    }

    public static LootFunction.Builder<?> builder(IRandomRange levels) {
        return simpleBuilder(conditions -> new FillXpItemFunction(conditions, levels));
    }

    @Override
    public LootFunctionType getType() {
        return ModLoot.FILL_XP_ITEM;
    }

    public static class Serializer extends LootFunction.Serializer<FillXpItemFunction> {
        @Override
        public void serialize(JsonObject json, FillXpItemFunction function, JsonSerializationContext context) {
            json.add("levels", RandomRanges.serialize(function.levels, context));
        }

        @Override
        public FillXpItemFunction deserialize(JsonObject json, JsonDeserializationContext context, ILootCondition[] conditionsIn) {
            IRandomRange levels = RandomRanges.deserialize(json.get("levels"), context);
            return new FillXpItemFunction(conditionsIn, levels);
        }
    }
}
