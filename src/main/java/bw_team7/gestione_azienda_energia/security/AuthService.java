package bw_team7.gestione_azienda_energia.security;

import bw_team7.gestione_azienda_energia.exceptions.custom.Unauthorized;
import bw_team7.gestione_azienda_energia.security.payloads.LoginDTO;
import bw_team7.gestione_azienda_energia.security.tools.JwtTools;
import bw_team7.gestione_azienda_energia.utenti.entities.Utente;
import bw_team7.gestione_azienda_energia.utenti.services.UtenteService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UtenteService utenteService;
    private final JwtTools jwtTools;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UtenteService utenteService, JwtTools jwtTools, PasswordEncoder passwordEncoder) {
        this.utenteService = utenteService;
        this.jwtTools = jwtTools;
        this.passwordEncoder = passwordEncoder;
    }

    public String check(LoginDTO loginDTO) {
        Utente utente = this.utenteService.findByEmail(loginDTO.email());

        System.out.println("HASH VALIDO PER 1234: " + this.passwordEncoder.encode("1234"));

        if (this.passwordEncoder.matches(loginDTO.password(), utente.getPassword())) {
            return this.jwtTools.generateToken(utente);
        } else {
            throw new Unauthorized("Credenziali non valide.");
        }
    }
}
