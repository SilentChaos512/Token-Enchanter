package net.silentchaos512.tokenenchanter;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.silentchaos512.tokenenchanter.api.xp.IXpStorage;
import net.silentchaos512.tokenenchanter.config.ModConfig;
import net.silentchaos512.tokenenchanter.item.HasSubItems;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.tokenenchanter.setup.Registration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

@Mod(TokenMod.MOD_ID)
@Mod.EventBusSubscriber(modid = TokenMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TokenMod {
    public static final String MOD_ID = "tokenenchanter";

    public static final Random RANDOM = new Random();
    public static final Logger LOGGER = LogManager.getLogger("Token Enchanter");

    @Nullable
    private static CreativeModeTab creativeModeTab;

    public TokenMod() {
        Registration.register();
        ModConfig.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(TokenMod::onRegisterCreativeTab);
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IXpStorage.class);
    }

    private static void onRegisterCreativeTab(CreativeModeTabEvent.Register event) {
        creativeModeTab = event.registerCreativeModeTab(getId("tab"), b -> b
                .icon(() -> new ItemStack(ModItems.GOLD_TOKEN.get()))
                .title(Component.translatable("itemGroup.tokenenchanter"))
                .displayItems((itemDisplayParameters, output) -> {
                    Registration.ITEMS.getEntries().forEach(ro -> {
                        Item item = ro.get();
                        if (item instanceof HasSubItems) {
                            output.acceptAll(((HasSubItems) item).getSubItems());
                        } else {
                            output.accept(item);
                        }
                    });
                })
                .build()
        );
    }

    public static String getVersion() {
        Optional<? extends ModContainer> o = ModList.get().getModContainerById(MOD_ID);
        if (o.isPresent()) {
            return o.get().getModInfo().getVersion().toString();
        }
        return "0.0.0";
    }

    public static boolean isDevBuild() {
        return "NONE".equals(getVersion());
    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    @Nullable
    public static ResourceLocation getIdWithDefaultNamespace(String name) {
        if (name.contains(":"))
            return ResourceLocation.tryParse(name);
        return ResourceLocation.tryParse(MOD_ID + ":" + name);
    }

    public static String shortenId(@Nullable ResourceLocation id) {
        if (id == null)
            return "null";
        if (MOD_ID.equals(id.getNamespace()))
            return id.getPath();
        return id.toString();
    }
}
