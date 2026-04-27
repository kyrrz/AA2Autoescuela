package com.svalero.autoescuela.controller;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.BadRequestException;
import com.svalero.autoescuela.exception.ProfesorNotFoundException;
import com.svalero.autoescuela.model.Autoescuela;
import com.svalero.autoescuela.service.AutoescuelaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutoescuelaController.class)
public class AutoescuelaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutoescuelaService autoescuelaService;

    @Autowired
    private ObjectMapper objectMapper;

    //GET /autoescuelas

        // 200
    @Test
    public void testGetAll() throws Exception {

        List<AutoescuelaOutDto> autoescuelaOutDtos = List.of(
                new AutoescuelaOutDto(1L, "Autoescuela Test", "Calle 123", "Reus", "911753917", "auto1@gmail.com", 4.5f, true,100,100),
                new AutoescuelaOutDto(2L, "Autoescuela Test 2", "Calle 1234", "Reus", "911753917", "auto1@gmail.com", 4.1f, false,100,100)
        );

        when(autoescuelaService.findByFiltros(null,null,null)).thenReturn(autoescuelaOutDtos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<AutoescuelaOutDto> autoescuelaListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertNotNull(autoescuelaListResponse);
        assertEquals(2, autoescuelaListResponse.size());
        assertEquals("Autoescuela Test", autoescuelaListResponse.getFirst().getNombre());
        assertEquals("Autoescuela Test 2", autoescuelaListResponse.getLast().getNombre());

    }
        // 400
    @Test
    public void testGetAll_badRequest() throws Exception {
        mockMvc.perform(get("/autoescuelas")
                        .param("minRating", "abc"))
                .andExpect(status().isBadRequest());
    }

    //GET /autoescuelas/{id}

        // 200
    @Test
    public void testFindById_ok() throws Exception {
        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");

        when(autoescuelaService.findById(1L)).thenReturn(dto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AutoescuelaDetailOutDto autoesculaResponse = objectMapper.readValue(jsonResponse, AutoescuelaDetailOutDto.class);

        assertNotNull(autoesculaResponse);
        assertEquals("Autoescuela Test", autoesculaResponse.getNombre());
        assertEquals("Calle 123", autoesculaResponse.getDireccion());
    }
        // 400
    @Test
    public void testFindById_badRequest() throws Exception {
        mockMvc.perform(get("/autoescuelas/a"))
                .andExpect(status().isBadRequest());
    }
        // 404
    @Test
    public void testFindById_notFound() throws Exception {
        when(autoescuelaService.findById(1L))
                .thenThrow(new AutoescuelaNotFoundException());

        mockMvc.perform(get("/autoescuelas/1"))
                .andExpect(status().isNotFound());
    }

    //POST /autoescuelas/{id}

        // 201
    @Test
    public void testPost_ok() throws Exception {
        AutoescuelaInDto inDto =
                new AutoescuelaInDto("Autoescuela Test","Calle 123", "Reus", "633333333", "1999alvaro@gmail.com", 100 , 4.5f, LocalDate.of(1999,6,16), true, 100, 100);

        AutoescuelaDetailOutDto outDto =
                new AutoescuelaDetailOutDto(1L, "Autoescuela Test", "Calle 123", "Reus", "633333333", "1999alvaro@gmail.com", 4.5f, true,10, LocalDate.of(1998, 11, 20), null, null ,100,100   );


        when(autoescuelaService.add(inDto)).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/autoescuelas")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AutoescuelaDetailOutDto autoescuelaResponse = objectMapper.readValue(jsonResponse, AutoescuelaDetailOutDto.class);

        assertNotNull(autoescuelaResponse);
        assertEquals("Autoescuela Test", autoescuelaResponse.getNombre());
        assertEquals("Calle 123", autoescuelaResponse.getDireccion());

    }
        // 400
    @Test
    public void testPost_validacionError() throws Exception {
        AutoescuelaInDto inDto = new AutoescuelaInDto();
        inDto.setNombre("");
        inDto.setDireccion("Calle 123");


        mockMvc.perform(MockMvcRequestBuilders.post("/autoescuelas").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isBadRequest());
    }

    //PUT /autoescuelas/{id}

        // 200
    @Test
    public void testPut_ok() throws Exception {
        AutoescuelaInDto inDto =
                new AutoescuelaInDto("Autoescuela Test","Calle 123", "Reus", "633333333", "1999alvaro@gmail.com", 100 , 4.5f, LocalDate.of(1999,6,16), true, 100, 100);

        AutoescuelaDetailOutDto outDto =
                new AutoescuelaDetailOutDto(1L, "Autoescuela Test", "Calle 123", "Reus", "633333333", "1999alvaro@gmail.com", 4.5f, true,10, LocalDate.of(1998, 11, 20), null, null,100,100    );

        when(autoescuelaService.modify(anyLong(), any(AutoescuelaInDto.class))).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/autoescuelas/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AutoescuelaDetailOutDto autoescuelaResponse = objectMapper.readValue(jsonResponse, AutoescuelaDetailOutDto.class);

        assertNotNull(autoescuelaResponse);
        assertEquals("Autoescuela Test", autoescuelaResponse.getNombre());
        assertEquals("Calle 123", autoescuelaResponse.getDireccion());

    }
        // 404
    @Test
    public void testPut_notFound() throws Exception {
        AutoescuelaInDto inDto =
                new AutoescuelaInDto("Autoescuela Test","Calle 123", "Reus", "633333333", "1999alvaro@gmail.com", 100 , 4.5f, LocalDate.of(1999,6,16), true, 100, 100);

        when(autoescuelaService.modify(2L, inDto)).thenThrow(new AutoescuelaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.put("/autoescuelas/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isNotFound());
    }
        // 400
    @Test
    public void testPut_badRequest() throws Exception {
        AutoescuelaInDto inDto =
                new AutoescuelaInDto("Autoescuela Test","Calle 123", "Reus", "633333333", "1999alvaro", 100 , 4.5f, LocalDate.of(1999,6,16), true, 100, 100);

        when(autoescuelaService.modify(1L, inDto)).thenThrow(new AutoescuelaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.put("/autoescuelas/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(inDto)))
                .andExpect(status().isBadRequest());
    }

    //DELETE /autoescuelas/{id}

        // 204
    @Test
    public void testDelete_ok() throws Exception {
        Autoescuela autoescuela = new Autoescuela();
        autoescuela.setId(1L);

        doNothing().when(autoescuelaService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/autoescuelas/1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());

    }
        // 404
    @Test
    public void testDelete_notFound() throws Exception {

        doThrow(new AutoescuelaNotFoundException()).when(autoescuelaService).delete(99L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/autoescuelas/99")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

    }
        // 400
    @Test
    public void testDelete_badRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/autoescuelas/aa")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

    }

    //PATCH /autoescuelas/{id}

        // 200
    @Test
    public void testPatch_ok() throws Exception {


        Autoescuela autoeescuela = new  Autoescuela(1L, "Autoescuela Test", "Calle 123", "Reus", "911753917", "auto1@gmail.com",
                100, 4.5f, LocalDate.of(2010, 1, 2), true, 100, 100, null, null,null  ,null   );

        Map<String, Object> patch = new HashMap<>();
        patch.put("telefono", "633333333");
        AutoescuelaDetailOutDto outDto =
                new AutoescuelaDetailOutDto(1L, "Autoescuela Test", "Calle 123", "Reus", "633333333", "auto1@gmail.com", 4.5f, true,10, LocalDate.of(1998, 11, 20), null, null ,100,100   );


        when(autoescuelaService.patch(1L, patch)).thenReturn(outDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/autoescuelas/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AutoescuelaDetailOutDto autoescuelaResponse = objectMapper.readValue(jsonResponse, AutoescuelaDetailOutDto.class);

        assertNotNull(autoescuelaResponse);
        assertEquals("Autoescuela Test", autoescuelaResponse.getNombre());
        assertEquals("Calle 123", autoescuelaResponse.getDireccion());
        assertEquals("633333333", autoescuelaResponse.getTelefono());

    }
        // 404
    @Test
    public void testPatch_notFound() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("telefono", "633333333");


        when(autoescuelaService.patch(99L, patch)).thenThrow(new AutoescuelaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.patch("/autoescuelas/99")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isNotFound());
    }
        // 400
    @Test
    public void testPatch_badRequest() throws Exception {

        Autoescuela a = new Autoescuela();
        a.setId(1L);
        Map<String, Object> patch = new HashMap<>();
        patch.put("nombre", "");

        when(autoescuelaService.findById(1L)).thenThrow(new AutoescuelaNotFoundException());
        when(autoescuelaService.patch(1L, patch)).thenThrow(new BadRequestException());

        mockMvc.perform(MockMvcRequestBuilders.patch("/autoescuelas/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isBadRequest());
    }

    //GET ENDPOINTS COMPUESTOS

    //GET autoescuelas/{id}/profesores
    // 200
    @Test
    public void testFindProfesresById_ok() throws Exception {
        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");

        List<ProfesorOutDto> outDtos = List.of(
                new ProfesorOutDto(1 , "Pedro", "Perez Perez", "39964150V", "633889900", "Teorico", true),
                new ProfesorOutDto(2 , "Alvaro", "Reyes de Diego", "33311150V", "633112200", "Practicas", true)
        );


        when(autoescuelaService.getProfesores(1L)).thenReturn(outDtos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/1/profesores")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<ProfesorOutDto> profesorListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(profesorListResponse);
        assertEquals(2, profesorListResponse.size());
        assertEquals("Pedro", profesorListResponse.getFirst().getNombre());
        assertEquals("Alvaro", profesorListResponse.getLast().getNombre());
    }
    // 404
    @Test
    public void testFindProfesresById_notFound() throws Exception {
        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");

        when(autoescuelaService.getProfesores(99L)).thenThrow(new AutoescuelaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/99/profesores")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    // 400
    @Test
    public void testFindProfesresById_badRequest() throws Exception {
        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");

        mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/a/profesores")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

    }

    //GET autoescuelas/{id}/coches
    // 200
    @Test
    public void testFindCochesById_ok() throws Exception {
        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");

        List<CocheOutDto> outDtos = List.of(
                new CocheOutDto(1L, "Seat", "Ibiza", "Manual" ,null ),
                new CocheOutDto(2L, "Renault", "Clio", "Automatico", null)
        );

        when(autoescuelaService.getCoches(1L)).thenReturn(outDtos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/1/coches")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<CocheOutDto> cocheListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(cocheListResponse);
        assertEquals(2, cocheListResponse.size());
        assertEquals("Ibiza", cocheListResponse.getFirst().getModelo());
        assertEquals("Clio", cocheListResponse.getLast().getModelo());
    }
    // 404
    @Test
    public void testFindCochesById_notFound() throws Exception {
        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");


        when(autoescuelaService.getCoches(99L)).thenThrow(new AutoescuelaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/99/coches")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

    }
    @Test
    public void testFindCochesById_badRequest() throws Exception {
        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");

        mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/a/coches")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }



    //GET autoescuelas/{id}/coches
    // 200
    @Test
    public void testFindMatriculasById_ok() throws Exception {
        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");

        List<MatriculaOutDto> outDtos = List.of(
                new MatriculaOutDto(1L, "Presencial", "Permiso B", false, null, null),
                new MatriculaOutDto(2L, "Online", "Permiso A", true, null, null)
        );

        when(autoescuelaService.getMatriculas(1L)).thenReturn(outDtos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/1/matriculas")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<MatriculaOutDto> matriculaListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(matriculaListResponse);
        assertEquals(2, matriculaListResponse.size());
        assertEquals("Presencial", matriculaListResponse.getFirst().getModalidad());
        assertEquals("Online", matriculaListResponse.getLast().getModalidad());
    }

    // 404
    @Test
    public void testFindMatriculasById_notFound() throws Exception {
        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");

        when(autoescuelaService.getMatriculas(99L)).thenThrow(new AutoescuelaNotFoundException());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/99/matriculas")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    // 400
    @Test
    public void testFindMatriculasById_badRequest() throws Exception {
        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");

        mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/a/matriculas")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    //GET autoescuelas/{id}/coches
    // 200
    @Test
    public void testFindMatriculasCompletadasById_ok() throws Exception {

        AutoescuelaOutDto autoescuela = new AutoescuelaOutDto();
        autoescuela.setId(1L);
        autoescuela.setNombre("Autoescuela Test");
        autoescuela.setDireccion("Calle 123");

        AlumnoOutDto alumno = new AlumnoOutDto();
        alumno.setId(1L);
        alumno.setNombre("Alvaro");

        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");

        List<MatriculaOutDto> outDtos = List.of(
                new MatriculaOutDto(1L, "Presencial", "Permiso B", true, autoescuela, alumno),
                new MatriculaOutDto(2L, "Online", "Permiso A", true, autoescuela, alumno)
        );

        when(autoescuelaService.getMatriculasCompletas(1L)).thenReturn(outDtos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/1/matriculas/completadas")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<MatriculaOutDto> matriculaListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(matriculaListResponse);
        assertEquals(2, matriculaListResponse.size());
        assertEquals("Presencial", matriculaListResponse.getFirst().getModalidad());
        assertEquals("Online", matriculaListResponse.getLast().getModalidad());
    }

    // 404
    @Test
    public void testFindMatriculasCompletadasById_notFound() throws Exception {

        AutoescuelaOutDto autoescuela = new AutoescuelaOutDto();
        autoescuela.setId(1L);
        autoescuela.setNombre("Autoescuela Test");
        autoescuela.setDireccion("Calle 123");

        AlumnoOutDto alumno = new AlumnoOutDto();
        alumno.setId(1L);
        alumno.setNombre("Alvaro");

        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");



        when(autoescuelaService.getMatriculasCompletas(99L)).thenThrow(new AutoescuelaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/99/matriculas/completadas")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

    }
    // 400
    @Test
    public void testFindMatriculasCompletadasById_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/a/matriculas/completadas")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

    }

    //GET autoescuelas/{id}/coches
    // 200
    @Test
    public void testFindAlumnosSuspensosById_ok() throws Exception {
        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");

        List<AlumnoOutDto> outDtos = List.of(
                new AlumnoOutDto(1L, "Alvaro", "Reyes","39999999V", "Reus", 2.4f),
                new AlumnoOutDto(2L, "Pedro", "Perez","67676767V", "Zaragoza", 4.2f)
        );

        when(autoescuelaService.getAlumnosSuspensos(1L)).thenReturn(outDtos);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/1/alumnos/suspensos")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<AlumnoOutDto> alumnoListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(alumnoListResponse);
        assertEquals(2, alumnoListResponse.size());
        assertEquals("Alvaro", alumnoListResponse.getFirst().getNombre());
        assertEquals("Pedro", alumnoListResponse.getLast().getNombre());
    }
    // 404
    @Test
    public void testFindAlumnosSuspensosById_notFound() throws Exception {
        AutoescuelaDetailOutDto dto = new AutoescuelaDetailOutDto();
        dto.setId(1L);
        dto.setNombre("Autoescuela Test");
        dto.setDireccion("Calle 123");

        when(autoescuelaService.getAlumnosSuspensos(99L)).thenThrow(new AutoescuelaNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/99/alumnos/suspensos")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }
    // 400
    @Test
    public void testFindAlumnosSuspensosById_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/autoescuelas/a/alumnos/suspensos")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}



