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
        map.forEach((p_218436_2_, p_218436_3_) -> LootTableManager.validateLootTable(validationtracker, p_218436_2_, p_218436_3_));
    }

    private static final class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {
        @Override
        protected void addTables() {
            registerDropSelfLootTable(ModBlocks.TOKEN_ENCHANTER.get());
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
            LootTable.Builder builder = LootTable.builder();
            builder.addLootPool(LootPool.builder()
                    .rolls(ConstantRange.of(1))
                    .addEntry(EmptyLootEntry.func_216167_a()
                            .weight(5)
                    )
                    .addEntry(ItemLootEntry.builder(ModItems.XP_BREAD)
                            .weight(15)
                            .acceptFunction(SetCount.builder(RandomValueRange.of(1, 3)))
                            .acceptFunction(FillXpItemFunction.builder(RandomValueRange.of(2, 4)))
                    )
                    .addEntry(ItemLootEntry.builder(ModItems.SMALL_XP_CRYSTAL)
                            .weight(5)
                            .acceptFunction(FillXpItemFunction.builder(RandomValueRange.of(1, 9)))
                    )
            );
            return builder;
        }
    }
}
