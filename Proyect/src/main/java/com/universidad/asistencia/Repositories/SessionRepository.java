package com.universidad.asistencia.Repositories;

import com.universidad.asistencia.Entities.Session;
import com.universidad.asistencia.Entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByDocenteNumberIdAndActivaTrue(String numberId);
    List<Session> findByActivaTrue();
    List<Session> findByDocente(Teacher docente);
    List<Session> findBySubjectId(Long subjectId);
    List<Session> findBySubjectIdAndActivaTrue(Long subjectId);
    List<Session> findByDocenteNumberId(String numberId);
    List<Session> findByDocenteNumberIdAndFechaAperturaBetween(
            String numberId, LocalDateTime desde, LocalDateTime hasta
    );
}