package com.svalero.autoescuela.service;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.model.*;
import com.svalero.autoescuela.repository.*;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutoescuelaServiceTests {

    @InjectMocks
    private AutoescuelaService autoescuelaService;

    @Mock
    private AutoescuelaRepository autoescuelaRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CocheRepository cocheRepository;

    @Mock
    private ProfesorRepository profesorRepository;

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private AlumnoRepository alumnoRepository;


    @Test
    void testAdd_ok() {
        AutoescuelaInDto inDto = new AutoescuelaInDto();
        inDto.setNombre("Autoescuela Test");

        Autoescuela autoescuela = new Autoescuela();
        Autoescuela savedAutoescuela = new Autoescuela();
        savedAutoescuela.setId(1L);
        savedAutoescuela.setNombre("Autoescuela Test");

        AutoescuelaDetailOutDto outDto = new AutoescuelaDetailOutDto();
        outDto.setId(1L);
        outDto.setNombre("Autoescuela Test");

        when(modelMapper.map(inDto, Autoescuela.class)).thenReturn(autoescuela);
        when(autoescuelaRepository.save(autoescuela)).thenReturn(savedAutoescuela);
        when(modelMapper.map(savedAutoescuela, AutoescuelaDetailOutDto.class)).thenReturn(outDto);

        AutoescuelaDetailOutDto result = autoescuelaService.add(inDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Autoescuela Test", result.getNombre());

        verify(autoescuelaRepository).save(autoescuela);
    }

    @Test
    void testDelete_ok() throws AutoescuelaNotFoundException {
        Autoescuela autoescuela = new Autoescuela();
        autoescuela.setId(1L);

        when(autoescuelaRepository.findById(1L)).thenReturn(Optional.of(autoescuela));
        doNothing().when(autoescuelaRepository).delete(autoescuela);

        autoescuelaService.delete(1L);

        verify(autoescuelaRepository).delete(autoescuela);
    }

    @Test
    void testDelete_notFound() {
        when(autoescuelaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AutoescuelaNotFoundException.class, () -> autoescuelaService.delete(1L));
    }

    @Test
    void testFindById_ok() throws AutoescuelaNotFoundException {
        Autoescuela autoescuela = new Autoescuela();
        autoescuela.setId(1L);
        autoescuela.setNombre("Test");
        autoescuela.setCoches(Collections.emptyList());
        autoescuela.setProfesores(Collections.emptyList());

        when(autoescuelaRepository.findById(1L)).thenReturn(Optional.of(autoescuela));
        lenient().when(modelMapper.map(any(), eq(CocheOutDto.class))).thenReturn(new CocheOutDto());
        lenient().when(modelMapper.map(any(), eq(ProfesorOutDto.class))).thenReturn(new ProfesorOutDto());

        AutoescuelaDetailOutDto dto = autoescuelaService.findById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test", dto.getNombre());
    }

    @Test
    void testFindById_notFound() {
        when(autoescuelaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AutoescuelaNotFoundException.class, () -> autoescuelaService.findById(1L));
    }

    @Test
    void testFindAll_ok() {
        List<Autoescuela> autoescuelas = List.of(
                new Autoescuela(1L, "Autoescuela Test", "Calle 123", "Reus", "911753917", "auto1@gmail.com",
                        100, 4.5f, LocalDate.of(2010, 1, 2), true,100,100, null, null, null, null),
                new Autoescuela(2L, "Autoescuela Test 2 ", "Calle 1234", "Reus", "911753918", "auto2@gmail.com",
                        120, 4.1f, LocalDate.of(2010, 1, 1), true,100,100, null, null, null, null)
        );
        List<AutoescuelaOutDto> modelMapperOut = List.of(
                new AutoescuelaOutDto(1L, "Autoescuela Test", "Calle 123", "Reus", "911753917", "auto1@gmail.com", 4.5f, true,100,100),
                new AutoescuelaOutDto(2L, "Autoescuela Test 2", "Calle 1234", "Reus", "911753917", "auto1@gmail.com", 4.1f, false,100,100)
        );

        when(autoescuelaRepository.findAll(any(Specification.class))).thenReturn(autoescuelas);


        when(modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>() {
        }.getType()))
                .thenReturn(modelMapperOut);

        List<AutoescuelaOutDto> result = autoescuelaService.findByFiltros(null, null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Reus", result.getFirst().getCiudad());
        assertEquals(4.5f, result.getFirst().getRating());
        assertEquals("Autoescuela Test 2", result.getLast().getNombre());
        assertTrue(result.getFirst().isActiva());

        verify(autoescuelaRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testFindByFiltros_ok() {
        List<Autoescuela> autoescuelas = List.of(
                new Autoescuela(1L, "Autoescuela Test", "Calle 123", "Reus", "911753917", "auto1@gmail.com",
                        100, 4.5f, LocalDate.of(2010, 1, 2),true, 100, 100,null,null,null,null),
                new Autoescuela(2L, "Autoescuela Test 2 ", "Calle 1234", "Reus", "911753917", "auto1@gmail.com",
                100, 4.1f, LocalDate.of(2010, 1, 2),true,100,100,null,null,null,null)
        );
        List<AutoescuelaOutDto> modelMapperOut = List.of(
                new AutoescuelaOutDto(1L, "Autoescuela Test", "Calle 123", "Reus", "911753917", "auto1@gmail.com", 4.5f,true,100,100)
        );

        when(autoescuelaRepository.findAll(any(Specification.class))).thenReturn(autoescuelas);

        when(modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>(){}.getType()))
                .thenReturn(modelMapperOut);

        List<AutoescuelaOutDto> result = autoescuelaService.findByFiltros("Reus", 4.5f, true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Reus", result.getFirst().getCiudad());
        assertEquals(4.5f, result.getFirst().getRating());
        assertTrue(result.getFirst().isActiva());

        verify(autoescuelaRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testFindByFiltros_ratingFilter() {
        Autoescuela a = new Autoescuela();
        a.setId(1L);
        a.setCiudad("Madrid");
        a.setRating(7.2f);
        a.setActiva(true);

        List<Autoescuela> autoescuelas = List.of(a);
        List<AutoescuelaOutDto> dtos = List.of(new AutoescuelaOutDto());

        when(autoescuelaRepository.findAll(any(Specification.class))).thenReturn(autoescuelas);
        when(modelMapper.map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>(){}.getType())).thenReturn(dtos);

        List<AutoescuelaOutDto> result = autoescuelaService.findByFiltros(null, 7.5f, null);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(autoescuelaRepository, times(1)).findAll(any(Specification.class));
        verify(modelMapper, times(1)).map(autoescuelas, new TypeToken<List<AutoescuelaOutDto>>(){}.getType());
    }

    @Test
    void testModify_ok() throws AutoescuelaNotFoundException {
        AutoescuelaInDto inDto = new AutoescuelaInDto();
        inDto.setNombre("Modificado");

        Autoescuela autoescuelaExistente = new Autoescuela();
        autoescuelaExistente.setId(1L);

        Autoescuela autoescuelaModificada = new Autoescuela();
        autoescuelaModificada.setId(1L);
        autoescuelaModificada.setNombre("Modificado");

        AutoescuelaDetailOutDto outDto = new AutoescuelaDetailOutDto();
        outDto.setId(1L);
        outDto.setNombre("Modificado");

        when(autoescuelaRepository.findById(1L)).thenReturn(Optional.of(autoescuelaExistente));
        doAnswer(invocation -> {
            AutoescuelaInDto source = invocation.getArgument(0);
            Autoescuela destination = invocation.getArgument(1);
            destination.setNombre(source.getNombre());
            return null;
        }).when(modelMapper).map(inDto, autoescuelaExistente);

        when(autoescuelaRepository.save(autoescuelaExistente)).thenReturn(autoescuelaModificada);
        when(modelMapper.map(autoescuelaModificada, AutoescuelaDetailOutDto.class)).thenReturn(outDto);

        AutoescuelaDetailOutDto result = autoescuelaService.modify(1L, inDto);

        assertNotNull(result);
        assertEquals("Modificado", result.getNombre());
        assertEquals(1L, result.getId());
    }

    @Test
    void testModify_notFound() {
        AutoescuelaInDto inDto = new AutoescuelaInDto();
        when(autoescuelaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AutoescuelaNotFoundException.class, () -> autoescuelaService.modify(1L, inDto));
    }

    @Test
    void testPatch_ok() throws AutoescuelaNotFoundException, com.svalero.autoescuela.exception.BadRequestException {
        Autoescuela autoescuela = new Autoescuela();
        autoescuela.setId(1L);
        autoescuela.setNombre("Nombre Original");
        autoescuela.setDireccion("Dir Original");
        autoescuela.setCiudad("Ciudad Original");
        autoescuela.setTelefono("123");
        autoescuela.setEmail("email@old.com");
        autoescuela.setCapacidad(50);
        autoescuela.setRating(3.5f);
        autoescuela.setFechaApertura(LocalDate.of(2020,1,1));
        autoescuela.setActiva(true);

        Map<String, Object> patch = new HashMap<>();
        patch.put("nombre", "Nuevo Nombre");
        patch.put("capacidad", 100);
        patch.put("activa", false);
        patch.put("fechaApertura", "2023-01-01");

        Autoescuela autoescuelaActualizada = new Autoescuela();
        autoescuelaActualizada.setId(1L);
        autoescuelaActualizada.setNombre("Nuevo Nombre");
        autoescuelaActualizada.setCapacidad(100);
        autoescuelaActualizada.setActiva(false);
        autoescuelaActualizada.setFechaApertura(LocalDate.of(2023, 1, 1));

        AutoescuelaDetailOutDto outDto = new AutoescuelaDetailOutDto();
        outDto.setId(1L);
        outDto.setNombre("Nuevo Nombre");
        outDto.setActiva(false);


        when(autoescuelaRepository.findById(1L)).thenReturn(Optional.of(autoescuela));
        when(autoescuelaRepository.save(any(Autoescuela.class))).thenReturn(autoescuelaActualizada);
        when(modelMapper.map(autoescuelaActualizada, AutoescuelaDetailOutDto.class)).thenReturn(outDto);

        AutoescuelaDetailOutDto result = autoescuelaService.patch(1L, patch);

        assertNotNull(result);
        assertEquals("Nuevo Nombre", result.getNombre());
        assertFalse(result.isActiva());
    }

    @Test
    void testPatch_notFound() {
        when(autoescuelaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AutoescuelaNotFoundException.class, () -> autoescuelaService.patch(1L, Map.of("nombre", "test")));
    }

    @Test
    void testGetCoches_ok() throws AutoescuelaNotFoundException {


        Coche coche1 = new Coche();
        coche1.setId(1L);
        coche1.setMarca("FIAT");
        coche1.setMatricula("1234FCB");

        Coche coche2 = new Coche();
        coche2.setId(2L);
        coche2.setMarca("SEAT");
        coche2.setMatricula("5678DBF");

        List<Coche> coches = Arrays.asList(coche1, coche2);

        CocheOutDto cocheOutDto1 = new CocheOutDto();
        cocheOutDto1.setId(1L);
        cocheOutDto1.setMarca("FIAT");

        CocheOutDto cocheOutDto2 = new CocheOutDto();
        cocheOutDto2.setId(2L);
        cocheOutDto2.setMarca("SEAT");

        when(cocheRepository.findCochesByAutoescuelaId(1L)).thenReturn(coches);
        when(modelMapper.map(coche1, CocheOutDto.class)).thenReturn(cocheOutDto1);
        when(modelMapper.map(coche2, CocheOutDto.class)).thenReturn(cocheOutDto2);

        List<CocheOutDto> result = autoescuelaService.getCoches(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("FIAT", result.get(0).getMarca());
        assertEquals("SEAT", result.get(1).getMarca());

        verify(cocheRepository, times(1)).findCochesByAutoescuelaId(1L);
        verify(modelMapper, times(2)).map(any(Coche.class), eq(CocheOutDto.class));
    }

    @Test
    void getCoches_vacio() throws AutoescuelaNotFoundException {

        when(cocheRepository.findCochesByAutoescuelaId(1L)).thenReturn(Collections.emptyList());

        List<CocheOutDto> result = autoescuelaService.getCoches(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(cocheRepository, times(1)).findCochesByAutoescuelaId(1L);
        verify(modelMapper, never()).map(any(Coche.class), eq(CocheOutDto.class));
    }

    @Test
    void getMatriculaso_k() throws AutoescuelaNotFoundException {

        Matricula matricula1 = new Matricula();
        matricula1.setId(1L);

        Matricula matricula2 = new Matricula();
        matricula2.setId(2L);

        List<Matricula> matriculas = Arrays.asList(matricula1, matricula2);

        MatriculaOutDto matriculaOutDto1 = new MatriculaOutDto();
        matriculaOutDto1.setId(1L);

        MatriculaOutDto matriculaOutDto2 = new MatriculaOutDto();
        matriculaOutDto2.setId(2L);

        when(matriculaRepository.findMatriculaByAutoescuelaId(1L)).thenReturn(matriculas);
        when(modelMapper.map(matricula1, MatriculaOutDto.class)).thenReturn(matriculaOutDto1);
        when(modelMapper.map(matricula2, MatriculaOutDto.class)).thenReturn(matriculaOutDto2);

        List<MatriculaOutDto> result = autoescuelaService.getMatriculas(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(matriculaRepository, times(1)).findMatriculaByAutoescuelaId(1L);
        verify(modelMapper, times(2)).map(any(Matricula.class), eq(MatriculaOutDto.class));
    }

    @Test
    void getMatriculas_vacio() throws AutoescuelaNotFoundException {
        when(matriculaRepository.findMatriculaByAutoescuelaId(1L)).thenReturn(Collections.emptyList());

        List<MatriculaOutDto> result = autoescuelaService.getMatriculas(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(matriculaRepository, times(1)).findMatriculaByAutoescuelaId(1L);
        verify(modelMapper, never()).map(any(Matricula.class), eq(MatriculaOutDto.class));
    }

    @Test
    void getProfesores_ok() throws AutoescuelaNotFoundException {

        Profesor profesor1 = new Profesor();
        profesor1.setId(1L);
        profesor1.setNombre("Juan");

        Profesor profesor2 = new Profesor();
        profesor2.setId(2L);
        profesor2.setNombre("María");

        List<Profesor> profesores = Arrays.asList(profesor1, profesor2);

        ProfesorOutDto profesorOutDto1 = new ProfesorOutDto();
        profesorOutDto1.setId(1L);
        profesorOutDto1.setNombre("Juan");

        ProfesorOutDto profesorOutDto2 = new ProfesorOutDto();
        profesorOutDto2.setId(2L);
        profesorOutDto2.setNombre("María");

        when(profesorRepository.findProfesoresByAutoescuelaId(1L)).thenReturn(profesores);
        when(modelMapper.map(profesor1, ProfesorOutDto.class)).thenReturn(profesorOutDto1);
        when(modelMapper.map(profesor2, ProfesorOutDto.class)).thenReturn(profesorOutDto2);

        List<ProfesorOutDto> result = autoescuelaService.getProfesores(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        assertEquals("María", result.get(1).getNombre());

        verify(profesorRepository, times(1)).findProfesoresByAutoescuelaId(1L);
        verify(modelMapper, times(2)).map(any(Profesor.class), eq(ProfesorOutDto.class));
    }

    @Test
    void getProfesores_vacia() throws AutoescuelaNotFoundException {
        when(profesorRepository.findProfesoresByAutoescuelaId(1L)).thenReturn(Collections.emptyList());

        List<ProfesorOutDto> result = autoescuelaService.getProfesores(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(profesorRepository, times(1)).findProfesoresByAutoescuelaId(1L);
        verify(modelMapper, never()).map(any(Profesor.class), eq(ProfesorOutDto.class));
    }

    @Test
    void getMatriculasCompletada_ok() throws AutoescuelaNotFoundException {
        Matricula matricula1 = new Matricula();
        matricula1.setId(1L);
        matricula1.setCompletada(true);

        Matricula matricula2 = new Matricula();
        matricula2.setId(2L);
        matricula2.setCompletada(true);

        List<Matricula> matriculas = Arrays.asList(matricula1, matricula2);

        MatriculaOutDto matriculaOutDto1 = new MatriculaOutDto();
        matriculaOutDto1.setId(1L);

        MatriculaOutDto matriculaOutDto2 = new MatriculaOutDto();
        matriculaOutDto2.setId(2L);

        when(matriculaRepository.findMatriculaCompletasByAutoescuelaId(1L)).thenReturn(matriculas);
        when(modelMapper.map(matricula1, MatriculaOutDto.class)).thenReturn(matriculaOutDto1);
        when(modelMapper.map(matricula2, MatriculaOutDto.class)).thenReturn(matriculaOutDto2);

        List<MatriculaOutDto> result = autoescuelaService.getMatriculasCompletas(1L);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(matriculaRepository, times(1)).findMatriculaCompletasByAutoescuelaId(1L);
        verify(modelMapper, times(2)).map(any(Matricula.class), eq(MatriculaOutDto.class));
    }

    @Test
    void getMatriculasCompletas_vacio() throws AutoescuelaNotFoundException {
        when(matriculaRepository.findMatriculaCompletasByAutoescuelaId(1L)).thenReturn(Collections.emptyList());

        List<MatriculaOutDto> result = autoescuelaService.getMatriculasCompletas(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(matriculaRepository, times(1)).findMatriculaCompletasByAutoescuelaId(1L);
        verify(modelMapper, never()).map(any(Matricula.class), eq(MatriculaOutDto.class));
    }

    @Test
    void getAlumnosSuspensos_ok() throws AutoescuelaNotFoundException {
        Alumno alumno1 = new Alumno();
        alumno1.setId(1L);
        alumno1.setNombre("Pedro");

        Alumno alumno2 = new Alumno();
        alumno2.setId(2L);
        alumno2.setNombre("Ana");

        List<Alumno> alumnos = Arrays.asList(alumno1, alumno2);

        AlumnoOutDto alumnoOutDto1 = new AlumnoOutDto();
        alumnoOutDto1.setId(1L);
        alumnoOutDto1.setNombre("Pedro");

        AlumnoOutDto alumnoOutDto2 = new AlumnoOutDto();
        alumnoOutDto2.setId(2L);
        alumnoOutDto2.setNombre("Ana");

        when(alumnoRepository.findAlumnosSuspensosByAutoescuela(1L)).thenReturn(alumnos);
        when(modelMapper.map(alumno1, AlumnoOutDto.class)).thenReturn(alumnoOutDto1);
        when(modelMapper.map(alumno2, AlumnoOutDto.class)).thenReturn(alumnoOutDto2);

        List<AlumnoOutDto> result = autoescuelaService.getAlumnosSuspensos(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pedro", result.get(0).getNombre());
        assertEquals("Ana", result.get(1).getNombre());

        verify(alumnoRepository, times(1)).findAlumnosSuspensosByAutoescuela(1L);
        verify(modelMapper, times(2)).map(any(Alumno.class), eq(AlumnoOutDto.class));
    }
    @Test
    void getAlumnosSuspensos_vacio() throws AutoescuelaNotFoundException {
        when(alumnoRepository.findAlumnosSuspensosByAutoescuela(1L)).thenReturn(Collections.emptyList());

        List<AlumnoOutDto> result = autoescuelaService.getAlumnosSuspensos(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(alumnoRepository, times(1)).findAlumnosSuspensosByAutoescuela(1L);
        verify(modelMapper, never()).map(any(Alumno.class), eq(AlumnoOutDto.class));
    }




}
