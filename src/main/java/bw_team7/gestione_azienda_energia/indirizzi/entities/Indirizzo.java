package bw_team7.gestione_azienda_energia.indirizzi.entities;

import bw_team7.gestione_azienda_energia.comuni.entities.Comune;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "indirizzi")
@NoArgsConstructor
@Getter
public class Indirizzo {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String via;
    @Column(nullable = false)
    private String civico;
    @Column(nullable = false)
    private String localita;
    @Column(nullable = false)
    private String cap;
    @ManyToOne(optional = false)
    @JoinColumn(name = "comune_id", nullable = false)
    private Comune comune;

    public Indirizzo(String via, String civico, String localita, String cap, Comune comune) {
        this.via = via;
        this.civico = civico;
        this.localita = localita;
        this.cap = cap;
        this.comune = comune;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public void setComune(Comune comune) {
        this.comune = comune;
    }
}
