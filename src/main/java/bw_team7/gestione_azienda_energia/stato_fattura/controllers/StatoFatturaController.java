package bw_team7.gestione_azienda_energia.stato_fattura.controllers;

import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.stato_fattura.entities.StatoFattura;
import bw_team7.gestione_azienda_energia.stato_fattura.payloads.StatoFatturaDTO;
import bw_team7.gestione_azienda_energia.stato_fattura.services.StatoFatturaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stati-fattura")
public class StatoFatturaController {

    private final StatoFatturaService statoFatturaService;

    public StatoFatturaController(StatoFatturaService statoFatturaService) {
        this.statoFatturaService = statoFatturaService;
    }

    // POST - Creazione Stato Fattura: Solo ADMIN
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public StatoFattura saveStatoFattura(@RequestBody @Validated StatoFatturaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        return this.statoFatturaService.save(body);
    }

    // GET ALL - Lettura Stati Fattura: ADMIN e USER
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public Page<StatoFattura> getStatiFattura(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy
    ) {
        return this.statoFatturaService.getAll(page, size, orderBy);
    }

    // GET BY ID - Lettura Singolo Stato Fattura: ADMIN e USER
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public StatoFattura getById(@PathVariable UUID id) {
        return this.statoFatturaService.findById(id);
    }

    // PUT - Modifica Stato Fattura: Solo ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public StatoFattura getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated StatoFatturaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.statoFatturaService.findByIdAndUpdate(id, body);
    }

    // DELETE - Cancellazione Stato Fattura: Solo ADMIN
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.statoFatturaService.findByIdAndDelete(id);
    }

}