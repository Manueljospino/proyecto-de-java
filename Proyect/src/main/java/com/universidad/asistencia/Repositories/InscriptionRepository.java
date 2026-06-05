package com.universidad.asistencia.Repositories;

import com.universidad.asistencia.Entities.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByStudentNumberId(String numberId);
    boolean existsByStudentNumberIdAndTeacherNumberId(String studentNumberId, String teacherNumberId);
}