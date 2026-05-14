package fr.uga.miage.l3.spring.tp3.services;

import fr.uga.miage.l3.spring.tp3.components.ChallengeComponent;
import fr.uga.miage.l3.spring.tp3.components.EquipeComponent;
import fr.uga.miage.l3.spring.tp3.components.IncidentComponent;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.ChallengeNotFoundRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.EquipeNotFoundRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.TeamNotRegisteredToChallengeRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.ChallengeNotFoundException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.EquipeNotFoundException;
import fr.uga.miage.l3.spring.tp3.mappers.domains.ChallengeMapper;
import fr.uga.miage.l3.spring.tp3.models.entities.ChallengeEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.IncidentEntity;
import fr.uga.miage.l3.spring.tp3.request.IncidentChallengeCreationRequest;
import fr.uga.miage.l3.spring.tp3.responses.ChallengeIncidentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentComponent incidentComponent;
    private final EquipeComponent equipeComponent;
    private final ChallengeComponent challengeComponent;
    private final ChallengeMapper challengeMapper;

    @Transactional
    public ChallengeIncidentResponse reportIncidentOnChallenge(Long challengeId, IncidentChallengeCreationRequest request) {

        // 1. Vérifier que l'équipe existe (404)
        EquipeEntity equipe;
        try {
            equipe = equipeComponent.findEntityByName(request.nom());
        } catch (EquipeNotFoundException e) {
            throw new EquipeNotFoundRestException(e);
        }

        // 2. Vérifier que le challenge existe (404)
        ChallengeEntity challenge;
        try {
            challenge = challengeComponent.findById(challengeId);
        } catch (ChallengeNotFoundException e) {
            throw new ChallengeNotFoundRestException(e.getMessage());
        }

        // 3. Vérifier si l'équipe est bien inscrite au challenge (409)
        boolean isRegistered = equipe.getChallengeEntities() != null && equipe.getChallengeEntities().contains(challenge);
        if (!isRegistered) {
            throw new TeamNotRegisteredToChallengeRestException(
                    "L'équipe " + request.nom() + " n'est pas inscrite au challenge " + challengeId);
        }

        // 4. Créer l'incident
        IncidentEntity incident = new IncidentEntity();
        incident.setDateDeclaration(request.date());
        incident.setMotif(request.motif());
        incident.setChallengeEntity(challenge);
        incidentComponent.save(incident);

        // 5. Enlever 1 point au challenge
        if (challenge.getPoints() != null && challenge.getPoints() > 0) {
            challenge.setPoints(challenge.getPoints() - 1);
            challengeComponent.save(challenge);
        }

        // 6. Retourner la réponse formatée
        return challengeMapper.toResponse(challenge);
    }
}