package net.silentchaos512.tokenenchanter.setup;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterTileEntity;

import java.util.Collection;
import java.util.function.Supplier;

public final class ModTileEntities {
    public static final RegistryObject<TileEntityType<TokenEnchanterTileEntity>> TOKEN_ENCHANTER = register("token_enchanter",
            TokenEnchanterTileEntity::new,
            ModBlocks.TOKEN_ENCHANTER);

    private ModTileEntities() {throw new IllegalAccessError("Utility class");}

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    static void registerRenderers(FMLClientSetupEvent event) {
    }

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factoryIn, IBlockProvider validBlock) {
        return register(name, factoryIn, () -> ImmutableList.of(validBlock.asBlock()));
    }

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factoryIn, Supplier<Collection<? extends Block>> validBlocksSupplier) {
        return Registration.TILE_ENTITIES.register(name, () -> {
            Block[] validBlocks = validBlocksSupplier.get().toArray(new Block[0]);
            //noinspection ConstantConditions -- null in build
            return TileEntityType.Builder.of(factoryIn, validBlocks).build(null);
        });
    }
}
