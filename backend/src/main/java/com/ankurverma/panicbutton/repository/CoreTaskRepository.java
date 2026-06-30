package com.ankurverma.panicbutton.repository;

import com.ankurverma.panicbutton.entity.CoreTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoreTaskRepository extends JpaRepository<CoreTask, Long> {
}