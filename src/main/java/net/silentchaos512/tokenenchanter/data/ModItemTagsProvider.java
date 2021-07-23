package net.silentchaos512.tokenenchanter.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.tokenenchanter.setup.ModTags;

import javax.annotation.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, TokenMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.Items.TOKENS_GOLD).add(ModItems.GOLD_TOKEN.get());
        tag(ModTags.Items.TOKENS_SILVER).add(ModItems.SILVER_TOKEN.get());
    }
}
