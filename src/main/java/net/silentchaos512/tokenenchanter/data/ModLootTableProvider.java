package net.silentchaos512.tokenenchanter.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fmllegacy.RegistryObject;
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
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(
                Pair.of(BlockLootTables::new, LootContextParamSets.BLOCK),
                Pair.of(ChestLootTables::new, LootContextParamSets.CHEST)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        map.forEach((p_218436_2_, p_218436_3_) -> LootTables.validate(validationtracker, p_218436_2_, p_218436_3_));
    }

    private static final class BlockLootTables extends net.minecraft.data.loot.BlockLoot {
        @Override
        protected void addTables() {
            dropSelf(ModBlocks.TOKEN_ENCHANTER.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }
    }

    private static final class ChestLootTables extends net.minecraft.data.loot.ChestLoot {
        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            consumer.accept(ModLoot.Injector.Tables.CHESTS_SIMPLE_DUNGEON, addXpItems());
        }

        private static LootTable.Builder addXpItems() {
            LootTable.Builder builder = LootTable.lootTable();
            builder.withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(EmptyLootItem.emptyItem()
                            .setWeight(5)
                    )
                    .add(LootItem.lootTableItem(ModItems.XP_BREAD)
                            .setWeight(15)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                            .apply(FillXpItemFunction.builder(UniformGenerator.between(2, 4)))
                    )
                    .add(LootItem.lootTableItem(ModItems.SMALL_XP_CRYSTAL)
                            .setWeight(5)
                            .apply(FillXpItemFunction.builder(UniformGenerator.between(1, 9)))
                    )
            );
            return builder;
        }
    }
}
