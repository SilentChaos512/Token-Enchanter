package net.silentchaos512.tokenenchanter.setup;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ModClientProxy {
    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModClientProxy::clientSetup);
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        ModBlocks.registerRenderTypes(event);
        ModTileEntities.registerRenderers(event);
        ModContainers.registerScreens(event);
    }
}
