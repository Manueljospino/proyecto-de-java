package com.universidad.asistencia.Controller;

import com.universidad.asistencia.Service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inscription")
public class InscriptionController {

    @Autowired
    private InscriptionService inscriptionService;

    @PostMapping("/inscribe")
    public ResponseEntity<?> inscribe(@RequestBody Map<String, Object> body) {
        try {
            String studentNumberId = (String) body.get("studentNumberId");
            Object subjectIdObj = body.get("subjectId");

            // ✅ VALIDACIÓN NUEVA
            if (studentNumberId == null || studentNumberId.isBlank()) {
                return ResponseEntity.badRequest().body("El studentNumberId es obligatorio.");
            }
            if (subjectIdObj == null) {
                return ResponseEntity.badRequest().body("El subjectId es obligatorio.");
            }

            Long subjectId = Long.valueOf(subjectIdObj.toString());

            if (subjectId <= 0) {
                return ResponseEntity.badRequest().body("El subjectId no es válido.");
            }

            inscriptionService.inscribeStudent(studentNumberId, subjectId);
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