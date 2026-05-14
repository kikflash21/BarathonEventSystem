package fr.uga.miage.l3.spring.tp3.exceptions.technical;

public class ChallengeNotFoundException extends Exception {

    public ChallengeNotFoundException(String message) {
        super(message);
    }

    public ChallengeNotFoundException(String message, Long id) {
        super(message + " (ID: " + id + ")");
    }
}