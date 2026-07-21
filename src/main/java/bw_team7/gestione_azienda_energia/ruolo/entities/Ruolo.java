package bw_team7.gestione_azienda_energia.ruolo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "ruoli")
@NoArgsConstructor
@Getter
public class Ruolo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    public Ruolo(String nome) {
        this.nome = nome;
    }
}
