package bw_team7.gestione_azienda_energia.exceptions.exceptionsDTO;

import java.time.LocalDateTime;

public record ExceptionDTO(LocalDateTime localDateTime, String message) {
}