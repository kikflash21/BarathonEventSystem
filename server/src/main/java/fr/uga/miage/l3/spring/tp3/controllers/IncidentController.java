package fr.uga.miage.l3.spring.tp3.controllers;

import fr.uga.miage.l3.spring.tp3.endpoints.IncidentEndpoints;
import fr.uga.miage.l3.spring.tp3.request.IncidentChallengeCreationRequest;
import fr.uga.miage.l3.spring.tp3.responses.ChallengeIncidentResponse;
import fr.uga.miage.l3.spring.tp3.services.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IncidentController implements IncidentEndpoints {

    private final IncidentService incidentService;

    @Override
    public ChallengeIncidentResponse reportIncident(
            @PathVariable Long challengeId,
            @RequestBody IncidentChallengeCreationRequest request
    ) {
        return incidentService.reportIncidentOnChallenge(challengeId, request);
    }
}