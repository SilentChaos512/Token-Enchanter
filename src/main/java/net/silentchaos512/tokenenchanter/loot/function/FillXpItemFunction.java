package net.silentchaos512.tokenenchanter.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.silentchaos512.tokenenchanter.api.item.IXpItem;
import net.silentchaos512.tokenenchanter.setup.ModLoot;

public class FillXpItemFunction extends LootFunction {
    public static final Serializer SERIALIZER = new Serializer();

    private final IRandomRange levels;

    public FillXpItemFunction(ILootCondition[] conditionsIn, IRandomRange levels) {
        super(conditionsIn);
        this.levels = levels;
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {
        if (stack.getItem() instanceof IXpItem) {
            ItemStack ret = stack.copy();
            ((IXpItem) stack.getItem()).addLevels(ret, this.levels.generateInt(context.getRandom()));
            return ret;
        }
        return stack;
    }

    public static LootFunction.Builder<?> builder(int levels) {
        return builder(ConstantRange.of(levels));
    }

    public static LootFunction.Builder<?> builder(IRandomRange levels) {
        return builder(conditions -> new FillXpItemFunction(conditions, levels));
    }

    @Override
    public LootFunctionType getFunctionType() {
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
