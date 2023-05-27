package net.silentchaos512.tokenenchanter.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.tokenenchanter.setup.ModTags;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(GatherDataEvent event, BlockTagsProvider blockTagProvider) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), blockTagProvider, TokenMod.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.TOKENS_GOLD).add(ModItems.GOLD_TOKEN.get());
        tag(ModTags.Items.TOKENS_SILVER).add(ModItems.SILVER_TOKEN.get());
    }
}
