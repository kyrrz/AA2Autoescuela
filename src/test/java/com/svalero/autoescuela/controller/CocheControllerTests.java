package com.svalero.autoescuela.controller;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.CocheNotFoundException;
import com.svalero.autoescuela.model.Coche;
import com.svalero.autoescuela.service.AutoescuelaService;
import com.svalero.autoescuela.service.CocheService;
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

@WebMvcTest(CocheController.class)
public class CocheControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CocheService cocheService;

    @MockitoBean
    private AutoescuelaService autoescuelaService;

    @Autowired
    private ObjectMapper objectMapper;

    //GET /coches

    // 200
    @Test
    public void testGetAll() throws Exception {
        List<CocheOutDto> cocheOutDtos = List.of(
                new CocheOutDto(1L, "Seat", "Ibiza", "Manual", null ),
                new CocheOutDto(2L, "Renault", "Clio", "Automatico", null)
        );

        when(cocheService.findByFiltros(null, null, null)).thenReturn(cocheOutDtos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/coches")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<CocheOutDto> cocheListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertNotNull(cocheListResponse);
        assertEquals(2, cocheListResponse.size());
        assertEquals("Ibiza", cocheListResponse.get(0).getModelo());
        assertEquals("Clio", cocheListResponse.get(1).getModelo());
    }


    //GET /coches/{id}

    // 200
    @Test
    public void testFindById_ok() throws Exception {
        CocheDetailOutDto dto = new CocheDetailOutDto();
        dto.setId(1L);
        dto.setMatricula("1234ABC");
        dto.setMarca("Seat");
        dto.setModelo("Ibiza");

        when(cocheService.findById(1L)).thenReturn(dto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/coches/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CocheDetailOutDto cocheResponse = objectMapper.readValue(jsonResponse, CocheDetailOutDto.class);

        assertNotNull(cocheResponse);
        assertEquals("1234ABC", cocheResponse.getMatricula());
        assertEquals("Seat", cocheResponse.getMarca());
    }

    // 400
    @Test
    public void testFindById_badRequest() throws Exception {
        mockMvc.perform(get("/api/v1/coches/a"))
                .andExpect(status().isBadRequest());
    }

    // 404
    @Test
    public void testFindById_notFound() throws Exception {
        when(cocheService.findById(999L))
                .thenThrow(new CocheNotFoundException());

        mockMvc.perform(get("/api/v1/coches/999"))
                .andExpect(status().isNotFound());
    }

    //POST /coches

    // 201
    @Test
    public void testPost_ok() throws Exception {
        CocheInDto inDto =
                new CocheInDto("1234FCB", "Seat", "Leon","Manual", 10000, LocalDate.of(2012,3,2), 12000.40f, true, 1L, null);


        AutoescuelaDetailOutDto autoescuelaDetailOutDto =
                new AutoescuelaDetailOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true,10, LocalDate.of(1998, 11, 20), null, null,100,100);

        AutoescuelaOutDto autoescuelaOutDto =
                new AutoescuelaOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true,100,100);

        CocheDetailOutDto outDto = new CocheDetailOutDto(1L, "Seat", "Leon", "Manual", "1234FCB", 10000, 12000.40f,true , LocalDate.of(2012,3,2), autoescuelaOutDto, null );


        when(autoescuelaService.findById(1L)).thenReturn(autoescuelaDetailOutDto);
        when(cocheService.add(inDto, autoescuelaDetailOutDto)).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/coches")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CocheDetailOutDto cocheResponse = objectMapper.readValue(jsonResponse, CocheDetailOutDto.class);

        assertNotNull(cocheResponse);
        assertEquals("1234FCB", cocheResponse.getMatricula());
        assertEquals("Seat", cocheResponse.getMarca());
    }

    // 400
    @Test
    public void testPost_validacionError() throws Exception {
        CocheInDto inDto = new CocheInDto();
        inDto.setMatricula("");
        inDto.setMarca("Seat");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/coches")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isBadRequest());
    }

    // 404
    @Test
    public void testPost_autoescuelaNotFound() throws Exception {
        CocheInDto inDto =
                new CocheInDto("1234FCB", "Seat", "Leon","Manual", 10000, LocalDate.of(2012,3,2), 12000.40f, true, 999L, null);


        when(autoescuelaService.findById(999L)).thenThrow(new AutoescuelaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/coches")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isNotFound());
    }

    //PUT /coches/{id}

    // 200
    @Test
    public void testPut_ok() throws Exception {
        CocheInDto inDto =
                new CocheInDto("1234FCB", "Seat", "Leon","Manual", 10000, LocalDate.of(2012,3,2), 12000.40f, true, 1L, null);


        AutoescuelaDetailOutDto autoescuelaDetailOutDto =
                new AutoescuelaDetailOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true,10, LocalDate.of(1998, 11, 20), null, null,100,100);

        AutoescuelaOutDto autoescuelaOutDto =
                new AutoescuelaOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true,100,100);

        CocheDetailOutDto outDto = new CocheDetailOutDto(1L, "Seat", "Leon", "Manual", "1234FCB", 10000, 12000.40f,true , LocalDate.of(2012,3,2), autoescuelaOutDto, null );


        when(autoescuelaService.findById(1L)).thenReturn(autoescuelaDetailOutDto);
        when(cocheService.modify(1L, inDto, autoescuelaDetailOutDto)).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/coches/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CocheDetailOutDto cocheResponse = objectMapper.readValue(jsonResponse, CocheDetailOutDto.class);

        assertNotNull(cocheResponse);
        assertEquals("1234FCB", cocheResponse.getMatricula());
        assertEquals("Seat", cocheResponse.getMarca());
    }

    // 404
    @Test
    public void testPut_notFound() throws Exception {
        CocheInDto inDto =
                new CocheInDto("1234FCB", "Seat", "Leon","Manual", 10000, LocalDate.of(2012,3,2), 12000.40f, true, 1L, null);

        AutoescuelaDetailOutDto autoescuelaDetailOutDto =
                new AutoescuelaDetailOutDto(1L, "Autoescuela Test", "Calle 123", "Madrid",
                        "911111111", "auto@test.com", 4.5f, true, 10, LocalDate.of(1998, 11, 20), null, null,100,100);

        when(autoescuelaService.findById(1L)).thenReturn(autoescuelaDetailOutDto);
        when(cocheService.modify(eq(999L), eq(inDto), any(AutoescuelaDetailOutDto.class)))
                .thenThrow(new CocheNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/coches/999")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isNotFound());
    }

    // 400
    @Test
    public void testPut_badRequest() throws Exception {
        CocheInDto inDto = new CocheInDto();
        inDto.setMatricula("");
        inDto.setMarca("Seat");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/coches/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isBadRequest());
    }

    //DELETE /coches/{id}

    // 204
    @Test
    public void testDelete_ok() throws Exception {
        doNothing().when(cocheService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/coches/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    // 404
    @Test
    public void testDelete_notFound() throws Exception {
        doThrow(new CocheNotFoundException()).when(cocheService).delete(999L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/coches/999")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    // 400
    @Test
    public void testDelete_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/coches/abc")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    //PATCH /coches/{id}

    // 200
    @Test
    public void testPatch_ok() throws Exception {
        Map<String, Object> patch = new HashMap<>();
        patch.put("kilometraje", 55000);


        CocheDetailOutDto outDto = new CocheDetailOutDto();
        outDto.setId(1L);
        outDto.setMatricula("1234FBC");
        outDto.setMarca("Seat");
        outDto.setKilometraje(55000);


        when(cocheService.patch(1L, patch)).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/coches/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CocheDetailOutDto cocheResponse = objectMapper.readValue(jsonResponse, CocheDetailOutDto.class);

        assertNotNull(cocheResponse);
        assertEquals(55000, cocheResponse.getKilometraje());
    }

    // 404
    @Test
    public void testPatch_notFound() throws Exception {
        Map<String, Object> patch = new HashMap<>();
        patch.put("kilometraje", 55000);

        when(cocheService.patch(999L, patch)).thenThrow(new CocheNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/coches/999")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isNotFound());
    }

    // 400
    @Test
    public void testPatch_badRequest() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/coches/abc")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}