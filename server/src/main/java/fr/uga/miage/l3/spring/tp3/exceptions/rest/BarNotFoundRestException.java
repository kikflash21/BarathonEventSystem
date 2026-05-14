package fr.uga.miage.l3.spring.tp3.exceptions.rest;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarNotFoundException;

public class BarNotFoundRestException extends RuntimeException {
    public BarNotFoundRestException(BarNotFoundException e) {
        super(e.getMessage());
    }
}
