package bw_team7.gestione_azienda_energia.comuni.entities;

import bw_team7.gestione_azienda_energia.province.entities.Provincia;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "comuni")
@NoArgsConstructor
@Getter
public class Comune {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne(optional = false)
    @JoinColumn(name = "provincia_id", nullable = false)
    private Provincia provincia;

    public Comune(String nome, Provincia provincia) {
        this.nome = nome;
        this.provincia = provincia;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
}
