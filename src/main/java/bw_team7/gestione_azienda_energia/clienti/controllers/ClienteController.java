package bw_team7.gestione_azienda_energia.clienti.controllers;

import bw_team7.gestione_azienda_energia.clienti.entities.Cliente;
import bw_team7.gestione_azienda_energia.clienti.payloads.ClienteDTO;
import bw_team7.gestione_azienda_energia.clienti.services.ClienteService;
import bw_team7.gestione_azienda_energia.email.payloads.EmailPayloadDTO;
import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clienti")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // POST - Solo ADMIN
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Cliente saveCliente(@RequestBody @Validated ClienteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        return this.clienteService.save(body);
    }

    // GET LIST - ADMIN e USER
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public Page<Cliente> getClienti(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ragioneSociale") String sortBy,
            @RequestParam(required = false) BigDecimal minFatturato,
            @RequestParam(required = false) BigDecimal maxFatturato,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInserimento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataUltimoContatto,
            @RequestParam(required = false) String nome
    ) {
        return this.clienteService.getFilteredAndSorted(
                page, size, sortBy, minFatturato, maxFatturato, dataInserimento, dataUltimoContatto, nome
        );
    }

    // GET BY ID - ADMIN e USER
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public Cliente getById(@PathVariable UUID id) {
        return this.clienteService.findById(id);
    }

    // PUT - Solo ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Cliente getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated ClienteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.clienteService.findByIdAndUpdate(id, body);
    }

    // INVIO EMAIL - ADMIN e USER
    @PostMapping("/{id}/send-email")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public void sendEmailToCliente(
            @PathVariable UUID id,
            @RequestBody @Validated EmailPayloadDTO payload,
            BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new BadRequest("Errori nella composizione dell'email: " + errorsList);
        }

        this.clienteService.sendEmailToContatto(id, payload.subject(), payload.body());
    }

    // DELETE - Solo ADMIN
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.clienteService.findByIdAndDelete(id);
    }
}