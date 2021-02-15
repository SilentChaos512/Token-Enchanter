package net.silentchaos512.tokenenchanter.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.tokenenchanter.api.xp.IXpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorage;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterScreen;
import net.silentchaos512.tokenenchanter.capability.XpStorageCapability;
import net.silentchaos512.tokenenchanter.crafting.recipe.TokenEnchanterRecipe;
import net.silentchaos512.tokenenchanter.setup.ModBlocks;
import net.silentchaos512.tokenenchanter.util.TextUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TokenEnchantingRecipeCategoryJei implements IRecipeCategory<TokenEnchanterRecipe> {
    private static final int GUI_START_X = 21;
    private static final int GUI_START_Y = 24;
    private static final int GUI_WIDTH = 134;
    private static final int GUI_HEIGHT = 50;

    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public TokenEnchantingRecipeCategoryJei(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(TokenEnchanterScreen.TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.TOKEN_ENCHANTER));
        localizedName = I18n.format("category.tokenenchanter.token_enchanting");
    }

    @Override
    public ResourceLocation getUid() {
        return TokenEnchanterJeiPlugin.TOKEN_ENCHANTING;
    }

    @Override
    public Class<? extends TokenEnchanterRecipe> getRecipeClass() {
        return TokenEnchanterRecipe.class;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(TokenEnchanterRecipe recipe, IIngredients ingredients) {
        List<Ingredient> inputs = new ArrayList<>();
        inputs.add(recipe.getToken());
        inputs.addAll(recipe.getIngredientMap().keySet());
        ingredients.setInputIngredients(inputs);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, TokenEnchanterRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 0, 30);
        itemStacks.init(1, true, 0, 10);
        itemStacks.init(2, true, 26, 0);
        itemStacks.init(3, true, 44, 0);
        itemStacks.init(4, true, 62, 0);
        itemStacks.init(5, true, 26, 18);
        itemStacks.init(6, true, 44, 18);
        itemStacks.init(7, true, 62, 18);
        itemStacks.init(8, false, 111, 10);

        itemStacks.set(0, getXpCrystals(recipe));
        itemStacks.set(1, Arrays.asList(recipe.getToken().getMatchingStacks()));
        List<List<ItemStack>> inputs = new ArrayList<>();
        recipe.getIngredientMap().forEach((ingredient, count) -> {
            List<ItemStack> list = Arrays.asList(ingredient.getMatchingStacks());
            list.forEach(stack -> stack.setCount(count));
            inputs.add(list);
        });
        for (int i = 0; i < inputs.size(); ++i) {
            itemStacks.set(i + 2, inputs.get(i));
        }
        itemStacks.set(8, recipe.getResult());
    }

    private static List<ItemStack> getXpCrystals(TokenEnchanterRecipe recipe) {
        return ForgeRegistries.ITEMS.getValues().stream()
                .map(ItemStack::new)
                .filter(stack -> stack.getCapability(XpStorageCapability.INSTANCE).isPresent())
                .map(TokenEnchantingRecipeCategoryJei::getFullCrystal)
                .filter(stack -> {
                    LazyOptional<IXpStorage> lazy = stack.getCapability(XpStorageCapability.INSTANCE);
                    return lazy.orElseGet(() -> new XpStorage(0)).getCapacity() >= recipe.getLevelCost();
                })
                .collect(Collectors.toList());
    }

    private static ItemStack getFullCrystal(ItemStack stack) {
        stack.getCapability(XpStorageCapability.INSTANCE).ifPresent(xp -> xp.addLevels(xp.getCapacity()));
        return stack;
    }

    @Override
    public void draw(TokenEnchanterRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        FontRenderer font = Minecraft.getInstance().fontRenderer;
        ITextComponent text = TextUtil.translate("misc", "level_cost", recipe.getLevelCost());
        font.drawStringWithShadow(matrixStack, text.getString(), 25, GUI_HEIGHT - font.FONT_HEIGHT - 1, -1);
    }
}
