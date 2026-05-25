package com.universidad.asistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssistanceRepository extends JpaRepository<Assistance, Long> {
    List<Assistance> findByStudentNumberId(String numberId);
    List<Assistance> findBySessionId(Long sessionId);
}