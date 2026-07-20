package bw_team7.gestione_azienda_energia.fatture.entities;

import bw_team7.gestione_azienda_energia.clienti.entities.Cliente;
import bw_team7.gestione_azienda_energia.stato_fattura.entities.StatoFattura;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "fatture")
@NoArgsConstructor
@Getter
public class Fattura {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    private Integer numero;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal importo;
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @ManyToOne(optional = false)
    @JoinColumn(name = "stato_fattura_id", nullable = false)
    private StatoFattura statoFattura;

    public Fattura(LocalDate data, Integer numero, BigDecimal importo, Cliente cliente, StatoFattura statoFattura) {
        this.data = data;
        this.numero = numero;
        this.importo = importo;
        this.cliente = cliente;
        this.statoFattura = statoFattura;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setStatoFattura(StatoFattura statoFattura) {
        this.statoFattura = statoFattura;
    }
}
