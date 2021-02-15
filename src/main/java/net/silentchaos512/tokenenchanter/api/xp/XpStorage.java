package net.silentchaos512.tokenenchanter.api.xp;

public class XpStorage implements IXpStorage {
    public static final XpStorage INVALID = new XpStorage(0, false);

    private float levels;
    private final int capacity;
    private final boolean canDrain;

    public XpStorage(int capacity) {
        this(capacity, true);
    }

    public XpStorage(int capacity, boolean canDrain) {
        this.capacity = capacity;
        this.canDrain = canDrain;
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

    @Override
    public boolean canDrain() {
        return this.canDrain;
    }
}
