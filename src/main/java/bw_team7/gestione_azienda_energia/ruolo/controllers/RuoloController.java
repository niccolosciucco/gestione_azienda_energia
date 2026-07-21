package bw_team7.gestione_azienda_energia.ruolo.controllers;

import bw_team7.gestione_azienda_energia.ruolo.entities.Ruolo;
import bw_team7.gestione_azienda_energia.ruolo.services.RuoloService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ruoli")
public class RuoloController {

    private final RuoloService ruoloService;

    public RuoloController(RuoloService ruoloService) {
        this.ruoloService = ruoloService;
    }

    // POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ruolo createRuolo(@RequestBody Ruolo body) {
        return this.ruoloService.save(body);
    }
}
