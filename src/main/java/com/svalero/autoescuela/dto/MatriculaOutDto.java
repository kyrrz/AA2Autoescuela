package com.svalero.autoescuela.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class MatriculaOutDto {

    private long id;
    private String modalidad;
    private String tipoMatricula;
    private boolean completada;
    private AutoescuelaOutDto autoescuela;
    private AlumnoOutDto alumno;
}
