package com.svalero.autoescuela.specification;

import com.svalero.autoescuela.model.Alumno;
import org.springframework.data.jpa.domain.Specification;

public class AlumnoSpecification {

    public static Specification<Alumno> nombreEquals(String nombre) {
        return (root, query, cb) ->
                nombre == null ? null : cb.equal(root.get("nombre"), nombre);
    }

    public static Specification<Alumno> ciudadEquals(String ciudad) {
        return (root, query, cb) ->
                ciudad == null ? null : cb.equal(root.get("ciudad"), ciudad);
    }

    public static Specification<Alumno> usaGafasEquals(Boolean usaGafas) {
        return (root, query, cb) ->
                usaGafas == null ? null : cb.equal(root.get("usaGafas"), usaGafas);
    }

    public static Specification<Alumno> notaTeoricoGreater(Float minNotaTeorico) {
        return (root, query, cb) ->
                minNotaTeorico == null ? null : cb.greaterThanOrEqualTo(root.get("notaTeorico"), minNotaTeorico);
    }
}
