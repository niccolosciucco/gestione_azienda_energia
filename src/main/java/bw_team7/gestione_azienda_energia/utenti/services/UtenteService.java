package bw_team7.gestione_azienda_energia.utenti.services;

import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.exceptions.custom.NotFound;
import bw_team7.gestione_azienda_energia.ruolo.entities.Ruolo;
import bw_team7.gestione_azienda_energia.ruolo.services.RuoloService;
import bw_team7.gestione_azienda_energia.utenti.entities.Utente;
import bw_team7.gestione_azienda_energia.utenti.payloads.UtenteDTO;
import bw_team7.gestione_azienda_energia.utenti.repositories.UtenteRepository;
import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final RuoloService ruoloService;
    private final Cloudinary cloudinary;

    public UtenteService(UtenteRepository utenteRepository, RuoloService ruoloService, Cloudinary cloudinary) {
        this.utenteRepository = utenteRepository;
        this.ruoloService = ruoloService;
        this.cloudinary = cloudinary;
    }

    // SAVE
    public Utente save(UtenteDTO payload) {
        if (this.utenteRepository.existsByEmail(payload.email())) {
            throw new BadRequest("L'email " + payload.email() + " è già in uso!");
        }

        if (this.utenteRepository.existsByUsername(payload.username())) {
            throw new BadRequest("Lo username " + payload.username() + " è già in uso!");
        }

        Ruolo ruoloDefault = this.ruoloService.getRuoloUser();
        Set<Ruolo> ruoli = Set.of(ruoloDefault);

        Utente newUtente = new Utente(
                payload.username(),
                payload.email(),
                payload.password(),
                payload.nome(),
                payload.cognome(),
                ruoli
        );

        Utente saved = this.utenteRepository.save(newUtente);
        log.info("Utente " + saved.getId() + " salvato con successo");
        return saved;
    }

    // GET ALL
    public Page<Utente> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.utenteRepository.findAll(pageable);
    }

    // FIND BY ID
    public Utente findById(UUID id) {
        return this.utenteRepository.findById(id)
                .orElseThrow(() -> new NotFound("L'utente con ID " + id + " non è stato trovato!"));
    }

    // UPDATE
    public Utente findByIdAndUpdate(UUID id, UtenteDTO payload) {
        Utente found = this.findById(id);

        if (!found.getEmail().equals(payload.email())) {
            if (this.utenteRepository.existsByEmail(payload.email())) {
                throw new BadRequest("L'email " + payload.email() + " è già registrata per un altro utente!");
            }
        }

        if (!found.getUsername().equals(payload.username())) {
            if (this.utenteRepository.existsByUsername(payload.username())) {
                throw new BadRequest("Lo username " + payload.username() + " è già registrato per un altro utente!");
            }
        }

        found.setUsername(payload.username());
        found.setEmail(payload.email());
        found.setPassword(payload.password());
        found.setNome(payload.nome());
        found.setCognome(payload.cognome());

        if (!found.getNome().equals(payload.nome()) || !found.getCognome().equals(payload.cognome()))
            found.setAvatar("https://ui-avatars.com/api/?name=" + payload.nome() + "+" + payload.cognome());

        Utente updated = this.utenteRepository.save(found);
        log.info("Utente " + updated.getId() + " aggiornato con successo");
        return updated;
    }

    //    PATCH AVATAR
    public Utente updateAvatar(UUID id, MultipartFile file) {
        Utente found = this.utenteRepository.findById(id)
                .orElseThrow(() -> new NotFound("Impossibile aggiornare l'avatar. Utente con id " + id + " non trovato"));

        try {
            String url = (String) this.cloudinary.uploader().upload(file.getBytes(), com.cloudinary.utils.ObjectUtils.emptyMap()).get("secure_url");

            found.setAvatar(url);
            return this.utenteRepository.save(found);

        } catch (java.io.IOException e) {
            throw new RuntimeException("Errore durante il caricamento del file su Cloudinary", e);
        }
    }

    // DELETE
    public void findByIdAndDelete(UUID id) {
        Utente found = this.findById(id);
        this.utenteRepository.delete(found);
        log.info("Utente " + id + " eliminato con successo");
    }

    //Cambia ruolo utente
    public Utente updateRuoloUtente(UUID id, String nuovoNomeRuolo) {
        Utente found = this.findById(id);

        // Trova il nuovo ruolo tramite il RuoloService
        Ruolo ruoloDaAggiungere = this.ruoloService.findByNome(nuovoNomeRuolo);

        // Assegna il nuovo ruolo
        found.getRuoli().add(ruoloDaAggiungere);

        Utente updated = this.utenteRepository.save(found);
        log.info("Ruolo dell'utente " + updated.getId() + " aggiornato a: " + nuovoNomeRuolo);
        return updated;
    }

    //Rimuovere ruolo
    public Utente removeRuoloUtente(UUID id, String nomeRuoloDaRimuovere) {
        Utente found = this.findById(id);

        // Rimuove il ruolo dalla lista basandosi sul nome
        found.getRuoli().removeIf(ruolo -> ruolo.getNome().equalsIgnoreCase(nomeRuoloDaRimuovere));

        return this.utenteRepository.save(found);
    }

}