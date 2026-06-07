package com.universidad.asistencia.Service;

import com.universidad.asistencia.Entities.Subject;
import com.universidad.asistencia.Entities.Teacher;
import com.universidad.asistencia.Entities.TeacherSubject;
import com.universidad.asistencia.Repositories.PersonRepository;
import com.universidad.asistencia.Repositories.SubjectRepository;
import com.universidad.asistencia.Repositories.TeacherSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherSubjectService {

    @Autowired
    private TeacherSubjectRepository teacherSubjectRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    // Asignar materia a docente
    public TeacherSubject assign(String teacherNumberId, Long subjectId) {
        if (teacherSubjectRepository.existsByTeacherNumberIdAndSubjectId(teacherNumberId, subjectId)) {
            throw new RuntimeException("Esta materia ya está asignada a este docente.");
        }
        Teacher teacher = (Teacher) personRepository.findByNumberId(teacherNumberId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado."));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada."));

        TeacherSubject ts = new TeacherSubject();
        ts.setTeacher(teacher);
        ts.setSubject(subject);
        return teacherSubjectRepository.save(ts);
    }

    // Desasignar materia de docente
    @Transactional
    public void unassign(String teacherNumberId, Long subjectId) {
        if (!teacherSubjectRepository.existsByTeacherNumberIdAndSubjectId(teacherNumberId, subjectId)) {
            throw new RuntimeException("Esta asignación no existe.");
        }
        teacherSubjectRepository.deleteByTeacherNumberIdAndSubjectId(teacherNumberId, subjectId);
    }

    // Materias de un docente
    public List<TeacherSubject> getByTeacher(String teacherNumberId) {
        return teacherSubjectRepository.findByTeacherNumberId(teacherNumberId);
    }

    // Docentes de una materia
    public List<TeacherSubject> getBySubject(Long subjectId) {
        return teacherSubjectRepository.findBySubjectId(subjectId);
    }
}