package com.factory.model;

public enum ProductionLineStatus {
    ACTIVE,
    STOPPED,
    MAINTENANCE;

    public ProductionLineStatus next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
