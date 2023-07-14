package com.shishir.drones.enums;

import lombok.Getter;

@Getter
public enum Model {
    LIGHT_WEIGHT("Lightweight"),
    MIDDLE_WEIGHT("Middleweight"),
    CRUISER_WEIGHT("Cruiserweight"),
    HEAVY_WEIGHT("Heavyweight");
    private final String value;

    Model(String value) {
        this.value = value;
    }
}
