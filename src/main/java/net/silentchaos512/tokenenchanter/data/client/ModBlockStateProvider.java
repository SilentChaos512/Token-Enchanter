package net.silentchaos512.tokenenchanter.data.client;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
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
        simpleBlock(ModBlocks.TOKEN_ENCHANTER);
    }

    private void simpleBlock(IBlockProvider block) {
        simpleBlock(block.asBlock());
    }

    private void simpleBlock(IBlockProvider block, String texture) {
        simpleBlock(block.asBlock(), texture);
    }

    private void simpleBlock(Block block, String texture) {
        String name = NameUtils.from(block).getPath();
        simpleBlock(block, models().cubeAll(name, modLoc(texture)));
    }
}
