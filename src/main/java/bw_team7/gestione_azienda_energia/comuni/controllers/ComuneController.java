package bw_team7.gestione_azienda_energia.comuni.controllers;

import bw_team7.gestione_azienda_energia.comuni.entities.Comune;
import bw_team7.gestione_azienda_energia.comuni.payloads.ComuneDTO;
import bw_team7.gestione_azienda_energia.comuni.services.ComuneService;
import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    // POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comune saveComune(@RequestBody @Validated ComuneDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        return this.comuneService.save(body);
    }

    //GET
    @GetMapping
    public Page<Comune> getComuni(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy
    ) {
        return this.comuneService.getAll(page, size, orderBy);
    }

    // GET
    @GetMapping("/{id}")
    public Comune getById(@PathVariable UUID id) {
        return this.comuneService.findById(id);
    }

    // PUT
    @PutMapping("/{id}")
    public Comune getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated ComuneDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.comuneService.findByIdAndUpdate(id, body);
    }

    //DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.comuneService.findByIdAndDelete(id);
    }

}
