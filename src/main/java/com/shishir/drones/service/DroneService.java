package com.shishir.drones.service;

import com.shishir.drones.entity.Drone;

import java.util.List;

public interface DroneService extends GenericCrudService<Drone, String> {
    List<Drone> getAvailableDrones();

    double getBatteryLevel(final String serialNumber);
}
