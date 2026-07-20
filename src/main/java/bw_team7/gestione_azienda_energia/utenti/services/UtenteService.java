package bw_team7.gestione_azienda_energia.utenti.services;

import bw_team7.gestione_azienda_energia.utenti.entities.Utente;
import bw_team7.gestione_azienda_energia.utenti.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    public Utente save(Utente payload) {
        return utenteRepository.save(payload);
    }

    public Utente findById(UUID id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente con ID " + id + " non trovato!"));
    }

    public Utente findByIdAndUpdate(UUID id, Utente body) {
        Utente found = this.findById(id);

        found.setUsername(body.getUsername());
        found.setEmail(body.getEmail());
        found.setPassword(body.getPassword());
        found.setNome(body.getNome());
        found.setCognome(body.getCognome());
        found.setAvatar(body.getAvatar());
        found.setRuoli(body.getRuoli());

        return utenteRepository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        Utente found = this.findById(id);
        utenteRepository.delete(found);
    }
}
