package fr.uga.miage.l3.spring.tp3.Services;

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
import fr.uga.miage.l3.spring.tp3.services.IncidentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class IncidentServiceTest {

    @Autowired
    private IncidentService incidentService;

    @MockitoBean
    private IncidentComponent incidentComponent;

    @MockitoBean
    private EquipeComponent equipeComponent;

    @MockitoBean
    private ChallengeComponent challengeComponent;

    @MockitoSpyBean
    private ChallengeMapper challengeMapper;

    @Test
    void reportIncidentOnChallenge_Success() throws EquipeNotFoundException, ChallengeNotFoundException {
        // GIVEN
        Long challengeId = 10L;
        String teamName = "PSG";

        ChallengeEntity challenge = new ChallengeEntity();
        challenge.setIntitule("Challenge Foot");
        challenge.setPoints(5);

        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom(teamName);
        // On inscrit l'équipe au challenge
        equipe.setChallengeEntities(new ArrayList<>(List.of(challenge)));

        IncidentChallengeCreationRequest request = new IncidentChallengeCreationRequest(
                teamName,
                LocalDateTime.now(),
                "Mauvais comportement"
        );

        ChallengeIncidentResponse expectedResponse = ChallengeIncidentResponse.builder()
                .intitule("Challenge Foot")
                .points(4) // 5 - 1 = 4
                .build();

        when(equipeComponent.findEntityByName(teamName)).thenReturn(equipe);
        when(challengeComponent.findById(challengeId)).thenReturn(challenge);
        when(challengeMapper.toResponse(challenge)).thenReturn(expectedResponse);

        // WHEN
        ChallengeIncidentResponse response = incidentService.reportIncidentOnChallenge(challengeId, request);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getPoints()).isEqualTo(4);

        // On vérifie que le point a été déduit et l'entité sauvegardée
        assertThat(challenge.getPoints()).isEqualTo(4);
        verify(incidentComponent, times(1)).save(any(IncidentEntity.class));
        verify(challengeComponent, times(1)).save(challenge);
    }

    @Test
    void reportIncidentOnChallenge_TeamNotFound_ThrowsException() throws EquipeNotFoundException {
        // GIVEN
        Long challengeId = 10L;
        IncidentChallengeCreationRequest request = new IncidentChallengeCreationRequest("GhostTeam", LocalDateTime.now(), "motif");

        when(equipeComponent.findEntityByName("GhostTeam")).thenThrow(new EquipeNotFoundException("Équipe non trouvée"));

        // WHEN / THEN
        assertThrows(EquipeNotFoundRestException.class, () ->
                incidentService.reportIncidentOnChallenge(challengeId, request));

        verifyNoInteractions(incidentComponent);
        verifyNoInteractions(challengeComponent);
    }

    @Test
    void reportIncidentOnChallenge_ChallengeNotFound_ThrowsException() throws EquipeNotFoundException, ChallengeNotFoundException {
        // GIVEN
        Long challengeId = 999L;
        String teamName = "PSG";
        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom(teamName);

        IncidentChallengeCreationRequest request = new IncidentChallengeCreationRequest(teamName, LocalDateTime.now(), "motif");

        when(equipeComponent.findEntityByName(teamName)).thenReturn(equipe);
        when(challengeComponent.findById(challengeId)).thenThrow(new ChallengeNotFoundException("Challenge non trouvé"));

        // WHEN / THEN
        assertThrows(ChallengeNotFoundRestException.class, () ->
                incidentService.reportIncidentOnChallenge(challengeId, request));

        verifyNoInteractions(incidentComponent);
    }

    @Test
    void reportIncidentOnChallenge_TeamNotRegistered_ThrowsException() throws EquipeNotFoundException, ChallengeNotFoundException {
        // GIVEN
        Long challengeId = 10L;
        String teamName = "PSG";

        ChallengeEntity challenge = new ChallengeEntity();
        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom(teamName);
        // L'équipe n'a AUCUN challenge associé (liste vide)
        equipe.setChallengeEntities(new ArrayList<>());

        IncidentChallengeCreationRequest request = new IncidentChallengeCreationRequest(teamName, LocalDateTime.now(), "Triche");

        when(equipeComponent.findEntityByName(teamName)).thenReturn(equipe);
        when(challengeComponent.findById(challengeId)).thenReturn(challenge);

        // WHEN / THEN
        assertThrows(TeamNotRegisteredToChallengeRestException.class, () ->
                incidentService.reportIncidentOnChallenge(challengeId, request));

        verifyNoInteractions(incidentComponent);
        // On vérifie qu'aucun point n'a été retiré
        verify(challengeComponent, never()).save(any());
    }
}