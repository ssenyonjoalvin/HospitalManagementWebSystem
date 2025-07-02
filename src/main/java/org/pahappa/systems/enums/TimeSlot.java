package org.pahappa.systems.enums;

public enum TimeSlot {
    // Enum Constant("Display Value")
    SLOT_09_TO_11("09:00 - 11:00"),
    SLOT_12_TO_14("12:00 - 02:00"),
    SLOT_15_TO_17("03:00 - 05:00");

    private final String displayValue;

    TimeSlot(String displayValue) {
        this.displayValue = displayValue;
    }

    // This getter will be used in the UI to show the friendly text
    public String getDisplayValue() {
        return displayValue;
    }
}