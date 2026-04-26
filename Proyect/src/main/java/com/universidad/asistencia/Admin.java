package com.universidad.asistencia;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Person {

    public Admin() {}
}