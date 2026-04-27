package com.svalero.autoescuela.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AlumnoDetailOutDto {

    private long id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String dni;
    private LocalDate fechaNacimiento;
    private Boolean usaGafas;
    private Float notaTeorico;
    private AutoescuelaOutDto autoescuela;


}
