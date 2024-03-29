package net.silentchaos512.tokenenchanter.setup;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterBlockEntity;

import java.util.Collection;
import java.util.function.Supplier;

public final class ModBlockEntityTypes {
    public static final RegistryObject<BlockEntityType<TokenEnchanterBlockEntity>> TOKEN_ENCHANTER = register("token_enchanter",
            TokenEnchanterBlockEntity::new,
            ModBlocks.TOKEN_ENCHANTER);

    private ModBlockEntityTypes() {throw new IllegalAccessError("Utility class");}

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    static void registerRenderers(FMLClientSetupEvent event) {
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factoryIn, IBlockProvider validBlock) {
        return register(name, factoryIn, () -> ImmutableList.of(validBlock.asBlock()));
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factoryIn, Supplier<Collection<? extends Block>> validBlocksSupplier) {
        return Registration.BLOCK_ENTITIES.register(name, () -> {
            Block[] validBlocks = validBlocksSupplier.get().toArray(new Block[0]);
            //noinspection ConstantConditions -- null in build
            return BlockEntityType.Builder.of(factoryIn, validBlocks).build(null);
        });
    }
}
