package com.svalero.autoescuela.controller;



import com.svalero.autoescuela.dto.*;
import com.svalero.autoescuela.exception.*;
import com.svalero.autoescuela.service.AutoescuelaService;
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
public class AutoescuelaController {

    @Autowired
    private AutoescuelaService autoescuelaService;

    @GetMapping("/v1/autoescuelas")
    public ResponseEntity<List<AutoescuelaOutDto>> getAll(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Boolean activa
    ) {

        List<AutoescuelaOutDto> autoescuelaOutDtos = autoescuelaService.findByFiltros(ciudad, minRating, activa);
        return ResponseEntity.ok(autoescuelaOutDtos);
    }

    @GetMapping("/v1/autoescuelas/{id}")
    public ResponseEntity<AutoescuelaDetailOutDto> getAutoescuelaById(@PathVariable long id) throws AutoescuelaNotFoundException {
        return ResponseEntity.ok(autoescuelaService.findById(id));
    }

    @GetMapping("/v1/autoescuelas/{id}/profesores")
    public ResponseEntity<List<ProfesorOutDto>> getProfesoresByAutoescuelaId(@PathVariable long id) throws AutoescuelaNotFoundException {
        List<ProfesorOutDto> profesores = autoescuelaService.getProfesores(id);

        return ResponseEntity.ok(profesores);
    }

    @GetMapping("/v1/autoescuelas/{id}/coches")
    public ResponseEntity<List<CocheOutDto>> getCochesByAutoescuelaId(@PathVariable long id) throws AutoescuelaNotFoundException {
        List<CocheOutDto> coches = autoescuelaService.getCoches(id);

        return ResponseEntity.ok(coches);
    }

    @GetMapping("/v1/autoescuelas/{id}/matriculas")
    public ResponseEntity<List<MatriculaOutDto>> getMatriculasByAutoescuelaId(@PathVariable long id) throws AutoescuelaNotFoundException {
        List<MatriculaOutDto> matricula = autoescuelaService.getMatriculas(id);

        return ResponseEntity.ok(matricula);
    }

    @GetMapping("/v1/autoescuelas/{id}/matriculas/completadas")
    public ResponseEntity<List<MatriculaOutDto>> getMatriculasCompletas(@PathVariable Long id) throws AutoescuelaNotFoundException {

        return ResponseEntity.ok(
                autoescuelaService.getMatriculasCompletas(id)
        );
    }
    @GetMapping("/v1/autoescuelas/{id}/alumnos/suspensos")
    public ResponseEntity<List<AlumnoOutDto>> getAlumnosSuspensosByAutoescuelaId(@PathVariable long id) throws AutoescuelaNotFoundException {
        return ResponseEntity.ok(autoescuelaService.getAlumnosSuspensos(id));
    }




    @PostMapping("/v1/autoescuelas")
    public ResponseEntity<AutoescuelaDetailOutDto> addAutoescuela(@Valid  @RequestBody AutoescuelaInDto autoescuelaInDto){
        AutoescuelaDetailOutDto a = autoescuelaService.add(autoescuelaInDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(a);
    }

    @PutMapping("/v1/autoescuelas/{id}")
    public ResponseEntity<AutoescuelaDetailOutDto> modifyAutoescuela(@Valid @RequestBody AutoescuelaInDto autoescuelaInDto, @PathVariable long id) throws AutoescuelaNotFoundException {
        AutoescuelaDetailOutDto a = autoescuelaService.modify(id, autoescuelaInDto);
        return ResponseEntity.ok(a);
    }

    @DeleteMapping("/v1/autoescuelas/{id}")
    public ResponseEntity<Void> deleteAutoescuela(@PathVariable long id) throws AutoescuelaNotFoundException {
        autoescuelaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /* En vez de borrar la autoescuela, utilizaremos el campo active que tiene para ponerla inactiva
    y después quitamos todos los coches, profesores y alumnos que haya en ella.
     Las matriculas he decidido no quitarlas, ya que para el hacer papeles para cambiar de autoescuela
     se necesitarian tener para el translado*/
    @DeleteMapping("/v2/autoescuelas/{id}")
    public ResponseEntity<Void> deleteAutoescuelaV2(@PathVariable long id) throws AutoescuelaNotFoundException {
        autoescuelaService.deleteV2(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/v1/autoescuelas/{id}")
    public ResponseEntity<AutoescuelaDetailOutDto> patchAutoescuela(@Valid @PathVariable Long id, @RequestBody Map<String, Object> patch) throws AutoescuelaNotFoundException, BadRequestException {
        AutoescuelaDetailOutDto autoescuelaPatch = autoescuelaService.patch(id, patch);
        return ResponseEntity.ok(autoescuelaPatch);
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
}
