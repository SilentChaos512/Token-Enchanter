package net.silentchaos512.tokenenchanter.api.xp;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public final class XpStorageCapability {
    public static final Capability<IXpStorage> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    private XpStorageCapability() {}
}
