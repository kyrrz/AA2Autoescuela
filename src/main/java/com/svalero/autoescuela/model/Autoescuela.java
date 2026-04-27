package com.svalero.autoescuela.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity(name = "autoescuelas")
@Table(name = "autoescuelas")
public class Autoescuela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String nombre;
    @Column
    private String direccion;
    @Column
    private String ciudad;
    @Column
    private String telefono;
    @Column
    private String email;
    @Column
    private int capacidad;
    @Column
    private float rating;
    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;
    @Column
    private boolean activa;
    @Column
    private double latitud;
    @Column
    private double longitud;

    @OneToMany(mappedBy = "autoescuela")
    @JsonBackReference
    private List<Matricula> matriculas;

    @OneToMany(mappedBy = "autoescuela")
    @JsonBackReference
    private List<Alumno> alumnos;

    @OneToMany(mappedBy = "autoescuela")
    @JsonBackReference
    private List<Coche> coches;

    @ManyToMany(mappedBy = "autoescuelas")
    private List<Profesor> profesores = new ArrayList<>();
}
