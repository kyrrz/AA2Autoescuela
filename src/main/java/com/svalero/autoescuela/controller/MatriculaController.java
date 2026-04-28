package com.svalero.autoescuela.controller;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.*;
import com.svalero.autoescuela.model.Matricula;
import com.svalero.autoescuela.service.AlumnoService;
import com.svalero.autoescuela.service.AutoescuelaService;
import com.svalero.autoescuela.service.MatriculaService;
import jakarta.persistence.Temporal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MatriculaController {
    @Autowired
    private MatriculaService matriculaService;
    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private AutoescuelaService autoescuelaService;

    @GetMapping("/v1/matriculas")
    public ResponseEntity<List<MatriculaOutDto>> getAll(
            @RequestParam(required = false) String modalidad,
            @RequestParam(required = false) String tipoMatricula,
            @RequestParam(required = false) Integer horasPracticas,
            @RequestParam(required = false) Integer horasTeoricas
    ) {
        List<MatriculaOutDto> mod = matriculaService.findByFiltros(modalidad, tipoMatricula, horasPracticas, horasTeoricas);

        return new ResponseEntity<>(mod, HttpStatus.OK);
    }

    @GetMapping("/v1/matriculas/{id}")
    public ResponseEntity<MatriculaDetailOutDto> getMatriculaById(@PathVariable long id) throws MatriculaNotFoundException {
        return ResponseEntity.ok(matriculaService.findById(id));
    }

    @PostMapping("/v1/matriculas")
    public ResponseEntity<MatriculaDetailOutDto> addMatricula(@Valid  @RequestBody MatriculaInDto matriculaInDto) throws MatriculaNotFoundException, AlumnoNotFoundException, AutoescuelaNotFoundException {
        AlumnoDetailOutDto alumnoDetailOutDto = alumnoService.findById(matriculaInDto.getAlumnoId());
        AutoescuelaDetailOutDto autoescuelaDetailOutDto = autoescuelaService.findById(matriculaInDto.getAutoescuelaId());
        MatriculaDetailOutDto matriculaDetailOutDto = matriculaService.add(matriculaInDto, alumnoDetailOutDto, autoescuelaDetailOutDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaDetailOutDto);
    }

    @PutMapping("/v1/matriculas/{id}")
    public ResponseEntity<MatriculaDetailOutDto> modifyMatricula(@Valid @RequestBody MatriculaInDto matriculaInDto, @PathVariable long id) throws MatriculaNotFoundException,AlumnoNotFoundException, AutoescuelaNotFoundException  {
        AlumnoDetailOutDto alumnoDetailOutDto = alumnoService.findById(matriculaInDto.getAlumnoId());
        AutoescuelaDetailOutDto autoescuelaDetailOutDto = autoescuelaService.findById(matriculaInDto.getAutoescuelaId());
        MatriculaDetailOutDto matriculaDetailOutDto = matriculaService.modify(id, matriculaInDto, alumnoDetailOutDto, autoescuelaDetailOutDto);
        return ResponseEntity.ok(matriculaDetailOutDto);
    }


    /* Añadido nuevo campo TIPO DE PAGO, que solo permite transferencia o tarjeta y añadido dos checks,
    * uno para que no se pueda completar la matricula si un alumno no ha hecho menos de 5 horas teoricas
    * y otro para, que si la matricula esta hecha con transferencia para completarla
    * el banco tiene que confirmar el pago */
    @PutMapping("/v2/matriculas/{id}")
    public ResponseEntity<MatriculaDetailOutDtoV2> updateMatriculaV2( @PathVariable long id, @Valid @RequestBody MatriculaInDtoV2 dto)
            throws MatriculaNotFoundException, ValidationException {

        if (dto.isCompletada() && dto.getHorasTeoricas() < 5) {
            throw new ValidationException("No se puede completar la matrícula: el alumno necesita al menos 5 horas teóricas.");
        }

        MatriculaDetailOutDtoV2 actualizada = matriculaService.modifyV2(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/v1/matriculas/{id}")
    public ResponseEntity<Void> deleteMatricula(@PathVariable long id) throws MatriculaNotFoundException {
        matriculaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/v1/matriculas/{id}")
    public ResponseEntity<MatriculaDetailOutDto> patchMatricula(@PathVariable Long id, @RequestBody Map<String, Object> patch) throws MatriculaNotFoundException, AutoescuelaNotFoundException, AlumnoNotFoundException {

        MatriculaDetailOutDto matriculaActualizada = matriculaService.patch(id, patch);
        return ResponseEntity.ok(matriculaActualizada);
    }

    @ExceptionHandler(MatriculaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(MatriculaNotFoundException mnfe){
        ErrorResponse errorResponse = ErrorResponse.notFound("Matricula no encontrada");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlumnoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(AlumnoNotFoundException anfe){
        ErrorResponse errorResponse = ErrorResponse.notFound("Alumno no encontrado");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AutoescuelaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(AutoescuelaNotFoundException aunfe){
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
}
