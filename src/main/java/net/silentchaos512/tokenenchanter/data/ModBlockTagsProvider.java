package net.silentchaos512.tokenenchanter.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.tokenenchanter.TokenMod;

import javax.annotation.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, TokenMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
