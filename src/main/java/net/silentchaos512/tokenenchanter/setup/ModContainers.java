package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fmllegacy.network.IContainerFactory;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterContainerMenu;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterContainerScreen;

public class ModContainers {
    public static final RegistryObject<MenuType<TokenEnchanterContainerMenu>> TOKEN_ENCHANTER = register("token_enchanter", TokenEnchanterContainerMenu::new);

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        MenuScreens.register(TOKEN_ENCHANTER.get(), TokenEnchanterContainerScreen::new);
    }

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return Registration.CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
    }
}
