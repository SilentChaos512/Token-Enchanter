package net.silentchaos512.tokenenchanter.data.client;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.util.NameUtils;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.item.EnchantedTokenItem;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.tokenenchanter.setup.Registration;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, TokenMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(new ResourceLocation("item/generated"));

        Registration.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .forEach(this::blockBuilder);

        ModItems.getSimpleModelItems().forEach(iro -> builder(iro.get(), itemGenerated));

        registerEnchantedTokens(itemGenerated);
    }

    private void registerEnchantedTokens(ModelFile itemGenerated) {
        for (EnchantedTokenItem.Icon icon : EnchantedTokenItem.Icon.values()) {
            getBuilder("item/enchanted_token/" + icon.getName())
                    .parent(itemGenerated)
                    .texture("layer0", modLoc("item/gold_token"))
                    .texture("layer1", modLoc("item/enchanted_token/outline"))
                    .texture("layer2", modLoc("item/enchanted_token/" + icon.getName()));
        }

        ItemModelBuilder builder = getBuilder("enchanted_token")
                .parent(itemGenerated)
                .texture("layer0", modLoc("item/enchanted_token/any"));

        for (EnchantedTokenItem.Icon icon : EnchantedTokenItem.Icon.values()) {
            builder.override()
                    .model(getExistingFile(modLoc("item/enchanted_token/" + icon.getName())))
                    .predicate(EnchantedTokenItem.MODEL_INDEX, icon.ordinal())
                    .end();
        }
    }

    private void blockBuilder(IBlockProvider block) {
        blockBuilder(block.asBlock());
    }

    private void blockBuilder(Block block) {
        String name = NameUtils.from(block).getPath();
        withExistingParent(name, modLoc("block/" + name));
    }

    private void builder(IItemProvider item, ModelFile parent) {
        String name = NameUtils.fromItem(item).getPath();
        builder(item, parent, "item/" + name);
    }

    private void builder(IItemProvider item, ModelFile parent, String texture) {
        getBuilder(NameUtils.fromItem(item).getPath())
                .parent(parent)
                .texture("layer0", modLoc(texture));
    }
}
