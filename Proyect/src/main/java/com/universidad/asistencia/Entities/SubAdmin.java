package com.universidad.asistencia.Entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SUBADMIN")
public class SubAdmin extends Person {
    public SubAdmin() {}
}