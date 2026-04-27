package com.svalero.autoescuela.dto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlumnoInDto {
    @NotBlank(message = "Nombre es un campo obligatorio")
    private String nombre;
    @NotBlank(message = "Apellidos es un campo obligatorio")
    private String apellidos;
    @NotBlank(message = "DNI es un campo obligatorio")
    @Pattern(regexp = "[0-9]{8}[A-Z]", message = "Formato DNI no valido")
    private String dni;
    @NotNull(message = "Fecha de Nacimiento es un campo obligatorio")
    @Past
    private LocalDate fechaNacimiento;
    @NotBlank(message = "Telefono es un campo obligatorio")
    @Pattern(regexp = "[0-9]{9}", message = "Formato del telefono no valido")
    private String telefono;
    @NotNull(message = "Email es un campo obligatorio")
    @Email
    private String email;
    @NotNull(message = "Direccion es un campo obligatorio")
    private String direccion;
    @NotNull(message = "Ciudad es un campo obligatorio")
    private String ciudad;
    @NotNull(message = "usaGafas es un campo obligatorio")
    private boolean usaGafas;
    @Range(min = 1, max = 10)
    private float notaTeorico;
    private long autoescuelaId;
}
