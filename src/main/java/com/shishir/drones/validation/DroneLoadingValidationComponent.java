package com.shishir.drones.validation;

import com.shishir.drones.entity.Drone;
import com.shishir.drones.exception.LowBatteryCapacityException;
import org.springframework.stereotype.Component;

@Component
public class DroneLoadingValidationComponent {
    public void validate(Drone drone) throws LowBatteryCapacityException {
        this.hasLoadableBatteryCapacity(drone);
    }

    public void hasLoadableBatteryCapacity(Drone drone) throws LowBatteryCapacityException {
        if (drone.getBatteryCapacity() < 25) {
            throw new LowBatteryCapacityException("Drone has battery capacity lower than 25%");
        }
    }
}
