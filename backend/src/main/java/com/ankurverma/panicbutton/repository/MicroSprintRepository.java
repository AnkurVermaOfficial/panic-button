package com.ankurverma.panicbutton.repository;

import com.ankurverma.panicbutton.entity.MicroSprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MicroSprintRepository extends JpaRepository<MicroSprint, Long> {
}
