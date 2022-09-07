package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.tokenenchanter.TokenMod;

public class Registration {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = create(ForgeRegistries.BLOCK_ENTITY_TYPES);
    public static final DeferredRegister<Block> BLOCKS = create(ForgeRegistries.BLOCKS);
    public static final DeferredRegister<Item> ITEMS = create(ForgeRegistries.ITEMS);
    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS = create(Registry.LOOT_FUNCTION_REGISTRY);
    public static final DeferredRegister<MenuType<?>> MENUS = create(ForgeRegistries.MENU_TYPES);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = create(ForgeRegistries.RECIPE_SERIALIZERS);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = create(ForgeRegistries.RECIPE_TYPES);

    private Registration() {}

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCK_ENTITIES.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        LOOT_FUNCTIONS.register(modEventBus);
        MENUS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);

        ModBlockEntityTypes.register();
        ModBlocks.register();
        ModContainers.register();
        ModItems.register();
        ModRecipes.register();
    }

    private static <T> DeferredRegister<T> create(IForgeRegistry<T> registry) {
        return DeferredRegister.create(registry, TokenMod.MOD_ID);
    }

    private static <B> DeferredRegister<B> create(ResourceKey<Registry<B>> registry) {
        return DeferredRegister.create(registry, TokenMod.MOD_ID);
    }
}
