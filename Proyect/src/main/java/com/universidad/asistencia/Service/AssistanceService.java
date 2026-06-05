package com.universidad.asistencia.Service;

import com.universidad.asistencia.Entities.*;
import com.universidad.asistencia.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssistanceService {


    @Autowired
    private InscriptionRepository inscriptionRepository;
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AssistanceRepository assistanceRepository;

    @Autowired
    private PersonRepository personRepository;

    public List<Session> getActiveSessions(String studentNumberId) {
        List<Inscription> inscriptions = inscriptionRepository.findByStudentNumberId(studentNumberId);

        return inscriptions.stream()
                .map(inscription -> sessionRepository
                        .findByDocenteNumberIdAndActivaTrue(inscription.getTeacher().getNumberId())
                        .orElse(null))
                .filter(session -> session != null)
                .toList();
    }

    // Docente abre la clase
    public Session openSession(String numberId, String subject) {
        Teacher teacher = (Teacher) personRepository.findByNumberId(numberId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        // Verificar si ya tiene una sesión activa
        if (sessionRepository.findByDocenteNumberIdAndActivaTrue(numberId).isPresent()) {
            throw new RuntimeException("Ya tienes una clase abierta");
        }

        Session session = new Session();
        session.setDocente(teacher);
        session.setMateria(subject);
        session.setFechaApertura(LocalDateTime.now());
        session.setActiva(true);

        return sessionRepository.save(session);
    }

    // Estudiante registra su asistencia
    public Assistance registerAssistance(String studentNumberId, String teacherNumberId) {
        Session session = sessionRepository.findByDocenteNumberIdAndActivaTrue(teacherNumberId)
                .orElseThrow(() -> new RuntimeException("No hay ninguna clase abierta para ese docente"));

        Student student = (Student) personRepository.findByNumberId(studentNumberId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        Assistance assistance = new Assistance();
        assistance.setStudent(student);
        assistance.setTeacher(session.getDocente());
        assistance.setSession(session);
        assistance.setDate(LocalDate.now());
        assistance.setState("PRESENT");

        return assistanceRepository.save(assistance);
    }

    // Docente cierra la clase
    @Autowired
    private StudentRepository studentRepository;

    public void closeSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        // Buscar estudiantes que no registraron asistencia
        List<Student> allStudents = studentRepository.findAll();
        List<String> presentStudents = session.getAssistances().stream()
                .map(a -> a.getStudent().getNumberId())
                .toList();

        for (Student student : allStudents) {
            if (!presentStudents.contains(student.getNumberId())) {
                Assistance absence = new Assistance();
                absence.setStudent(student);
                absence.setTeacher(session.getDocente());
                absence.setSession(session);
                absence.setDate(LocalDate.now());
                absence.setState("Ausente");
                assistanceRepository.save(absence);
            }
        }

        session.setActiva(false);
        sessionRepository.save(session);
    }

    // Estudiante ve su asistencia
    public List<Assistance> getMyAssistance(String numberId) {
        return assistanceRepository.findByStudentNumberId(numberId);
    }
// profesor ve la lista de estudiantes
    public List<Assistance> getSessionAssistance(Long sessionId) {
        return assistanceRepository.findBySessionId(sessionId);
    }

    public List<Session> getSessionsByTeacher(String teacherNumberId) {
        Teacher docente = (Teacher) personRepository.findByNumberId(teacherNumberId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        return sessionRepository.findByDocente(docente);
    }

}