package com.universidad.asistencia.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sesiones")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "docente_id")
    private Teacher docente;

    private LocalDateTime fechaApertura;
    private boolean activa;

    @JsonIgnore
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Assistance> assistances;

    public Session() {}

    public Long getId() { return id; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public Teacher getDocente() { return docente; }
    public void setDocente(Teacher docente) { this.docente = docente; }
    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public List<Assistance> getAssistances() { return assistances; }
    public void setAssistances(List<Assistance> assistances) { this.assistances = assistances; }
}