package fr.uga.miage.l3.spring.tp3.exceptions.rest;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarathonNotFoundException;

public class BarathonNotFoundRestException extends RuntimeException {
    public BarathonNotFoundRestException(BarathonNotFoundException e) {
        super(e.getMessage());
    }
}
