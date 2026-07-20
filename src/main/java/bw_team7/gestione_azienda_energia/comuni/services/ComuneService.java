package bw_team7.gestione_azienda_energia.comuni.services;

import bw_team7.gestione_azienda_energia.comuni.entities.Comune;
import bw_team7.gestione_azienda_energia.comuni.payloads.ComuneDTO;
import bw_team7.gestione_azienda_energia.comuni.repositories.ComuneRepository;
import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.exceptions.custom.NotFound;
import bw_team7.gestione_azienda_energia.province.entities.Provincia;
import bw_team7.gestione_azienda_energia.province.services.ProvinciaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ComuneService {
    private final ComuneRepository comuneRepository;
    private final ProvinciaService provinciaService;

    public ComuneService(ComuneRepository comuneRepository, ProvinciaService provinciaService) {
        this.comuneRepository = comuneRepository;
        this.provinciaService = provinciaService;
    }

    //SAVE
    public Comune save(ComuneDTO payload) {
        Provincia provincia = this.provinciaService.findById(payload.provinciaId());

        if (this.comuneRepository.existsByNomeAndProvinciaId(payload.nome(), payload.provinciaId())) {
            throw new BadRequest("Esiste già un comune con questo nome in questa provincia!");
        }

        Comune newComune = new Comune(payload.nome(), provincia);
        Comune saved = this.comuneRepository.save(newComune);

        log.info("Comune " + saved.getNome() + " salvato");
        return saved;
    }

    //GET ALL
    public Page<Comune> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.comuneRepository.findAll(pageable);
    }

    // FIND BY ID
    public Comune findById(UUID comuneId) {
        return this.comuneRepository.findById(comuneId)
                .orElseThrow(() -> new NotFound("Il comune con ID " + comuneId + " non è stato trovato!"));
    }

    //Update
    public Comune findByIdAndUpdate(UUID id, ComuneDTO payload) {
        Comune found = this.findById(id);
        Provincia provincia = this.provinciaService.findById(payload.provinciaId());

        if (!found.getNome().equals(payload.nome()) || !found.getProvincia().getId().equals(payload.provinciaId())) {
            if (this.comuneRepository.existsByNomeAndProvinciaId(payload.nome(), payload.provinciaId())) {
                throw new BadRequest("Esiste già un comune con questo nome in questa provincia!");
            }
        }

        found.setNome(payload.nome());
        found.setProvincia(provincia);

        Comune updated = this.comuneRepository.save(found);
        log.info("Comune " + updated.getId() + " aggiornato");
        return updated;
    }

    // DELETE
    public void findByIdAndDelete(UUID id) {
        Comune found = this.findById(id);
        this.comuneRepository.delete(found);
        log.info("Comune " + id + " eliminato");
    }

    public long count() {
        return this.comuneRepository.count();
    }
}
