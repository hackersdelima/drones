package com.shishir.drones.entity;

import com.shishir.drones.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {
    List<Drone> findAllByStateIn(List<State> states);
}
