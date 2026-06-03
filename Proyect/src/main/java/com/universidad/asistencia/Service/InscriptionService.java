package com.universidad.asistencia.Service;

import com.universidad.asistencia.Entities.Inscription;
import com.universidad.asistencia.Entities.Student;
import com.universidad.asistencia.Entities.Teacher;
import com.universidad.asistencia.Repositories.InscriptionRepository;
import com.universidad.asistencia.Repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InscriptionService {

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private PersonRepository personRepository;

    public Inscription inscribeStudent(String studentNumberId, String teacherNumberId) {
        Student student = (Student) personRepository.findByNumberId(studentNumberId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        Teacher teacher = (Teacher) personRepository.findByNumberId(teacherNumberId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        Inscription inscription = new Inscription();
        inscription.setStudent(student);
        inscription.setTeacher(teacher);

        return inscriptionRepository.save(inscription);
    }

    public List<Inscription> getStudentInscriptions(String studentNumberId) {
        return inscriptionRepository.findByStudentNumberId(studentNumberId);
    }
}
