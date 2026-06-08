package com.universidad.asistencia.Controller;

import com.universidad.asistencia.Entities.InscriptionRequest;
import com.universidad.asistencia.Service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inscription")
public class InscriptionController {

    @Autowired
    private InscriptionService inscriptionService;

    @PostMapping("/inscribe")
    public ResponseEntity<?> inscribe(@RequestBody InscriptionRequest request) {
        try {
            if (request.getStudentNumberId() == null || request.getStudentNumberId().isBlank()) {
                return ResponseEntity.badRequest().body("El studentNumberId es obligatorio.");
            }
            if (request.getSubjectId() == null || request.getSubjectId() <= 0) {
                return ResponseEntity.badRequest().body("El subjectId no es válido.");
            }

            inscriptionService.inscribeStudent(request.getStudentNumberId(), request.getSubjectId());
            return ResponseEntity.ok("Estudiante inscrito exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Estudiante ve sus inscripciones (materias en las que está inscrito)
    // GET /api/inscription/mine
    @GetMapping("/mine")
    public ResponseEntity<?> myInscriptions() {
        try {
            String studentNumberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(inscriptionService.getStudentInscriptions(studentNumberId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
