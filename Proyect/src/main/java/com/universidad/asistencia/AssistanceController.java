package com.universidad.asistencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assistance")
public class AssistanceController {

    @Autowired
    private AssistanceService assistanceService;

    // Docente abre la clase
    @PostMapping("/open")
    public ResponseEntity<?> openSession(@RequestParam String subject) {
        try {
            String numberId = SecurityContextHolder.getContext().getAuthentication().getName();
            Session session = assistanceService.openSession(numberId, subject);
            return ResponseEntity.ok(session);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/active-sessions")
    public ResponseEntity<?> getActiveSessions() {
        try {
            String studentNumberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(assistanceService.getActiveSessions(studentNumberId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // Estudiante registra asistencia
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AssistanceRequest request) {
        try {
            String studentNumberId = SecurityContextHolder.getContext().getAuthentication().getName();
            Assistance assistance = assistanceService.registerAssistance(studentNumberId, request.getTeacherNumberId());
            return ResponseEntity.ok("Asistencia registrada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Docente cierra la clase
    @PutMapping("/close/{sessionId}")
    public ResponseEntity<?> closeSession(@PathVariable Long sessionId) {
        try {
            assistanceService.closeSession(sessionId);
            return ResponseEntity.ok("Clase cerrada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Estudiante ve su asistencia
    @GetMapping("/mine")
    public ResponseEntity<?> myAssistance() {
        try {
            String numberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(assistanceService.getMyAssistance(numberId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//profesor ve el listado de asistidos
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getSessionAssistance(@PathVariable Long sessionId) {
        try {
            return ResponseEntity.ok(assistanceService.getSessionAssistance(sessionId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}