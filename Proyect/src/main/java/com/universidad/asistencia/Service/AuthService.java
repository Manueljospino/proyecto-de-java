package com.universidad.asistencia.Service;

import com.universidad.asistencia.Entities.Person;
import com.universidad.asistencia.Entities.RegisterRequest;
import com.universidad.asistencia.Entities.Session;
import com.universidad.asistencia.Entities.Student;
import com.universidad.asistencia.Entities.Teacher;
import com.universidad.asistencia.Entities.TeacherSubject;
import com.universidad.asistencia.Entities.Admin;
import com.universidad.asistencia.Entities.SubAdmin;
import com.universidad.asistencia.Utils.JwtUtil;
import com.universidad.asistencia.Repositories.PersonRepository;
import com.universidad.asistencia.Repositories.SessionRepository;
import com.universidad.asistencia.Repositories.TeacherSubjectRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AuthService {

    @Autowired
    private PersonRepository PersonRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherSubjectRepository teacherSubjectRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String numberId, String password) {
        Person person = PersonRepository.findByNumberId(numberId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, person.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtUtil.generateToken(person);
    }

    public Person register(RegisterRequest request) {
        // Validar campos obligatorios
        if (request.getName() == null || request.getName().isBlank() ||
                request.getMail() == null || request.getMail().isBlank() ||
                request.getNumberId() == null || request.getNumberId().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Todos los campos obligatorios deben estar completos");
        }

        for (char c : request.getName().toCharArray()) {
            if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                throw new RuntimeException("El nombre solo debe contener letras");
            }
        }

        for (char c : request.getNumberId().toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new RuntimeException("El número de ID solo debe contener dígitos");
            }
        }

        if (!request.getMail().endsWith("@unicesar.edu.co")) {
            throw new RuntimeException("El correo debe ser del dominio @unicesar.edu.co");
        }

        if (request.getPassword().length() < 8) {
            throw new RuntimeException("La contraseña debe tener al menos 8 caracteres");
        }

        if (PersonRepository.findByNumberId(request.getNumberId()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con ese número de identificación");
        }

        Person person;

        if (request.getRole().equals("ESTUDIANTE")) {
            Student student = new Student();
            student.setName(request.getName());
            student.setMail(request.getMail());
            student.setPassword(passwordEncoder.encode(request.getPassword()));
            student.setNumberId(request.getNumberId());
            student.setSemester(request.getSemester());
            student.setProgram(request.getProgram());
            student.setSubject(request.getSubject());
            person = student;

        } else if (request.getRole().equals("DOCENTE")) {
            Teacher teacher = new Teacher();
            teacher.setName(request.getName());
            teacher.setMail(request.getMail());
            teacher.setPassword(passwordEncoder.encode(request.getPassword()));
            teacher.setNumberId(request.getNumberId());
            teacher.setDepartment(request.getDepartment());
            teacher.setSubject(request.getSubject());
            person = teacher;

        } else if (request.getRole().equals("ADMIN")) {
            Admin admin = new Admin();
            admin.setName(request.getName());
            admin.setMail(request.getMail());
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setNumberId(request.getNumberId());
            person = admin;

        } else if (request.getRole().equals("SUBADMIN")) {
            SubAdmin subAdmin = new SubAdmin();
            subAdmin.setName(request.getName());
            subAdmin.setMail(request.getMail());
            subAdmin.setPassword(passwordEncoder.encode(request.getPassword()));
            subAdmin.setNumberId(request.getNumberId());
            person = subAdmin;

        } else {
            throw new RuntimeException("Rol no válido");
        }

        return PersonRepository.save(person);
    }

    public void resetPassword(String numberId, String newPassword) {
        Person person = PersonRepository.findByNumberId(numberId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        person.setPassword(passwordEncoder.encode(newPassword));
        PersonRepository.save(person);
    }

    public void changePassword(String numberId, String currentPassword, String newPassword) {
        Person person = PersonRepository.findByNumberId(numberId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(currentPassword, person.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        person.setPassword(passwordEncoder.encode(newPassword));
        PersonRepository.save(person);
    }

    @Transactional
    public void deleteUser(String numberId) {
        Person person = PersonRepository.findByNumberId(numberId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (person instanceof Teacher teacher) {
            // Eliminar asignaciones docente-materia
            List<TeacherSubject> asignaciones = teacherSubjectRepository.findByTeacherNumberId(numberId);
            teacherSubjectRepository.deleteAll(asignaciones);

            // Eliminar sesiones (y sus asistencias en cascada)
            List<Session> sesiones = sessionRepository.findByDocente(teacher);
            sessionRepository.deleteAll(sesiones);
        }

        PersonRepository.delete(person);
    }

    public Person findUser(String numberId) {
        return PersonRepository.findByNumberId(numberId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<Map<String, Object>> getAllUsers() {
        List<Object[]> rows = entityManager.createNativeQuery(
                "SELECT id, name, mail, role, number_id FROM usuarios"
        ).getResultList();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> user = new HashMap<>();
            user.put("id",       row[0]);
            user.put("name",     row[1]);
            user.put("mail",     row[2]);
            user.put("role",     row[3]);
            user.put("numberId", row[4]);
            result.add(user);
        }
        return result;
    }
}