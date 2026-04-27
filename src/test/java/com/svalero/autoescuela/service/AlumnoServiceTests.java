package com.svalero.autoescuela.service;

import com.svalero.autoescuela.model.Alumno;
import com.svalero.autoescuela.model.Autoescuela;
import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.AlumnoNotFoundException;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.repository.AlumnoRepository;
import com.svalero.autoescuela.repository.AutoescuelaRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumnoServiceTests {

    @InjectMocks
    private AlumnoService alumnoService;

    @Mock
    private AlumnoRepository alumnoRepository;

    @Mock
    private AutoescuelaRepository autoescuelaRepository;

    @Mock
    private ModelMapper modelMapper;


    @Test
    void testAdd_ok() throws AutoescuelaNotFoundException {
        AlumnoInDto inDto = new AlumnoInDto();
        AutoescuelaDetailOutDto autoDto = new AutoescuelaDetailOutDto();
        autoDto.setId(1L);

        Alumno alumno = new Alumno();
        Autoescuela autoescuela = new Autoescuela();
        Alumno saved = new Alumno();
        AlumnoDetailOutDto outDto = new AlumnoDetailOutDto();

        when(modelMapper.map(inDto, Alumno.class)).thenReturn(alumno);
        when(autoescuelaRepository.findById(1L)).thenReturn(Optional.of(autoescuela));
        when(alumnoRepository.save(alumno)).thenReturn(saved);
        when(modelMapper.map(saved, AlumnoDetailOutDto.class)).thenReturn(outDto);

        AlumnoDetailOutDto result = alumnoService.add(inDto, autoDto);

        assertNotNull(result);
        verify(alumnoRepository).save(alumno);
    }

    @Test
    void testDelete_ok() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno();
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));

        alumnoService.delete(1L);

        verify(alumnoRepository).delete(alumno);
    }

    @Test
    void testDelete_notFound() {
        when(alumnoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AlumnoNotFoundException.class,
                () -> alumnoService.delete(1L));
    }

    @Test
    void testFindAll_ok() {
        List<Alumno> alumnos = List.of(new Alumno(), new Alumno());
        List<AlumnoOutDto> outDtos = List.of(new AlumnoOutDto(), new AlumnoOutDto());

        when(alumnoRepository.findAll()).thenReturn(alumnos);
        when(modelMapper.map(
                eq(alumnos),
                any(Type.class)
        )).thenReturn(outDtos);

        List<AlumnoOutDto> result = alumnoService.findAll();

        assertEquals(2, result.size());
    }


    @Test
    void testFindById_ok() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno();
        AlumnoDetailOutDto dto = new AlumnoDetailOutDto();

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(modelMapper.map(alumno, AlumnoDetailOutDto.class)).thenReturn(dto);

        AlumnoDetailOutDto result = alumnoService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void testFindById_notFound() {
        when(alumnoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AlumnoNotFoundException.class,
                () -> alumnoService.findById(1L));
    }


    @Test
    void testModify_ok() throws AutoescuelaNotFoundException, AlumnoNotFoundException {
        AlumnoInDto inDto = new AlumnoInDto();
        AutoescuelaDetailOutDto autoDto = new AutoescuelaDetailOutDto();
        autoDto.setId(1L);

        Alumno alumno = new Alumno();
        Autoescuela autoescuela = new Autoescuela();
        Alumno saved = new Alumno();
        AlumnoDetailOutDto outDto = new AlumnoDetailOutDto();

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(autoescuelaRepository.findById(1L)).thenReturn(Optional.of(autoescuela));
        when(alumnoRepository.save(alumno)).thenReturn(saved);
        when(modelMapper.map(saved, AlumnoDetailOutDto.class)).thenReturn(outDto);

        AlumnoDetailOutDto result = alumnoService.modify(1L, inDto, autoDto);

        assertNotNull(result);
        verify(alumnoRepository).save(alumno);
    }


    @Test
    void testPatch_ok() throws AlumnoNotFoundException, AutoescuelaNotFoundException, BadRequestException, com.svalero.autoescuela.exception.BadRequestException {
        Alumno alumno = new Alumno();
        alumno.setNombre("Old");

        Map<String, Object> patch = new HashMap<>();
        patch.put("nombre", "Nuevo");

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(alumnoRepository.save(alumno)).thenReturn(alumno);
        when(modelMapper.map(alumno, AlumnoDetailOutDto.class))
                .thenReturn(new AlumnoDetailOutDto());

        AlumnoDetailOutDto result = alumnoService.patch(1L, patch);

        assertNotNull(result);
        assertEquals("Nuevo", alumno.getNombre());
    }
}
