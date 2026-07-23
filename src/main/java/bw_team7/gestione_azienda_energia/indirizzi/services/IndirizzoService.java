package bw_team7.gestione_azienda_energia.indirizzi.services;

import bw_team7.gestione_azienda_energia.comuni.entities.Comune;
import bw_team7.gestione_azienda_energia.comuni.services.ComuneService;
import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.exceptions.custom.NotFound;
import bw_team7.gestione_azienda_energia.indirizzi.entities.Indirizzo;
import bw_team7.gestione_azienda_energia.indirizzi.payloads.IndirizzoDTO;
import bw_team7.gestione_azienda_energia.indirizzi.repository.IndirizzoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class IndirizzoService {

    private final IndirizzoRepository indirizzoRepository;
    private final ComuneService comuneService;

    public IndirizzoService(IndirizzoRepository indirizzoRepository, ComuneService comuneService) {
        this.indirizzoRepository = indirizzoRepository;
        this.comuneService = comuneService;
    }

    // SAVE
    public Indirizzo save(IndirizzoDTO payload) {
        Comune comune = this.comuneService.findById(payload.comuneId());

        boolean esiste = indirizzoRepository.existsByViaAndCivicoAndCapAndComuneId(
                payload.via(),
                payload.civico(),
                payload.cap(),
                payload.comuneId()
        );

        if (esiste) {
            throw new BadRequest("Questo indirizzo esiste già nel sistema!");
        }

        Indirizzo newIndirizzo = new Indirizzo(
                payload.via(),
                payload.civico(),
                payload.localita(),
                payload.cap(),
                comune
        );

        Indirizzo saved = this.indirizzoRepository.save(newIndirizzo);
        log.info("Indirizzo " + saved.getId() + " salvato");
        return saved;
    }

    // GET ALL
    public Page<Indirizzo> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.indirizzoRepository.findAll(pageable);
    }

    // FIND BY ID
    public Indirizzo findById(UUID indirizzoId) {
        return this.indirizzoRepository.findById(indirizzoId)
                .orElseThrow(() -> new NotFound("L'indirizzo con ID " + indirizzoId + " non è stato trovato!"));
    }


    // UPDATE
    public Indirizzo findByIdAndUpdate(UUID id, IndirizzoDTO payload) {
        Indirizzo found = this.findById(id);
        Comune comune = this.comuneService.findById(payload.comuneId());

        found.setVia(payload.via());
        found.setCivico(payload.civico());
        found.setLocalita(payload.localita());
        found.setCap(payload.cap());
        found.setComune(comune);

        Indirizzo updated = this.indirizzoRepository.save(found);
        log.info("Indirizzo " + updated.getId() + " aggiornato");
        return updated;
    }

    // DELETE
    public void findByIdAndDelete(UUID id) {
        Indirizzo found = this.findById(id);
        this.indirizzoRepository.delete(found);
        log.info("Indirizzo " + id + " eliminato");
    }
}

