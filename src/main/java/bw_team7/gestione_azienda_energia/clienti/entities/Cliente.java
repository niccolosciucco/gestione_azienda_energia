package bw_team7.gestione_azienda_energia.clienti.entities;

import bw_team7.gestione_azienda_energia.clienti.enums.TipoCliente;
import bw_team7.gestione_azienda_energia.indirizzi.entities.Indirizzo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "clienti")
@NoArgsConstructor
@Getter
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "ragione_sociale", nullable = false)
    private String ragioneSociale;
    @Column(name = "partita_iva", nullable = false)
    private String partitaIva;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false)
    private TipoCliente tipoCliente;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String pec;
    @Column(nullable = false)
    private String telefono;
    @Column(name = "logo_aziendale", nullable = false)
    private String logoAziendale;
    @Column(name = "data_inserimento", nullable = false)
    private LocalDate dataInserimento;
    @Column(name = "data_ultimo_contatto")
    private LocalDate dataUltimoContatto;
    @Column(name = "fatturato_annuale", precision = 15, scale = 2)
    private BigDecimal fatturatoAnnuale;
    @Column(name = "nome_contatto", nullable = false)
    private String nomeContatto;
    @Column(name = "cognome_contatto", nullable = false)
    private String cognomeContatto;
    @Column(name = "email_contatto", nullable = false)
    private String emailContatto;
    @Column(name = "telefono_contatto", nullable = false)
    private String telefonoContatto;
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "sede_legale_id", nullable = false)
    private Indirizzo sedeLegale;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sede_operativa_id")
    private Indirizzo sedeOperativa;

    public Cliente(String ragioneSociale, String partitaIva, TipoCliente tipoCliente, String email, String pec, String telefono, LocalDate dataInserimento, LocalDate dataUltimoContatto, BigDecimal fatturatoAnnuale, String nomeContatto, String cognomeContatto, String emailContatto, String telefonoContatto, Indirizzo sedeLegale, Indirizzo sedeOperativa) {
        this.ragioneSociale = ragioneSociale;
        this.partitaIva = partitaIva;
        this.tipoCliente = tipoCliente;
        this.email = email;
        this.pec = pec;
        this.telefono = telefono;
        this.dataInserimento = dataInserimento;
        this.dataUltimoContatto = dataUltimoContatto;
        this.fatturatoAnnuale = fatturatoAnnuale;
        this.nomeContatto = nomeContatto;
        this.cognomeContatto = cognomeContatto;
        this.emailContatto = emailContatto;
        this.telefonoContatto = telefonoContatto;
        this.sedeLegale = sedeLegale;
        this.sedeOperativa = sedeOperativa;
        this.logoAziendale = "https://img.magnific.com/vettori-gratuito/sfera-astratta-colorata-con-logo-a-forma-di-stella_125964-4539.jpg?semt=ais_hybrid&w=740&q=80";
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPec(String pec) {
        this.pec = pec;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setLogoAziendale(String logoAziendale) {
        this.logoAziendale = logoAziendale;
    }

    public void setDataInserimento(LocalDate dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public void setDataUltimoContatto(LocalDate dataUltimoContatto) {
        this.dataUltimoContatto = dataUltimoContatto;
    }

    public void setFatturatoAnnuale(BigDecimal fatturatoAnnuale) {
        this.fatturatoAnnuale = fatturatoAnnuale;
    }

    public void setNomeContatto(String nomeContatto) {
        this.nomeContatto = nomeContatto;
    }

    public void setCognomeContatto(String cognomeContatto) {
        this.cognomeContatto = cognomeContatto;
    }

    public void setEmailContatto(String emailContatto) {
        this.emailContatto = emailContatto;
    }

    public void setTelefonoContatto(String telefonoContatto) {
        this.telefonoContatto = telefonoContatto;
    }

    public void setSedeLegale(Indirizzo sedeLegale) {
        this.sedeLegale = sedeLegale;
    }

    public void setSedeOperativa(Indirizzo sedeOperativa) {
        this.sedeOperativa = sedeOperativa;
    }
}