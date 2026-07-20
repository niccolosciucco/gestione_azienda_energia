package bw_team7.gestione_azienda_energia.clienti.payloads;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ClienteDTO(
        @NotBlank(message = "La ragione sociale è obbligatoria")
        String ragioneSociale,

        @NotBlank(message = "La partita IVA è obbligatoria")
        String partitaIva,

        @NotBlank(message = "Il tipo cliente è obbligatorio")
        String tipoCliente,

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Formato email non valido")
        String email,

        @NotBlank(message = "La PEC è obbligatoria")
        @Email(message = "Formato PEC non valido")
        String pec,

        @NotBlank(message = "Il telefono è obbligatorio")
        String telefono,

        @NotNull(message = "La data di inserimento è obbligatoria")
        LocalDate dataInserimento,

        @NotNull(message = "La data dell'ultimo contatto è obbligatoria")
        LocalDate dataUltimoContatto,

        @NotNull(message = "Il fatturato annuale è obbligatorio")
        BigDecimal fatturatoAnnuale,

        @NotBlank(message = "Il nome del contatto è obbligatorio")
        String nomeContatto,

        @NotBlank(message = "Il cognome del contatto è obbligatorio")
        String cognomeContatto,

        @NotBlank(message = "L'email di contatto è obbligatoria")
        @Email(message = "Formato email di contatto non valido")
        String emailContatto,

        @NotBlank(message = "Il telefono del contatto è obbligatorio")
        String telefonoContatto,

        @NotNull(message = "L'ID della sede legale è obbligatorio")
        UUID sedeLegaleId,

        @NotNull(message = "L'ID della sede operativa è obbligatorio")
        UUID sedeOperativaId
) {
}