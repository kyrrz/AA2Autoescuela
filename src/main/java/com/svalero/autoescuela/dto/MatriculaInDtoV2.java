package com.svalero.autoescuela.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatriculaInDtoV2 {
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

    @NotBlank(message = "Metodo de pago es un campo obligatorio")
    @Pattern(regexp = "^(TARJETA|TRANSFERENCIA)$", message = "Metodo de pago no válido")
    private String metodoPago;
    private String codigoDescuento;
}

