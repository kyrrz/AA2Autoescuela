package com.svalero.autoescuela.specification;

import com.svalero.autoescuela.model.Profesor;
import org.springframework.data.jpa.domain.Specification;

public class ProfesorSpecification {
    public static Specification<Profesor> nombreEquals(String nombre) {
        return (root, query, cb) ->
                nombre == null ? null : cb.equal(root.get("nombre"), nombre);
    }

    public static Specification<Profesor> especialidadEquals(String especialidad) {
        return (root, query, cb) ->
                especialidad == null ? null : cb.equal(root.get("especialidad"), especialidad);
    }

    public static Specification<Profesor> activoEquals(Boolean activo) {
        return (root, query, cb) ->
                activo == null ? null : cb.equal(root.get("activo"), activo);
    }
}
