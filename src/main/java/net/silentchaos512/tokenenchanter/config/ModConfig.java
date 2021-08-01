package net.silentchaos512.tokenenchanter.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.tokenenchanter.TokenMod;

@Mod.EventBusSubscriber(modid = TokenMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModConfig {
    public static final class Common {
        static final ForgeConfigSpec spec;

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

            spec = builder.build();
        }

        private Common() {}
    }

    private ModConfig() {}

    public static void init() {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, Common.spec);
    }

    /*public static void sync() {}

    @SubscribeEvent
    public static void sync(net.minecraftforge.fml.config.ModConfig.Loading event) {
        sync();
    }

    @SubscribeEvent
    public static void sync(net.minecraftforge.fml.config.ModConfig.Reloading event) {
        sync();
    }*/
}
