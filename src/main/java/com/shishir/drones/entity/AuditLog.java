package com.shishir.drones.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "key")
    private String key;

    @Column(name = "user")
    private String user;

    @Column(name = "details")
    private String details;

    @Column(name = "status")
    private String status;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
        this.user = "system";
    }
}

