package com.shishir.drones.service;

import com.shishir.drones.entity.Drone;
import com.shishir.drones.entity.DroneRepository;
import com.shishir.drones.entity.Medication;
import com.shishir.drones.entity.MedicationRepository;
import com.shishir.drones.enums.State;
import com.shishir.drones.exception.DroneNotFoundException;
import com.shishir.drones.exception.LowBatteryCapacityException;
import com.shishir.drones.exception.WeightLimitExceededException;
import com.shishir.drones.validation.DroneLoadingValidationComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.shishir.drones.constants.MessageConstants.DRONE_NOT_FOUND_FOR_SERIAL_NUMBER;

@Transactional
@Service
public class DroneServiceImpl implements DroneService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;

    private final DroneLoadingValidationComponent droneLoadingValidationComponent;

    @Autowired
    public DroneServiceImpl(DroneRepository droneRepository, MedicationRepository medicationRepository, DroneLoadingValidationComponent droneLoadingValidationComponent) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
        this.droneLoadingValidationComponent = droneLoadingValidationComponent;
    }

    @Override
    public List<Drone> saveAll(List<Drone> entities) {
        return droneRepository.saveAll(entities);
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
        throw new DroneNotFoundException(String.format(DRONE_NOT_FOUND_FOR_SERIAL_NUMBER, serialNumber));
    }

    @Override
    public Optional<Drone> loadMedications(final String serialNumber, final List<Medication> medications) throws DroneNotFoundException, LowBatteryCapacityException, WeightLimitExceededException {
        Optional<Drone> droneOptional = this.getOne(serialNumber);
        if (droneOptional.isPresent()) {
            Drone drone = droneOptional.get();

            droneLoadingValidationComponent.validate(medications, drone);

            List<Medication> existingMedications = drone.getMedications();

            if (existingMedications != null) {
                existingMedications.addAll(medications);
            } else {
                existingMedications = new ArrayList<>(medications);
                drone.setMedications(existingMedications);
            }
            drone.setState(State.LOADED);

            medications.forEach(medication -> medication.setDrone(drone));

            return Optional.of(droneRepository.save(drone));
        } else {
            throw new DroneNotFoundException(String.format(DRONE_NOT_FOUND_FOR_SERIAL_NUMBER, serialNumber));
        }
    }

    @Override
    public List<Medication> getMedications(final String serialNumber) {
        return medicationRepository.findAllByDroneSerialNumber(serialNumber);
    }
}
