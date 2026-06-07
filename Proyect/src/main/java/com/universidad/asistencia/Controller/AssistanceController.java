package com.universidad.asistencia.Controller;

import com.universidad.asistencia.Service.AssistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/assistance")
public class AssistanceController {

    @Autowired
    private AssistanceService assistanceService;

    // Docente abre clase de una materia asignada
    // POST /api/assistance/open?subjectId=1
    @PostMapping("/open")
    public ResponseEntity<?> openSession(@RequestParam Long subjectId) {
        try {
            String numberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(assistanceService.openSession(numberId, subjectId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Docente cierra clase
    // PUT /api/assistance/close/{sessionId}
    @PutMapping("/close/{sessionId}")
    public ResponseEntity<?> closeSession(@PathVariable Long sessionId) {
        try {
            assistanceService.closeSession(sessionId);
            return ResponseEntity.ok("Sesión cerrada exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Estudiante ve sesiones activas de sus materias
    // GET /api/assistance/active-sessions
    @GetMapping("/active-sessions")
    public ResponseEntity<?> getActiveSessions() {
        try {
            String studentNumberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(assistanceService.getActiveSessions(studentNumberId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Estudiante registra asistencia en una sesión
    // POST /api/assistance/register?sessionId=3
    @PostMapping("/register")
    public ResponseEntity<?> registerAssistance(@RequestParam Long sessionId) {
        try {
            String studentNumberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(assistanceService.registerAssistance(studentNumberId, sessionId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Estudiante ve su historial
    // GET /api/assistance/my-history
    @GetMapping("/my-history")
    public ResponseEntity<?> myHistory() {
        try {
            String studentNumberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(assistanceService.getMyAssistance(studentNumberId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Docente ve asistidos de una sesión
    // GET /api/assistance/session/{sessionId}
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getSessionAssistance(@PathVariable Long sessionId) {
        try {
            return ResponseEntity.ok(assistanceService.getSessionAssistance(sessionId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Docente ve sus sesiones
    // GET /api/assistance/my-sessions
    @GetMapping("/my-sessions")
    public ResponseEntity<?> mySessions() {
        try {
            String numberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(assistanceService.getSessionsByTeacher(numberId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Docente ve sus materias asignadas
    // GET /api/assistance/my-subjects
    @GetMapping("/my-subjects")
    public ResponseEntity<?> mySubjects() {
        try {
            String numberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(assistanceService.getTeacherSubjects(numberId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Docente ve su historial de sesiones, con filtro opcional por fechas
// GET /api/assistance/session-history?desde=2026-06-01T00:00:00&hasta=2026-06-30T23:59:59
    @GetMapping("/session-history")
    public ResponseEntity<?> sessionHistory(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        try {
            String numberId = SecurityContextHolder.getContext().getAuthentication().getName();

            LocalDateTime desdeDate = (desde != null) ? LocalDateTime.parse(desde) : null;
            LocalDateTime hastaDate = (hasta != null) ? LocalDateTime.parse(hasta) : null;

            return ResponseEntity.ok(
                    assistanceService.getTeacherSessionHistory(numberId, desdeDate, hastaDate)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Docente ve el detalle de asistidos de una sesión
// GET /api/assistance/session-detail/{sessionId}
    @GetMapping("/session-detail/{sessionId}")
    public ResponseEntity<?> sessionDetail(@PathVariable Long sessionId) {
        try {
            return ResponseEntity.ok(assistanceService.getSessionDetail(sessionId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}