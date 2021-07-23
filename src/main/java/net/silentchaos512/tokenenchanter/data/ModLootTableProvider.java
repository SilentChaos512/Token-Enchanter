package net.silentchaos512.tokenenchanter.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.tokenenchanter.loot.function.FillXpItemFunction;
import net.silentchaos512.tokenenchanter.setup.ModBlocks;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.tokenenchanter.setup.ModLoot;
import net.silentchaos512.tokenenchanter.setup.Registration;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(BlockLootTables::new, LootParameterSets.BLOCK),
                Pair.of(ChestLootTables::new, LootParameterSets.CHEST)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((p_218436_2_, p_218436_3_) -> LootTableManager.validate(validationtracker, p_218436_2_, p_218436_3_));
    }

    private static final class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {
        @Override
        protected void addTables() {
            dropSelf(ModBlocks.TOKEN_ENCHANTER.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }
    }

    private static final class ChestLootTables extends net.minecraft.data.loot.ChestLootTables {
        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            consumer.accept(ModLoot.Injector.Tables.CHESTS_SIMPLE_DUNGEON, addXpItems());
        }

        private static LootTable.Builder addXpItems() {
            LootTable.Builder builder = LootTable.lootTable();
            builder.withPool(LootPool.lootPool()
                    .setRolls(ConstantRange.exactly(1))
                    .add(EmptyLootEntry.emptyItem()
                            .setWeight(5)
                    )
                    .add(ItemLootEntry.lootTableItem(ModItems.XP_BREAD)
                            .setWeight(15)
                            .apply(SetCount.setCount(RandomValueRange.between(1, 3)))
                            .apply(FillXpItemFunction.builder(RandomValueRange.between(2, 4)))
                    )
                    .add(ItemLootEntry.lootTableItem(ModItems.SMALL_XP_CRYSTAL)
                            .setWeight(5)
                            .apply(FillXpItemFunction.builder(RandomValueRange.between(1, 9)))
                    )
            );
            return builder;
        }
    }
}
