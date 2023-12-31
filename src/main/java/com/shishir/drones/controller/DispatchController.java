package com.shishir.drones.controller;

import com.shishir.drones.dto.*;
import com.shishir.drones.entity.Drone;
import com.shishir.drones.entity.Medication;
import com.shishir.drones.exception.*;
import com.shishir.drones.mapper.DroneMapper;
import com.shishir.drones.mapper.MedicationMapper;
import com.shishir.drones.service.DroneService;
import com.shishir.drones.validation.DroneLoadingValidationComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.shishir.drones.constants.MessageConstants.*;

@Slf4j
@RestController
@RequestMapping("api/v1/dispatches")
public class DispatchController {
    private final DroneService droneService;
    private final DroneMapper droneMapper;

    private final MedicationMapper medicationMapper;
    private final DroneLoadingValidationComponent droneLoadingValidationComponent;

    @Autowired
    public DispatchController(DroneService droneService, DroneMapper droneMapper, MedicationMapper medicationMapper, DroneLoadingValidationComponent droneLoadingValidationComponent) {
        this.droneService = droneService;
        this.droneMapper = droneMapper;
        this.medicationMapper = medicationMapper;
        this.droneLoadingValidationComponent = droneLoadingValidationComponent;
    }

    @PostMapping("/drones")
    public ResponseEntity<GenericResponseDto<DroneResponse>> registerDrone(@RequestBody DroneRequest request) {
        log.info("Register drone requested.");
        Optional<Drone> registeredDrone = droneService.save(
                droneMapper.toDrone(request)
        );

        return registeredDrone.map(drone -> new ResponseEntity<>(new GenericResponseDto<>(
                HttpStatus.OK.value(),
                DRONE_REGISTERED,
                droneMapper.toDroneResponse(drone)
        ), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new GenericResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                DRONE_REGISTRATION_FAILED,
                null
        ), HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/drones/available")
    public ResponseEntity<GenericResponseDto<List<DroneResponse>>> getAvailableDronesForLoading() {
        log.info("View available drones requested.");
        List<Drone> availableDrones = droneService.getAvailableDrones();

        return new ResponseEntity<>(new GenericResponseDto<>(
                HttpStatus.OK.value(),
                AVAILABLE_DRONES_FOUND,
                droneMapper.toDroneResponses(availableDrones)
        ), HttpStatus.OK);
    }

    @GetMapping("/drones/{serialNumber}/battery-level")
    public ResponseEntity<GenericResponseDto<Double>> getDroneBatteryLevel(@PathVariable String serialNumber) {
        log.info("View battery level for drone {} requested.", serialNumber);
        try {
            double batteryLevel = droneService.getBatteryLevel(serialNumber);

            return new ResponseEntity<>(new GenericResponseDto<>(
                    HttpStatus.OK.value(),
                    DRONE_BATTERY_LEVEL_FOUND,
                    batteryLevel
            ), HttpStatus.OK);
        } catch (DroneNotFoundException ex) {
            log.error("Exception", ex);
            return new ResponseEntity<>(new GenericResponseDto<>(
                    HttpStatus.NOT_FOUND.value(),
                    ex.getMessage(),
                    null
            ), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/drones/{serialNumber}/medications")
    public ResponseEntity<GenericResponseDto<List<MedicationResponse>>> loadMedications(@PathVariable String serialNumber, @RequestBody List<MedicationRequest> request) {
        log.info("Load medications to drone {} requested.", serialNumber);
        try {
            droneLoadingValidationComponent.validateMedicationRequest(request);

            List<Medication> medications = medicationMapper.toMedications(request);
            if (!medications.isEmpty()) {
                Optional<Drone> droneOptional = droneService.loadMedications(serialNumber, medications);
                if (droneOptional.isPresent()) {
                    return new ResponseEntity<>(new GenericResponseDto<>(
                            HttpStatus.OK.value(),
                            MEDICATIONS_LOADED,
                            medicationMapper.toMedicationResponses(droneOptional.get().getMedications())
                    ), HttpStatus.OK);
                }
            }
        } catch (InvalidCodeException | InvalidNameException invalidEx) {
            log.error("Exception", invalidEx);
            return new ResponseEntity<>(new GenericResponseDto<>(
                    HttpStatus.BAD_REQUEST.value(),
                    invalidEx.getMessage(),
                    null
            ), HttpStatus.BAD_REQUEST);
        } catch (DroneNotFoundException ex) {
            log.error("Exception", ex);
            return new ResponseEntity<>(new GenericResponseDto<>(
                    HttpStatus.NOT_FOUND.value(),
                    ex.getMessage(),
                    null
            ), HttpStatus.NOT_FOUND);
        } catch (LowBatteryCapacityException | WeightLimitExceededException ex) {
            log.error("Exception", ex);
            return new ResponseEntity<>(new GenericResponseDto<>(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage(),
                    null
            ), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new GenericResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                MEDICATIONS_LOADING_FAILED,
                null
        ), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/drones/{serialNumber}/medications")
    public ResponseEntity<GenericResponseDto<List<MedicationResponse>>> getDroneMedications(@PathVariable String serialNumber) {
        log.info("Get medications of drone {} requested.", serialNumber);
        List<Medication> medications = droneService.getMedications(serialNumber);

        return new ResponseEntity<>(new GenericResponseDto<>(
                HttpStatus.OK.value(),
                MEDICATIONS_FOUND,
                medicationMapper.toMedicationResponses(medications)
        ), HttpStatus.OK);
    }

}
