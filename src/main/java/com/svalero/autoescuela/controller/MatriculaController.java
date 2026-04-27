package com.svalero.autoescuela.controller;

import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.*;
import com.svalero.autoescuela.service.AlumnoService;
import com.svalero.autoescuela.service.AutoescuelaService;
import com.svalero.autoescuela.service.MatriculaService;
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
@RequestMapping("/matriculas")
public class MatriculaController {
    @Autowired
    private MatriculaService matriculaService;
    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private AutoescuelaService autoescuelaService;

    @GetMapping("")
    public ResponseEntity<List<MatriculaOutDto>> getAll(
            @RequestParam(required = false) String modalidad,
            @RequestParam(required = false) String tipoMatricula,
            @RequestParam(required = false) Integer horasPracticas,
            @RequestParam(required = false) Integer horasTeoricas
    ) {
        List<MatriculaOutDto> mod = matriculaService.findByFiltros(modalidad, tipoMatricula, horasPracticas, horasTeoricas);

        return new ResponseEntity<>(mod, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaDetailOutDto> getMatriculaById(@PathVariable long id) throws MatriculaNotFoundException {
        return ResponseEntity.ok(matriculaService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<MatriculaDetailOutDto> addMatricula(@Valid  @RequestBody MatriculaInDto matriculaInDto) throws MatriculaNotFoundException, AlumnoNotFoundException, AutoescuelaNotFoundException {
        AlumnoDetailOutDto alumnoDetailOutDto = alumnoService.findById(matriculaInDto.getAlumnoId());
        AutoescuelaDetailOutDto autoescuelaDetailOutDto = autoescuelaService.findById(matriculaInDto.getAutoescuelaId());
        MatriculaDetailOutDto matriculaDetailOutDto = matriculaService.add(matriculaInDto, alumnoDetailOutDto, autoescuelaDetailOutDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaDetailOutDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatriculaDetailOutDto> modifyMatricula(@Valid @RequestBody MatriculaInDto matriculaInDto, @PathVariable long id) throws MatriculaNotFoundException,AlumnoNotFoundException, AutoescuelaNotFoundException  {
        AlumnoDetailOutDto alumnoDetailOutDto = alumnoService.findById(matriculaInDto.getAlumnoId());
        AutoescuelaDetailOutDto autoescuelaDetailOutDto = autoescuelaService.findById(matriculaInDto.getAutoescuelaId());
        MatriculaDetailOutDto matriculaDetailOutDto = matriculaService.modify(id, matriculaInDto, alumnoDetailOutDto, autoescuelaDetailOutDto);
        return ResponseEntity.ok(matriculaDetailOutDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatricula(@PathVariable long id) throws MatriculaNotFoundException {
        matriculaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
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
