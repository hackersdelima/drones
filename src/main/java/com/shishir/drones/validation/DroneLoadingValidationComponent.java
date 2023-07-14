package com.shishir.drones.validation;

import com.shishir.drones.entity.Drone;
import com.shishir.drones.entity.Medication;
import com.shishir.drones.exception.LowBatteryCapacityException;
import com.shishir.drones.exception.WeightLimitExceededException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DroneLoadingValidationComponent {
    public void validate(List<Medication> medications, Drone drone) throws LowBatteryCapacityException, WeightLimitExceededException {
        this.hasBatteryCapacity(drone);
        this.hasWeightLimitExceeded(medications, drone);
    }

    public void hasBatteryCapacity(Drone drone) throws LowBatteryCapacityException {
        if (drone.getBatteryCapacity() < 25) {
            throw new LowBatteryCapacityException("Drone has battery capacity lower than 25%");
        }
    }

    public void hasWeightLimitExceeded(List<Medication> medications, Drone drone) throws WeightLimitExceededException {
        double requestWeight = medications.stream().mapToDouble(Medication::getWeight).sum();
        double droneWeightCapacity = drone.getWeightLimit();
        if (requestWeight > droneWeightCapacity) {
            throw new WeightLimitExceededException("Weight limit exceeded.");
        }
    }
}
