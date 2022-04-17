package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.tags.TagKey;
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

        private static TagKey<Block> forge(String path) {
            return tag("forge", path);
        }

        private static TagKey<Block> mod(String path) {
            return tag(TokenMod.MOD_ID, path);
        }

        private static TagKey<Block> tag(String namespace, String name) {
            return BlockTags.create(new ResourceLocation(namespace, name));
        }
    }

    public static final class Items {
        public static final TagKey<Item> TOKENS_GOLD = mod("tokens/gold");
        public static final TagKey<Item> TOKENS_SILVER = mod("tokens/silver");

        public static final TagKey<Item> INGOTS_SILVER = forge("ingots/silver");

        private Items() {}

        private static TagKey<Item> forge(String path) {
            return tag("forge", path);
        }

        private static TagKey<Item> mod(String path) {
            return tag(TokenMod.MOD_ID, path);
        }

        private static TagKey<Item> tag(String namespace, String name) {
            return ItemTags.create(new ResourceLocation(namespace, name));
        }
    }
}
