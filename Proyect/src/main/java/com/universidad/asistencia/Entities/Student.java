package com.universidad.asistencia.Entities;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ESTUDIANTE")

public class Student extends Person {

    private String semester;
    private String program;
    private String subject;
    public Student()  {}

    public Student(String name, String mail, String password, String numberId, String semester, String program, String subject ){
        super(name, mail, password, "ESTUDIANTE", numberId);

        this.semester = semester;
        this.program  = program;
        this.subject = subject;



    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
