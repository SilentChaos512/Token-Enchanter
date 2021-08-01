package net.silentchaos512.tokenenchanter.api.xp;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class XpStorageCapability {
    @CapabilityInject(IXpStorage.class)
    public static Capability<IXpStorage> INSTANCE = null;

    private XpStorageCapability() {}

    public static void register() {
        CapabilityManager.INSTANCE.register(IXpStorage.class//,
//                new Capability.IStorage<IXpStorage>() {
//                    @Override
//                    public Tag writeNBT(Capability<IXpStorage> capability, IXpStorage instance, Direction side) {
//                        return FloatTag.valueOf(instance.getLevels());
//                    }
//
//                    @Override
//                    public void readNBT(Capability<IXpStorage> capability, IXpStorage instance, Direction side, Tag nbt) {
//                        instance.setLevels(((FloatTag) nbt).getAsFloat());
//                    }
//                },
//                () -> new XpStorage(100)
        );
    }
}
