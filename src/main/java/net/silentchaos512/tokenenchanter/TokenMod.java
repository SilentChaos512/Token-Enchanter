package net.silentchaos512.tokenenchanter;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.tokenenchanter.config.ModConfig;
import net.silentchaos512.tokenenchanter.setup.ModClientProxy;
import net.silentchaos512.tokenenchanter.setup.ModItems;
import net.silentchaos512.tokenenchanter.setup.Registration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

@Mod(TokenMod.MOD_ID)
public class TokenMod {
    public static final String MOD_ID = "tokenenchanter";

    public static final Random RANDOM = new Random();
    public static final Logger LOGGER = LogManager.getLogger("Token Enchanter");

    public TokenMod() {
        Registration.register();
        ModConfig.init();

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ModClientProxy::register);
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
            return ResourceLocation.tryCreate(name);
        return ResourceLocation.tryCreate(MOD_ID + ":" + name);
    }

    public static String shortenId(@Nullable ResourceLocation id) {
        if (id == null)
            return "null";
        if (MOD_ID.equals(id.getNamespace()))
            return id.getPath();
        return id.toString();
    }

    public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.BLANK_TOKEN);
        }
    };
}
