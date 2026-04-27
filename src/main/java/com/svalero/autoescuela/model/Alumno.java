package com.svalero.autoescuela.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity (name = "alumnos")
@Table(name = "alumnos")
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String nombre;
    @Column
    private String apellidos;
    @Column
    private String dni;
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    @Column
    private String telefono;
    @Column
    private String email;
    @Column
    private String direccion;
    @Column
    private String ciudad;
    @Column(name = "usa_gafas")
    private boolean usaGafas;
    @Column(name = "nota_teorico")
    private float notaTeorico;

    @OneToMany(mappedBy = "alumno")
    private List<Matricula> matriculas;

    @ManyToOne
    @JoinColumn(name = "autoescuela_id")
    private Autoescuela autoescuela;
}
