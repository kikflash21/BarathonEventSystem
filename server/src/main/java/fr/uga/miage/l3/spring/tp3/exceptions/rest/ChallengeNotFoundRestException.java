package fr.uga.miage.l3.spring.tp3.exceptions.rest;

public class ChallengeNotFoundRestException extends RuntimeException {
    public ChallengeNotFoundRestException(String message) {
        super(message);
    }
}
