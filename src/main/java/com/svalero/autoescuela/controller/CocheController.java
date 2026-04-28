package com.svalero.autoescuela.controller;

import com.svalero.autoescuela.dto.AutoescuelaDetailOutDto;
import com.svalero.autoescuela.dto.CocheDetailOutDto;
import com.svalero.autoescuela.dto.CocheInDto;
import com.svalero.autoescuela.dto.CocheOutDto;
import com.svalero.autoescuela.exception.AutoescuelaNotFoundException;
import com.svalero.autoescuela.exception.BadRequestException;
import com.svalero.autoescuela.exception.CocheNotFoundException;
import com.svalero.autoescuela.exception.ErrorResponse;
import com.svalero.autoescuela.service.AutoescuelaService;
import com.svalero.autoescuela.service.CocheService;
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
public class CocheController {

    @Autowired
    private CocheService cocheService;
    @Autowired
    private AutoescuelaService autoescuelaService;

    @GetMapping("/v1/coches")
    public ResponseEntity<List<CocheOutDto>> getAll(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String tipoCambio
                                                    ) {

        List<CocheOutDto> cod = cocheService.findByFiltros(marca, modelo, tipoCambio);
        return ResponseEntity.ok(cod);
    }

    @GetMapping("/v1/coches/{id}")
    public ResponseEntity<CocheDetailOutDto> getCocheById(@PathVariable long id) throws CocheNotFoundException {
        return ResponseEntity.ok(cocheService.findById(id));
    }

    @PostMapping("/v1/coches")
    public ResponseEntity<CocheDetailOutDto> addCoche(@Valid  @RequestBody CocheInDto cocheInDto) throws AutoescuelaNotFoundException {

        AutoescuelaDetailOutDto autoescuelaDetailOutDto = autoescuelaService.findById(cocheInDto.getAutoescuelaId());
        CocheDetailOutDto cocheDetailOutDto = cocheService.add(cocheInDto, autoescuelaDetailOutDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(cocheDetailOutDto);
    }

    @PutMapping("/v1/coches/{id}")
    public ResponseEntity<CocheDetailOutDto> modifyCoche(@Valid @RequestBody CocheInDto cocheInDto, @PathVariable long id) throws CocheNotFoundException, AutoescuelaNotFoundException {

        AutoescuelaDetailOutDto autoescuelaDetailOutDto = autoescuelaService.findById(cocheInDto.getAutoescuelaId());
        CocheDetailOutDto cocheDetailOutDto = cocheService.modify(id, cocheInDto, autoescuelaDetailOutDto);

        return ResponseEntity.ok(cocheDetailOutDto);
    }

    @DeleteMapping("/v1/coches/{id}")
    public ResponseEntity<Void> deleteCoche(@PathVariable long id) throws CocheNotFoundException {
        cocheService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/v1/coches/{id}")
    public ResponseEntity<CocheDetailOutDto> patchCoche(@PathVariable Long id, @RequestBody Map<String, Object> patch) throws CocheNotFoundException, AutoescuelaNotFoundException {

        CocheDetailOutDto cocheActualizado = cocheService.patch(id, patch);
        return ResponseEntity.ok(cocheActualizado);
    }

    /* Añadido un patch más robusto que no permite cambiar campos que son inmutables en un coche
    * y lógica para no poder reducir el kilometraje del coche, ya que este campo nunca podría reducirse*/
    @PatchMapping("/v2/coches/{id}")
    public ResponseEntity<CocheDetailOutDto> patchCocheV2(@PathVariable Long id, @RequestBody Map<String, Object> patch) throws CocheNotFoundException, AutoescuelaNotFoundException {

        CocheDetailOutDto cocheActualizado = cocheService.patchV2(id, patch);
        return ResponseEntity.ok(cocheActualizado);
    }

    @ExceptionHandler(CocheNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(CocheNotFoundException cnfe){
        ErrorResponse errorResponse = ErrorResponse.notFound("Coche no encontrado");
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
}
