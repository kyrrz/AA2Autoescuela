package com.svalero.autoescuela.service;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.AlumnoNotFoundException;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.BadRequestException;
import com.svalero.autoescuela.model.Alumno;
import com.svalero.autoescuela.model.Autoescuela;
import com.svalero.autoescuela.repository.AlumnoRepository;
import com.svalero.autoescuela.repository.AutoescuelaRepository;
import com.svalero.autoescuela.specification.AlumnoSpecification;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AutoescuelaRepository autoescuelaRepository;

    public AlumnoDetailOutDto add(AlumnoInDto alumnoInDto, AutoescuelaDetailOutDto autoescuelaDetailOutDto) throws AutoescuelaNotFoundException {
        Alumno alumno = modelMapper.map(alumnoInDto, Alumno.class);
//        a.setNombre(alumnoInDto.getNombre());
//        a.setApellidos(alumnoInDto.getApellidos());
//        a.setDni(alumnoInDto.getDni());
//        a.setFechaNacimiento(alumnoInDto.getFechaNacimiento());
//        a.setTelefono(alumnoInDto.getTelefono());
//        a.setEmail(alumnoInDto.getEmail());
//        a.setDireccion(alumnoInDto.getDireccion());
//        a.setUsaGafas(a.isUsaGafas());
//        a.setNotaTeorico(alumnoInDto.getNotaTeorico());
        Autoescuela autoescuela = autoescuelaRepository.findById(autoescuelaDetailOutDto.getId())
                .orElseThrow(AutoescuelaNotFoundException::new);

        alumno.setAutoescuela(autoescuela);

        Alumno a = alumnoRepository.save(alumno);
        return modelMapper.map(a, AlumnoDetailOutDto.class);
    }

    public void delete( long id ) throws AlumnoNotFoundException {
        Alumno a = alumnoRepository.findById(id)
                .orElseThrow(AlumnoNotFoundException::new);
        alumnoRepository.delete(a);
    }

    public List<AlumnoOutDto> findAll(){

        List<Alumno> alumnoList = alumnoRepository.findAll();
        List<AlumnoOutDto> all =  modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());

        return all;
    }

//    public List<AlumnoOutDto> findByFiltros(String nombre, String ciudad, Boolean usaGafas, Float notaTeorico){
//        if (nombre != null && ciudad != null && usaGafas != null && notaTeorico != null) {
//            List<Alumno> alumnoList = alumnoRepository.findByNombreAndCiudadAndUsaGafasAndNotaTeorico(
//                    nombre, ciudad, usaGafas, notaTeorico);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//        if( nombre != null && ciudad != null && usaGafas != null ){
//            List<Alumno> alumnoList = alumnoRepository.findByNombreAndCiudadAndUsaGafas(nombre, ciudad, usaGafas);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//        if( nombre != null && ciudad != null && notaTeorico != null ){
//            List<Alumno> alumnoList = alumnoRepository.findByNombreAndCiudadAndNotaTeorico(nombre, ciudad, notaTeorico);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//        if( nombre != null && usaGafas != null && notaTeorico != null ){
//            List<Alumno> alumnoList = alumnoRepository.findByNombreAndUsaGafasAndNotaTeorico(nombre,  usaGafas, notaTeorico);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//        if ( ciudad != null && usaGafas != null && notaTeorico != null ){
//            List<Alumno> alumnoList = alumnoRepository.findByCiudadAndUsaGafasAndNotaTeorico(ciudad, usaGafas, notaTeorico);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//
//        if(ciudad != null && usaGafas != null){
//            List<Alumno> alumnoList = alumnoRepository.findByCiudadAndUsaGafas(ciudad, usaGafas);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//        if(ciudad != null && notaTeorico != null){
//            List<Alumno> alumnoList = alumnoRepository.findByCiudadAndNotaTeorico(ciudad, notaTeorico);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//        if(usaGafas != null && notaTeorico != null){
//            List<Alumno> alumnoList = alumnoRepository.findByUsaGafasAndNotaTeorico(usaGafas, notaTeorico);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//        if(nombre != null && notaTeorico != null){
//            List<Alumno> alumnoList = alumnoRepository.findByNombreAndNotaTeorico(nombre, notaTeorico);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//        if(nombre != null && usaGafas != null){
//            List<Alumno> alumnoList = alumnoRepository.findByNombreAndUsaGafas(nombre, usaGafas);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//        if(nombre != null && ciudad != null){
//            List<Alumno> alumnoList = alumnoRepository.findByNombreAndCiudad(nombre, ciudad);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//
//        if(nombre != null){
//            List<Alumno> alumnoList = alumnoRepository.findByNombre(nombre);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//        if(ciudad != null){
//            List<Alumno> alumnoList = alumnoRepository.findByCiudad(ciudad);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//        if(usaGafas != null){
//            List<Alumno> alumnoList = alumnoRepository.findByUsaGafas(usaGafas);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//        if(notaTeorico != null){
//            List<Alumno> alumnoList = alumnoRepository.findByNotaTeorico(notaTeorico);
//            List<AlumnoOutDto> aod = modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//            return aod;
//        }
//
//
//        List<Alumno> alumnoList = alumnoRepository.findAll();
//        List<AlumnoOutDto> all =  modelMapper.map(alumnoList, new TypeToken<List<AlumnoOutDto>>() {}.getType());
//
//        return all;
//    }


    public List<AlumnoOutDto> findByFiltros(String nombre, String ciudad, Boolean usaGafas, Float minNotaTeorico) {

        Specification<Alumno> spec = Specification
                .where(AlumnoSpecification.nombreEquals(nombre))
                .and(AlumnoSpecification.ciudadEquals(ciudad))
                .and(AlumnoSpecification.usaGafasEquals(usaGafas))
                .and(AlumnoSpecification.notaTeoricoGreater(minNotaTeorico));

        List<Alumno> alumnos = alumnoRepository.findAll(spec);

        return modelMapper.map(alumnos, new TypeToken<List<AlumnoOutDto>>() {}.getType());
    }

    public AlumnoDetailOutDto findById(long id) throws AlumnoNotFoundException{
        return alumnoRepository.findById(id).map(alumno -> modelMapper.map(alumno, AlumnoDetailOutDto.class))
                .orElseThrow(AlumnoNotFoundException::new);
    }

    public AlumnoDetailOutDto modify(long id, AlumnoInDto alumnoInDto, AutoescuelaDetailOutDto autoescuelaDetailOutDto) throws AutoescuelaNotFoundException, AlumnoNotFoundException {
        Alumno a = alumnoRepository.findById(id)
                .orElseThrow(AlumnoNotFoundException::new);

        a.setNombre(alumnoInDto.getNombre());
        a.setApellidos(alumnoInDto.getApellidos());
        a.setDni(alumnoInDto.getDni());
        a.setTelefono(alumnoInDto.getTelefono());
        a.setEmail(alumnoInDto.getEmail());
        a.setDireccion(alumnoInDto.getDireccion());
        a.setCiudad(alumnoInDto.getCiudad());
        a.setFechaNacimiento(alumnoInDto.getFechaNacimiento());
        a.setNotaTeorico(alumnoInDto.getNotaTeorico());
        a.setUsaGafas(alumnoInDto.isUsaGafas());

        Autoescuela autoescuela = autoescuelaRepository.findById(autoescuelaDetailOutDto.getId())
                .orElseThrow(AutoescuelaNotFoundException::new);
        a.setId(id);
        a.setAutoescuela(autoescuela);

        Alumno alumno = alumnoRepository.save(a);
        return modelMapper.map(alumno, AlumnoDetailOutDto.class);
    }

    public AlumnoDetailOutDto patch(long id, Map<String, Object> patch) throws AlumnoNotFoundException, AutoescuelaNotFoundException, BadRequestException {
        Alumno alumno = alumnoRepository.findById(id).orElseThrow(AlumnoNotFoundException::new);

        for (Map.Entry<String, Object> entry : patch.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            switch (key) {
                case "nombre":
                    alumno.setNombre((String) value);
                    break;
                case "apellidos":
                    alumno.setApellidos((String) value);
                    break;
                case "dni":
                    alumno.setDni((String) value);
                    break;
                case "fechaNacimiento":
                    alumno.setFechaNacimiento(LocalDate.parse((String) value));
                    break;
                case "telefono":
                    alumno.setTelefono((String) value);
                    break;
                case "email":
                    alumno.setEmail((String) value);
                    break;
                case "direccion":
                    alumno.setDireccion((String) value);
                    break;
                case "ciudad":
                    alumno.setCiudad((String) value);
                    break;
                case "usaGafas":
                    alumno.setUsaGafas((Boolean) value);
                    break;
                case "notaTeorico":
                    alumno.setNotaTeorico(((Number) value).floatValue());
                    break;
                case "autoescuelaId":
                    Long autoescuelaId = ((Number) value).longValue();
                    Autoescuela autoescuela = autoescuelaRepository.findById(autoescuelaId)
                            .orElseThrow(AutoescuelaNotFoundException::new);

                    alumno.setAutoescuela(autoescuela);
                    break;
                default:
                    throw new BadRequestException();
            }

        };
        Alumno alumnoPatch = alumnoRepository.save(alumno);
        return modelMapper.map(alumnoPatch, AlumnoDetailOutDto.class);
    }
}
