package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.tokenenchanter.item.EnchantedTokenItem;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModClientProxy {
    private ModClientProxy() {}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ModBlocks.registerRenderTypes(event);
        ModBlockEntityTypes.registerRenderers(event);
        ModContainers.registerScreens(event);

        ItemProperties.register(ModItems.ENCHANTED_TOKEN.get(),
                EnchantedTokenItem.MODEL_INDEX,
                EnchantedTokenItem::getModel);
    }

    @SubscribeEvent
    public static void onItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register(EnchantedTokenItem::getItemColor, ModItems.ENCHANTED_TOKEN);
    }
}
