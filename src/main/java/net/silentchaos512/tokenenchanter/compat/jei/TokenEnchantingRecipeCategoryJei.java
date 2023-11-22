package net.silentchaos512.tokenenchanter.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.tokenenchanter.TokenMod;
import net.silentchaos512.tokenenchanter.api.xp.IXpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorage;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageCapability;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterContainerScreen;
import net.silentchaos512.tokenenchanter.crafting.recipe.TokenEnchanterRecipe;
import net.silentchaos512.tokenenchanter.setup.ModBlocks;
import net.silentchaos512.tokenenchanter.util.TextUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TokenEnchantingRecipeCategoryJei implements IRecipeCategory<TokenEnchanterRecipe> {
    public static final RecipeType<TokenEnchanterRecipe> RECIPE_TYPE = RecipeType.create(TokenMod.MOD_ID, "token_enchanting", TokenEnchanterRecipe.class);

    private static final int GUI_START_X = 21;
    private static final int GUI_START_Y = 24;
    private static final int GUI_WIDTH = 134;
    private static final int GUI_HEIGHT = 50;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public TokenEnchantingRecipeCategoryJei(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(TokenEnchanterContainerScreen.TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.TOKEN_ENCHANTER));
        localizedName = Component.translatable("category.tokenenchanter.token_enchanting");
    }

    @Override
    public RecipeType<TokenEnchanterRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, TokenEnchanterRecipe recipe, IFocusGroup focuses) {
        List<List<ItemStack>> inputs = new ArrayList<>();
        recipe.getIngredientMap().forEach((ingredient, count) -> {
            List<ItemStack> list = Arrays.asList(ingredient.getItems());
            list.forEach(stack -> stack.setCount(count));
            inputs.add(list);
        });

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 31)
                .addIngredients(VanillaTypes.ITEM_STACK, getXpCrystals(recipe));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 11)
                .addIngredients(VanillaTypes.ITEM_STACK, Arrays.asList(recipe.getToken().getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 1)
                .addIngredients(VanillaTypes.ITEM_STACK, getStacksOrEmptyList(inputs, 0));
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 1)
                .addIngredients(VanillaTypes.ITEM_STACK, getStacksOrEmptyList(inputs, 1));
        builder.addSlot(RecipeIngredientRole.INPUT, 63, 1)
                .addIngredients(VanillaTypes.ITEM_STACK, getStacksOrEmptyList(inputs, 2));
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 19)
                .addIngredients(VanillaTypes.ITEM_STACK, getStacksOrEmptyList(inputs, 3));
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 19)
                .addIngredients(VanillaTypes.ITEM_STACK, getStacksOrEmptyList(inputs, 4));
        builder.addSlot(RecipeIngredientRole.INPUT, 63, 19)
                .addIngredients(VanillaTypes.ITEM_STACK, getStacksOrEmptyList(inputs, 5));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 11)
                .addIngredients(VanillaTypes.ITEM_STACK, Collections.singletonList(recipe.getResult()));
    }

    private static List<ItemStack> getStacksOrEmptyList(List<List<ItemStack>> list, int index) {
        if (index < 0 || index >= list.size()) {
            return Collections.emptyList();
        }
        return list.get(index);
    }

    private static List<ItemStack> getXpCrystals(TokenEnchanterRecipe recipe) {
        return ForgeRegistries.ITEMS.getValues().stream()
                .map(ItemStack::new)
                .filter(stack -> {
                    IXpStorage xp = stack.getCapability(XpStorageCapability.INSTANCE).orElse(XpStorage.INVALID);
                    return xp.canDrain() && xp.getCapacity() >= recipe.getLevelCost();
                })
                .map(TokenEnchantingRecipeCategoryJei::getFullCrystal)
                .collect(Collectors.toList());
    }

    private static ItemStack getFullCrystal(ItemStack stack) {
        stack.getCapability(XpStorageCapability.INSTANCE).ifPresent(xp -> xp.addLevels(xp.getCapacity()));
        return stack;
    }

    @Override
    public void draw(TokenEnchanterRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        Component text = TextUtil.translate("misc", "level_cost", recipe.getLevelCost());
        graphics.drawString(font, text.getString(), 25, GUI_HEIGHT - font.lineHeight - 1, -1);
    }
}
