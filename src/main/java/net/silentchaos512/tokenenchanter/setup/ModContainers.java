package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.IContainerFactory;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterContainer;
import net.silentchaos512.tokenenchanter.block.tokenenchanter.TokenEnchanterScreen;

public class ModContainers {
    public static final RegistryObject<ContainerType<TokenEnchanterContainer>> TOKEN_ENCHANTER = register("token_enchanter", TokenEnchanterContainer::new);

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(TOKEN_ENCHANTER.get(), TokenEnchanterScreen::new);
    }

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, IContainerFactory<T> factory) {
        return Registration.CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
    }
}
