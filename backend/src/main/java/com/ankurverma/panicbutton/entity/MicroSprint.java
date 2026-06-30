package com.ankurverma.panicbutton.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class MicroSprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @JsonProperty("duration_mins")
    private Integer durationMins;
    private Integer sequence;
    @JsonProperty("action_url")
    private String actionUrl; // Optional link to docs, tools, etc.

    @ManyToOne
    @JoinColumn(name = "task_id")
    private CoreTask coreTask;
}