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
    Optional<Session> findByTeacherNumberIdAndActiveTrue(String numberId);
    List<Session> findByActiveTrue();
    List<Session> findByTeacher(Teacher teacher);
    List<Session> findBySubjectId(Long subjectId);
    List<Session> findBySubjectIdAndActiveTrue(Long subjectId);
    List<Session> findByTeacherNumberId(String numberId);
    List<Session> findByTeacherNumberIdAndLocalDateTimeBetween(
            String numberId, LocalDateTime from, LocalDateTime to
    );
}