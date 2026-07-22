package bw_team7.gestione_azienda_energia.fatture.controllers;

import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.fatture.entities.Fattura;
import bw_team7.gestione_azienda_energia.fatture.payloads.FatturaDTO;
import bw_team7.gestione_azienda_energia.fatture.services.FatturaService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fatture")
public class FatturaController {

    private final FatturaService fatturaService;

    public FatturaController(FatturaService fatturaService) {
        this.fatturaService = fatturaService;
    }

    // POST - Creazione Fattura: Solo ADMIN
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Fattura saveFattura(@RequestBody @Validated FatturaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        return this.fatturaService.save(body);
    }

    // GET Filtri - Lettura Fatture: ADMIN e USER
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public Page<Fattura> getAllFatture(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) UUID clienteId,
            @RequestParam(required = false) String stato,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Integer anno,
            @RequestParam(required = false) BigDecimal minImporto,
            @RequestParam(required = false) BigDecimal maxImporto
    ) {
        return fatturaService.getFattureFiltered(
                page,
                size,
                clienteId,
                stato,
                data,
                anno,
                minImporto, maxImporto
        );
    }

    // GET BY ID - Lettura Singola Fattura: ADMIN e USER
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public Fattura getById(@PathVariable UUID id) {
        return this.fatturaService.findById(id);
    }

    // PUT - Modifica Fattura: Solo ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Fattura getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated FatturaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.fatturaService.findByIdAndUpdate(id, body);
    }

    // DELETE - Cancellazione Fattura: Solo ADMIN
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.fatturaService.findByIdAndDelete(id);
    }
}