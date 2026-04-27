package com.svalero.autoescuela.specification;

import com.svalero.autoescuela.model.Matricula;
import org.springframework.data.jpa.domain.Specification;

public class MatriculaSpecification {
    public static Specification<Matricula> modalidadEquals(String modalidad) {
        return (root, query, cb) ->
                modalidad == null ? null : cb.equal(root.get("modalidad"), modalidad);
    }

    public static Specification<Matricula> tipoMatriculaEquals(String tipoMatricula) {
        return (root, query, cb) ->
                tipoMatricula == null ? null : cb.equal(root.get("tipoMatricula"), tipoMatricula);
    }

    public static Specification<Matricula> horasPracticasEquals(Integer horasPracticas) {
        return (root, query, cb) ->
                horasPracticas == null ? null : cb.equal(root.get("horasPracticas"), horasPracticas);
    }

    public static Specification<Matricula> horasTeoricasEquals(Integer horasTeoricas) {
        return (root, query, cb) ->
                horasTeoricas == null ? null : cb.equal(root.get("horasTeoricas"), horasTeoricas);
    }
}
