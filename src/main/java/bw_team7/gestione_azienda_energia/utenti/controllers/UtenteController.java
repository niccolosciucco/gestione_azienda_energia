package bw_team7.gestione_azienda_energia.utenti.controllers;

import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.utenti.entities.Utente;
import bw_team7.gestione_azienda_energia.utenti.payloads.UtenteDTO;
import bw_team7.gestione_azienda_energia.utenti.services.UtenteService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    // GET ALL
    @GetMapping
    public Page<Utente> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy) {
        return this.utenteService.getAll(page, size, orderBy);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Utente findById(@PathVariable UUID id) {
        return this.utenteService.findById(id);
    }

    // POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente save(@RequestBody @Validated UtenteDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            String errors = validation.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(". "));

            throw new BadRequest(errors);
        }
        return this.utenteService.save(body);
    }

    // PUT
    @PutMapping("/{id}")
    public Utente findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated UtenteDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            String errors = validation.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(". "));

            throw new BadRequest(errors);
        }
        return this.utenteService.findByIdAndUpdate(id, body);
    }

    // PATCH AVATAR
    @PatchMapping("/{id}/avatar")
    public Utente updateAvatar(@PathVariable UUID id, @RequestParam("avatar") MultipartFile file) {
        return this.utenteService.updateAvatar(id, file);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID id) {
        this.utenteService.findByIdAndDelete(id);
    }
}