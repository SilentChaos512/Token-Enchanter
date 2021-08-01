package net.silentchaos512.tokenenchanter.api.xp;

import net.minecraft.util.Mth;

public interface IXpStorage {
    float getLevels();

    void setLevels(float levels);

    int getCapacity();

    boolean canDrain();

    default void drainLevels(float amount) {
        setLevels(Mth.clamp(getLevels() - amount, 0, getCapacity()));
    }

    default void addLevels(float amountToAdd) {
        drainLevels(-amountToAdd);
    }
}
