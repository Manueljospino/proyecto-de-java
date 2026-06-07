package com.universidad.asistencia.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencia")
public class Assistance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;
    private String state;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    public Assistance() {}

    public Long getId() { return id; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
}