package com.universidad.asistencia.Service;

import com.universidad.asistencia.Entities.Person;
import com.universidad.asistencia.Entities.RegisterRequest;
import com.universidad.asistencia.Entities.Student;
import com.universidad.asistencia.Entities.Teacher;
import com.universidad.asistencia.Utils.JwtUtil;
import com.universidad.asistencia.Repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String numberId, String password) {
        Person person = personRepository.findByNumberId(numberId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, person.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtUtil.generateToken(person);
    }

    public Person register(RegisterRequest request) {
        if (personRepository.findByNumberId(request.getNumberId()).isPresent()) {
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

        return personRepository.save(person);
    }
    public void resetPassword(String numberId, String newPassword) {
        Person person = personRepository.findByNumberId(numberId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        person.setPassword(passwordEncoder.encode(newPassword));
        personRepository.save(person);
    }
    public void changePassword(String numberId, String currentPassword, String newPassword) {
        Person person = personRepository.findByNumberId(numberId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(currentPassword, person.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        person.setPassword(passwordEncoder.encode(newPassword));
        personRepository.save(person);
    }

}