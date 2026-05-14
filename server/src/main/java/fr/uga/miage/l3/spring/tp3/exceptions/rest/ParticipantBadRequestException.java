package fr.uga.miage.l3.spring.tp3.exceptions.rest;

public class ParticipantBadRequestException extends RuntimeException {
    public ParticipantBadRequestException(String message) {
        super(message);
    }
}
