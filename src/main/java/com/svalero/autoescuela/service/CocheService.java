package com.svalero.autoescuela.service;


import com.svalero.autoescuela.dto.AutoescuelaDetailOutDto;
import com.svalero.autoescuela.dto.CocheDetailOutDto;
import com.svalero.autoescuela.dto.CocheInDto;
import com.svalero.autoescuela.dto.CocheOutDto;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.CocheNotFoundException;
import com.svalero.autoescuela.model.Autoescuela;
import com.svalero.autoescuela.model.Coche;
import com.svalero.autoescuela.repository.AutoescuelaRepository;
import com.svalero.autoescuela.repository.CocheRepository;

import com.svalero.autoescuela.specification.CocheSpecification;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CocheService {

    @Autowired
    private CocheRepository cocheRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AutoescuelaRepository autoescuelaRepository;

    public CocheDetailOutDto add(CocheInDto cocheInDto, AutoescuelaDetailOutDto autoescuelaDetailOutDto) throws AutoescuelaNotFoundException {
        Coche coche = new Coche();
        modelMapper.map(cocheInDto, coche);
//        coche.setMatricula(cocheInDto.getMatricula());
//        coche.setMarca(cocheInDto.getMarca());
//        coche.setModelo(cocheInDto.getModelo());
//        coche.setTipoCambio(cocheInDto.getTipoCambio());
//        coche.setKilometraje(cocheInDto.getKilometraje());
//        coche.setFechaCompra(cocheInDto.getFechaCompra());
//        coche.setPrecioCompra(cocheInDto.getPrecioCompra());
//        coche.setDisponible(cocheInDto.getDisponible());
        Autoescuela autoescuela = autoescuelaRepository.findById(autoescuelaDetailOutDto.getId())
                .orElseThrow(AutoescuelaNotFoundException::new);
        coche.setAutoescuela(autoescuela);

        Coche savedCoche = cocheRepository.save(coche);
        return modelMapper.map(savedCoche, CocheDetailOutDto.class);
    }

//    public List<CocheOutDto> findByFiltros(String marca, String modelo, String tipoCambio){
//
//        if (marca != null && modelo != null && tipoCambio != null) {
//            List<Coche> coches = cocheRepository.findByMarcaAndModeloAndTipoCambio(marca, modelo, tipoCambio);
//            List<CocheOutDto> cocheOutDtos = modelMapper.map(coches, new TypeToken<List<CocheOutDto>>() {}.getType());
//            return cocheOutDtos;
//        }
//
//        if (modelo != null && tipoCambio != null) {
//            List<Coche> coches = cocheRepository.findByModeloAndTipoCambio(modelo, tipoCambio);
//            List<CocheOutDto> cocheOutDtos = modelMapper.map(coches, new TypeToken<List<CocheOutDto>>() {}.getType());
//            return cocheOutDtos;
//        }
//        if (marca != null && tipoCambio != null) {
//            List<Coche> coches = cocheRepository.findByMarcaAndTipoCambio(marca, tipoCambio);
//            List<CocheOutDto> cocheOutDtos = modelMapper.map(coches, new TypeToken<List<CocheOutDto>>() {}.getType());
//            return cocheOutDtos;
//        }
//        if (modelo != null && marca != null) {
//            List<Coche> coches = cocheRepository.findByMarcaAndModelo(marca, modelo);
//            List<CocheOutDto> cocheOutDtos = modelMapper.map(coches, new TypeToken<List<CocheOutDto>>() {}.getType());
//            return cocheOutDtos;
//        }
//
//        if (tipoCambio != null) {
//            List<Coche> coches = cocheRepository.findByTipoCambio(tipoCambio);
//            List<CocheOutDto> cocheOutDtos = modelMapper.map(coches, new TypeToken<List<CocheOutDto>>() {}.getType());
//            return cocheOutDtos;
//        }
//        if (modelo != null) {
//            List<Coche> coches = cocheRepository.findByModelo(modelo);
//            List<CocheOutDto> cocheOutDtos = modelMapper.map(coches, new TypeToken<List<CocheOutDto>>() {}.getType());
//            return cocheOutDtos;
//        }
//        if (marca != null) {
//            List<Coche> coches = cocheRepository.findByMarca(marca);
//            List<CocheOutDto> cocheOutDtos = modelMapper.map(coches, new TypeToken<List<CocheOutDto>>() {}.getType());
//            return cocheOutDtos;
//        }
//
//        List<Coche>  coches = cocheRepository.findAll();
//        List<CocheOutDto> cocheOutDtos = modelMapper.map(coches, new TypeToken<List<CocheOutDto>>() {}.getType());
//        return cocheOutDtos;
//    }

    public List<CocheOutDto> findByFiltros(String marca, String modelo, String tipoCambio) {

        Specification<Coche> spec = Specification
                .where(CocheSpecification.marcaEquals(marca))
                .and(CocheSpecification.modeloEquals(modelo))
                .and(CocheSpecification.tipoCambioEquals(tipoCambio));

        List<Coche> coches = cocheRepository.findAll(spec);
        return modelMapper.map(coches, new TypeToken<List<CocheOutDto>>() {}.getType());
    }

    public void delete( long id ) throws CocheNotFoundException {
        Coche c = cocheRepository.findById(id)
                .orElseThrow(CocheNotFoundException::new);
        cocheRepository.delete(c);
    }

    public List<Coche> findAll(){
        return cocheRepository.findAll();
    }

    public List<CocheOutDto> findAllDto(){
        List<Coche> coches = cocheRepository.findAll();
        return modelMapper.map(coches, new TypeToken<List<CocheOutDto>>() {}.getType());
    }

    public CocheDetailOutDto findById(long id) throws CocheNotFoundException{
        return cocheRepository.findById(id).map(coche -> modelMapper.map(coche, CocheDetailOutDto.class))
                .orElseThrow(CocheNotFoundException::new);
    }

    public CocheDetailOutDto modify(long id, CocheInDto cocheInDto, AutoescuelaDetailOutDto autoescuelaDetailOutDto) throws AutoescuelaNotFoundException, CocheNotFoundException {
        Coche coche = cocheRepository.findById(id)
                .orElseThrow(CocheNotFoundException::new);

        modelMapper.map(cocheInDto, coche);
//        coche.setMatricula(cocheInDto.getMatricula());
//        coche.setMarca(cocheInDto.getMarca());
//        coche.setModelo(cocheInDto.getModelo());
//        coche.setTipoCambio(cocheInDto.getTipoCambio());
//        coche.setKilometraje(cocheInDto.getKilometraje());
//        coche.setFechaCompra(cocheInDto.getFechaCompra());
//        coche.setPrecioCompra(cocheInDto.getPrecioCompra());
//        coche.setDisponible(cocheInDto.getDisponible());
        Autoescuela autoescuela = autoescuelaRepository.findById(autoescuelaDetailOutDto.getId()).orElseThrow(AutoescuelaNotFoundException::new);
        coche.setAutoescuela(autoescuela);
        coche.setId(id);

        Coche savedCoche = cocheRepository.save(coche);

        return modelMapper.map(savedCoche, CocheDetailOutDto.class);
    }

    public CocheDetailOutDto patch(Long id, Map<String, Object> patch)
            throws AutoescuelaNotFoundException, CocheNotFoundException {

        Coche coche = cocheRepository.findById(id)
                .orElseThrow(CocheNotFoundException::new);

        for (Map.Entry<String, Object> entry : patch.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            switch (key) {
                case "matricula":
                    coche.setMatricula((String) value);
                    break;
                case "marca":
                    coche.setMarca((String) value);
                    break;
                case "modelo":
                    coche.setModelo((String) value);
                    break;
                case "tipoCambio":
                    coche.setTipoCambio((String) value);
                    break;
                case "kilometraje":
                    coche.setKilometraje(((Number) value).intValue());
                    break;
                case "fechaCompra":
                    coche.setFechaCompra(LocalDate.parse((String) value));
                    break;
                case "precioCompra":
                    coche.setPrecioCompra(((Number) value).floatValue());
                    break;
                case "disponible":
                    coche.setDisponible((Boolean) value);
                    break;
                case "autoescuelaId":
                    Long autoescuelaId = ((Number) value).longValue();
                    Autoescuela autoescuela = autoescuelaRepository.findById(autoescuelaId)
                            .orElseThrow(AutoescuelaNotFoundException::new);
                    coche.setAutoescuela(autoescuela);
                    break;
            }
        };

        Coche cocheActualizado = cocheRepository.save(coche);
        return modelMapper.map(cocheActualizado, CocheDetailOutDto.class);
    }
    }
