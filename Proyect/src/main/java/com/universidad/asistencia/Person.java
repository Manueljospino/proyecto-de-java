package com.universidad.asistencia;
import jakarta.persistence.*;

@Entity
@Table(name = "Usuarios")


public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    private String mail;
    private String password;
    private String role;
    public Person() {}

    public Person(String name, String mail, String password, String role) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }


}
