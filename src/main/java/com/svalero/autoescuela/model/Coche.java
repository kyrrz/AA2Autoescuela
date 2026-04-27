package com.svalero.autoescuela.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity(name = "coches")
@Table(name = "coches")
public class Coche {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String matricula;
    @Column
    private String marca;
    @Column
    private String modelo;
    @Column(name = "tipo_cambio")
    private String tipoCambio;
    @Column
    private int kilometraje;
    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;
    @Column(name = "precio_compra")
    private float precioCompra;
    @Column
    private boolean disponible;
    @Column
    private String image;


    @ManyToOne
    @JoinColumn(name = "autoescuela_id")
    private Autoescuela autoescuela;

}
