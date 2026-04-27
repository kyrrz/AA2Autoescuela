package com.svalero.autoescuela.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity(name = "matriculas")
@Table(name = "matriculas")
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String modalidad;
    @Column(name = "tipo_matricula")
    private String tipoMatricula;
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    @Column(name = "fecha_final")
    private LocalDate fechaFinal;
    @Column
    private float precio;
    @Column(name = "horas_practicas")
    private int horasPracticas;
    @Column(name = "horas_teoricas")
    private int horasTeoricas;
    @Column
    private boolean completada;
    @Column
    private String observaciones;


    @ManyToOne
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "autoescuela_id")
    private Autoescuela autoescuela;
}
