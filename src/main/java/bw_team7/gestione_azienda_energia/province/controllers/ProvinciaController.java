package bw_team7.gestione_azienda_energia.province.controllers;

import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.province.entities.Provincia;
import bw_team7.gestione_azienda_energia.province.payloads.ProvinciaDTO;
import bw_team7.gestione_azienda_energia.province.services.ProvinciaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public class ProvinciaController {

    private final ProvinciaService provinciaService;

    public ProvinciaController(ProvinciaService provinciaService) {
        this.provinciaService = provinciaService;
    }

    // POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Provincia saveProvincia(@RequestBody @Validated ProvinciaDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        return this.provinciaService.save(payload);
    }

    //GET
    @GetMapping
    public Page<Provincia> getProvincia(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy
    ) {
        return this.provinciaService.getAll(page, size, orderBy);
    }

    @GetMapping("/{id}")
    public Provincia getById(@PathVariable UUID id) {
        return this.provinciaService.findById(id);
    }

    //PUT
    @PutMapping("/{id}")
    public Provincia getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated ProvinciaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.provinciaService.findByIdAndUpdate(id, body);
    }

    //DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.provinciaService.findByIdAndDelete(id);
    }

}
