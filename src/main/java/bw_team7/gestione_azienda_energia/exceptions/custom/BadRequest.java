package bw_team7.gestione_azienda_energia.exceptions.custom;

public class BadRequest extends RuntimeException {
    public BadRequest(String message) {
        super(message);
    }
}
