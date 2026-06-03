package com.universidad.asistencia.Entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DOCENTE")
public class Teacher extends Person {

    private String department;
    private String subject;

    public Teacher() {}

    public Teacher(String name, String mail, String password, String numberId, String department, String subject) {
        super(name, mail, password, "DOCENTE", numberId);
        this.department = department;
        this.subject = subject;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
}