package bw_team7.gestione_azienda_energia.indirizzi.controllers;

import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.indirizzi.entities.Indirizzo;
import bw_team7.gestione_azienda_energia.indirizzi.payloads.IndirizzoDTO;
import bw_team7.gestione_azienda_energia.indirizzi.services.IndirizzoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/indirizzi")
public class IndirizzoController {

    private final IndirizzoService indirizzoService;

    public IndirizzoController(IndirizzoService indirizzoService) {
        this.indirizzoService = indirizzoService;
    }

    // POST - Creazione Indirizzo: Solo ADMIN
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Indirizzo saveIndirizzo(@RequestBody @Validated IndirizzoDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        return this.indirizzoService.save(body);
    }

    // GET ALL - Lettura Indirizzi: ADMIN e USER
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public Page<Indirizzo> getIndirizzi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy
    ) {
        return this.indirizzoService.getAll(page, size, orderBy);
    }

    // GET BY ID - Lettura Singolo Indirizzo: ADMIN e USER
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public Indirizzo getById(@PathVariable UUID id) {
        return this.indirizzoService.findById(id);
    }

    // PUT - Modifica Indirizzo: Solo ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Indirizzo getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated IndirizzoDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.indirizzoService.findByIdAndUpdate(id, body);
    }

    // DELETE - Cancellazione Indirizzo: Solo ADMIN
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.indirizzoService.findByIdAndDelete(id);
    }
}