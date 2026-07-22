package bw_team7.gestione_azienda_energia.comuni.controllers;

import bw_team7.gestione_azienda_energia.comuni.entities.Comune;
import bw_team7.gestione_azienda_energia.comuni.payloads.ComuneDTO;
import bw_team7.gestione_azienda_energia.comuni.services.ComuneService;
import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comuni")
public class ComuneController {

    private final ComuneService comuneService;

    public ComuneController(ComuneService comuneService) {
        this.comuneService = comuneService;
    }

    // POST - Creazione Comune: Solo ADMIN
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Comune saveComune(@RequestBody @Validated ComuneDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        return this.comuneService.save(body);
    }

    // GET ALL - Lettura Comuni: ADMIN e USER
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public Page<Comune> getComuni(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy
    ) {
        return this.comuneService.getAll(page, size, orderBy);
    }

    // GET BY ID - Lettura Singolo Comune: ADMIN e USER
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public Comune getById(@PathVariable UUID id) {
        return this.comuneService.findById(id);
    }

    // PUT - Modifica Comune: Solo ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Comune getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated ComuneDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.comuneService.findByIdAndUpdate(id, body);
    }

    // DELETE - Cancellazione Comune: Solo ADMIN
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.comuneService.findByIdAndDelete(id);
    }

}