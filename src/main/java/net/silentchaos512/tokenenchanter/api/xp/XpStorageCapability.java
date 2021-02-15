package net.silentchaos512.tokenenchanter.api.xp;

import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class XpStorageCapability {
    @CapabilityInject(IXpStorage.class)
    public static Capability<IXpStorage> INSTANCE = null;

    private XpStorageCapability() {}

    public static void register() {
        CapabilityManager.INSTANCE.register(IXpStorage.class,
                new Capability.IStorage<IXpStorage>() {
                    @Override
                    public INBT writeNBT(Capability<IXpStorage> capability, IXpStorage instance, Direction side) {
                        return FloatNBT.valueOf(instance.getLevels());
                    }

                    @Override
                    public void readNBT(Capability<IXpStorage> capability, IXpStorage instance, Direction side, INBT nbt) {
                        instance.setLevels(((FloatNBT) nbt).getFloat());
                    }
                },
                () -> new XpStorage(100));
    }
}
