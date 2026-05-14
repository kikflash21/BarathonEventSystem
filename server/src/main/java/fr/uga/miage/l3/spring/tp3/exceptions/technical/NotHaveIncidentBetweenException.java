package fr.uga.miage.l3.spring.tp3.exceptions.technical;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotHaveIncidentBetweenException extends Throwable {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public NotHaveIncidentBetweenException(LocalDateTime start, LocalDateTime end, String message) {
        super(message);
        this.start = start;
        this.end = end;
    }
}
