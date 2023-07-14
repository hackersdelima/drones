package com.shishir.drones.service;

import com.shishir.drones.entity.Drone;
import com.shishir.drones.entity.Medication;
import com.shishir.drones.exception.DroneNotFoundException;
import com.shishir.drones.exception.LowBatteryCapacityException;

import java.util.List;
import java.util.Optional;

public interface DroneService extends GenericCrudService<Drone, String> {
    List<Drone> getAvailableDrones();

    double getBatteryLevel(final String serialNumber) throws DroneNotFoundException;

    Optional<Drone> loadMedications(final String serialNumber, final List<Medication> medications) throws DroneNotFoundException, LowBatteryCapacityException;

    List<Medication> getMedications(final String serialNumber);
}
