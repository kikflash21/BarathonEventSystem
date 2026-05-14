package fr.uga.miage.l3.spring.tp3.exceptions.rest;

public class TeamNotRegisteredToChallengeRestException extends RuntimeException {
    public TeamNotRegisteredToChallengeRestException(String message) {
        super(message);
    }
}
