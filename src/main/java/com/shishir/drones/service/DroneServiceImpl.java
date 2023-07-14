package com.shishir.drones.service;

import com.shishir.drones.entity.Drone;
import com.shishir.drones.entity.DroneRepository;
import com.shishir.drones.enums.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DroneServiceImpl implements DroneService {
    private final DroneRepository droneRepository;

    @Autowired
    public DroneServiceImpl(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
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
    public double getBatteryLevel(final String serialNumber) {
        Optional<Drone> droneOptional = this.getOne(serialNumber);
        if(droneOptional.isPresent()){
            return droneOptional.get().getBatteryCapacity();
        }

        throw new RuntimeException();
    }
}
