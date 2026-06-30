package com.ankurverma.panicbutton.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class CoreTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime deadline;
    private String status; // e.g., PENDING, IN_PROGRESS, COMPLETED

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "coreTask", cascade = CascadeType.ALL)
    private List<MicroSprint> microSprints;
}