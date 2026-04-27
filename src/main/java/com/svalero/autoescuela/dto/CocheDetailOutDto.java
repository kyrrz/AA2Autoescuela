package com.svalero.autoescuela.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CocheDetailOutDto {

    private long id;
    private String marca;
    private String modelo;
    private String tipoCambio;
    private String matricula;
    private int kilometraje;
    private float precioCompra;
    private boolean disponible;
    private LocalDate fechaCompra;
    private AutoescuelaOutDto autoescuela;
    private String image;

}
