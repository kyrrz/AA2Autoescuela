package com.svalero.autoescuela.repository;

import com.svalero.autoescuela.model.Profesor;
import org.hibernate.query.sqm.mutation.internal.temptable.LocalTemporaryTableInsertStrategy;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfesorRepository extends CrudRepository<Profesor, Long>, JpaSpecificationExecutor<Profesor> {
    List<Profesor> findAll();

    @Query(value = "SELECT p FROM profesores p JOIN p.autoescuelas a WHERE a.id = :autoescuelaId ")
    List<Profesor> findProfesoresByAutoescuelaId(@Param("autoescuelaId") Long autoescuelaId);


    List<Profesor> findByEspecialidad(String especialidad);
    List<Profesor> findByNombre(String nombre);
    List<Profesor> findByActivo(Boolean activo);

    List<Profesor> findByNombreAndEspecialidad(String nombre, String especialidad);
    List<Profesor> findByNombreAndActivo(String nombre, Boolean activo);
    List<Profesor> findByActivoAndEspecialidad(Boolean activo, String especialidad);

    List<Profesor> findByNombreAndEspecialidadAndActivo(String nombre, String especialidad, Boolean activo);
}
