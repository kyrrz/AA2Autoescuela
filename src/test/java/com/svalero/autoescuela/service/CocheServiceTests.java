package com.svalero.autoescuela.service;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.CocheNotFoundException;
import com.svalero.autoescuela.model.Autoescuela;
import com.svalero.autoescuela.model.Coche;
import com.svalero.autoescuela.repository.AutoescuelaRepository;
import com.svalero.autoescuela.repository.CocheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
class CocheServiceTests {

    @Mock
    private CocheRepository cocheRepository;

    @Mock
    private AutoescuelaRepository autoescuelaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CocheService cocheService;


    @Test
    void testAdd_ok() throws AutoescuelaNotFoundException {
        CocheInDto cocheInDto = new CocheInDto();
        AutoescuelaDetailOutDto autoescuelaDto = new AutoescuelaDetailOutDto();
        autoescuelaDto.setId(1L);

        Autoescuela autoescuela = new Autoescuela();
        autoescuela.setId(1L);

        Coche cocheSaved = new Coche();
        cocheSaved.setId(10L);

        CocheDetailOutDto outDto = new CocheDetailOutDto();
        outDto.setId(10L);

        when(autoescuelaRepository.findById(1L)).thenReturn(Optional.of(autoescuela));
        doNothing().when(modelMapper)
                .map(any(CocheInDto.class), any(Coche.class));
        ;
        when(cocheRepository.save(any(Coche.class)))
                .thenReturn(cocheSaved );

        when(modelMapper.map(cocheSaved, CocheDetailOutDto.class)).thenReturn(outDto);

        CocheDetailOutDto result = cocheService.add(cocheInDto, autoescuelaDto);

        assertEquals(10L, result.getId());
    }

    @Test
    void testAdd_autoescuelaNotFound() {
        CocheInDto cocheInDto = new CocheInDto();
        AutoescuelaDetailOutDto autoescuelaDto = new AutoescuelaDetailOutDto();
        autoescuelaDto.setId(99L);

        when(autoescuelaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                AutoescuelaNotFoundException.class,
                () -> cocheService.add(cocheInDto, autoescuelaDto)
        );
    }

    @Test
    void testFindByFiltros_ok() {
        List<Coche> coches = List.of(
                new Coche(1L, "1234FSX", "Seat", "Ibiza", "Manual",
                        1000, LocalDate.of(2010, 1, 2), 1000.50f, true, null,null)
        );

        when(cocheRepository.findAll(ArgumentMatchers.<Specification<Coche>>any()))
                .thenReturn(coches);

        when(modelMapper.map(eq(coches), eq(new TypeToken<List<CocheOutDto>>(){}.getType())))
                .thenReturn(List.of(
                        new CocheOutDto(1L, "Seat", "Ibiza", "Manual", null)
                ));

        List<CocheOutDto> result = cocheService.findByFiltros("Seat", "Ibiza", "Manual");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Seat", result.getFirst().getMarca());
        assertEquals("Ibiza", result.getFirst().getModelo());
        assertEquals("Manual", result.getFirst().getTipoCambio());
    }

    @Test
    void testFindById_ok() throws CocheNotFoundException {
        Coche coche = new Coche();
        coche.setId(1L);

        CocheDetailOutDto outDto = new CocheDetailOutDto();
        outDto.setId(1L);

        when(cocheRepository.findById(1L)).thenReturn(Optional.of(coche));
        when(modelMapper.map(coche, CocheDetailOutDto.class)).thenReturn(outDto);

        CocheDetailOutDto result = cocheService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testFindById_notFound() {
        when(cocheRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                CocheNotFoundException.class,
                () -> cocheService.findById(1L)
        );
    }

    @Test
    void testDelete_ok() throws CocheNotFoundException {
        Coche coche = new Coche();
        coche.setId(1L);

        when(cocheRepository.findById(1L)).thenReturn(Optional.of(coche));

        cocheService.delete(1L);

        verify(cocheRepository).delete(coche);
    }

    @Test
    void testDelete_notFound() {
        when(cocheRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                CocheNotFoundException.class,
                () -> cocheService.delete(1L)
        );
    }

    @Test
    void testModify_ok() throws AutoescuelaNotFoundException, CocheNotFoundException {
        CocheInDto cocheInDto = new CocheInDto();
        AutoescuelaDetailOutDto autoescuelaDto = new AutoescuelaDetailOutDto();
        autoescuelaDto.setId(1L);

        Autoescuela autoescuela = new Autoescuela();
        autoescuela.setId(1L);

        Coche coche = new Coche();
        coche.setId(1L);

        Coche cocheSaved = new Coche();
        cocheSaved.setId(1L);

        CocheDetailOutDto outDto = new CocheDetailOutDto();
        outDto.setId(1L);

        when(cocheRepository.findById(1L)).thenReturn(Optional.of(coche));
        when(autoescuelaRepository.findById(1L)).thenReturn(Optional.of(autoescuela));
        when(cocheRepository.save(coche)).thenReturn(cocheSaved);
        when(modelMapper.map(cocheSaved, CocheDetailOutDto.class)).thenReturn(outDto);

        doNothing().when(modelMapper).map(cocheInDto, coche);

        CocheDetailOutDto result = cocheService.modify(1L, cocheInDto, autoescuelaDto);

        assertEquals(1L, result.getId());
    }

    @Test
    void testModify_notFound() {
        when(cocheRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                CocheNotFoundException.class,
                () -> cocheService.modify(1L, new CocheInDto(), new AutoescuelaDetailOutDto())
        );
    }

    @Test
    void testPatch_ok() throws CocheNotFoundException, AutoescuelaNotFoundException {
        Long cocheId = 1L;

        Coche coche = new Coche();
        coche.setId(cocheId);

        Coche cocheSaved = new Coche();
        cocheSaved.setId(cocheId);
        cocheSaved.setMarca("Toyota");

        CocheDetailOutDto outDto = new CocheDetailOutDto();
        outDto.setId(cocheId);
        outDto.setMarca("Toyota");

        Map<String, Object> patch = Map.of(
                "marca", "Toyota",
                "disponible", true
        );

        when(cocheRepository.findById(cocheId)).thenReturn(Optional.of(coche));
        when(cocheRepository.save(coche)).thenReturn(cocheSaved);
        when(modelMapper.map(cocheSaved, CocheDetailOutDto.class)).thenReturn(outDto);

        CocheDetailOutDto result = cocheService.patch(cocheId, patch);

        assertEquals("Toyota", result.getMarca());
    }

    @Test
    void testPatch_cocheNotFound() {
        when(cocheRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                CocheNotFoundException.class,
                () -> cocheService.patch(1L, Map.of("marca", "Seat"))
        );
    }

    @Test
    void testPatch_autoescuelaNotFound() {
        Coche coche = new Coche();
        coche.setId(1L);

        when(cocheRepository.findById(1L)).thenReturn(Optional.of(coche));
        when(autoescuelaRepository.findById(99L)).thenReturn(Optional.empty());

        Map<String, Object> patch = Map.of(
                "autoescuelaId", 99L
        );

        assertThrows(
                AutoescuelaNotFoundException.class,
                () -> cocheService.patch(1L, patch)
        );
    }


}

