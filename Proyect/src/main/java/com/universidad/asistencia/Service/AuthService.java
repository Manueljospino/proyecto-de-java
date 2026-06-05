package com.universidad.asistencia.Service;

import com.universidad.asistencia.Entities.Person;
import com.universidad.asistencia.Entities.RegisterRequest;
import com.universidad.asistencia.Entities.Student;
import com.universidad.asistencia.Entities.Teacher;
import com.universidad.asistencia.Utils.JwtUtil;
import com.universidad.asistencia.Repositories.PersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public void deleteUser(String numberId) {
        Person person = PersonRepository.findByNumberId(numberId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
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
                "SELECT id, name, mail, role, number_id FROM Usuarios"
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