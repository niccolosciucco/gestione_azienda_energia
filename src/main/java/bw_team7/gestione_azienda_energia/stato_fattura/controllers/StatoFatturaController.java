package bw_team7.gestione_azienda_energia.stato_fattura.controllers;

import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.stato_fattura.entities.StatoFattura;
import bw_team7.gestione_azienda_energia.stato_fattura.payloads.StatoFatturaDTO;
import bw_team7.gestione_azienda_energia.stato_fattura.services.StatoFatturaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    // 1. POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StatoFattura saveStatoFattura(@RequestBody @Validated StatoFatturaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        return this.statoFatturaService.save(body);
    }

    // GET
    @GetMapping
    public Page<StatoFattura> getStatiFattura(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy
    ) {
        return this.statoFatturaService.getAll(page, size, orderBy);
    }

    // GET
    @GetMapping("/{id}")
    public StatoFattura getById(@PathVariable UUID id) {
        return this.statoFatturaService.findById(id);
    }

    // PUT
    @PutMapping("/{id}")
    public StatoFattura getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated StatoFatturaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.statoFatturaService.findByIdAndUpdate(id, body);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.statoFatturaService.findByIdAndDelete(id);
    }

}
