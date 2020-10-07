package org.architectdrone.archevo.action;

abstract class PositionalAction implements Action {
    private final int x_offset;
    private final int y_offset;

    public PositionalAction(int x_offset, int y_offset) {
        this.x_offset = x_offset;
        this.y_offset = y_offset;
    }

    @Override
    public boolean has_external_effect() {
        return true;
    }

    public int getXOffset() {
        return x_offset;
    }

    public int getYOffset() {
        return y_offset;
    }
}
