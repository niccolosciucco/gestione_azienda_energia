package bw_team7.gestione_azienda_energia.stato_fattura.services;

import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.exceptions.custom.NotFound;
import bw_team7.gestione_azienda_energia.stato_fattura.entities.StatoFattura;
import bw_team7.gestione_azienda_energia.stato_fattura.payloads.StatoFatturaDTO;
import bw_team7.gestione_azienda_energia.stato_fattura.repositories.StatoFatturaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class StatoFatturaService {

    private final StatoFatturaRepository statoFatturaRepository;

    public StatoFatturaService(StatoFatturaRepository statoFatturaRepository) {
        this.statoFatturaRepository = statoFatturaRepository;
    }

    // SAVE
    public StatoFattura save(StatoFatturaDTO payload) {
        if (this.statoFatturaRepository.existsByNome(payload.nome())) {
            throw new BadRequest("Esiste già uno stato fattura con questo nome!");
        }

        StatoFattura newStato = new StatoFattura(payload.nome());
        StatoFattura saved = this.statoFatturaRepository.save(newStato);
        log.info("Stato fattura " + saved.getId() + " salvato con successo");
        return saved;
    }

    // GET ALL
    public Page<StatoFattura> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.statoFatturaRepository.findAll(pageable);
    }

    // FIND BY ID
    public StatoFattura findById(UUID id) {
        return this.statoFatturaRepository.findById(id)
                .orElseThrow(() -> new NotFound("Lo stato fattura con ID " + id + " non è stato trovato!"));
    }

    // UPDATE
    public StatoFattura findByIdAndUpdate(UUID id, StatoFatturaDTO payload) {
        StatoFattura found = this.findById(id);

        if (!found.getNome().equalsIgnoreCase(payload.nome())) {
            if (this.statoFatturaRepository.existsByNome(payload.nome())) {
                throw new BadRequest("Esiste già uno stato fattura con questo nome!");
            }
        }

        found.setNome(payload.nome());
        StatoFattura updated = this.statoFatturaRepository.save(found);
        log.info("Stato fattura " + updated.getId() + " aggiornato con successo");
        return updated;
    }

    // DELETE
    public void findByIdAndDelete(UUID id) {
        StatoFattura found = this.findById(id);
        this.statoFatturaRepository.delete(found);
        log.info("Stato fattura " + id + " eliminato con successo");
    }

}
