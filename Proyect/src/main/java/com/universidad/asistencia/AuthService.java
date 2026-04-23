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

    public Person login(String numberId, String password) {
        Person person = personRepository.findByNumberId(numberId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, person.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return person;
    }

    public Person register(LoginRequest request) {
        Person person = new Person();
        person.setNumberId(request.getNumberId());
        person.setPassword(passwordEncoder.encode(request.getPassword()));
        return personRepository.save(person);
    }
}