package com.svalero.autoescuela.service;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.AlumnoNotFoundException;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.MatriculaNotFoundException;
import com.svalero.autoescuela.model.Alumno;
import com.svalero.autoescuela.model.Autoescuela;
import com.svalero.autoescuela.model.Matricula;
import com.svalero.autoescuela.repository.AlumnoRepository;
import com.svalero.autoescuela.repository.AutoescuelaRepository;
import com.svalero.autoescuela.repository.MatriculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTests    {

    @InjectMocks
    private MatriculaService matriculaService;

    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AlumnoRepository alumnoRepository;
    @Mock
    private AutoescuelaRepository autoescuelaRepository;

    @Test
    void testAdd_ok() throws Exception {

        MatriculaInDto inDto = new MatriculaInDto();

        AlumnoDetailOutDto alumnoDto = new AlumnoDetailOutDto();
        alumnoDto.setId(1L);

        AutoescuelaDetailOutDto autoDto = new AutoescuelaDetailOutDto();
        autoDto.setId(2L);

        Matricula matricula = new Matricula();
        Alumno alumno = new Alumno();
        Autoescuela autoescuela = new Autoescuela();

        when(modelMapper.map(inDto, Matricula.class)).thenReturn(matricula);
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(autoescuelaRepository.findById(2L)).thenReturn(Optional.of(autoescuela));
        when(matriculaRepository.save(matricula)).thenReturn(matricula);
        when(modelMapper.map(matricula, MatriculaDetailOutDto.class))
                .thenReturn(new MatriculaDetailOutDto());

        MatriculaDetailOutDto result =
                matriculaService.add(inDto, alumnoDto, autoDto);

        assertNotNull(result);
    }

    @Test
    void testAdd_alumnoNotFound() {
        MatriculaInDto inDto = new MatriculaInDto();
        AlumnoDetailOutDto alumnoDto = new AlumnoDetailOutDto();
        alumnoDto.setId(1L);
        AutoescuelaDetailOutDto autoDto = new AutoescuelaDetailOutDto();
        autoDto.setId(2L);

        when(modelMapper.map(inDto, Matricula.class)).thenReturn(new Matricula());
        when(alumnoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AlumnoNotFoundException.class,
                () -> matriculaService.add(inDto, alumnoDto, autoDto));
    }

    @Test
    void testAdd_autoescuelaNotFound() {
        MatriculaInDto inDto = new MatriculaInDto();
        AlumnoDetailOutDto alumnoDto = new AlumnoDetailOutDto();
        alumnoDto.setId(1L);
        AutoescuelaDetailOutDto autoDto = new AutoescuelaDetailOutDto();
        autoDto.setId(2L);

        when(alumnoRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Alumno()));

        when(autoescuelaRepository.findById(2L))
                .thenReturn(Optional.empty());

        when(autoescuelaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(AutoescuelaNotFoundException.class,
                () -> matriculaService.add(inDto, alumnoDto, autoDto));
    }


    @Test
    void testDelete_ok() throws MatriculaNotFoundException {
        Matricula m = new Matricula();
        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(m));

        matriculaService.delete(1L);

        verify(matriculaRepository).delete(m);
    }

    @Test
    void testDelete_notFound() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MatriculaNotFoundException.class,
                () -> matriculaService.delete(1L));
    }

    @Test
    void testFindById_ok() throws MatriculaNotFoundException {
        Matricula m = new Matricula();
        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(m));
        when(modelMapper.map(m, MatriculaDetailOutDto.class))
                .thenReturn(new MatriculaDetailOutDto());

        MatriculaDetailOutDto dto = matriculaService.findById(1L);

        assertNotNull(dto);
    }

    @Test
    void testFindById_notFound() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MatriculaNotFoundException.class,
                () -> matriculaService.findById(1L));
    }

    @Test
    void testModify_ok() throws Exception {

        Matricula matricula = new Matricula();
        MatriculaInDto inDto = new MatriculaInDto();

        AlumnoDetailOutDto alumnoDto = new AlumnoDetailOutDto();
        alumnoDto.setId(1L);
        AutoescuelaDetailOutDto autoDto = new AutoescuelaDetailOutDto();
        autoDto.setId(2L);

        Alumno alumno = new Alumno();
        Autoescuela autoescuela = new Autoescuela();

        when(matriculaRepository.findById(1L))
                .thenReturn(Optional.of(matricula));

        doNothing().when(modelMapper).map(any(MatriculaInDto.class), any(Matricula.class));

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(autoescuelaRepository.findById(2L)).thenReturn(Optional.of(autoescuela));
        when(matriculaRepository.save(matricula)).thenReturn(matricula);
        when(modelMapper.map(matricula, MatriculaDetailOutDto.class))
                .thenReturn(new MatriculaDetailOutDto());

        MatriculaDetailOutDto result =
                matriculaService.modify(1L, inDto, alumnoDto, autoDto);

        assertNotNull(result);
    }

    @Test
    void testModify_notFound() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MatriculaNotFoundException.class,
                () -> matriculaService.modify(1L,
                        new MatriculaInDto(),
                        new AlumnoDetailOutDto(),
                        new AutoescuelaDetailOutDto()));
    }

    @Test
    void testPatch_ok() throws MatriculaNotFoundException, AutoescuelaNotFoundException, AlumnoNotFoundException {

        Matricula matricula = new Matricula();
        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matricula));
        when(matriculaRepository.save(matricula)).thenReturn(matricula);
        when(modelMapper.map(matricula, MatriculaDetailOutDto.class))
                .thenReturn(new MatriculaDetailOutDto());

        Map<String, Object> patch = new HashMap<>();
        patch.put("modalidad", "B");
        patch.put("horasPracticas", 10);
        patch.put("completada", true);

        MatriculaDetailOutDto dto = matriculaService.patch(1L, patch);

        assertNotNull(dto);
        assertEquals("B", matricula.getModalidad());
        assertEquals(10, matricula.getHorasPracticas());
        assertTrue(matricula.isCompletada());
    }

    @Test
    void testPatch_notFound() {
        when(matriculaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MatriculaNotFoundException.class,
                () -> matriculaService.patch(1L, Map.of()));
    }

}