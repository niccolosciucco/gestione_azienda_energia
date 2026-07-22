package bw_team7.gestione_azienda_energia.utenti.controllers;

import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.utenti.entities.Utente;
import bw_team7.gestione_azienda_energia.utenti.payloads.UtenteDTO;
import bw_team7.gestione_azienda_energia.utenti.services.UtenteService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    // GET ALL - Elenco Utenti: Solo ADMIN
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<Utente> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy) {
        return this.utenteService.getAll(page, size, orderBy);
    }

    // GET BY ID - Dettaglio Utente: Solo ADMIN
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Utente findById(@PathVariable UUID id) {
        return this.utenteService.findById(id);
    }

    // POST - Creazione Utente da Backoffice: Solo ADMIN
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Utente save(@RequestBody @Validated UtenteDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            String errors = validation.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(". "));

            throw new BadRequest(errors);
        }
        return this.utenteService.save(body);
    }

    // PUT - Modifica Utente: Solo ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Utente findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated UtenteDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            String errors = validation.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(". "));

            throw new BadRequest(errors);
        }
        return this.utenteService.findByIdAndUpdate(id, body);
    }

    // PATCH AVATAR - Modifica Avatar: Solo ADMIN
    @PatchMapping("/{id}/avatar")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Utente updateAvatar(@PathVariable UUID id, @RequestParam("avatar") MultipartFile file) {
        return this.utenteService.updateAvatar(id, file);
    }

    // DELETE - Eliminazione Utente: Solo ADMIN
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID id) {
        this.utenteService.findByIdAndDelete(id);
    }

    // PATCH - Assegnazione Ruolo: Solo ADMIN
    @PatchMapping("/{id}/ruolo")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Utente updateRuoloUtente(@PathVariable UUID id, @RequestParam String nuovoNomeRuolo) {
        return this.utenteService.updateRuoloUtente(id, nuovoNomeRuolo);
    }

    // DELETE - Rimozione Ruolo: Solo ADMIN
    @DeleteMapping("/{id}/ruolo")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Utente removeRuoloUtente(
            @PathVariable UUID id,
            @RequestParam(name = "nomeRuolo") String nomeRuolo) {

        return this.utenteService.removeRuoloUtente(id, nomeRuolo);
    }
}