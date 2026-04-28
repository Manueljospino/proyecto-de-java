package com.universidad.asistencia;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Usuarios")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)


public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//creamos variables, constructores, getters y setters para la clase padre llamada "Person"
    private Long id;
    private String name;
    private String mail;
    @JsonIgnore
    private String password;
    @Column(insertable = false, updatable = false)
    private String role;
    private String numberId;
    public Person() {}

    public Person(String name, String mail, String password, String role, String numberId) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.role = role;
        this.numberId = numberId;
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
    public String getNumberId() {
        return numberId;
    }
    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }
}
