package com.svalero.autoescuela.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesorInDto {
    @NotBlank(message = "Nombre es un campo obligatorio")
    private String nombre;
    @NotBlank(message = "Apellidos es un campo obligatorio")
    private String apellidos;
    @NotBlank(message = "DNI es un campo obligatorio")
    @Pattern(regexp = "[0-9]{8}[A-Z]", message = "Formato DNI no valido")
    private String dni;
    @NotNull(message = "Telefono es un campo obligatorio")
    @Pattern(regexp = "[0-9]{9}", message = "Formato del telefono no valido")
    private String telefono;
    @NotNull(message = "Salario es un campo obligatorio")
    @Positive(message = "Salario no puede ser negativo")
    private float salario;
    @NotNull(message = "Fecha de contratacion es un campo obligatorio")
    @PastOrPresent(message = "Fecha no valida")
    private LocalDate fechaContratacion;
    @NotBlank(message = "Especialidad es un campo obligatorio")
    private String especialidad;
    @NotNull
    private boolean activo;
    private List<Long> autoescuelaId;
}
