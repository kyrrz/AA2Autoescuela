package com.svalero.autoescuela.service;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.AlumnoNotFoundException;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.MatriculaNotFoundException;
import com.svalero.autoescuela.model.Alumno;
import com.svalero.autoescuela.model.Autoescuela;
import com.svalero.autoescuela.model.Coche;
import com.svalero.autoescuela.model.Matricula;
import com.svalero.autoescuela.repository.AlumnoRepository;
import com.svalero.autoescuela.repository.AutoescuelaRepository;
import com.svalero.autoescuela.repository.MatriculaRepository;
import com.svalero.autoescuela.specification.MatriculaSpecification;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private AutoescuelaRepository autoescuelaRepository;

    public MatriculaDetailOutDto add(MatriculaInDto matriculaInDto, AlumnoDetailOutDto alumnoDetailOutDto, AutoescuelaDetailOutDto autoescuelaDetailOutDto)
            throws AlumnoNotFoundException, MatriculaNotFoundException, AutoescuelaNotFoundException {

        Matricula m = modelMapper.map(matriculaInDto, Matricula.class);
//        m.setModalidad(matriculaInDto.getModalidad());
//        m.setTipoMatricula(matriculaInDto.getTipoMatricula());
//        m.setFechaInicio(matriculaInDto.getFechaInicio());
//        m.setFechaFinal(matriculaInDto.getFechaFinal());
//        m.setPrecio(matriculaInDto.getPrecio());
//        m.setHorasPracticas(matriculaInDto.getHorasPracticas());
//        m.setHorasTeoricas(matriculaInDto.getHorasTeoricas());
//        m.setCompletada(matriculaInDto.isCompletada());
//        m.setObservaciones(matriculaInDto.getObservaciones());
        Alumno alumno = alumnoRepository.findById(alumnoDetailOutDto.getId()).orElseThrow(AlumnoNotFoundException::new);
        Autoescuela autoescuela = autoescuelaRepository.findById(autoescuelaDetailOutDto.getId()).orElseThrow(AutoescuelaNotFoundException::new);
        m.setAlumno(alumno);
        m.setAutoescuela(autoescuela);

        Matricula matricula = matriculaRepository.save(m);
        return modelMapper.map(matricula, MatriculaDetailOutDto.class) ;
    }

    public void delete( long id ) throws MatriculaNotFoundException {
        Matricula m = matriculaRepository.findById(id)
                .orElseThrow(MatriculaNotFoundException::new);
        matriculaRepository.delete(m);
    }

    public List<Matricula> findAll(){
    return matriculaRepository.findAll();
    }

    public MatriculaDetailOutDto findById(long id) throws MatriculaNotFoundException {
        return matriculaRepository.findById(id)
                .map(matricula -> modelMapper.map(matricula, MatriculaDetailOutDto.class))
                .orElseThrow(MatriculaNotFoundException::new);
    }

//    public List<MatriculaOutDto> findByFiltros(String modalidad, String tipoMatricula, Integer horasPracticas, Integer horasTeoricas)
//    {
//
//        if (modalidad != null && tipoMatricula != null && horasPracticas != null && horasTeoricas != null) {
//            List<Matricula> matriculas = matriculaRepository.findByModalidadAndTipoMatriculaAndHorasPracticasAndHorasTeoricas(
//                    modalidad, tipoMatricula, horasPracticas, horasTeoricas);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//        if (tipoMatricula != null && horasPracticas != null && horasTeoricas != null) {
//            List<Matricula> matriculas = matriculaRepository.findByTipoMatriculaAndHorasPracticasAndHorasTeoricas(
//                    tipoMatricula, horasPracticas, horasTeoricas);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//        if (modalidad != null && horasPracticas != null && horasTeoricas != null) {
//            List<Matricula> matriculas = matriculaRepository.findByModalidadAndHorasPracticasAndHorasTeoricas(
//                    modalidad, horasPracticas, horasTeoricas);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//        if (modalidad != null && tipoMatricula != null && horasTeoricas != null) {
//            List<Matricula> matriculas = matriculaRepository.findByModalidadAndTipoMatriculaAndHorasTeoricas(
//                    modalidad, tipoMatricula, horasTeoricas);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//        if (modalidad != null && tipoMatricula != null && horasPracticas != null) {
//            List<Matricula> matriculas = matriculaRepository.findByModalidadAndTipoMatriculaAndHorasPracticas(
//                    modalidad, tipoMatricula, horasPracticas);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//
//        if (horasPracticas != null && horasTeoricas != null ) {
//            List<Matricula> matriculas = matriculaRepository.findByHorasPracticasAndHorasTeoricas(
//                     horasPracticas, horasTeoricas);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//        if (tipoMatricula != null && horasTeoricas != null ) {
//            List<Matricula> matriculas = matriculaRepository.findByTipoMatriculaAndHorasTeoricas(
//                    tipoMatricula, horasTeoricas);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//        if (tipoMatricula != null && horasPracticas != null ) {
//            List<Matricula> matriculas = matriculaRepository.findByTipoMatriculaAndHorasPracticas(
//                    tipoMatricula, horasPracticas);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//        if (modalidad != null && horasTeoricas != null ) {
//            List<Matricula> matriculas = matriculaRepository.findByModalidadAndHorasTeoricas(
//                    modalidad, horasTeoricas);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//        if (modalidad != null && horasPracticas != null ) {
//            List<Matricula> matriculas = matriculaRepository.findByModalidadAndHorasPracticas(
//                    modalidad, horasPracticas);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//        if (modalidad != null && tipoMatricula != null ) {
//            List<Matricula> matriculas = matriculaRepository.findByModalidadAndTipoMatricula(
//                    modalidad, tipoMatricula);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//
//        if (horasTeoricas != null ) {
//            List<Matricula> matriculas = matriculaRepository.findByHorasTeoricas(
//                    horasTeoricas);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//        if (horasPracticas != null ) {
//            List<Matricula> matriculas = matriculaRepository.findByHorasPracticas(
//                    horasPracticas);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//        if (tipoMatricula != null ) {
//            List<Matricula> matriculas = matriculaRepository.findByTipoMatricula(
//                    tipoMatricula);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//        if (modalidad != null ) {
//            List<Matricula> matriculas = matriculaRepository.findByModalidad(
//                    modalidad);
//            List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//            return mod;
//        }
//
//
//
//        List<Matricula> matriculas = matriculaRepository.findAll();
//        List<MatriculaOutDto> mod = modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
//
//        return mod;
//    }

    public List<MatriculaOutDto> findByFiltros(String modalidad, String tipoMatricula, Integer horasPracticas, Integer horasTeoricas) {

        Specification<Matricula> spec = Specification
                .where(MatriculaSpecification.modalidadEquals(modalidad))
                .and(MatriculaSpecification.tipoMatriculaEquals(tipoMatricula))
                .and(MatriculaSpecification.horasPracticasEquals(horasPracticas))
                .and(MatriculaSpecification.horasTeoricasEquals(horasTeoricas));

        List<Matricula> matriculas = matriculaRepository.findAll(spec);

        return modelMapper.map(matriculas, new TypeToken<List<MatriculaOutDto>>() {}.getType());
    }

    public MatriculaDetailOutDto modify(long id, MatriculaInDto matriculaInDto, AlumnoDetailOutDto alumnoDetailOutDto, AutoescuelaDetailOutDto autoescuelaDetailOutDto)
            throws MatriculaNotFoundException, AlumnoNotFoundException, AutoescuelaNotFoundException {
        Matricula matriculaAnterior = matriculaRepository.findById(id)
                .orElseThrow(MatriculaNotFoundException::new);

        modelMapper.map(matriculaInDto, matriculaAnterior);
//        m.setTipoMatricula(matriculaInDto.getTipoMatricula());
//        m.setFechaInicio(matriculaInDto.getFechaInicio());
//        m.setFechaFinal(matriculaInDto.getFechaFinal());
//        m.setPrecio(matriculaInDto.getPrecio());
//        m.setHorasPracticas(matriculaInDto.getHorasPracticas());
//        m.setHorasTeoricas(matriculaInDto.getHorasTeoricas());
//        m.setCompletada(matriculaInDto.isCompletada());
//        m.setObservaciones(matriculaInDto.getObservaciones());
        Alumno alumno = alumnoRepository.findById(alumnoDetailOutDto.getId())
                .orElseThrow(AlumnoNotFoundException::new);
        Autoescuela autoescuela =  autoescuelaRepository.findById(autoescuelaDetailOutDto.getId())
                .orElseThrow(AutoescuelaNotFoundException::new);
        matriculaAnterior.setAlumno(alumno);
        matriculaAnterior.setAutoescuela(autoescuela);
        matriculaAnterior.setId(id);

        Matricula matriculaSaved = matriculaRepository.save(matriculaAnterior);

        return modelMapper.map(matriculaSaved, MatriculaDetailOutDto.class);
    }

    public MatriculaDetailOutDto patch(Long id, Map<String, Object> patch)
            throws MatriculaNotFoundException, AutoescuelaNotFoundException, AlumnoNotFoundException {

        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(MatriculaNotFoundException::new);

        for (Map.Entry<String, Object> entry : patch.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            switch (key) {
                case "fechaInicio":
                    matricula.setFechaInicio(LocalDate.parse((String) value));
                    break;
                case "fechaFinal":
                    matricula.setFechaFinal(LocalDate.parse((String) value));
                    break;
                case "modalidad":
                    matricula.setModalidad((String) value);
                    break;
                case "tipoMatricula":
                    matricula.setTipoMatricula((String) value);
                    break;
                case "precio":
                    matricula.setPrecio(((Number) value).floatValue());
                    break;
                case "horasPracticas":
                    matricula.setHorasPracticas(((Number) value).intValue());
                    break;
                case "horasTeoricas":
                    matricula.setHorasTeoricas(((Number) value).intValue());
                    break;
                case "completada":
                    matricula.setCompletada((Boolean) value);
                    break;
                case "alumnoId":
                    Long alumnoId = ((Number) value).longValue();
                    Alumno alumno = alumnoRepository.findById(alumnoId)
                                .orElseThrow(AlumnoNotFoundException::new);
                    matricula.setAlumno(alumno);
                    break;
                case "autoescuelaId":
                    Long autoescuelaId = ((Number) value).longValue();
                    Autoescuela autoescuela = autoescuelaRepository.findById(autoescuelaId)
                                .orElseThrow(AutoescuelaNotFoundException::new);
                    matricula.setAutoescuela(autoescuela);
                    break;
                case "observaciones":
                    matricula.setObservaciones((String) value);
                    break;
            }
        };

        Matricula matriculaActualizada = matriculaRepository.save(matricula);
        return modelMapper.map(matriculaActualizada, MatriculaDetailOutDto.class);
    }

}


