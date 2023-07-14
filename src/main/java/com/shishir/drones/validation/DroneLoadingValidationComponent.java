package com.shishir.drones.validation;

import com.shishir.drones.dto.MedicationRequest;
import com.shishir.drones.entity.Drone;
import com.shishir.drones.entity.Medication;
import com.shishir.drones.exception.InvalidCodeException;
import com.shishir.drones.exception.InvalidNameException;
import com.shishir.drones.exception.LowBatteryCapacityException;
import com.shishir.drones.exception.WeightLimitExceededException;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.shishir.drones.constants.MessageConstants.*;

@Component
public class DroneLoadingValidationComponent {
    public void validate(List<Medication> medications, Drone drone) throws LowBatteryCapacityException, WeightLimitExceededException {
        this.hasBatteryCapacity(drone);
        this.hasWeightLimitExceeded(medications, drone);
    }

    public void hasBatteryCapacity(Drone drone) throws LowBatteryCapacityException {
        double limit = 25;
        if (drone.getBatteryCapacity() < limit) {
            throw new LowBatteryCapacityException(String.format(DRONE_LOW_BATTERY_CAPACITY, limit));
        }
    }

    public void hasWeightLimitExceeded(List<Medication> medications, Drone drone) throws WeightLimitExceededException {
        double requestWeight = medications.stream().mapToDouble(Medication::getWeight).sum();
        double droneWeightCapacity = drone.getWeightLimit();
        if (requestWeight > droneWeightCapacity) {
            throw new WeightLimitExceededException(DRONE_WEIGHT_LIMIT_EXCEEDED);
        }
    }

    public void validateMedicationRequest(List<MedicationRequest> medicationRequests) throws InvalidCodeException, InvalidNameException {
        for (MedicationRequest medicationRequest : medicationRequests) {
            String codeRegex = "^[A-Z0-9_]+$";
            if (!medicationRequest.getCode().matches(codeRegex)) {
                throw new InvalidCodeException(String.format(INVALID_CODE, medicationRequest.getCode()));
            }

            String nameRegex = "^[A-Za-z0-9_\\s-]+$";
            if (!medicationRequest.getName().matches(nameRegex)) {
                throw new InvalidNameException(String.format(INVALID_NAME, medicationRequest.getName()));
            }
        }
    }
}
