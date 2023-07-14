package com.shishir.drones.tasks;

import com.shishir.drones.entity.AuditLog;
import com.shishir.drones.entity.AuditLogRepository;
import com.shishir.drones.entity.Drone;
import com.shishir.drones.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DroneBatteryLevelAuditTask {
    private final AuditLogRepository auditLogRepository;
    private final DroneService droneService;

    @Autowired
    public DroneBatteryLevelAuditTask(AuditLogRepository auditLogRepository, DroneService droneService) {
        this.auditLogRepository = auditLogRepository;
        this.droneService = droneService;
    }

    @Scheduled(fixedDelayString = "${drone.log.battery.level.timer}")
    public void checkBatteryLevels() {
        List<Drone> drones = droneService.getAll();
        drones.forEach(drone -> {
            double batteryLevel = getBatteryLevelFromDrone(drone);
            this.createBatteryLog(drone, batteryLevel);
        });
    }

    private double getBatteryLevelFromDrone(Drone drone) {
        return drone.getBatteryCapacity();
    }

    private void createBatteryLog(Drone drone, double batteryLevel) {
        AuditLog auditLog = new AuditLog();
        auditLog.setKey(drone.getSerialNumber());
        auditLog.setDetails(String.valueOf(batteryLevel));
        auditLog.setStatus("BATTERY_LEVEL");
        auditLogRepository.save(auditLog);
    }
}
