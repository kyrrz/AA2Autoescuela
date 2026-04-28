package com.svalero.autoescuela.service;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.BadRequestException;
import com.svalero.autoescuela.model.*;
import com.svalero.autoescuela.repository.*;
import com.svalero.autoescuela.specification.AutoescuelaSpecification;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class AutoescuelaService {

    @Autowired
    private AutoescuelaRepository autoescuelaRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProfesorRepository profesorRepository;
    @Autowired
    private CocheRepository cocheRepository;
    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private AlumnoRepository alumnoRepository;

    public AutoescuelaDetailOutDto add(AutoescuelaInDto autoescuelaInDto){

        Autoescuela autoescuela = modelMapper.map(autoescuelaInDto, Autoescuela.class);
        Autoescuela a = autoescuelaRepository.save(autoescuela);

        return modelMapper.map(a, AutoescuelaDetailOutDto.class);
    }

    public void delete( long id ) throws AutoescuelaNotFoundException {
        Autoescuela a = autoescuelaRepository.findById(id)
                .orElseThrow(AutoescuelaNotFoundException::new);
        autoescuelaRepository.delete(a);
    }

    public void deleteV2(long id) throws AutoescuelaNotFoundException {
        Autoescuela autoescuela = autoescuelaRepository.findById(id)
                .orElseThrow(AutoescuelaNotFoundException::new);

        List<Profesor> profesores = profesorRepository.findProfesoresByAutoescuelaId(id);
        for (Profesor profesor : profesores) {
            profesor.getAutoescuelas().remove(autoescuela);
            profesorRepository.save(profesor);
        }

        List<Alumno> alumnos = alumnoRepository.findAllByAutoescuelaId(id);
        for (Alumno alumno : alumnos) {
            alumno.setAutoescuela(null);
            alumnoRepository.save(alumno);
        }

        List<Coche> coches = cocheRepository.findCochesByAutoescuelaId(id);
        for (Coche coche : coches) {
            coche.setAutoescuela(null);
            cocheRepository.save(coche);
        }

        autoescuela.setActiva(false);
        autoescuelaRepository.save(autoescuela);
    }

    public List<AutoescuelaOutDto> findAll(){
        List<Autoescuela> autoescuelas = autoescuelaRepository.findAll();
        List<AutoescuelaOutDto> aod =  modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>() {}.getType());

        return aod;
    }

    public AutoescuelaDetailOutDto findById(long id) throws AutoescuelaNotFoundException{

        Autoescuela a = autoescuelaRepository.findById(id)
                .orElseThrow(AutoescuelaNotFoundException::new);

        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(a.getId());
        dto.setNombre(a.getNombre());
        dto.setDireccion(a.getDireccion());
        dto.setCiudad(a.getCiudad());
        dto.setTelefono(a.getTelefono());
        dto.setEmail(a.getEmail());
        dto.setRating(a.getRating());
        dto.setCapacidad(a.getCapacidad());
        dto.setFechaApertura(a.getFechaApertura());
        dto.setActiva(a.isActiva());
        dto.setLatitud(a.getLatitud());
        dto.setLongitud(a.getLongitud());

        dto.setCoches(
                a.getCoches().stream()
                        .map(c -> modelMapper.map(c, CocheOutDto.class))
                        .toList()
        );

        dto.setProfesores(
                a.getProfesores().stream()
                        .map(p -> modelMapper.map(p, ProfesorOutDto.class))
                        .toList()
        );

        return dto;
    }

    public AutoescuelaDetailOutDto toDetailDto(Autoescuela a) {

        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(a.getId());
        dto.setNombre(a.getNombre());
        dto.setDireccion(a.getDireccion());
        dto.setCiudad(a.getCiudad());
        dto.setTelefono(a.getTelefono());
        dto.setEmail(a.getEmail());
        dto.setCapacidad(a.getCapacidad());
        dto.setRating(a.getRating());
        dto.setActiva(a.isActiva());
        dto.setCoches(
                a.getCoches().stream().map(coche -> modelMapper.map(coche, CocheOutDto.class)).toList()
        );

        dto.setProfesores(
                a.getProfesores().stream().map(p -> modelMapper.map(p, ProfesorOutDto.class)).toList()
        );

        return dto;
    }

    public List<AutoescuelaDetailOutDto> findAllById(List<Long> ids) {

        Iterable<Autoescuela> iterable = autoescuelaRepository.findAllById(ids);

        List<Autoescuela> autoescuelas = new ArrayList<>();
        iterable.forEach(autoescuelas::add);

        return autoescuelas.stream()
                .map(this::toDetailDto)
                .toList();
    }



//    public List<AutoescuelaOutDto> findByFiltros(String ciudad, String ratingG, Boolean activa){
//
//
//        if (ciudad != null && ratingG != null && activa != null) {
//            Float rating = Float.parseFloat(ratingG);
//            List<Autoescuela> autoescuelas = autoescuelaRepository.findByCiudadAndRatingAndActiva(ciudad, rating, activa);
//
//            return modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>() {}.getType());
//        }
//        if (ratingG != null && activa != null) {
//            Float rating = Float.parseFloat(ratingG);
//            List<Autoescuela> autoescuelas = autoescuelaRepository.findByRatingAndActiva( rating, activa);
//
//            return modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>() {}.getType());
//        }
//        if (ciudad != null && activa != null) {
//            List<Autoescuela> autoescuelas = autoescuelaRepository.findByCiudadAndActiva( ciudad, activa);
//
//            return modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>() {}.getType());
//        }
//        if (ciudad != null && ratingG != null) {
//            Float rating = Float.parseFloat(ratingG);
//            List<Autoescuela> autoescuelas = autoescuelaRepository.findByCiudadAndRating( ciudad, rating);
//
//            return modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>() {}.getType());
//        }
//
//        if (activa != null) {
//            List<Autoescuela> autoescuelas = autoescuelaRepository.findByActiva( activa);
//
//            return modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>() {}.getType());
//        }
//        if (ratingG != null) {
//            Float rating = Float.parseFloat(ratingG);
//            List<Autoescuela> autoescuelas = autoescuelaRepository.findByRating( rating);
//
//            return modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>() {}.getType());
//        }
//        if (ciudad != null) {
//            List<Autoescuela> autoescuelas = autoescuelaRepository.findByCiudad( ciudad);
//
//            return modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>() {}.getType());
//        }
//
//        List<Autoescuela> autoescuelas = autoescuelaRepository.findAll();
//        return modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>() {}.getType());
//    }


    public List<AutoescuelaOutDto> findByFiltros(String ciudad, Float minRating, Boolean activa) {

        Specification<Autoescuela> spec = Specification
                .where(AutoescuelaSpecification.ciudadEquals(ciudad))
                .and(AutoescuelaSpecification.ratingGreater(minRating))
                .and(AutoescuelaSpecification.activaEquals(activa));

        List<Autoescuela> autoescuelas = autoescuelaRepository.findAll(spec);

        return modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>() {}.getType());
    }

    public AutoescuelaDetailOutDto modify(long id, AutoescuelaInDto autoescuelaInDto) throws AutoescuelaNotFoundException {
        Autoescuela a = autoescuelaRepository.findById(id)
                .orElseThrow(AutoescuelaNotFoundException::new);
        modelMapper.map(autoescuelaInDto, a);
        a.setId(id);
//        a.setNombre(autoescuelaInDto.getNombre());
//        a.setDireccion(autoescuelaInDto.getDireccion());
//        a.setCiudad(autoescuelaInDto.getCiudad());
//        a.setTelefono(autoescuelaInDto.getTelefono());
//        a.setEmail(autoescuelaInDto.getEmail());
//        a.setCapacidad(autoescuelaInDto.getCapacidad());
//        a.setRating(autoescuelaInDto.getRating());
//        a.setFechaApertura(autoescuelaInDto.getFechaApertura());
//        a.setActiva(autoescuelaInDto.isActiva());
        Autoescuela au = autoescuelaRepository.save(a);
        return modelMapper.map(au, AutoescuelaDetailOutDto.class);
    }

    public List<CocheOutDto> getCoches(Long autoescuelaId) throws AutoescuelaNotFoundException {
        List<Coche> coche = cocheRepository.findCochesByAutoescuelaId( autoescuelaId);
        return coche.stream().map(c -> modelMapper.map(c, CocheOutDto.class)).toList();
    }

    public List<MatriculaOutDto> getMatriculas(Long autoescuelaId) throws AutoescuelaNotFoundException {
        List<Matricula> matricula = matriculaRepository.findMatriculaByAutoescuelaId( autoescuelaId);

        return matricula.stream().map(m -> modelMapper.map(m, MatriculaOutDto.class)).toList();
    }

    public List<ProfesorOutDto> getProfesores(Long autoescuelaId) throws AutoescuelaNotFoundException {
        List<Profesor> profesor = profesorRepository.findProfesoresByAutoescuelaId(autoescuelaId);
             return profesor.stream().map(p -> modelMapper.map(p, ProfesorOutDto.class)).toList();
    }

    public List<MatriculaOutDto> getMatriculasCompletas(Long autoescuelaId) throws AutoescuelaNotFoundException {
        List<Matricula> matriculas = matriculaRepository.findMatriculaCompletasByAutoescuelaId(autoescuelaId);

        return matriculas.stream().map(m -> modelMapper.map(m, MatriculaOutDto.class)).toList();
    }

    public List<AlumnoOutDto> getAlumnosSuspensos(Long autoescuelaId) throws AutoescuelaNotFoundException {
        List<Alumno> alumnos = alumnoRepository.findAlumnosSuspensosByAutoescuela(autoescuelaId);

        return alumnos.stream().map(a -> modelMapper.map(a, AlumnoOutDto.class)).toList();
    }

   public AutoescuelaDetailOutDto patch(Long id, Map<String, Object> patch)
           throws AutoescuelaNotFoundException, BadRequestException {
        Autoescuela autoescuela = autoescuelaRepository.findById(id)
                .orElseThrow(AutoescuelaNotFoundException::new);

       for (Map.Entry<String, Object> entry : patch.entrySet()) {
           String key = entry.getKey();
           Object value = entry.getValue();
            switch (key) {
                case "nombre":
                    autoescuela.setNombre((String) value);
                    break;
                case "direccion":
                    autoescuela.setDireccion((String) value);
                    break;
                case "ciudad":
                    autoescuela.setCiudad((String) value);
                    break;
                case "telefono":
                    autoescuela.setTelefono((String) value);
                    break;
                case "email":
                    autoescuela.setEmail((String) value);
                    break;
                case "capacidad":
                    autoescuela.setCapacidad(((Number) value).intValue());
                    break;
                case "rating":
                    autoescuela.setRating(((Number) value).floatValue());
                    break;
                case "fechaApertura":
                    autoescuela.setFechaApertura(LocalDate.parse((String) value));
                    break;
                case "activa":
                    autoescuela.setActiva((Boolean) value);
                    break;

                case "latitud":
                    autoescuela.setLatitud(((Number) value).floatValue());
                    break;
                case "longitud":
                    autoescuela.setLongitud(((Number) value).floatValue());
                    break;
                default:
                    throw new BadRequestException();
            }
        };

        Autoescuela autoescuelaActualizada = autoescuelaRepository.save(autoescuela);
        return modelMapper.map(autoescuelaActualizada, AutoescuelaDetailOutDto.class);
    }
}
