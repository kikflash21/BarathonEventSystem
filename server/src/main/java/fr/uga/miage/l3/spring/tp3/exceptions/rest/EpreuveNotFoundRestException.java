package fr.uga.miage.l3.spring.tp3.exceptions.rest;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.EpreuveNotFoundException;

public class EpreuveNotFoundRestException extends RuntimeException {
    public EpreuveNotFoundRestException(EpreuveNotFoundException e) {
        super(e.getMessage(),e);
    }
}
