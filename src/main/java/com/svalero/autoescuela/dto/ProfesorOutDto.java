package com.svalero.autoescuela.dto;

import com.svalero.autoescuela.model.Autoescuela;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesorOutDto {
    private long id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String telefono;
    private String especialidad;
    private boolean activo;
//    private List<AutoescuelaOutDto> autoescuelas;

}
