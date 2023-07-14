package com.shishir.drones.controller;

import com.shishir.drones.dto.DroneRequest;
import com.shishir.drones.dto.DroneResponse;
import com.shishir.drones.dto.GenericResponseDto;
import com.shishir.drones.entity.Drone;
import com.shishir.drones.mapper.DroneMapper;
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

    @Autowired
    public DispatchController(DroneService droneService, DroneMapper droneMapper) {
        this.droneService = droneService;
        this.droneMapper = droneMapper;
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

}
