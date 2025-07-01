package org.pahappa.systems.enums;

public enum Shift {
    MORNING("8am-11am"),
    AFTERNOON("12pm-4pm"),
    NIGHT("4pm-9pm");

    private final String timeRange;

    Shift(String timeRange) {
        this.timeRange = timeRange;
    }
}
