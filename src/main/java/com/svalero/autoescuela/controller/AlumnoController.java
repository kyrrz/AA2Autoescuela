package com.svalero.autoescuela.controller;

import com.svalero.autoescuela.dto.AlumnoDetailOutDto;
import com.svalero.autoescuela.dto.AlumnoInDto;
import com.svalero.autoescuela.dto.AlumnoOutDto;
import com.svalero.autoescuela.dto.AutoescuelaDetailOutDto;
import com.svalero.autoescuela.exception.AlumnoNotFoundException;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.BadRequestException;
import com.svalero.autoescuela.exception.ErrorResponse;
import com.svalero.autoescuela.model.Alumno;
import com.svalero.autoescuela.service.AlumnoService;
import com.svalero.autoescuela.service.AutoescuelaService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private AutoescuelaService autoescuelaService;

    @GetMapping("/v1/alumnos")
    public ResponseEntity<List<AlumnoOutDto>> getAll(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) Boolean usaGafas,
            @RequestParam(required = false) Float minNotaTeorico
    ) {
        List<AlumnoOutDto> alumnoOutDtos = alumnoService.findByFiltros(nombre, ciudad, usaGafas, minNotaTeorico);
        return ResponseEntity.ok(alumnoOutDtos);
    }

    /*Pensado para una API grande, el que tenga que mostrar todos los alumnos sin paginación sería una carga muy grande
    he pensado también  que hacer el filtrado por Apellidos es mucho mejor, ya que en ámbito profesional es mucho mejor
    tener guardados a los alumnos con Apellido1 Apellido2, Nombre , Ej: Reyes de Diego, Álvaro. */
    @GetMapping("/v2/alumnos")
    public ResponseEntity<Page<AlumnoOutDto>> getAlumnosV2(
            @RequestParam(required = false) String apellido,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(alumnoService.findByFiltrosV2(apellido, pageable));
    }

    @GetMapping("/v1/alumnos/{id}")
    public ResponseEntity<AlumnoDetailOutDto> getAlumnoById(@PathVariable long id) throws AlumnoNotFoundException{
        return ResponseEntity.ok(alumnoService.findById(id));
    }


    @PostMapping("/v1/alumnos")
    public ResponseEntity<AlumnoDetailOutDto> addAlumno(@Valid @RequestBody AlumnoInDto alumnoInDto) throws AutoescuelaNotFoundException {

        AutoescuelaDetailOutDto autoescuelaDetailOutDto = autoescuelaService.findById(alumnoInDto.getAutoescuelaId());
        AlumnoDetailOutDto nuevoAlumno = alumnoService.add(alumnoInDto, autoescuelaDetailOutDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlumno);
    }

    @PutMapping("/v1/alumnos/{id}")
    public ResponseEntity<AlumnoDetailOutDto> modifyAlumno(@Valid @RequestBody AlumnoInDto alumnoInDto, @PathVariable long id) throws AlumnoNotFoundException, AutoescuelaNotFoundException {
        AutoescuelaDetailOutDto autoescuelaDetailOutDto = autoescuelaService.findById(alumnoInDto.getAutoescuelaId());
        System.out.println(autoescuelaDetailOutDto);
        AlumnoDetailOutDto alumnoUpdated = alumnoService.modify(id, alumnoInDto, autoescuelaDetailOutDto);

        return ResponseEntity.ok(alumnoUpdated);
    }

    @DeleteMapping("/v1/alumnos/{id}")
    public ResponseEntity<Void> deleteAlumno(@PathVariable long id) throws AlumnoNotFoundException {
        alumnoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/v1/alumnos/{id}")
    public ResponseEntity<AlumnoDetailOutDto> patchAlumno(@Valid @RequestBody Map<String, Object> patch, @PathVariable long id) throws AlumnoNotFoundException, AutoescuelaNotFoundException, BadRequestException {
        AlumnoDetailOutDto alumnoDetailOutDto = alumnoService.patch(id, patch);
        return  ResponseEntity.ok(alumnoDetailOutDto);
    }

    @ExceptionHandler(AlumnoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(AlumnoNotFoundException anfe){
        ErrorResponse errorResponse = ErrorResponse.notFound("Alumno no encontrado");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AutoescuelaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(AutoescuelaNotFoundException anfe){
        ErrorResponse errorResponse = ErrorResponse.notFound("Autoescuela no encontrada");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException manve){
        Map<String,String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName,message);
        });
        ErrorResponse errorResponse = ErrorResponse.validationError(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleException(BadRequestException bre){
        ErrorResponse errorResponse = ErrorResponse.badRequest("Bad request");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorResponse errorResponse = ErrorResponse.badRequest("Formato de ID incorrecto. Debe ser un número.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
