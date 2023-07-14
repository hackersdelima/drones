package com.shishir.drones;

import com.shishir.drones.entity.Drone;
import com.shishir.drones.enums.Model;
import com.shishir.drones.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.shishir.drones.enums.State.IDLE;

@Component
public class InitDataComponent {
    private final DroneService droneService;

    @Autowired
    public InitDataComponent(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostConstruct
    public void initDrones() {
        if (droneService.getAll().isEmpty()) {
            Random random = new Random();

            List<Drone> droneList = new ArrayList<>();

            for (int i = 1; i <= 10; i++) {
                Drone drone = new Drone();
                drone.setSerialNumber(UUID.randomUUID().toString());
                drone.setBatteryCapacity(Math.round(random.nextDouble() * 100));
                drone.setState(IDLE);
                drone.setWeightLimit(Math.round(random.nextDouble() * 1000));
                drone.setModel(Model.values()[random.nextInt(Model.values().length)].getValue());
                droneList.add(drone);
            }

            droneService.saveAll(droneList);
        }
    }
}
