package net.silentchaos512.tokenenchanter.util;

import net.minecraft.network.chat.Component;
import net.silentchaos512.tokenenchanter.TokenMod;

public final class TextUtil {
    private TextUtil() {throw new IllegalAccessError("Utility class");}

    public static Component translate(String prefix, String suffix, Object... params) {
        String key = String.format("%s.%s.%s", prefix, TokenMod.MOD_ID, suffix);
        return Component.translatable(key, params);
    }
}
