package fr.uga.miage.l3.spring.tp3.exceptions.technical;

import lombok.Getter;

@Getter
public class SupervisorNotFoundException extends Exception{
    private final Long supervisorId;

    public SupervisorNotFoundException(String message, Long supervisorId) {
        this.supervisorId = supervisorId;
    }
}
