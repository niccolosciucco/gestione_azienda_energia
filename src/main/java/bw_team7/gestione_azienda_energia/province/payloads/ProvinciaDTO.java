package bw_team7.gestione_azienda_energia.province.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProvinciaDTO(
        @NotBlank(message = "Il nome della provincia è obbligatorio")
        @Size(min = 2, max = 15, message = "Il nome della provincia deve essere compreso tra 2 e 15 caratteri")
        String nome,

        @NotBlank(message = "La sigla della provincia è obbligatoria")
        @Size(min = 2, max = 2, message = "La sigla deve essere esattamente di 2 caratteri")
        String sigla
) {
}
