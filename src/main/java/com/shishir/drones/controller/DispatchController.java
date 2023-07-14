package com.shishir.drones.controller;

import com.shishir.drones.dto.DroneRegisterRequest;
import com.shishir.drones.dto.DroneRegisterResponse;
import com.shishir.drones.dto.GenericResponseDto;
import com.shishir.drones.entity.Drone;
import com.shishir.drones.mapper.DroneMapper;
import com.shishir.drones.service.DroneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.shishir.drones.constants.MessageConstants.DRONE_REGISTERED;
import static com.shishir.drones.constants.MessageConstants.DRONE_REGISTRATION_FAILED;

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
    public ResponseEntity<GenericResponseDto<DroneRegisterResponse>> registerDrone(@RequestBody DroneRegisterRequest request) {
        log.info("Register drone requested.");
        Optional<Drone> registeredDrone = droneService.save(
                droneMapper.toDrone(request)
        );

        return registeredDrone.map(drone -> new ResponseEntity<>(new GenericResponseDto<>(
                HttpStatus.OK.value(),
                DRONE_REGISTERED,
                droneMapper.toDroneRegisterResponse(drone)
        ), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new GenericResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                DRONE_REGISTRATION_FAILED,
                null
        ), HttpStatus.BAD_REQUEST));
    }

}
