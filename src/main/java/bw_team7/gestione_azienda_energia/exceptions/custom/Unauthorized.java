package bw_team7.gestione_azienda_energia.exceptions.custom;

public class Unauthorized extends RuntimeException {
    public Unauthorized(String message) {
        super(message);
    }
}
