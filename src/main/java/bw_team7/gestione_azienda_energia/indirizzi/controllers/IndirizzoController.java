package bw_team7.gestione_azienda_energia.indirizzi.controllers;

import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.indirizzi.entities.Indirizzo;
import bw_team7.gestione_azienda_energia.indirizzi.payloads.IndirizzoDTO;
import bw_team7.gestione_azienda_energia.indirizzi.services.IndirizzoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    //POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Indirizzo saveIndirizzo(@RequestBody @Validated IndirizzoDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        return this.indirizzoService.save(body);
    }

    // GET
    @GetMapping
    public Page<Indirizzo> getIndirizzi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy
    ) {
        return this.indirizzoService.getAll(page, size, orderBy);
    }

    //GET
    @GetMapping("/{id}")
    public Indirizzo getById(@PathVariable UUID id) {
        return this.indirizzoService.findById(id);
    }

    // PUT
    @PutMapping("/{id}")
    public Indirizzo getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated IndirizzoDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.indirizzoService.findByIdAndUpdate(id, body);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.indirizzoService.findByIdAndDelete(id);
    }

}
