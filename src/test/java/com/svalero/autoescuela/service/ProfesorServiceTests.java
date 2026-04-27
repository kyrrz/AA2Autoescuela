package com.svalero.autoescuela.service;

import com.svalero.autoescuela.dto.AutoescuelaDetailOutDto;
import com.svalero.autoescuela.dto.AutoescuelaOutDto;
import com.svalero.autoescuela.dto.ProfesorDetailOutDto;
import com.svalero.autoescuela.dto.ProfesorInDto;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.ProfesorNotFoundException;
import com.svalero.autoescuela.model.Autoescuela;
import com.svalero.autoescuela.model.Profesor;
import com.svalero.autoescuela.repository.AutoescuelaRepository;
import com.svalero.autoescuela.repository.ProfesorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProfesorServiceTests {

    @InjectMocks
    private ProfesorService profesorService;

    @Mock
    private ProfesorRepository profesorRepository;

    @Mock
    private AutoescuelaRepository autoescuelaRepository;

    @Mock
    private ModelMapper modelMapper;


    @Test
    void testAdd_ok() throws AutoescuelaNotFoundException {

        ProfesorInDto inDto = new ProfesorInDto();
        inDto.setNombre("Juan");

        AutoescuelaDetailOutDto autoDto = new AutoescuelaDetailOutDto();
        autoDto.setId(1L);

        List<AutoescuelaDetailOutDto> autoDtos = List.of(autoDto);

        Profesor profesor = new Profesor();
        profesor.setNombre("Juan");

        Autoescuela autoescuela = new Autoescuela();
        autoescuela.setId(1L);

        when(modelMapper.map(inDto, Profesor.class)).thenReturn(profesor);
        when(autoescuelaRepository.findAllById(List.of(1L))).thenReturn(List.of(autoescuela));
        when(profesorRepository.save(profesor)).thenReturn(profesor);
        when(modelMapper.map(profesor, ProfesorDetailOutDto.class))
                .thenReturn(new ProfesorDetailOutDto());

        ProfesorDetailOutDto result = profesorService.add(inDto, autoDtos);

        assertNotNull(result);
        verify(profesorRepository).save(profesor);
    }

    @Test
    void testDelete_ok() throws ProfesorNotFoundException {

        Profesor profesor = new Profesor();
        profesor.setId(1L);

        when(profesorRepository.findById(1L)).thenReturn(Optional.of(profesor));

        profesorService.delete(1L);

        verify(profesorRepository).delete(profesor);
    }

    @Test
    void testDelete_notFound() {

        when(profesorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProfesorNotFoundException.class,
                () -> profesorService.delete(1L));
    }

    @Test
    void testFindAll_ok() {

        List<Profesor> profesors = List.of(
                new Profesor(),
                new Profesor());

        when(profesorRepository.findAll()).thenReturn(profesors);

        List<Profesor> result = profesorService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void testGetAutoescuelas_ok() throws ProfesorNotFoundException {

        Autoescuela autoescuela = new Autoescuela();
        autoescuela.setId(1L);

        when(autoescuelaRepository.findAutoescuelasByProfesorId(1L))
                .thenReturn(List.of(autoescuela));

        when(modelMapper.map(autoescuela, AutoescuelaOutDto.class))
                .thenReturn(new AutoescuelaOutDto());

        List<AutoescuelaOutDto> result =
                profesorService.getAutoescuelas(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testFindById_ok() throws ProfesorNotFoundException {

        Profesor profesor = new Profesor();
        profesor.setId(1L);

        when(profesorRepository.findById(1L)).thenReturn(Optional.of(profesor));
        when(modelMapper.map(profesor, ProfesorDetailOutDto.class))
                .thenReturn(new ProfesorDetailOutDto());

        ProfesorDetailOutDto result = profesorService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void testFindById_notFound() {

        when(profesorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProfesorNotFoundException.class,
                () -> profesorService.findById(1L));
    }

    @Test
    void testModify_ok() throws ProfesorNotFoundException {

        Profesor profesor = new Profesor();
        profesor.setId(1L);

        ProfesorInDto inDto = new ProfesorInDto();
        inDto.setNombre("Nuevo");

        AutoescuelaDetailOutDto autoDto = new AutoescuelaDetailOutDto();
        autoDto.setId(2L);

        Autoescuela auto = new Autoescuela();
        auto.setId(2L);

        when(profesorRepository.findById(1L))
                .thenReturn(Optional.of(profesor));

        doNothing().when(modelMapper).map(any(ProfesorInDto.class), any(Profesor.class));

        when(autoescuelaRepository.findAllById(List.of(2L)))
                .thenReturn(List.of(auto));

        when(profesorRepository.save(profesor))
                .thenReturn(profesor);

        when(modelMapper.map(profesor, ProfesorDetailOutDto.class))
                .thenReturn(new ProfesorDetailOutDto());

        ProfesorDetailOutDto result =
                profesorService.modify(1L, inDto, List.of(autoDto));

        assertNotNull(result);
    }

    @Test
    void testPatch_ok() throws Exception {

        Profesor profesor = new Profesor();
        profesor.setId(1L);

        when(profesorRepository.findById(1L))
                .thenReturn(Optional.of(profesor));
        when(profesorRepository.save(any()))
                .thenReturn(profesor);
        when(modelMapper.map(any(), eq(ProfesorDetailOutDto.class)))
                .thenReturn(new ProfesorDetailOutDto());

        Map<String, Object> patch = new HashMap<>();
        patch.put("nombre", "Pedro");
        patch.put("activo", true);

        ProfesorDetailOutDto result =
                profesorService.patch(1L, patch);

        assertNotNull(result);
        assertEquals("Pedro", profesor.getNombre());
    }

    @Test
    void testPatch_notFound() {

        when(profesorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProfesorNotFoundException.class,
                () -> profesorService.patch(1L, Map.of()));
    }











}