package com.universidad.asistencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssistanceService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AssistanceRepository assistanceRepository;

    @Autowired
    private PersonRepository personRepository;

    // Docente abre la clase
    public Session openSession(String numberId, String subject) {
        Teacher teacher = (Teacher) personRepository.findByNumberId(numberId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        Session session = new Session();
        session.setDocente(teacher);
        session.setMateria(subject);
        session.setFechaApertura(LocalDateTime.now());
        session.setActiva(true);

        return sessionRepository.save(session);
    }

    // Estudiante registra su asistencia
    public Assistance registerAssistance(String numberId) {
        Session session = sessionRepository.findByActivaTrue()
                .orElseThrow(() -> new RuntimeException("No hay ninguna clase abierta"));

        Student student = (Student) personRepository.findByNumberId(numberId)
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
    public void closeSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        session.setActiva(false);
        sessionRepository.save(session);
    }

    // Estudiante ve su asistencia
    public List<Assistance> getMyAssistance(String numberId) {
        return assistanceRepository.findByStudentNumberId(numberId);
    }
}