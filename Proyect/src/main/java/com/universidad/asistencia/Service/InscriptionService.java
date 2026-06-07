package com.universidad.asistencia.Service;

import com.universidad.asistencia.Entities.Inscription;
import com.universidad.asistencia.Entities.Student;
import com.universidad.asistencia.Entities.Subject;
import com.universidad.asistencia.Repositories.InscriptionRepository;
import com.universidad.asistencia.Repositories.PersonRepository;
import com.universidad.asistencia.Repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscriptionService {

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    // Inscribir estudiante a una materia
    public Inscription inscribeStudent(String studentNumberId, Long subjectId) {

        if (subjectId == null || subjectId <= 0) {
            throw new RuntimeException("El subjectId proporcionado no es válido.");
        }

        if (inscriptionRepository.existsByStudentNumberIdAndSubjectId(studentNumberId, subjectId)) {
            throw new RuntimeException("El estudiante ya está inscrito en esta materia.");
        }
        Student student = (Student) personRepository.findByNumberId(studentNumberId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado."));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada."));

        Inscription inscription = new Inscription();
        inscription.setStudent(student);
        inscription.setSubject(subject);
        return inscriptionRepository.save(inscription);
    }

    // Inscripciones de un estudiante
    public List<Inscription> getStudentInscriptions(String studentNumberId) {
        return inscriptionRepository.findByStudentNumberId(studentNumberId);
    }
}