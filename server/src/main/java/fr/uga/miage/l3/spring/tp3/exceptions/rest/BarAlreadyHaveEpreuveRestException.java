package fr.uga.miage.l3.spring.tp3.exceptions.rest;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarAlreadyHaveEpreuveException;

public class BarAlreadyHaveEpreuveRestException extends RuntimeException {
    public BarAlreadyHaveEpreuveRestException(BarAlreadyHaveEpreuveException e) {
        super(e.getMessage());
    }
}
