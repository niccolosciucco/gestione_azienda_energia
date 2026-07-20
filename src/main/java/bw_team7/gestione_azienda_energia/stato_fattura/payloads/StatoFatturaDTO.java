package bw_team7.gestione_azienda_energia.stato_fattura.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StatoFatturaDTO(

        @NotBlank(message = "Il nome dello stato fattura è obbligatorio")
        @Size(min = 2, max = 30, message = "Il nome deve essere compreso tra 2 e 30 caratteri")
        String nome
) {
}
