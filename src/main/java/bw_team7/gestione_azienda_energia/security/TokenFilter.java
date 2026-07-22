package bw_team7.gestione_azienda_energia.security;

import bw_team7.gestione_azienda_energia.exceptions.custom.Unauthorized;
import bw_team7.gestione_azienda_energia.security.tools.JwtTools;
import bw_team7.gestione_azienda_energia.utenti.entities.Utente;
import bw_team7.gestione_azienda_energia.utenti.services.UtenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final JwtTools jwtTools;
    private final UtenteService utenteService;

    public TokenFilter(JwtTools jwtTools, @Lazy UtenteService utenteService) {
        this.jwtTools = jwtTools;
        this.utenteService = utenteService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer "))
            throw new Unauthorized("Impossibile completare l'operazione: inserire il token nella richiesta");
        String token = header.replace("Bearer ", "");
        this.jwtTools.verify(token);
        UUID utenteId = this.jwtTools.idFromToken(token);
        Utente utente = this.utenteService.findById(utenteId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(utente, null, utente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().startsWith("/auth/");
    }
}