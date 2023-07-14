package com.shishir.drones.service;

import com.shishir.drones.entity.Drone;
import com.shishir.drones.entity.DroneRepository;
import com.shishir.drones.entity.Medication;
import com.shishir.drones.entity.MedicationRepository;
import com.shishir.drones.enums.State;
import com.shishir.drones.exception.DroneNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class DroneServiceImpl implements DroneService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;

    @Autowired
    public DroneServiceImpl(DroneRepository droneRepository, MedicationRepository medicationRepository) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
    }

    @Override
    public Optional<Drone> save(Drone entity) {
        return Optional.of(droneRepository.save(entity));
    }

    @Override
    public Optional<Drone> update(String primaryKey, Drone entity) {
        Optional<Drone> droneOptional = droneRepository.findById(primaryKey);
        if (droneOptional.isPresent()) {
            return Optional.of(droneRepository.save(entity));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Drone> getOne(String primaryKey) {
        return droneRepository.findById(primaryKey);
    }

    @Override
    public List<Drone> getAll() {
        return droneRepository.findAll();
    }

    @Override
    public List<Drone> getAvailableDrones() {
        State[] states = {State.IDLE, State.DELIVERED};
        return droneRepository.findAllByStateIn(Arrays.asList(states));
    }

    @Override
    public double getBatteryLevel(final String serialNumber) throws DroneNotFoundException {
        Optional<Drone> droneOptional = this.getOne(serialNumber);
        if (droneOptional.isPresent()) {
            return droneOptional.get().getBatteryCapacity();
        }
        throw new DroneNotFoundException("Drone not found for serialNumber: " + serialNumber);
    }

    @Override
    public Optional<Drone> loadMedications(final String serialNumber, final List<Medication> medications) throws DroneNotFoundException {
        Optional<Drone> droneOptional = this.getOne(serialNumber);
        if (droneOptional.isPresent()) {
            Drone drone = droneOptional.get();

            List<Medication> existingMedications = drone.getMedications();

            if (existingMedications != null) {
                existingMedications.addAll(medications);
            } else {
                existingMedications = new ArrayList<>(medications);
                drone.setMedications(existingMedications);
            }

            medications.forEach(medication -> medication.setDrone(drone));

            return Optional.of(droneRepository.save(drone));
        } else {
            throw new DroneNotFoundException("Drone not found for serialNumber: " + serialNumber);
        }
    }

    @Override
    public List<Medication> getMedications(final String serialNumber) {
        return medicationRepository.findAllByDroneSerialNumber(serialNumber);
    }
}
