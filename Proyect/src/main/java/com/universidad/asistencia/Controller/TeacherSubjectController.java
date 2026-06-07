package com.universidad.asistencia.Controller;

import com.universidad.asistencia.Service.TeacherSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/teacher-subjects")
public class TeacherSubjectController {

    @Autowired
    private TeacherSubjectService teacherSubjectService;

    // Admin asigna materia a docente
    // POST /api/teacher-subjects/assign
    @PostMapping("/assign")
    public ResponseEntity<?> assign(@RequestBody Map<String, Object> body) {
        try {
            String teacherNumberId = (String) body.get("teacherNumberId");
            Long subjectId = Long.valueOf(body.get("subjectId").toString());
            return ResponseEntity.ok(teacherSubjectService.assign(teacherNumberId, subjectId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Admin desasigna materia de docente
    // DELETE /api/teacher-subjects/unassign
    @DeleteMapping("/unassign")
    public ResponseEntity<?> unassign(@RequestBody Map<String, Object> body) {
        try {
            String teacherNumberId = (String) body.get("teacherNumberId");
            Long subjectId = Long.valueOf(body.get("subjectId").toString());
            teacherSubjectService.unassign(teacherNumberId, subjectId);
            return ResponseEntity.ok("Materia desasignada.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Docente ve sus materias asignadas
    // GET /api/teacher-subjects/my-subjects
    @GetMapping("/my-subjects")
    public ResponseEntity<?> mySubjects() {
        try {
            String numberId = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(teacherSubjectService.getByTeacher(numberId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Admin ve materias de un docente
    // GET /api/teacher-subjects/by-teacher/{teacherNumberId}
    @GetMapping("/by-teacher/{teacherNumberId}")
    public ResponseEntity<?> byTeacher(@PathVariable String teacherNumberId) {
        try {
            return ResponseEntity.ok(teacherSubjectService.getByTeacher(teacherNumberId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Admin ve docentes de una materia
    // GET /api/teacher-subjects/by-subject/{subjectId}
    @GetMapping("/by-subject/{subjectId}")
    public ResponseEntity<?> bySubject(@PathVariable Long subjectId) {
        try {
            return ResponseEntity.ok(teacherSubjectService.getBySubject(subjectId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}