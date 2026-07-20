package bw_team7.gestione_azienda_energia.comuni.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ComuneDTO(
        @NotBlank(message = "Il nome del comune è obbligatorio")
        @Size(min = 2, max = 15, message = "Il nome del comune deve essere compreso tra 2 e 15 caratteri")
        String nome,

        @NotNull(message = "L'ID della provincia è obbligatorio")
        UUID provinciaId
) {
}
