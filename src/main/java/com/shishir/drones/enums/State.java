package com.shishir.drones.enums;

import lombok.Getter;

@Getter
public enum State {
    IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING;
}
