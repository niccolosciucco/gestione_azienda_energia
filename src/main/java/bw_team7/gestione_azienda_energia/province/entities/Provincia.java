package bw_team7.gestione_azienda_energia.province.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "province")
@NoArgsConstructor
@Getter
public class Provincia {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 2)
    private String sigla;

    public Provincia(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}