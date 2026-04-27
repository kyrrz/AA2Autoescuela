package com.svalero.autoescuela.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AutoescuelaDetailOutDto {

    private long id;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String telefono;
    private String email;
    private float rating;
    private boolean activa;
    private int capacidad;
    private LocalDate fechaApertura;
    private List<CocheOutDto> coches;
    private List<ProfesorOutDto> profesores;
    private double latitud;
    private double longitud;


}
