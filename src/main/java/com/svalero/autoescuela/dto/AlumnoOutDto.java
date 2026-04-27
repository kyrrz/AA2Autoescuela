package com.svalero.autoescuela.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlumnoOutDto {
    private long id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String ciudad;
    private float notaTeorico;

}
