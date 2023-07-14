package com.shishir.drones.entity;

import com.shishir.drones.enums.State;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "drone")
public class Drone {
    @Id
    @Column(name = "serial_number", length = 100)
    private String serialNumber;
    @Column(name = "model")
    private String model;
    @Column(name = "weight_limit")
    private double weightLimit; //in grams
    @Column(name = "battery_capacity", length = 3)
    private double batteryCapacity; // in percentage
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 10)
    private State state;
}
