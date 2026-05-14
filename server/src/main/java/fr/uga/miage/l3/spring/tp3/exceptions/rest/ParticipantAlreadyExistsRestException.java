package fr.uga.miage.l3.spring.tp3.exceptions.rest;

public class ParticipantAlreadyExistsRestException extends RuntimeException {
    public ParticipantAlreadyExistsRestException(String message) {
        super(message);
    }
}
