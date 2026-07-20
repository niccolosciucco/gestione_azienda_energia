package bw_team7.gestione_azienda_energia.clienti.services;

import bw_team7.gestione_azienda_energia.clienti.entities.Cliente;
import bw_team7.gestione_azienda_energia.clienti.enums.TipoCliente;
import bw_team7.gestione_azienda_energia.clienti.payloads.ClienteDTO;
import bw_team7.gestione_azienda_energia.clienti.repositories.ClienteRepository;
import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.exceptions.custom.NotFound;
import bw_team7.gestione_azienda_energia.indirizzi.entities.Indirizzo;
import bw_team7.gestione_azienda_energia.indirizzi.services.IndirizzoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final IndirizzoService indirizzoService;

    public ClienteService(ClienteRepository clienteRepository, IndirizzoService indirizzoService) {
        this.clienteRepository = clienteRepository;
        this.indirizzoService = indirizzoService;
    }

    // SAVE
    public Cliente save(ClienteDTO payload) {
        if (this.clienteRepository.existsByPartitaIva(payload.partitaIva())) {
            throw new BadRequest("Esiste già un cliente con questa Partita IVA: " + payload.partitaIva());
        }

        TipoCliente tipoScelto;
        try {
            tipoScelto = TipoCliente.valueOf(payload.tipoCliente().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequest("Tipo cliente non valido!");
        }

        Indirizzo sedeLegale = this.indirizzoService.findById(payload.sedeLegaleId());
        Indirizzo sedeOperativa = this.indirizzoService.findById(payload.sedeOperativaId());

        Cliente newCliente = new Cliente(
                payload.ragioneSociale(),
                payload.partitaIva(),
                tipoScelto,
                payload.email(),
                payload.pec(),
                payload.telefono(),
                payload.dataInserimento(),
                payload.dataUltimoContatto(),
                payload.fatturatoAnnuale(),
                payload.nomeContatto(),
                payload.cognomeContatto(),
                payload.emailContatto(),
                payload.telefonoContatto(),
                sedeLegale,
                sedeOperativa
        );

        Cliente saved = this.clienteRepository.save(newCliente);
        log.info("Cliente " + saved.getId() + " salvato con successo");
        return saved;
    }

    // GET ALL
    public Page<Cliente> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.clienteRepository.findAll(pageable);
    }

    // FIND BY ID
    public Cliente findById(UUID clienteId) {
        return this.clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFound("Il cliente con ID " + clienteId + " non è stato trovato!"));
    }

    // UPDATE
    public Cliente findByIdAndUpdate(UUID id, ClienteDTO payload) {
        Cliente found = this.findById(id);

        if (!found.getPartitaIva().equals(payload.partitaIva())) {
            if (this.clienteRepository.existsByPartitaIva(payload.partitaIva())) {
                throw new BadRequest("La Partita IVA " + payload.partitaIva() + " è già registrata per un altro cliente!");
            }
        }

        TipoCliente tipoScelto;
        try {
            tipoScelto = TipoCliente.valueOf(payload.tipoCliente().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequest("Tipo cliente non valido!");
        }

        Indirizzo sedeLegale = this.indirizzoService.findById(payload.sedeLegaleId());
        Indirizzo sedeOperativa = this.indirizzoService.findById(payload.sedeOperativaId());

        found.setRagioneSociale(payload.ragioneSociale());
        found.setPartitaIva(payload.partitaIva());
        found.setTipoCliente(tipoScelto);
        found.setEmail(payload.email());
        found.setPec(payload.pec());
        found.setTelefono(payload.telefono());
        found.setDataInserimento(payload.dataInserimento());
        found.setDataUltimoContatto(payload.dataUltimoContatto());
        found.setFatturatoAnnuale(payload.fatturatoAnnuale());
        found.setNomeContatto(payload.nomeContatto());
        found.setCognomeContatto(payload.cognomeContatto());
        found.setEmailContatto(payload.emailContatto());
        found.setTelefonoContatto(payload.telefonoContatto());
        found.setSedeLegale(sedeLegale);
        found.setSedeOperativa(sedeOperativa);

        Cliente updated = this.clienteRepository.save(found);
        log.info("Cliente " + updated.getId() + " aggiornato con successo");
        return updated;
    }

    // DELETE
    public void findByIdAndDelete(UUID id) {
        Cliente found = this.findById(id);
        this.clienteRepository.delete(found);
        log.info("Cliente " + id + " eliminato con successo");
    }
}
