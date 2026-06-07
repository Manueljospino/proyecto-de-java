package com.universidad.asistencia.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "teacher_subjects", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"teacher_id", "subject_id"})
})
public class TeacherSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    public TeacherSubject() {}

    public Long getId() { return id; }
    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
}