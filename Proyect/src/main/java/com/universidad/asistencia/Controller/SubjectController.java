package com.universidad.asistencia.Controller;

import com.universidad.asistencia.Entities.Subject;
import com.universidad.asistencia.Entities.TeacherSubject;
import com.universidad.asistencia.Repositories.TeacherSubjectRepository;
import com.universidad.asistencia.Service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherSubjectRepository teacherSubjectRepository;

    // GET /api/subjects — listar todas (Admin y Docente)
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(subjectService.getAll());
    }

    // GET /api/subjects/with-teachers — materias con sus docentes asignados
    @GetMapping("/with-teachers")
    public ResponseEntity<?> getAllWithTeachers() {
        List<Subject> subjects = subjectService.getAll();
        List<Map<String, Object>> result = subjects.stream().map(subject -> {
            List<TeacherSubject> asignaciones = teacherSubjectRepository.findBySubjectId(subject.getId());
            List<Map<String, String>> teachers = asignaciones.stream()
                    .filter(ts -> ts.getTeacher() != null)
                    .map(ts -> {
                        Map<String, String> td = new LinkedHashMap<>();
                        td.put("name", ts.getTeacher().getName());
                        td.put("numberId", ts.getTeacher().getNumberId());
                        td.put("department", ts.getTeacher().getDepartment());
                        return td;
                    }).collect(Collectors.toList());
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", subject.getId());
            row.put("name", subject.getName());
            row.put("description", subject.getDescription());
            row.put("teachers", teachers);
            return row;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // POST /api/subjects — crear (solo Admin)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, String> body) {
        try {
            String name = body.get("name");
            String description = body.get("description");
            if (name == null || name.isBlank()) {
                return ResponseEntity.badRequest().body("El nombre es obligatorio.");
            }
            Subject subject = subjectService.create(name, description);
            return ResponseEntity.ok(subject);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /api/subjects/{id} — editar (solo Admin)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String name = body.get("name");
            String description = body.get("description");
            if (name == null || name.isBlank()) {
                return ResponseEntity.badRequest().body("El nombre es obligatorio.");
            }
            Subject subject = subjectService.update(id, name, description);
            return ResponseEntity.ok(subject);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /api/subjects/{id} — eliminar (solo Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            subjectService.delete(id);
            return ResponseEntity.ok("Materia eliminada.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}