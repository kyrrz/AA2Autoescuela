package com.svalero.autoescuela.controller;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.ProfesorNotFoundException;
import com.svalero.autoescuela.service.AutoescuelaService;
import com.svalero.autoescuela.service.ProfesorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(ProfesorController.class)
public class ProfesorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfesorService profesorService;

    @MockitoBean
    private AutoescuelaService autoescuelaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAll() throws Exception {

        List<ProfesorOutDto> profesorOutDtos = List.of(
                new ProfesorOutDto(1 , "Pedro", "Perez Perez", "39964150V", "633889900", "Teorico", true),
                new ProfesorOutDto(2 , "Alvaro", "Reyes de Diego", "33311150V", "633112200", "Practicas", true)
        );

        when(profesorService.findByFilters(null,null,null)).thenReturn(profesorOutDtos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/profesores")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<ProfesorOutDto> profesorListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertNotNull(profesorListResponse);
        assertEquals(2, profesorListResponse.size());
        assertEquals("Pedro", profesorListResponse.getFirst().getNombre());

    }

    @Test
    public void testGetAllByEspecialidad() throws Exception {

        List<ProfesorOutDto> profesorOutDtos = List.of(
                new ProfesorOutDto(1 , "Pedro", "Perez Perez", "39964150V", "633889900", "Teorico", true),
                new ProfesorOutDto(2 , "Alvaro", "Reyes de Diego", "33311150V", "633112200", "Teorico", true)
        );

        when(profesorService.findByFilters(null,"Teorico",null)).thenReturn(profesorOutDtos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/profesores")
                        .queryParam("especialidad", "Teorico")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<ProfesorOutDto> profesorListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertNotNull(profesorListResponse);
        assertEquals(2, profesorListResponse.size());
        assertEquals("Pedro", profesorListResponse.getFirst().getNombre());

    }
    //GET /profesores/{id}

    // 200
    @Test
    public void testFindById_ok() throws Exception {
        ProfesorDetailOutDto dto = new ProfesorDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Carlos");
        dto.setApellidos("Martínez López");
        dto.setEspecialidad("Practico");

        when(profesorService.findById(1L)).thenReturn(dto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/profesores/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ProfesorDetailOutDto profesorResponse = objectMapper.readValue(jsonResponse, ProfesorDetailOutDto.class);

        assertNotNull(profesorResponse);
        assertEquals("Carlos", profesorResponse.getNombre());
        assertEquals("Martínez López", profesorResponse.getApellidos());
    }

    // 400
    @Test
    public void testFindById_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/profesores/a"))
                .andExpect(status().isBadRequest());
    }

    // 404
    @Test
    public void testFindById_notFound() throws Exception {
        when(profesorService.findById(999L))
                .thenThrow(new ProfesorNotFoundException());

        mockMvc.perform(get("/api/v1/profesores/999"))
                .andExpect(status().isNotFound());
    }

    //POST /profesores

    // 201
    @Test
    public void testPost_ok() throws Exception {
        List<AutoescuelaDetailOutDto> autoescuelasDto = List.of(
                new AutoescuelaDetailOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true,10, LocalDate.of(1998, 11, 20), null, null,100,100)
        );

        List<AutoescuelaOutDto> autoescuelasOut = List.of(
                new AutoescuelaOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true,100,100)
        );

        ProfesorInDto inDto = new ProfesorInDto();
        inDto.setNombre("Carlos");
        inDto.setApellidos("Martínez López");
        inDto.setDni("12345678A");
        inDto.setTelefono("666123456");
        inDto.setSalario(1800.00f);
        inDto.setFechaContratacion(LocalDate.of(2020, 9, 1));
        inDto.setEspecialidad("Practico");
        inDto.setActivo(true);

        ProfesorDetailOutDto outDto = new ProfesorDetailOutDto();
        outDto.setId(1L);
        outDto.setNombre("Carlos");
        outDto.setApellidos("Martínez López");
        outDto.setAutoescuelas(autoescuelasOut);

        when(autoescuelaService.findAllById(anyList())).thenReturn(autoescuelasDto);
        when(profesorService.add(any(ProfesorInDto.class), anyList())).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profesores")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ProfesorDetailOutDto profesorResponse = objectMapper.readValue(jsonResponse, ProfesorDetailOutDto.class);

        assertNotNull(profesorResponse);
        assertEquals("Carlos", profesorResponse.getNombre());
        assertEquals("Martínez López", profesorResponse.getApellidos());
    }

    // 400
    @Test
    public void testPost_validacionError() throws Exception {
        ProfesorInDto inDto = new ProfesorInDto();
        inDto.setNombre("");
        inDto.setApellidos("Martínez");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profesores")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isBadRequest());
    }

    //PUT /profesores/{id}

    // 200
    @Test
    public void testPut_ok() throws Exception {
        ProfesorInDto inDto = new ProfesorInDto();
        inDto.setNombre("Carlos");
        inDto.setApellidos("Martínez López");
        inDto.setDni("12345678A");
        inDto.setTelefono("666999888");
        inDto.setSalario(1900.00f);
        inDto.setFechaContratacion(LocalDate.of(2020, 9, 1));
        inDto.setEspecialidad("Practico");
        inDto.setActivo(true);

        List<AutoescuelaDetailOutDto> autoescuelasDto = List.of(
                new AutoescuelaDetailOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true,10, LocalDate.of(1998, 11, 20), null, null,100,100)
        );

        List<AutoescuelaOutDto> autoescuelasOut = List.of(
                new AutoescuelaOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true,100,100)
        );

        ProfesorDetailOutDto outDto = new ProfesorDetailOutDto();
        outDto.setId(1L);
        outDto.setNombre("Carlos");
        outDto.setApellidos("Martínez López");
        outDto.setSalario(1900.00f);
        outDto.setAutoescuelas(autoescuelasOut);

        when(autoescuelaService.findAllById(anyList())).thenReturn(autoescuelasDto);
        when(profesorService.modify(eq(1L), any(ProfesorInDto.class), anyList())).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/profesores/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ProfesorDetailOutDto profesorResponse = objectMapper.readValue(jsonResponse, ProfesorDetailOutDto.class);

        assertNotNull(profesorResponse);
        assertEquals("Carlos", profesorResponse.getNombre());
        assertEquals(1900.00f, profesorResponse.getSalario());
    }

    // 404
    @Test
    public void testPut_notFound() throws Exception {
        ProfesorInDto inDto = new ProfesorInDto();
        inDto.setNombre("Carlos");
        inDto.setApellidos("Martínez López");
        inDto.setDni("12345678A");
        inDto.setTelefono("666999888");
        inDto.setSalario(1900.00f);
        inDto.setFechaContratacion(LocalDate.of(2020, 9, 1));
        inDto.setEspecialidad("Practico");
        inDto.setActivo(true);
        inDto.setAutoescuelaId(List.of(1L));

        List<AutoescuelaDetailOutDto> autoescuelasDto = List.of(
                new AutoescuelaDetailOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true,10, LocalDate.of(1998, 11, 20), null, null,100,100)
        );

        when(autoescuelaService.findAllById(List.of(1L))).thenReturn(autoescuelasDto);
        when(profesorService.modify(999L, inDto, autoescuelasDto))
                .thenThrow(new ProfesorNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/profesores/999")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isNotFound());
    }

    // 400
    @Test
    public void testPut_badRequest() throws Exception {
        ProfesorInDto inDto = new ProfesorInDto();
        inDto.setNombre("");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/profesores/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isBadRequest());
    }

    //DELETE /profesores/{id}

    // 204
    @Test
    public void testDelete_ok() throws Exception {
        doNothing().when(profesorService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/profesores/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    // 404
    @Test
    public void testDelete_notFound() throws Exception {
        doThrow(new ProfesorNotFoundException()).when(profesorService).delete(999L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/profesores/999")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    // 400
    @Test
    public void testDelete_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/profesores/abc")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    //PATCH /profesores/{id}

    // 200
    @Test
    public void testPatch_ok() throws Exception {
        Map<String, Object> patch = new HashMap<>();
        patch.put("salario", 2200.00);
        patch.put("telefono", "688999777");

        ProfesorDetailOutDto outDto = new ProfesorDetailOutDto();
        outDto.setId(1L);
        outDto.setNombre("Carlos");
        outDto.setSalario(2200.00f);
        outDto.setTelefono("688999777");

        when(profesorService.patch(1L, patch)).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/profesores/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ProfesorDetailOutDto profesorResponse = objectMapper.readValue(jsonResponse, ProfesorDetailOutDto.class);

        assertNotNull(profesorResponse);
        assertEquals(2200.00f, profesorResponse.getSalario());
        assertEquals("688999777", profesorResponse.getTelefono());
    }

    // 404
    @Test
    public void testPatch_notFound() throws Exception {
        Map<String, Object> patch = new HashMap<>();
        patch.put("salario", 2200.00);

        when(profesorService.patch(999L, patch)).thenThrow(new ProfesorNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/profesores/999")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isNotFound());
    }

    // 400
    @Test
    public void testPatch_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/profesores/abc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    //GET profesores/{id}/autoescuelas
    // 200
    @Test
    public void testFindAutoescuelasById_ok() throws Exception {
        ProfesorDetailOutDto dto = new ProfesorDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Santi");
        dto.setApellidos("Faci");

        List<AutoescuelaOutDto> outDtos = List.of(
                new AutoescuelaOutDto(1L, "Autoescuela Test", "Calle 123", "Reus", "911753917", "auto1@gmail.com", 4.5f, true,100,100),
                new AutoescuelaOutDto(2L, "Autoescuela Test 2", "Calle 1234", "Reus", "911753917", "auto1@gmail.com", 4.1f, false,100,100)
        );

        when(profesorService.getAutoescuelas(1L)).thenReturn(outDtos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/profesores/1/autoescuelas")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<AutoescuelaOutDto> autoescuelaListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(autoescuelaListResponse);
        assertEquals(2, autoescuelaListResponse.size());
        assertEquals("Autoescuela Test", autoescuelaListResponse.getFirst().getNombre());
        assertEquals("Autoescuela Test 2", autoescuelaListResponse.getLast().getNombre());
    }
    // 404
    @Test
    public void testFindAutoescuelasById_notFound() throws Exception {
        ProfesorDetailOutDto dto = new ProfesorDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Santi");
        dto.setApellidos("Faci");

        when(profesorService.getAutoescuelas(99L)).thenThrow(new ProfesorNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/profesores/99/autoescuelas")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

    }

    // 400
    @Test
    public void testFindAutoescuelasById_badRequest() throws Exception {
        ProfesorDetailOutDto dto = new ProfesorDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Santi");
        dto.setApellidos("Faci");


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/profesores/a/autoescuelas")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

    }
}




