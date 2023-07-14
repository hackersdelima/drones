package com.shishir.drones.controller;

import com.shishir.drones.dto.*;
import com.shishir.drones.entity.Drone;
import com.shishir.drones.entity.Medication;
import com.shishir.drones.mapper.DroneMapper;
import com.shishir.drones.mapper.MedicationMapper;
import com.shishir.drones.service.DroneService;
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

    @Autowired
    public DispatchController(DroneService droneService, DroneMapper droneMapper, MedicationMapper medicationMapper) {
        this.droneService = droneService;
        this.droneMapper = droneMapper;
        this.medicationMapper = medicationMapper;
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
        List<Drone> availableDrones = droneService.getAvailableDrones();

        return new ResponseEntity<>(new GenericResponseDto<>(
                HttpStatus.OK.value(),
                AVAILABLE_DRONES_FOUND,
                droneMapper.toDroneResponses(availableDrones)
        ), HttpStatus.OK);
    }

    @GetMapping("/drones/{serialNumber}/battery-level")
    public ResponseEntity<GenericResponseDto<Double>> getDroneBatteryLevel(@PathVariable String serialNumber) {
        try {
            double batteryLevel = droneService.getBatteryLevel(serialNumber);

            return new ResponseEntity<>(new GenericResponseDto<>(
                    HttpStatus.OK.value(),
                    DRONE_BATTERY_LEVEL_FOUND,
                    batteryLevel
            ), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception in get drone battery level", ex);
            return new ResponseEntity<>(new GenericResponseDto<>(
                    HttpStatus.NOT_FOUND.value(),
                    DRONE_NOT_FOUND,
                    null
            ), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/drones/{serialNumber}/medications")
    public ResponseEntity<GenericResponseDto<List<MedicationResponse>>> loadMedications(@PathVariable String serialNumber, @RequestBody List<MedicationRequest> request) {
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

        return new ResponseEntity<>(new GenericResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                MEDICATIONS_LOADING_FAILED,
                null
        ), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/drones/{serialNumber}/medications")
    public ResponseEntity<GenericResponseDto<List<MedicationResponse>>> getDroneMedications(@PathVariable String serialNumber) {
        List<Medication> medications = droneService.getMedications(serialNumber);

        return new ResponseEntity<>(new GenericResponseDto<>(
                HttpStatus.OK.value(),
                MEDICATIONS_FOUND,
                medicationMapper.toMedicationResponses(medications)
        ), HttpStatus.OK);
    }

}
