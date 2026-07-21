package bw_team7.gestione_azienda_energia.clienti.services;

import bw_team7.gestione_azienda_energia.clienti.entities.Cliente;
import bw_team7.gestione_azienda_energia.clienti.enums.TipoCliente;
import bw_team7.gestione_azienda_energia.clienti.payloads.ClienteDTO;
import bw_team7.gestione_azienda_energia.clienti.repositories.ClienteRepository;
import bw_team7.gestione_azienda_energia.email.MailgunSender;
import bw_team7.gestione_azienda_energia.exceptions.custom.BadRequest;
import bw_team7.gestione_azienda_energia.exceptions.custom.NotFound;
import bw_team7.gestione_azienda_energia.indirizzi.entities.Indirizzo;
import bw_team7.gestione_azienda_energia.indirizzi.services.IndirizzoService;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final IndirizzoService indirizzoService;

    private final MailgunSender mailgunSender;

    public ClienteService(ClienteRepository clienteRepository, IndirizzoService indirizzoService, MailgunSender mailgunSender) {
        this.clienteRepository = clienteRepository;
        this.indirizzoService = indirizzoService;
        this.mailgunSender = mailgunSender;
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

    //GET filtri e ordinamento
    public Page<Cliente> getFilteredAndSorted(
            int page,
            int size,
            String sortBy,
            BigDecimal minFatturato,
            BigDecimal maxFatturato,
            LocalDate dataInserimento,
            LocalDate dataUltimoContatto,
            String nome
    ) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;

        //Ordinamento
        String sortField = switch (sortBy.toLowerCase()) {
            case "fatturato", "fatturatoannuale" -> "fatturatoAnnuale";
            case "datainserimento" -> "dataInserimento";
            case "dataultimocontatto" -> "dataUltimoContatto";
            case "provincia" -> "sedeLegale.comune.provincia.nome"; // Ordina per provincia della sede legale
            default -> "ragioneSociale"; // Default: Nome ragione sociale
        };

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField));

        //Filtri
        Specification<Cliente> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Gestione range fatturato
            if (minFatturato != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("fatturatoAnnuale"), minFatturato));
            }
            if (maxFatturato != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("fatturatoAnnuale"), maxFatturato));
            }

            // Filtro per data inserimento
            if (dataInserimento != null) {
                predicates.add(criteriaBuilder.equal(root.get("dataInserimento"), dataInserimento));
            }

            // Filtro per data ultimo contatto
            if (dataUltimoContatto != null) {
                predicates.add(criteriaBuilder.equal(root.get("dataUltimoContatto"), dataUltimoContatto));
            }

            // Filtro per parte del nome (ragione sociale)
            if (nome != null && !nome.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("ragioneSociale")), "%" + nome.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return this.clienteRepository.findAll(spec, pageable);
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
        if (payload.dataInserimento() != found.getDataInserimento())
            found.setDataInserimento(payload.dataInserimento());
        if (payload.dataUltimoContatto() != found.getDataUltimoContatto())
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

    public void sendEmailToContatto(UUID clienteId, String subject, String body) {
        Cliente cliente = this.findById(clienteId);
        String emailDestinatario = cliente.getEmailContatto();

        //Da eliminare
        emailDestinatario = "sciucco.niccolo@gmail.com";

        if (emailDestinatario == null || emailDestinatario.isBlank()) {
            throw new BadRequest("Il cliente selezionato non ha un'email di contatto valida!");
        }

        this.mailgunSender.sendEmail(emailDestinatario, subject, body);

        cliente.setDataUltimoContatto(LocalDate.now());
        this.clienteRepository.save(cliente);

        log.info("Email inviata con successo e data ultimo contatto aggiornata per il cliente: " + clienteId);
    }

    // DELETE
    public void findByIdAndDelete(UUID id) {
        Cliente found = this.findById(id);
        this.clienteRepository.delete(found);
        log.info("Cliente " + id + " eliminato con successo");
    }
}
