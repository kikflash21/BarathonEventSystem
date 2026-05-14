package fr.uga.miage.l3.spring.tp3.exceptions.rest;

public class TeamFullRestException extends RuntimeException {
    public TeamFullRestException(String message) {
        super(message);
    }
}
