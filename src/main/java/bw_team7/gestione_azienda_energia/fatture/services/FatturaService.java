package bw_team7.gestione_azienda_energia.fatture.services;

import bw_team7.gestione_azienda_energia.clienti.entities.Cliente;
import bw_team7.gestione_azienda_energia.clienti.services.ClienteService;
import bw_team7.gestione_azienda_energia.exceptions.custom.NotFound;
import bw_team7.gestione_azienda_energia.fatture.entities.Fattura;
import bw_team7.gestione_azienda_energia.fatture.payloads.FatturaDTO;
import bw_team7.gestione_azienda_energia.fatture.repositories.FatturaRepository;
import bw_team7.gestione_azienda_energia.stato_fattura.entities.StatoFattura;
import bw_team7.gestione_azienda_energia.stato_fattura.services.StatoFatturaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FatturaService {

    private final FatturaRepository fatturaRepository;
    private final ClienteService clienteService;
    private final StatoFatturaService statoFatturaService;

    public FatturaService(FatturaRepository fatturaRepository, ClienteService clienteService, StatoFatturaService statoFatturaService) {
        this.fatturaRepository = fatturaRepository;
        this.clienteService = clienteService;
        this.statoFatturaService = statoFatturaService;
    }

    // SAVE
    public Fattura save(FatturaDTO payload) {
        Cliente cliente = this.clienteService.findById(payload.clienteId());
        StatoFattura statoFattura = this.statoFatturaService.findById(payload.statoFatturaId());

        Fattura newFattura = new Fattura(
                payload.data(),
                payload.numero(),
                payload.importo(),
                cliente,
                statoFattura
        );

        Fattura saved = this.fatturaRepository.save(newFattura);
        log.info("Fattura " + saved.getId() + " salvata con successo");
        return saved;
    }

    // GET ALL
    public Page<Fattura> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.fatturaRepository.findAll(pageable);
    }

    // FIND BY ID
    public Fattura findById(UUID fatturaId) {
        return this.fatturaRepository.findById(fatturaId)
                .orElseThrow(() -> new NotFound("La fattura con ID " + fatturaId + " non è stata trovata!"));
    }

    // UPDATE
    public Fattura findByIdAndUpdate(UUID id, FatturaDTO payload) {
        Fattura found = this.findById(id);

        Cliente cliente = this.clienteService.findById(payload.clienteId());
        StatoFattura statoFattura = this.statoFatturaService.findById(payload.statoFatturaId());

        found.setData(payload.data());
        found.setNumero(payload.numero());
        found.setImporto(payload.importo());
        found.setCliente(cliente);
        found.setStatoFattura(statoFattura);

        Fattura updated = this.fatturaRepository.save(found);
        log.info("Fattura " + updated.getId() + " aggiornata con successo");
        return updated;
    }

    // DELETE
    public void findByIdAndDelete(UUID id) {
        Fattura found = this.findById(id);
        this.fatturaRepository.delete(found);
        log.info("Fattura " + id + " eliminata con successo");
    }

}
