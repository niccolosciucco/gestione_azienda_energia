package bw_team7.gestione_azienda_energia.security;

import bw_team7.gestione_azienda_energia.security.payloads.LoginDTO;
import bw_team7.gestione_azienda_energia.security.payloads.LoginResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO loginDTO) {
        return new LoginResponseDTO(this.authService.check(loginDTO));
    }
}
