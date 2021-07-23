package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterBlock;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ModBlocks {
    public static final BlockRegistryObject<TokenEnchanterBlock> TOKEN_ENCHANTER = register("token_enchanter", () ->
            new TokenEnchanterBlock(AbstractBlock.Properties.of(Material.METAL).strength(5, 50).sound(SoundType.METAL)));

    private ModBlocks() {}

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderTypes(FMLClientSetupEvent event) {
//        RenderTypeLookup.setRenderLayer(...);
    }

    private static <T extends Block> BlockRegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return new BlockRegistryObject<>(Registration.BLOCKS.register(name, block));
    }

    private static <T extends Block> BlockRegistryObject<T> register(String name, Supplier<T> block) {
        return register(name, block, ModBlocks::defaultItem);
    }

    private static <T extends Block> BlockRegistryObject<T> register(String name, Supplier<T> block, Function<BlockRegistryObject<T>, Supplier<? extends BlockItem>> item) {
        BlockRegistryObject<T> ret = registerNoItem(name, block);
        Registration.ITEMS.register(name, item.apply(ret));
        return ret;
    }

    public static <T extends Block> Supplier<BlockItem> defaultItem(BlockRegistryObject<T> block) {
        return () -> new BlockItem(block.get(), new Item.Properties().tab(TokenMod.ITEM_GROUP));
    }
}
