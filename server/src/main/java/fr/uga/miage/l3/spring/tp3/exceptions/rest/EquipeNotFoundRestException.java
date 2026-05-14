package fr.uga.miage.l3.spring.tp3.exceptions.rest;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.EquipeNotFoundException;

public class EquipeNotFoundRestException extends RuntimeException {
    public EquipeNotFoundRestException(EquipeNotFoundException e) {
        super(e.getMessage(),e);
    }
}
