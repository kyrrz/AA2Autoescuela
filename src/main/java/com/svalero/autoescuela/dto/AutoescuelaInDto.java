package com.svalero.autoescuela.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoescuelaInDto {
    @NotBlank(message = "Nombre es un campo obligatorio")
    private String nombre;
    @NotBlank(message = "Direccion es un campo obligatorio")
    private String direccion;
    @NotBlank(message = "Ciudad es un campo obligatorio")
    private String ciudad;
    @NotBlank(message = "Telefono es un campo obligatorio")
    private String telefono;
    @NotBlank(message = "Email es un campo obligatorio")
    @Email
    private String email;
    @NotNull(message = "Capacidad es un campo obligatorio")
    @Min(0)
    private int capacidad;
    @NotNull(message = "Rating es un campo obligatorio")
    @Min(value = 0, message = "Rating no puede ser menor a 0")
    @Max(value = 10, message = "Rating no puede ser mayor a 10")
    private float rating;
    @NotNull
    @PastOrPresent(message = "La fecha tiene que ser pasada")
    private LocalDate fechaApertura;
    @NotNull
    private boolean activa;
    private double latitud;
    private double longitud;



}
