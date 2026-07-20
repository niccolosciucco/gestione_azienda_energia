package bw_team7.gestione_azienda_energia.province.services;

import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.exceptions.custom.NotFound;
import bw_team7.gestione_azienda_energia.province.entities.Provincia;
import bw_team7.gestione_azienda_energia.province.payloads.ProvinciaDTO;
import bw_team7.gestione_azienda_energia.province.repositories.ProvinciaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ProvinciaService {
    private final ProvinciaRepository provinciaRepository;

    public ProvinciaService(ProvinciaRepository provinciaRepository) {
        this.provinciaRepository = provinciaRepository;
    }

    //SAVE
    public Provincia save(ProvinciaDTO payload) {
        if (this.provinciaRepository.existsByNomeOrSigla(payload.nome(), payload.sigla())) {
            throw new BadRequest("Esiste già una provincia con questo nome");
        }

        Provincia newProvincia = new Provincia(
                payload.nome(),
                payload.sigla()
        );

        Provincia saved = this.provinciaRepository.save(newProvincia);

        log.info("Provincia " + saved.getId() + " salvata");

        return saved;
    }

    //GET ALL
    public Page<Provincia> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.provinciaRepository.findAll(pageable);
    }

    // FIND BY ID
    public Provincia findById(UUID provinciaId) {
        return this.provinciaRepository.findById(provinciaId)
                .orElseThrow(() -> new NotFound("La provincia con ID " + provinciaId + " non è stata trovata!"));
    }

    //UPDATE
    public Provincia findByIdAndUpdate(UUID id, ProvinciaDTO payload) {
        Provincia found = this.findById(id);

        if (!found.getNome().equals(payload.nome()) || !found.getSigla().equals(payload.sigla())) {
            if (this.provinciaRepository.existsByNomeOrSigla(payload.nome(), payload.sigla())) {
                throw new BadRequest("Esiste già una provincia con questo nome o questa sigla");
            }
        }

        found.setNome(payload.nome());
        found.setSigla(payload.sigla());

        Provincia updated = this.provinciaRepository.save(found);
        log.info("Provincia " + updated.getId() + " aggiornata");

        return updated;
    }

    //DELETE
    public void findByIdAndDelete(UUID id) {
        Provincia found = this.findById(id);
        this.provinciaRepository.delete(found);
        log.info("Provincia " + id + " eliminata");
    }

    public Provincia findByNome(String nome) {
        return this.provinciaRepository.findByNome(nome)
                .orElseThrow(() -> new NotFound("Provincia non trovata: " + nome));
    }

    public long count() {
        return this.provinciaRepository.count();
    }
}
