package com.universidad.asistencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inscription")

public class InscriptionController {
    @Autowired
    private InscriptionService inscriptionService;

    @PostMapping("/inscribe")
    public ResponseEntity<?> inscribe(@RequestBody InscriptionRequest request) {
        try {
            Inscription inscription = inscriptionService.inscribeStudent(
                    request.getStudentNumberId(), request.getTeacherNumberId()
            );
            return ResponseEntity.ok("Estudiante inscrito exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mine")
    public ResponseEntity<?> myInscriptions() {
        try {
            String studentNumberId = org.springframework.security.core.context
                    .SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(inscriptionService.getStudentInscriptions(studentNumberId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
