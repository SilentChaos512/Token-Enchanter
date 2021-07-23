package net.silentchaos512.tokenenchanter.block.tokenenchanter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class TokenEnchanterBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 12, 16);

    public TokenEnchanterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TokenEnchanterTileEntity();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof IInventory) {
                InventoryHelper.dropContents(worldIn, pos, (IInventory) tile);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide) {
            TileEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof TokenEnchanterTileEntity) {
                player.openMenu((INamedContainerProvider) tile);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
