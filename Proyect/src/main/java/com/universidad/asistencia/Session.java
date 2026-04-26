package com.universidad.asistencia;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sesiones")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String materia;
    private LocalDateTime fechaApertura;
    private boolean activa;

    @ManyToOne
    @JoinColumn(name = "docente_id")
    private Teacher docente;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Assistance> assistances;
    public Session() {}

    public Long getId() { return id; }
    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public Teacher getDocente() { return docente; }
    public void setDocente(Teacher docente) { this.docente = docente; }
    public List<Assistance> getAsistencias() { return assistances; }
    public void setAsistencias(List<Assistance> asistencias) { this.assistances = asistencias; }
}