package com.universidad.asistencia.Entities;


import jakarta.persistence.*;

    @Entity
    @Table(name = "inscripciones")
    public class Inscription {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "student_id")
        private Student student;

        @ManyToOne
        @JoinColumn(name = "teacher_id")
        private Teacher teacher;

        public Inscription() {}

        public Long getId() { return id; }
        public Student getStudent() { return student; }
        public void setStudent(Student student) { this.student = student; }
        public Teacher getTeacher() { return teacher; }
        public void setTeacher(Teacher teacher) { this.teacher = teacher; }
    }

