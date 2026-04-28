package com.svalero.autoescuela.controller;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.AlumnoNotFoundException;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.BadRequestException;
import com.svalero.autoescuela.model.Alumno;

import com.svalero.autoescuela.service.AlumnoService;
import com.svalero.autoescuela.service.AutoescuelaService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlumnoController.class)
public class AlumnoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlumnoService alumnoService;

    @MockitoBean
    private AutoescuelaService autoescuelaService;

    @Autowired
    private ObjectMapper objectMapper;


    //GET /autoescuelas

    // 200
    @Test
    public void testGetAll() throws Exception {

        List<AlumnoOutDto> alumnoOutDtos = List.of(
                new AlumnoOutDto(1L, "Alvaro", "Reyes","39999999V", "Reus", 9.9f),
                new AlumnoOutDto(2L, "Pedro", "Perez","67676767V", "Zaragoza", 4.2f)
        );

        when(alumnoService.findByFiltros(null,null,null,null)).thenReturn(alumnoOutDtos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/alumnos")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<AlumnoOutDto> alumnoListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertNotNull(alumnoListResponse);
        assertEquals(2, alumnoListResponse.size());
        assertEquals("Alvaro", alumnoListResponse.getFirst().getNombre());
        assertEquals("Pedro", alumnoListResponse.getLast().getNombre());

    }
    // 400
    @Test
    public void testGetAll_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/alumnos")
                        .param("usaGafas", "abc"))
                .andExpect(status().isBadRequest());
    }

    //GET /autoescuelas/{id}

    // 200
    @Test
    public void testFindById_ok() throws Exception {
        AlumnoDetailOutDto dto = new AlumnoDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Santi");
        dto.setApellidos("Faci");

        when(alumnoService.findById(1L)).thenReturn(dto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/alumnos/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AlumnoDetailOutDto alumnoResponse = objectMapper.readValue(jsonResponse, AlumnoDetailOutDto.class);

        assertNotNull(alumnoResponse);
        assertEquals("Santi", alumnoResponse.getNombre());
        assertEquals("Faci", alumnoResponse.getApellidos());
    }
    // 400
    @Test
    public void testFindById_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/alumnos/a"))
                .andExpect(status().isBadRequest());
    }
    // 404
    @Test
    public void testFindById_notFound() throws Exception {
        when(alumnoService.findById(1L))
                .thenThrow(new AlumnoNotFoundException());

        mockMvc.perform(get("/api/v1/alumnos/1"))
                .andExpect(status().isNotFound());
    }

    //POST /autoescuelas/{id}

    // 201
    @Test
    public void testPost_ok() throws Exception {
        AutoescuelaDetailOutDto autoescuelaDetailOutDto =
                new AutoescuelaDetailOutDto(2L, "Autoescuela Test", "Calle 123", "Reus", "911753917", "auto1@gmail.com", 4.5f, true,10, LocalDate.of(1998, 11, 20), null, null, 100, 100    );
        AutoescuelaOutDto autoescuelaOutDto =
                new AutoescuelaOutDto(2L, "Autoescuela Test", "Calle 123", "Reus", "911753917", "auto1@gmail.com", 4.5f, true, 100, 100);
        AlumnoInDto inDto =
                new AlumnoInDto("María","López Fernández", "87654321B", LocalDate.of(1998, 11, 20), "677987654", "maria.lopez@email.com", "Avenida de la Constitución 45", "Barcelona", false,9.5f,2L);
        AlumnoDetailOutDto outDto =
                new AlumnoDetailOutDto(1L, "María","López Fernández", "maria.lopez@email.com" ,"677987654","Avenida de la Constitución 45","Barcelona","87654321B", LocalDate.of(1998, 11, 20), false , 9.5f, autoescuelaOutDto);

        when(autoescuelaService.findById(2L)).thenReturn(autoescuelaDetailOutDto);
        when(alumnoService.add(inDto,autoescuelaDetailOutDto)).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/alumnos")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AlumnoDetailOutDto alumnoResponse = objectMapper.readValue(jsonResponse,  AlumnoDetailOutDto.class);

        assertNotNull(alumnoResponse);
        assertEquals("María", alumnoResponse.getNombre());
        assertEquals("López Fernández", alumnoResponse.getApellidos());

    }
    // 400
    @Test
    public void testPost_validacionError() throws Exception {
        AlumnoInDto inDto = new AlumnoInDto();
        inDto.setNombre("");
        inDto.setApellidos("Reyes");


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/alumnos")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isBadRequest());
    }

    //PUT /autoescuelas/{id}

    // 200
    @Test
    public void testPut_ok() throws Exception {

        AlumnoInDto inDto =
                new AlumnoInDto("María","López Fernández", "87654321B", LocalDate.of(1998, 11, 20), "677987654", "maria.lopez@email.com", "Avenida de la Constitución 45", "Barcelona", false,9.5f,2L);

        AutoescuelaDetailOutDto autoescuelaDetailOutDto =
                new AutoescuelaDetailOutDto(2L, "Autoescuela Test", "Calle 123", "Reus", "911753917", "auto1@gmail.com", 4.5f, true, 10, LocalDate.of(1998, 11, 20), null, null ,100,100   );

        AutoescuelaOutDto autoescuelaOutDto =
                new AutoescuelaOutDto(1L, "Autoescuela Test", "Calle 123", "Reus", "911753917", "auto1@gmail.com", 4.5f, true, 100, 100);

        AlumnoDetailOutDto outDto =
                new AlumnoDetailOutDto(1L, "María","López Fernández", "maria.lopez@email.com" ,"677987654","Avenida de la Constitución 45","Barcelona","87654321B", LocalDate.of(1998, 11, 20), false , 9.5f, autoescuelaOutDto);

        when(autoescuelaService.findById(2L)).thenReturn(autoescuelaDetailOutDto);
        when(alumnoService.modify(1L, inDto, autoescuelaDetailOutDto)).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/alumnos/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AlumnoDetailOutDto alumnoResponse = objectMapper.readValue(jsonResponse, AlumnoDetailOutDto.class);

        assertNotNull(alumnoResponse);
        assertEquals("María", alumnoResponse.getNombre());
        assertEquals("López Fernández", alumnoResponse.getApellidos());

    }
    // 404
    @Test
    public void testPut_notFound() throws Exception {
        AlumnoInDto inDto =
                new AlumnoInDto("María","López Fernández", "87654321B", LocalDate.of(1998, 11, 20), "677987654", "maria.lopez@email.com", "Avenida de la Constitución 45", "Barcelona", false,9.5f,2L);

        AutoescuelaDetailOutDto autoescuelaDetailOutDto =
                new AutoescuelaDetailOutDto(2L, "Autoescuela Test", "Calle 123", "Reus", "911753917", "auto1@gmail.com", 4.5f, true, 10, LocalDate.of(1998, 11, 20), null, null ,100,100   );

        when(autoescuelaService.findById(2L)).thenReturn(autoescuelaDetailOutDto);
        when(alumnoService.modify(99L, inDto, autoescuelaDetailOutDto)).thenThrow(new AlumnoNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/alumnos/99")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isNotFound());
    }
    // 400
    @Test
    public void testPut_badRequest() throws Exception {
        AlumnoInDto inDto = new AlumnoInDto();
        inDto.setNombre("");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/alumnos/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isBadRequest());
    }

    //DELETE /autoescuelas/{id}

    // 204
    @Test
    public void testDelete_ok() throws Exception {
        Alumno alumno = new Alumno();
        alumno.setId(1L);

        doNothing().when(alumnoService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/alumnos/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());

    }
    // 404
    @Test
    public void testDelete_notFound() throws Exception {

        doThrow(new AlumnoNotFoundException()).when(alumnoService).delete(99L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/alumnos/99")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

    }
    // 400
    @Test
    public void testDelete_badRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/alumnos/aa")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

    }

    //PATCH /autoescuelas/{id}

    // 200
    @Test
    public void testPatch_ok() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("nombre", "Pedro");
        AlumnoDetailOutDto outDto =
                new AlumnoDetailOutDto(1L, "Pedro","López Fernández", "maria.lopez@email.com" ,"677987654","Avenida de la Constitución 45","Barcelona","87654321B", LocalDate.of(1998, 11, 20), false , 9.5f, null);


        when(alumnoService.patch(1L, patch)).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/alumnos/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AlumnoDetailOutDto alumnoResponse = objectMapper.readValue(jsonResponse, AlumnoDetailOutDto.class);

        assertNotNull(alumnoResponse);
        assertEquals("Pedro", alumnoResponse.getNombre());
        assertEquals("López Fernández", alumnoResponse.getApellidos());

    }
    // 404
    @Test
    public void testPatch_notFound() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("nombre", "Santi");


        when(alumnoService.patch(99L, patch)).thenThrow(new AlumnoNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/alumnos/99")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isNotFound());
    }
    // 400
    @Test
    public void testPatch_badRequest() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("e", "");

        when(alumnoService.findById(1L)).thenThrow(new AlumnoNotFoundException());
        when(alumnoService.patch(1L, patch)).thenThrow(new BadRequestException());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/alumnos/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isBadRequest());
    }





}
