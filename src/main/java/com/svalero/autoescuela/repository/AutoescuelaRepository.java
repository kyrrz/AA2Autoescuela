package com.svalero.autoescuela.repository;

import com.svalero.autoescuela.model.Autoescuela;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoescuelaRepository  extends CrudRepository<Autoescuela, Long>, JpaSpecificationExecutor<Autoescuela> {

    List<Autoescuela> findAll();

    @NativeQuery(value = "SELECT a.* FROM autoescuelas a " +
            "JOIN profesores_autoescuela pa " +
            "ON a.id = pa.autoescuela_id " +
            "WHERE pa.profesor_id = :profesorId")
    List<Autoescuela> findAutoescuelasByProfesorId(@Param("profesorId") Long profesorId);

    List<Autoescuela> findByCiudad(String ciudad);
    List<Autoescuela> findByRating(Float rating);
    List<Autoescuela> findByActiva(Boolean activa);

    List<Autoescuela> findByCiudadAndRating(String ciudad, Float rating);
    List<Autoescuela> findByCiudadAndActiva(String ciudad, Boolean activa);
    List<Autoescuela> findByRatingAndActiva(Float rating, Boolean activa);

    List<Autoescuela> findByCiudadAndRatingAndActiva(String ciudad, Float rating, Boolean activa);
}
