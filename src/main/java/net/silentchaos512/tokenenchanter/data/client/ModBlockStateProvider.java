package net.silentchaos512.tokenenchanter.data.client;

import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.setup.ModBlocks;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.util.NameUtils;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, TokenMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        threeQuartersBlock(ModBlocks.TOKEN_ENCHANTER);
    }

    private void simpleBlock(IBlockProvider block) {
        simpleBlock(block.asBlock());
    }

    private void simpleBlock(IBlockProvider block, String texture) {
        simpleBlock(block.asBlock(), texture);
    }

    private void simpleBlock(Block block, String texture) {
        String name = NameUtils.fromBlock(block).getPath();
        simpleBlock(block, models().cubeAll(name, modLoc(texture)));
    }

    private void threeQuartersBlock(IBlockProvider block) {
        String name = NameUtils.fromBlock(block).getPath();
        threeQuartersBlock(block.asBlock(),
                "block/" + name + "_bottom",
                "block/" + name + "_side",
                "block/" + name + "_top");
    }

    private void threeQuartersBlock(Block block, String bottomTexture, String sideTexture, String topTexture) {
        String name = NameUtils.fromBlock(block).getPath();
        simpleBlock(block, models().withExistingParent(name, mcLoc("block/block"))
                .texture("bottom", bottomTexture)
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("particle", bottomTexture)
                .element()
                .from(0, 0, 0)
                .to(16, 12, 16)
                .face(Direction.NORTH).uvs(0, 4, 16, 16).texture("#side").end()
                .face(Direction.EAST).uvs(0, 4, 16, 16).texture("#side").end()
                .face(Direction.SOUTH).uvs(0, 4, 16, 16).texture("#side").end()
                .face(Direction.WEST).uvs(0, 4, 16, 16).texture("#side").end()
                .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top").end()
                .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom").end()
                .end()
        );
    }
}
