package com.svalero.autoescuela.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MatriculaInDto {
    @NotBlank(message = "Marca es un campo obligatorio")
    private String modalidad;
    @NotBlank(message = "Marca es un campo obligatorio")
    private String tipoMatricula;
    @NotNull(message = "Fecha de inicio es un campo obligatorio")
    @PastOrPresent(message = "Fecha no valida")
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    @NotNull(message = "Precio es un campo obligatorio")
    @Positive(message = "Precio no puede ser negativo")
    private float precio;
    @NotNull(message = "Horas Practicas es un campo obligatorio")
    @Positive(message = "Horas Practicas no pueden ser negativas")
    private int horasPracticas;
    @NotNull(message = "Horas Teoricas es un campo obligatorio")
    @Positive(message = "Horas Teoricas no pueden ser negativas")
    private int horasTeoricas;
    @NotNull
    private boolean completada;
    private String observaciones;
    @NotNull(message = "Autoescuela ID es un campo obligatorio")
    private long autoescuelaId;
    @NotNull(message = "Alumno ID es un campo obligatorio")
    private long alumnoId;
}
