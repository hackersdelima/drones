package com.shishir.drones.service;

import com.shishir.drones.entity.Drone;
import com.shishir.drones.entity.Medication;

import java.util.List;
import java.util.Optional;

public interface DroneService extends GenericCrudService<Drone, String> {
    List<Drone> getAvailableDrones();

    double getBatteryLevel(final String serialNumber);

    Optional<Drone> loadMedications(final String serialNumber, final List<Medication> medications);

    List<Medication> getMedications(final String serialNumber);
}
