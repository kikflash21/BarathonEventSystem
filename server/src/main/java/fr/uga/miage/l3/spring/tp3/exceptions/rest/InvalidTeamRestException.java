package fr.uga.miage.l3.spring.tp3.exceptions.rest;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidTeamRestException extends RuntimeException {
    private final List<String> errors;

    public InvalidTeamRestException(
            String message,
            List<String> errors
    ) {
        super(message);
        this.errors = errors;
    }

    public String formatMessage() {
        return String.join("\n", errors);
    }
}
