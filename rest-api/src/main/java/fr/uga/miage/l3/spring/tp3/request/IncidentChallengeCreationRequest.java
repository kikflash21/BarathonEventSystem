package fr.uga.miage.l3.spring.tp3.request;

import java.time.LocalDateTime;

public record IncidentChallengeCreationRequest(
        String nom,
        LocalDateTime date,
        String motif
) {
}
