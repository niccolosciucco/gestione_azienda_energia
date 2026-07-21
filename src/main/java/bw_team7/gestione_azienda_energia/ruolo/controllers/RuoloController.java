package bw_team7.gestione_azienda_energia.ruolo.controllers;

import bw_team7.gestione_azienda_energia.ruolo.entities.Ruolo;
import bw_team7.gestione_azienda_energia.ruolo.services.RuoloService;
import org.springframework.http.HttpStatus;
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

    //Get All
    @GetMapping
    public List<Ruolo> getAllRuoli() {
        return ruoloService.findAll();
    }

    //Get
    @GetMapping("/{id}")
    public Ruolo getRuoloById(@PathVariable UUID id) {
        return ruoloService.findById(id);
    }

    // POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ruolo createRuolo(@RequestBody Ruolo body) {
        return this.ruoloService.save(body);
    }
}
