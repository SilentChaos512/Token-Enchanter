package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.silentchaos512.tokenenchanter.TokenMod;

public class ModTags {
    public static final class Blocks {
        private Blocks() {}

        private static Tag.Named<Block> forge(String path) {
            return tag("forge", path);
        }

        private static Tag.Named<Block> mod(String path) {
            return tag(TokenMod.MOD_ID, path);
        }

        private static Tag.Named<Block> tag(String namespace, String name) {
            return BlockTags.bind(new ResourceLocation(namespace, name).toString());
        }
    }

    public static final class Items {
        public static final Tag.Named<Item> TOKENS_GOLD = mod("tokens/gold");
        public static final Tag.Named<Item> TOKENS_SILVER = mod("tokens/silver");

        public static final Tag.Named<Item> INGOTS_SILVER = forge("ingots/silver");

        private Items() {}

        private static Tag.Named<Item> forge(String path) {
            return tag("forge", path);
        }

        private static Tag.Named<Item> mod(String path) {
            return tag(TokenMod.MOD_ID, path);
        }

        private static Tag.Named<Item> tag(String namespace, String name) {
            return ItemTags.bind(new ResourceLocation(namespace, name).toString());
        }
    }
}
