package com.universidad.asistencia.Service;

import com.universidad.asistencia.Entities.Subject;
import com.universidad.asistencia.Repositories.InscriptionRepository;
import com.universidad.asistencia.Repositories.SessionRepository;
import com.universidad.asistencia.Repositories.SubjectRepository;
import com.universidad.asistencia.Repositories.TeacherSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherSubjectRepository teacherSubjectRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public Subject create(String name, String description) {
        if (subjectRepository.existsByName(name.trim())) {
            throw new RuntimeException("Ya existe una materia con ese nombre.");
        }
        Subject subject = new Subject();
        subject.setName(name.trim());
        subject.setDescription(description != null ? description.trim() : "");
        return subjectRepository.save(subject);
    }

    public Subject update(Long id, String name, String description) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada."));
        subjectRepository.findByName(name.trim()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new RuntimeException("Ya existe otra materia con ese nombre.");
            }
        });
        subject.setName(name.trim());
        if (description != null) subject.setDescription(description.trim());
        return subjectRepository.save(subject);
    }

    @Transactional
    public void delete(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new RuntimeException("Materia no encontrada.");
        }
        // Eliminar inscripciones de estudiantes a esta materia
        inscriptionRepository.deleteAll(inscriptionRepository.findBySubjectId(id));
        // Eliminar asignaciones docente-materia
        teacherSubjectRepository.deleteAll(teacherSubjectRepository.findBySubjectId(id));
        // Eliminar sesiones de esta materia (y sus asistencias en cascada)
        sessionRepository.deleteAll(sessionRepository.findBySubjectId(id));
        // Eliminar la materia
        subjectRepository.deleteById(id);
    }
}