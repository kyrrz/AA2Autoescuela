package com.svalero.autoescuela.controller;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.AlumnoNotFoundException;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.MatriculaNotFoundException;
import com.svalero.autoescuela.service.AlumnoService;
import com.svalero.autoescuela.service.AutoescuelaService;
import com.svalero.autoescuela.service.MatriculaService;
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

@WebMvcTest(MatriculaController.class)
public class MatriculaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatriculaService matriculaService;

    @MockitoBean
    private AlumnoService alumnoService;

    @MockitoBean
    private AutoescuelaService autoescuelaService;

    @Autowired
    private ObjectMapper objectMapper;

    //GET /matriculas

    // 200
    @Test
    public void testGetAll() throws Exception {
        List<MatriculaOutDto> matriculaOutDtos = List.of(
                new MatriculaOutDto(1L, "Presencial", "Permiso B", false, null, null),
                new MatriculaOutDto(2L, "Online", "Permiso A", true, null, null)
        );

        when(matriculaService.findByFiltros(null, null, null, null)).thenReturn(matriculaOutDtos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/matriculas")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<MatriculaOutDto> matriculaListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(matriculaListResponse);
        assertEquals(2, matriculaListResponse.size());
        assertEquals("Presencial", matriculaListResponse.get(0).getModalidad());
        assertEquals("Online", matriculaListResponse.get(1).getModalidad());
    }

    // 400
    @Test
    public void testGetAll_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/matriculas")
                        .param("horasPracticas", "abc"))
                .andExpect(status().isBadRequest());
    }

    //GET /matriculas/{id}

    // 200
    @Test
    public void testFindById_ok() throws Exception {
        MatriculaDetailOutDto dto = new MatriculaDetailOutDto();
        dto.setId(1L);
        dto.setModalidad("Presencial");
        dto.setTipoMatricula("Permiso B");


        when(matriculaService.findById(1L)).thenReturn(dto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/matriculas/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        MatriculaDetailOutDto matriculaResponse = objectMapper.readValue(jsonResponse, MatriculaDetailOutDto.class);

        assertNotNull(matriculaResponse);
        assertEquals("Presencial", matriculaResponse.getModalidad());
        assertEquals("Permiso B", matriculaResponse.getTipoMatricula());
    }

    // 400
    @Test
    public void testFindById_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/matriculas/a"))
                .andExpect(status().isBadRequest());
    }

    // 404
    @Test
    public void testFindById_notFound() throws Exception {
        when(matriculaService.findById(999L))
                .thenThrow(new MatriculaNotFoundException());

        mockMvc.perform(get("/api/v1/matriculas/999"))
                .andExpect(status().isNotFound());
    }

    //POST /matriculas

    // 201
    @Test
    public void testPost_ok() throws Exception {
        AlumnoDetailOutDto alumnoDetailOutDto = new AlumnoDetailOutDto();
        alumnoDetailOutDto.setId(1L);
        alumnoDetailOutDto.setNombre("Juan");

        AutoescuelaDetailOutDto autoescuelaDetailOutDto =
                new AutoescuelaDetailOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true,10, LocalDate.of(1998, 11, 20), null, null,100,100);

        AlumnoOutDto alumnoOutDto = new AlumnoOutDto();
        alumnoOutDto.setId(1L);
        alumnoOutDto.setNombre("Juan");

        AutoescuelaOutDto autoescuelaOutDto = new AutoescuelaOutDto();
        autoescuelaOutDto.setId(1L);
        autoescuelaOutDto.setNombre("Autoescuela Test");

        MatriculaInDto inDto = new MatriculaInDto();
        inDto.setModalidad("Presencial");
        inDto.setTipoMatricula("Permiso B");
        inDto.setFechaInicio(LocalDate.of(2024, 1, 15));
        inDto.setFechaFinal(LocalDate.of(2024, 6, 15));
        inDto.setPrecio(850.00f);
        inDto.setHorasPracticas(20);
        inDto.setHorasTeoricas(30);
        inDto.setCompletada(false);
        inDto.setAutoescuelaId(1L);
        inDto.setAlumnoId(1L);


        MatriculaDetailOutDto outDto = new MatriculaDetailOutDto();
        outDto.setId(1L);
        outDto.setModalidad("Presencial");
        outDto.setTipoMatricula("Permiso B");
        outDto.setAlumno(alumnoOutDto);
        outDto.setAutoescuela(autoescuelaOutDto);

        when(alumnoService.findById(1L)).thenReturn(alumnoDetailOutDto);
        when(autoescuelaService.findById(1L)).thenReturn(autoescuelaDetailOutDto);
        when(matriculaService.add(inDto, alumnoDetailOutDto, autoescuelaDetailOutDto))
                .thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/matriculas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        MatriculaDetailOutDto matriculaResponse = objectMapper.readValue(jsonResponse, MatriculaDetailOutDto.class);

        assertNotNull(matriculaResponse);
        assertEquals("Presencial", matriculaResponse.getModalidad());
        assertEquals("Permiso B", matriculaResponse.getTipoMatricula());
    }

    // 400
    @Test
    public void testPost_validacionError() throws Exception {
        MatriculaInDto inDto = new MatriculaInDto();
        inDto.setModalidad("");
        inDto.setTipoMatricula("Permiso B");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/matriculas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isBadRequest());
    }

    // 404
    @Test
    public void testPost_alumnoNotFound() throws Exception {
        MatriculaInDto inDto = new MatriculaInDto();
        inDto.setModalidad("Presencial");
        inDto.setTipoMatricula("Permiso B");
        inDto.setFechaInicio(LocalDate.of(2024, 1, 15));
        inDto.setFechaFinal(LocalDate.of(2024, 6, 15));
        inDto.setPrecio(850.00f);
        inDto.setHorasPracticas(20);
        inDto.setHorasTeoricas(30);
        inDto.setCompletada(false);
        inDto.setAutoescuelaId(1L);
        inDto.setAlumnoId(999L);

        when(alumnoService.findById(999L)).thenThrow(new AlumnoNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.post("/matriculas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isNotFound());
    }
    // 404
    @Test
    public void testPost_autoescuelaNotFound() throws Exception {
        MatriculaInDto inDto = new MatriculaInDto();
        inDto.setModalidad("Presencial");
        inDto.setTipoMatricula("Permiso B");
        inDto.setFechaInicio(LocalDate.of(2024, 1, 15));
        inDto.setFechaFinal(LocalDate.of(2024, 6, 15));
        inDto.setPrecio(850.00f);
        inDto.setHorasPracticas(20);
        inDto.setHorasTeoricas(30);
        inDto.setCompletada(false);
        inDto.setAutoescuelaId(999L);
        inDto.setAlumnoId(1L);

        when(autoescuelaService.findById(999L)).thenThrow(new AutoescuelaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/matriculas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isNotFound());
    }

    //PUT /matriculas/{id}

    // 200
    @Test
    public void testPut_ok() throws Exception {
        MatriculaInDto inDto = new MatriculaInDto();
        inDto.setModalidad("Presencial");
        inDto.setTipoMatricula("Permiso B");
        inDto.setFechaInicio(LocalDate.of(2024, 1, 15));
        inDto.setFechaFinal(LocalDate.of(2024, 6, 15));
        inDto.setPrecio(850.00f);
        inDto.setHorasPracticas(20);
        inDto.setHorasTeoricas(30);
        inDto.setCompletada(false);
        inDto.setAutoescuelaId(1L);
        inDto.setAlumnoId(1L);


        AlumnoDetailOutDto alumnoDetailOutDto = new AlumnoDetailOutDto();
        alumnoDetailOutDto.setId(1L);

        AutoescuelaDetailOutDto autoescuelaDetailOutDto =
                new AutoescuelaDetailOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true, 10, LocalDate.of(1998, 11, 20), null, null,100,100);

        AlumnoOutDto alumnoOutDto = new AlumnoOutDto();
        alumnoOutDto.setId(1L);

        AutoescuelaOutDto autoescuelaOutDto = new AutoescuelaOutDto();
        autoescuelaOutDto.setId(1L);

        MatriculaDetailOutDto outDto = new MatriculaDetailOutDto();
        outDto.setId(1L);
        outDto.setModalidad("Presencial");
        outDto.setAlumno(alumnoOutDto);
        outDto.setAutoescuela(autoescuelaOutDto);

        when(alumnoService.findById(1L)).thenReturn(alumnoDetailOutDto);
        when(autoescuelaService.findById(1L)).thenReturn(autoescuelaDetailOutDto);
        when(matriculaService.modify(1L, inDto, alumnoDetailOutDto, autoescuelaDetailOutDto))
                .thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/matriculas/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        MatriculaDetailOutDto matriculaResponse = objectMapper.readValue(jsonResponse, MatriculaDetailOutDto.class);

        assertNotNull(matriculaResponse);
        assertEquals("Presencial", matriculaResponse.getModalidad());

    }

    // 404
    @Test
    public void testPut_notFound() throws Exception {
        MatriculaInDto inDto = new MatriculaInDto();
        inDto.setModalidad("Presencial");
        inDto.setTipoMatricula("Permiso B");
        inDto.setFechaInicio(LocalDate.of(2024, 1, 15));
        inDto.setFechaFinal(LocalDate.of(2024, 6, 15));
        inDto.setPrecio(850.00f);
        inDto.setHorasPracticas(20);
        inDto.setHorasTeoricas(30);
        inDto.setCompletada(false);
        inDto.setAutoescuelaId(1L);
        inDto.setAlumnoId(1L);


        AlumnoDetailOutDto alumnoDetailOutDto = new AlumnoDetailOutDto();
        alumnoDetailOutDto.setId(1L);

        AutoescuelaDetailOutDto autoescuelaDetailOutDto =
                new AutoescuelaDetailOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true,10, LocalDate.of(1998, 11, 20), null, null,100,100);

        when(alumnoService.findById(1L)).thenReturn(alumnoDetailOutDto);
        when(autoescuelaService.findById(1L)).thenReturn(autoescuelaDetailOutDto);
        when(matriculaService.modify(999L, inDto, alumnoDetailOutDto, autoescuelaDetailOutDto))
                .thenThrow(new MatriculaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/matriculas/999")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isNotFound());
    }

    // 400
    @Test
    public void testPut_badRequest() throws Exception {
        MatriculaInDto inDto = new MatriculaInDto();
        inDto.setModalidad("");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/matriculas/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isBadRequest());
    }

    //DELETE /matriculas/{id}

    // 204
    @Test
    public void testDelete_ok() throws Exception {
        doNothing().when(matriculaService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/matriculas/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
    // 404
    @Test
    public void testDelete_notFound() throws Exception {
        doThrow(new MatriculaNotFoundException()).when(matriculaService).delete(999L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/matriculas/999")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    // 400
    @Test
    public void testDelete_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/matriculas/abc")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

//PATCH /matriculas/{id}

    // 200
    @Test
    public void testPatch_ok() throws Exception {
        Map<String, Object> patch = new HashMap<>();
        patch.put("precio", 950.00);
        patch.put("completada", true);

        MatriculaDetailOutDto outDto = new MatriculaDetailOutDto();
        outDto.setId(1L);
        outDto.setModalidad("Presencial");
        outDto.setCompletada(true);

        when(matriculaService.patch(1L, patch)).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/matriculas/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        MatriculaDetailOutDto matriculaResponse = objectMapper.readValue(jsonResponse, MatriculaDetailOutDto.class);

        assertNotNull(matriculaResponse);
        assertEquals(true, matriculaResponse.isCompletada());
    }

    // 404
    @Test
    public void testPatch_notFound() throws Exception {
        Map<String, Object> patch = new HashMap<>();
        patch.put("precio", 950.00);

        when(matriculaService.patch(999L, patch)).thenThrow(new MatriculaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/matriculas/999")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isNotFound());
    }

    // 400
    @Test
    public void testPatch_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/matriculas/abc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}