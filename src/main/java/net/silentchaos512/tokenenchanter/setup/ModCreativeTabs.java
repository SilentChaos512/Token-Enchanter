package net.silentchaos512.tokenenchanter.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.silentchaos512.tokenenchanter.TokenMod;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TokenMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register("tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.GOLD_TOKEN.get()))
                    .title(Component.translatable("itemGroup.tokenenchanter"))
                    .displayItems((itemDisplayParameters, output) -> {
                        Registration.ITEMS.getEntries().forEach(ro -> output.accept(ro.get()));
                    })
                    .build());
}
