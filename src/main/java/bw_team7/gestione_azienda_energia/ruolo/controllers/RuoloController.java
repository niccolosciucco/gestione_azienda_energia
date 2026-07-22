package bw_team7.gestione_azienda_energia.ruolo.controllers;

import bw_team7.gestione_azienda_energia.ruolo.entities.Ruolo;
import bw_team7.gestione_azienda_energia.ruolo.services.RuoloService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ruoli")
public class RuoloController {

    private final RuoloService ruoloService;

    public RuoloController(RuoloService ruoloService) {
        this.ruoloService = ruoloService;
    }

    // GET ALL - Lettura Ruoli: Solo ADMIN
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Ruolo> getAllRuoli() {
        return ruoloService.findAll();
    }

    // GET BY ID - Lettura Singolo Ruolo: Solo ADMIN
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Ruolo getRuoloById(@PathVariable UUID id) {
        return ruoloService.findById(id);
    }

    // POST - Creazione Ruolo: Solo ADMIN
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Ruolo createRuolo(@RequestBody Ruolo body) {
        return this.ruoloService.save(body);
    }
}