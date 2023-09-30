package net.silentchaos512.tokenenchanter.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaChestLoot;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.silentchaos512.tokenenchanter.loot.function.FillXpItemFunction;
import net.silentchaos512.tokenenchanter.setup.ModBlocks;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.tokenenchanter.setup.ModLoot;
import net.silentchaos512.tokenenchanter.setup.Registration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(DataGenerator gen) {
        super(gen.getPackOutput(), Collections.emptySet(), VanillaLootTableProvider.create(gen.getPackOutput()).getTables());
    }

    @Override
    public List<SubProviderEntry> getTables() {
        return ImmutableList.of(
                new SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(ChestLootTables::new, LootContextParamSets.CHEST)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
        map.forEach((name, table) -> {
            table.validate(validationContext.setParams(table.getParamSet()).enterElement("{" + name + "}", new LootDataId<>(LootDataType.TABLE, name)));
        });
    }

    private static final class BlockLootTables extends BlockLootSubProvider {
        protected BlockLootTables() {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            dropSelf(ModBlocks.TOKEN_ENCHANTER.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
        }
    }

    private static final class ChestLootTables extends VanillaChestLoot {
        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
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
