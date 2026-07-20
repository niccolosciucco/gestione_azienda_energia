package bw_team7.gestione_azienda_energia.exceptions.exceptionsHandler;

import bw_team7.gestione_azienda_energia.exceptions.custom.NotFound;
import bw_team7.gestione_azienda_energia.exceptions.exceptionsDTO.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleNotFound(NotFound ex) {
        return new ExceptionDTO(LocalDateTime.now(), ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDTO handleGenericException(Exception ex) {
        ex.printStackTrace();
        return new ExceptionDTO(LocalDateTime.now(), ex.getMessage());
    }
}