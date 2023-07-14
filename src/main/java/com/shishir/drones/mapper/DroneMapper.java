package com.shishir.drones.mapper;

import com.shishir.drones.dto.DroneRequest;
import com.shishir.drones.dto.DroneResponse;
import com.shishir.drones.entity.Drone;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DroneMapper {
    public Drone toDrone(DroneRequest droneRequest){
        Drone drone = new Drone();
        BeanUtils.copyProperties(droneRequest, drone);
        return drone;
    }

    public DroneResponse toDroneResponse(Drone drone){
        DroneResponse droneResponse = new DroneResponse();
        BeanUtils.copyProperties(drone, droneResponse);
        return droneResponse;
    }

    public List<DroneResponse> toDroneResponses(List<Drone> drones){
        List<DroneResponse> droneResponses = new ArrayList<>();
        drones.forEach(drone -> droneResponses.add(this.toDroneResponse(drone)));

        return droneResponses;
    }
}
