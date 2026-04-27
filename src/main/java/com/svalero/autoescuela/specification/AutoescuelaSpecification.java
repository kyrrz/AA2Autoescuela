package com.svalero.autoescuela.specification;

import com.svalero.autoescuela.model.Autoescuela;
import org.springframework.data.jpa.domain.Specification;

public class AutoescuelaSpecification {

    public static Specification<Autoescuela> ciudadEquals(String ciudad) {
        return (root, query, cb) ->
                ciudad == null ? null : cb.equal(root.get("ciudad"), ciudad);
    }

    public static Specification<Autoescuela> ratingGreater(Float minRating) {
        return (root, query, cb) ->
                minRating == null ? null : cb.greaterThanOrEqualTo(root.get("rating"), minRating);
    }

    public static Specification<Autoescuela> activaEquals(Boolean activa) {
        return (root, query, cb) ->
                activa == null ? null : cb.equal(root.get("activa"), activa);
    }
}
