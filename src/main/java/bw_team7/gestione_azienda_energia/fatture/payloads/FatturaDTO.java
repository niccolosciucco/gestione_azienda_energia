package bw_team7.gestione_azienda_energia.fatture.payloads;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record FatturaDTO(
        @NotNull(message = "La data è obbligatoria")
        LocalDate data,

        @NotNull(message = "Il numero della fattura è obbligatorio")
        @Positive(message = "Il numero della fattura deve essere positivo")
        Long numero,

        @NotNull(message = "L'importo è obbligatorio")
        @Positive(message = "L'importo deve essere maggiore di zero")
        Long importo,

        @NotNull(message = "L'ID del cliente è obbligatorio")
        UUID clienteId,

        @NotNull(message = "L'ID dello stato fattura è obbligatorio")
        UUID statoFatturaId
) {
}
