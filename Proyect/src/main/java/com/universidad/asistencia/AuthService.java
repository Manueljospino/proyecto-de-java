package com.universidad.asistencia;

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

        Person person = new Person();
        person.setName(request.getName());
        person.setMail(request.getMail());
        person.setPassword(passwordEncoder.encode(request.getPassword()));
        person.setRole(request.getRole());
        person.setNumberId(request.getNumberId());

        return personRepository.save(person);
    }
}