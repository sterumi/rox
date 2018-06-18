package rox.main.event;

public enum EPriority {
    LOW(-1), NORMAL(0), HIGH(1), DIRECT(2);

    private final int slot;

    EPriority(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }
}
