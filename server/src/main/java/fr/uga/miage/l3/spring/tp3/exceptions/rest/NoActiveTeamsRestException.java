package fr.uga.miage.l3.spring.tp3.exceptions.rest;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.NoActiveTeamsException;

public class NoActiveTeamsRestException extends RuntimeException {
    public NoActiveTeamsRestException(String message) {
        super(message);
    }

    public NoActiveTeamsRestException(String message, NoActiveTeamsException e) {
        super(message,e);
    }
}
