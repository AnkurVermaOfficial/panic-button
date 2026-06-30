package com.ankurverma.panicbutton.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "users") // 'user' is often a reserved keyword in SQL
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String username;
        private String email;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
        private List<CoreTask> coreTasks;
}