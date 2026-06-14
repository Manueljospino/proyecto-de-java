package com.universidad.asistencia.Service;

import com.universidad.asistencia.Entities.*;
import com.universidad.asistencia.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssistanceService {

    @Autowired private InscriptionRepository inscriptionRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private AssistanceRepository assistanceRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private SubjectRepository subjectRepository;
    @Autowired private TeacherSubjectRepository teacherSubjectRepository;


    public List<Session> getActiveSessions(String studentNumberId) {
        List<Inscription> inscriptions = inscriptionRepository.findByStudentNumberId(studentNumberId);
        return inscriptions.stream()
                .filter(i -> i.getSubject() != null && i.getSubject().getId() != null)
                .flatMap(i -> sessionRepository.findBySubjectIdAndActivaTrue(i.getSubject().getId()).stream())
                .toList();
    }


    public Session openSession(String teacherNumberId, Long subjectId) {
        Teacher teacher = (Teacher) personRepository.findByNumberId(teacherNumberId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado."));

        if (!teacherSubjectRepository.existsByTeacherNumberIdAndSubjectId(teacherNumberId, subjectId)) {
            throw new RuntimeException("No tienes asignada esa materia.");
        }

        if (sessionRepository.findByDocenteNumberIdAndActivaTrue(teacherNumberId).isPresent()) {
            throw new RuntimeException("Ya tienes una clase abierta.");
        }

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada."));

        Session session = new Session();
        session.setTeacher(teacher);
        session.setSubject(subject);
        session.setLocalDateTime(LocalDateTime.now());
        session.setActive(true);
        return sessionRepository.save(session);
    }

    // Estudiante registra asistencia en una sesión activa de su materia
    public Assistance registerAssistance(String studentNumberId, Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));

        if (!session.isActive()) {
            throw new RuntimeException("La sesión ya está cerrada.");
        }

        if (!inscriptionRepository.existsByStudentNumberIdAndSubjectId(
                studentNumberId, session.getSubject().getId())) {
            throw new RuntimeException("No estás inscrito en esta materia.");
        }

        boolean yaRegistrado = assistanceRepository.findBySessionId(session.getId())
                .stream().anyMatch(a -> a.getStudent().getNumberId().equals(studentNumberId));
        if (yaRegistrado) {
            throw new RuntimeException("Ya registraste tu asistencia en esta sesión.");
        }

        Student student = (Student) personRepository.findByNumberId(studentNumberId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado."));

        Assistance assistance = new Assistance();
        assistance.setStudent(student);
        assistance.setTeacher(session.getTeacher());
        assistance.setSession(session);
        assistance.setDate(LocalDateTime.now()); // ← fecha + hora exacta
        assistance.setState("Presente");
        return assistanceRepository.save(assistance);
    }

    // Docente cierra clase — marca ausentes solo a inscritos en esa materia
    public void closeSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));

        List<Inscription> inscriptions = inscriptionRepository.findBySubjectId(session.getSubject().getId());
        List<String> presentIds = session.getAssistances().stream()
                .map(a -> a.getStudent().getNumberId())
                .toList();

        for (Inscription inscription : inscriptions) {
            String sid = inscription.getStudent().getNumberId();
            if (!presentIds.contains(sid)) {
                Assistance absence = new Assistance();
                absence.setStudent(inscription.getStudent());
                absence.setTeacher(session.getTeacher());
                absence.setSession(session);
                absence.setDate(LocalDateTime.now()); // ← fecha + hora del cierre
                absence.setState("Ausente");
                assistanceRepository.save(absence);
            }
        }

        session.setActive(false);
        sessionRepository.save(session);
    }


    public List<Assistance> getMyAssistance(String studentNumberId) {
        return assistanceRepository.findByStudentNumberId(studentNumberId);
    }


    public List<Assistance> getSessionAssistance(Long sessionId) {
        return assistanceRepository.findBySessionId(sessionId);
    }


    public List<Session> getSessionsByTeacher(String teacherNumberId) {
        Teacher teacher = (Teacher) personRepository.findByNumberId(teacherNumberId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado."));
        return sessionRepository.findByDocente(teacher);
    }


    public List<TeacherSubject> getTeacherSubjects(String teacherNumberId) {
        return teacherSubjectRepository.findByTeacherNumberId(teacherNumberId);
    }

    // Docente ve su historial de sesiones (con filtro opcional de fechas)
    public List<Session> getTeacherSessionHistory(String teacherNumberId,
                                                  LocalDateTime desde,
                                                  LocalDateTime hasta) {
        if (desde != null && hasta != null) {
            return sessionRepository.findByDocenteNumberIdAndFechaAperturaBetween(
                    teacherNumberId, desde, hasta);
        }
        return sessionRepository.findByDocenteNumberId(teacherNumberId);
    }
    // Docente ve el listado de asistidos de una sesión específica
    public List<Assistance> getSessionDetail(Long sessionId) {
        return assistanceRepository.findBySessionId(sessionId);
    }
}