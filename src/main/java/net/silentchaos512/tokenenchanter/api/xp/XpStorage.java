package net.silentchaos512.tokenenchanter.api.xp;

public class XpStorage implements IXpStorage {
    private float levels;
    private final int capacity;

    public XpStorage(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public float getLevels() {
        return this.levels;
    }

    @Override
    public void setLevels(float levels) {
        this.levels = levels;
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }
}
