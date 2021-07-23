package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.loot.function.FillXpItemFunction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModLoot {
    public static final LootFunctionType FILL_XP_ITEM = new LootFunctionType(FillXpItemFunction.SERIALIZER);

    static void register() {
        Registry.register(Registry.LOOT_FUNCTION_TYPE, TokenMod.getId("fill_xp_item"), FILL_XP_ITEM);
    }

    @Mod.EventBusSubscriber
    public static final class Injector {
        public static final class Tables {
            private static final Map<ResourceLocation, ResourceLocation> MAP = new HashMap<>();

            public static final ResourceLocation CHESTS_SIMPLE_DUNGEON = inject(LootTables.SIMPLE_DUNGEON);

            private Tables() {}

            public static Collection<ResourceLocation> getValues() {
                return MAP.values();
            }

            public static Optional<ResourceLocation> get(ResourceLocation lootTable) {
                return Optional.ofNullable(MAP.get(lootTable));
            }

            private static ResourceLocation inject(ResourceLocation lootTable) {
                ResourceLocation ret = TokenMod.getId("inject/" + lootTable.getPath());
                MAP.put(lootTable, ret);
                return ret;
            }
        }

        private Injector() {}

        @SubscribeEvent
        public static void onLootTableLoad(LootTableLoadEvent event) {
            Tables.get(event.getName()).ifPresent(injectorName -> {
                TokenMod.LOGGER.info("Injecting loot table '{}' into '{}'", injectorName, event.getName());
                event.getTable().addPool(
                        LootPool.lootPool()
                                .name("tokenenchanter_injected")
                                .add(TableLootEntry.lootTableReference(injectorName))
                                .build()
                );
            });
        }
    }
}
