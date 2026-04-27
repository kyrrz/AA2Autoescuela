package com.svalero.autoescuela.repository;

import com.svalero.autoescuela.model.Coche;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CocheRepository extends CrudRepository<Coche, Long>, JpaSpecificationExecutor<Coche> {

    List<Coche> findAll();


    @Query(value = "SELECT c FROM coches c WHERE c.autoescuela.id = :autoescuelaId")
    List<Coche> findCochesByAutoescuelaId(@Param("autoescuelaId") Long autoescuelaId);

    List<Coche> findByMarca(String marca);
    List<Coche> findByModelo(String modelo);
    List<Coche> findByTipoCambio(String tipoCambio);

    List<Coche> findByMarcaAndModelo(String marca, String modelo);
    List<Coche> findByMarcaAndTipoCambio(String marca, String tipoCambio);
    List<Coche> findByModeloAndTipoCambio(String modelo, String tipoCambio);

    List<Coche> findByMarcaAndModeloAndTipoCambio(String marca, String modelo, String tipoCambio);
}
