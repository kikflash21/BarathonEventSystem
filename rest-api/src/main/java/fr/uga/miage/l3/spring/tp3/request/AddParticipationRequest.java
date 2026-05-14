package fr.uga.miage.l3.spring.tp3.request;

import lombok.Builder;

@Builder
public record AddParticipationRequest(
    String equipeA,
    String equipeB
) {
}
