package com.svalero.autoescuela.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatriculaDetailOutDtoV2
{

    private long id;
    private String modalidad;
    private String tipoMatricula;
    private int horasPracticas;
    private int horasTeoricas;
    private boolean completada;
    private AlumnoOutDto alumno;
    private AutoescuelaOutDto autoescuela;
    private String observaciones;
    private String metodoPago;
    private String codigoDescuento;

}
