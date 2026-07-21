package bw_team7.gestione_azienda_energia.email.payloads;

import jakarta.validation.constraints.NotBlank;

public record EmailPayloadDTO(
        @NotBlank(message = "L'oggetto dell'email è obbligatorio")
        String subject,

        @NotBlank(message = "Il testo dell'email è obbligatorio")
        String body
) {
}
