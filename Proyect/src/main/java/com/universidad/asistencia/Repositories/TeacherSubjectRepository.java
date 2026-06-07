package com.universidad.asistencia.Repositories;

import com.universidad.asistencia.Entities.TeacherSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
    List<TeacherSubject> findByTeacherNumberId(String teacherNumberId);
    List<TeacherSubject> findBySubjectId(Long subjectId);
    boolean existsByTeacherNumberIdAndSubjectId(String teacherNumberId, Long subjectId);
    void deleteByTeacherNumberIdAndSubjectId(String teacherNumberId, Long subjectId);
}