package bw_team7.gestione_azienda_energia.utenti.entities;

import bw_team7.gestione_azienda_energia.utenti.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "utenti")
@NoArgsConstructor
@Getter
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cognome;
    private String avatar;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "utenti_ruoli",
            joinColumns = @JoinColumn(name = "utente_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "ruolo")
    private Set<RoleType> ruoli = new HashSet<>();

    public Utente(String username, String email, String password, String nome, String cognome, Set<RoleType> ruoli) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.avatar = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
        this.ruoli = ruoli;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setRuoli(Set<RoleType> ruoli) {
        this.ruoli = ruoli;
    }
}
