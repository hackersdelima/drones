package com.shishir.drones.dto;

import com.shishir.drones.enums.State;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DroneRegisterRequest {
    private String serialNumber;
    private String model;
    private double weightLimit; //in grams
    private double batteryCapacity; // in percentage
    private State state;
}
