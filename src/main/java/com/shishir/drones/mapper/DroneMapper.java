package com.shishir.drones.mapper;

import com.shishir.drones.dto.DroneRegisterRequest;
import com.shishir.drones.dto.DroneRegisterResponse;
import com.shishir.drones.entity.Drone;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DroneMapper {
    public Drone toDrone(DroneRegisterRequest droneRegisterRequest){
        Drone drone = new Drone();
        BeanUtils.copyProperties(droneRegisterRequest, drone);
        return drone;
    }

    public DroneRegisterResponse toDroneRegisterResponse(Drone drone){
        DroneRegisterResponse droneRegisterResponse = new DroneRegisterResponse();
        BeanUtils.copyProperties(drone, droneRegisterResponse);
        return droneRegisterResponse;
    }
}
