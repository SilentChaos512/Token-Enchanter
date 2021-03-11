package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.tokenenchanter.TokenMod;

public class ModTags {
    public static final class Blocks {
        private Blocks() {}

        private static ITag.INamedTag<Block> forge(String path) {
            return tag("forge", path);
        }

        private static ITag.INamedTag<Block> mod(String path) {
            return tag(TokenMod.MOD_ID, path);
        }

        private static ITag.INamedTag<Block> tag(String namespace, String name) {
            return BlockTags.makeWrapperTag(new ResourceLocation(namespace, name).toString());
        }
    }

    public static final class Items {
        public static final ITag.INamedTag<Item> TOKENS_GOLD = mod("tokens/gold");
        public static final ITag.INamedTag<Item> TOKENS_SILVER = mod("tokens/silver");

        public static final ITag.INamedTag<Item> INGOTS_SILVER = forge("ingots/silver");

        private Items() {}

        private static ITag.INamedTag<Item> forge(String path) {
            return tag("forge", path);
        }

        private static ITag.INamedTag<Item> mod(String path) {
            return tag(TokenMod.MOD_ID, path);
        }

        private static ITag.INamedTag<Item> tag(String namespace, String name) {
            return ItemTags.makeWrapperTag(new ResourceLocation(namespace, name).toString());
        }
    }
}
