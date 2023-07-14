package com.shishir.drones.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "medication")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @Column(name = "weight")
    private double weight;
    @Column(name = "image_url")
    private String image;

    @ManyToOne
    @JoinColumn(name = "drone_serial_number")
    @JsonIgnore
    private Drone drone;
}
