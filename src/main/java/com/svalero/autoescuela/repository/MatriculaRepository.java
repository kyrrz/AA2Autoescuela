package com.svalero.autoescuela.repository;

import com.svalero.autoescuela.model.Matricula;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends CrudRepository<Matricula, Long>, JpaSpecificationExecutor<Matricula> {
    List<Matricula> findAll();

    @Query(value = "SELECT m FROM matriculas m WHERE m.autoescuela.id = :autoescuelaId")
    List<Matricula> findMatriculaByAutoescuelaId(@Param("autoescuelaId") Long autoescuelaId);

    @NativeQuery(value = "SELECT * FROM matriculas m WHERE m.autoescuela_id AND m.completada=true")
    List<Matricula> findMatriculaCompletasByAutoescuelaId(@Param("autoescuelaId") Long autoescuelaId);

    List<Matricula> findByModalidad(String modalidad);
    List<Matricula> findByTipoMatricula(String tipoMatricula);
    List<Matricula> findByHorasPracticas(int horasPracticas);
    List<Matricula> findByHorasTeoricas(int horasTeoricas);

    List<Matricula> findByModalidadAndTipoMatricula(String modalidad, String tipoMatricula);
    List<Matricula> findByModalidadAndHorasPracticas(String modalidad, int horasPracticas);
    List<Matricula> findByModalidadAndHorasTeoricas(String modalidad, int horasTeoricas);
    List<Matricula> findByTipoMatriculaAndHorasPracticas(String tipoMatricula, int horasPracticas);
    List<Matricula> findByTipoMatriculaAndHorasTeoricas(String tipoMatricula, int horasTeoricas);
    List<Matricula> findByHorasPracticasAndHorasTeoricas(int horasPracticas, int horasTeoricas);

    List<Matricula> findByModalidadAndTipoMatriculaAndHorasPracticas(String modalidad, String tipoMatricula, int horasPracticas);
    List<Matricula> findByModalidadAndTipoMatriculaAndHorasTeoricas(String modalidad, String tipoMatricula, int horasTeoricas);
    List<Matricula> findByModalidadAndHorasPracticasAndHorasTeoricas(String modalidad, int horasPracticas, int horasTeoricas);
    List<Matricula> findByTipoMatriculaAndHorasPracticasAndHorasTeoricas(String tipoMatricula, int horasPracticas, int horasTeoricas);

    List<Matricula> findByModalidadAndTipoMatriculaAndHorasPracticasAndHorasTeoricas(String modalidad, String tipoMatricula, int horasPracticas, int horasTeoricas);
}
