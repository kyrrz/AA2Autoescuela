package com.svalero.autoescuela.dto;
import com.svalero.autoescuela.model.Autoescuela;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CocheInDto {

    @NotBlank(message = "Matricula es un campo obligatorio")
    @Pattern(regexp = "^[0-9]{1,4}(?!.*(LL|CH))[BCDFGHJKLMNPRSTVWXYZ]{3}", message = "Formato de matricula no valido")
    private String matricula;
    @NotBlank(message = "Marca es un campo obligatorio")
    private String marca;
    @NotBlank(message = "Modelo es un campo obligatorio")
    private String modelo;
    @NotBlank(message = "Tipo de Cambio es un campo obligatorio")
    private String tipoCambio;
    @NotNull(message = "Kilometraje es un campo obligatorio")
    @Positive
    private Integer kilometraje;
    @NotNull(message = "Fecha de compra es un campo obligatorio")
    @PastOrPresent
    private LocalDate fechaCompra;
    @NotNull(message = "Precio es un campo obligatorio")
    @Positive
    private Float precioCompra;
    @NotNull
    private Boolean disponible;
    private Long autoescuelaId;
    private String image;

}
