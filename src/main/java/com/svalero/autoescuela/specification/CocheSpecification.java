package com.svalero.autoescuela.specification;

import com.svalero.autoescuela.model.Coche;
import org.springframework.data.jpa.domain.Specification;

public class CocheSpecification {

    public static Specification<Coche> marcaEquals(String marca) {
        return (root, query, cb) ->
                marca == null ? null : cb.equal(root.get("marca"), marca);
    }

    public static Specification<Coche> modeloEquals(String modelo) {
        return (root, query, cb) ->
                modelo == null ? null : cb.equal(root.get("modelo"), modelo);
    }

    public static Specification<Coche> tipoCambioEquals(String tipoCambio) {
        return (root, query, cb) ->
                tipoCambio == null ? null : cb.equal(root.get("tipoCambio"), tipoCambio);
    }
}

