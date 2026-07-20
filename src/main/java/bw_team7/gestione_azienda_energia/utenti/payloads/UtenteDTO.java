package bw_team7.gestione_azienda_energia.utenti.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UtenteDTO(
        @NotBlank(message = "Lo username è obbligatorio!")
        @Size(min = 3, max = 20, message = "Lo username deve essere compreso tra 3 e 20 caratteri!")
        String username,

        @NotBlank(message = "L'email è obbligatoria!")
        @Email(message = "L'indirizzo email inserito non è valido!")
        String email,

        @NotBlank(message = "La password è obbligatoria!")
        @Size(min = 6, message = "La password deve contenere almeno 6 caratteri!")
        String password,

        @NotBlank(message = "Il nome è obbligatorio!")
        @Size(min = 2, max = 30, message = "Il nome deve essere compreso tra 2 e 30 caratteri!")
        String nome,

        @NotBlank(message = "Il cognome è obbligatorio!")
        @Size(min = 2, max = 30, message = "Il cognome deve essere compreso tra 2 e 30 caratteri!")
        String cognome,

        String avatar
) {
}