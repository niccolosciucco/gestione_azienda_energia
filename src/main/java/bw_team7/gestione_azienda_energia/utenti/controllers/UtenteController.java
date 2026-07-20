package bw_team7.gestione_azienda_energia.utenti.controllers;

import bw_team7.gestione_azienda_energia.utenti.entities.Utente;
import bw_team7.gestione_azienda_energia.utenti.payloads.UtenteDTO;
import bw_team7.gestione_azienda_energia.utenti.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    // 1) POST: Creazione di un nuovo utente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente saveUtente(@RequestBody UtenteDTO body) {
        return utenteService.save(body);
    }

    // 2) GET: Restituisce un singolo utente tramite ID

    @GetMapping("/{id}")
    public Utente getUtenteById(@PathVariable UUID id) {
        return utenteService.findById(id);
    }

    // 3) PUT: Aggiorna un utente esistente tramite ID

    @PutMapping("/{id}")
    public Utente updateUtente(@PathVariable UUID id, @RequestBody UtenteDTO body) {
        return utenteService.findByIdAndUpdate(id, body);
    }

    // 4) DELETE: Elimina un utente tramite ID

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUtente(@PathVariable UUID id) {
        utenteService.findByIdAndDelete(id);
    }
}


