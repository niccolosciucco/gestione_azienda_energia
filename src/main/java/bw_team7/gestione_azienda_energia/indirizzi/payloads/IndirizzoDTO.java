package bw_team7.gestione_azienda_energia.indirizzi.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record IndirizzoDTO(
        @NotBlank(message = "La via è obbligatoria")
        @Size(min = 2, max = 30, message = "La via deve essere compresa tra 2 e 30 caratteri")
        String via,

        @NotBlank(message = "Il civico è obbligatorio")
        String civico,

        @NotBlank(message = "La località è obbligatoria")
        String località,

        @NotNull(message = "Il CAP è obbligatorio")
        @Positive(message = "Il CAP deve essere un valore positivo")
        String cap,

        @NotNull(message = "L'ID del comune è obbligatorio")
        UUID comuneId
) {
}
