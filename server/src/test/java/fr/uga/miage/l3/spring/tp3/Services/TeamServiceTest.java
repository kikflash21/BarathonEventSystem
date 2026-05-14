package fr.uga.miage.l3.spring.tp3.Services;

import fr.uga.miage.l3.spring.tp3.components.EquipeComponent;
import fr.uga.miage.l3.spring.tp3.components.ParticipantComponent;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.ParticipantBadRequestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.TeamFullRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.EquipeNotFoundException;
import fr.uga.miage.l3.spring.tp3.mappers.domains.EquipeMapper;
import fr.uga.miage.l3.spring.tp3.mappers.entities.EquipeEntityMapper;
import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.ParticipantEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.PassEntity;
import fr.uga.miage.l3.spring.tp3.request.ParticipantCreationRequest;
import fr.uga.miage.l3.spring.tp3.responses.EquipeResponse;
import fr.uga.miage.l3.spring.tp3.services.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @MockitoBean
    private EquipeComponent equipeComponent;

    @MockitoBean
    private ParticipantComponent participantComponent;

    @MockitoSpyBean
    private EquipeEntityMapper entityMapper;

    @MockitoSpyBean
    private EquipeMapper domainMapper;

    @Test
    void addParticipantToTeam_Success() throws EquipeNotFoundException {
        // GIVEN
        String teamName = "MyTeams";
        ParticipantCreationRequest request = new ParticipantCreationRequest(
                "toto", "titi", 20, "toto@toto.com", "0123456789",
                LocalDate.of(2000, 1, 1), "pseudo", true
        );

        //  Création du Pass
        PassEntity pass = new PassEntity();
        pass.setActif(true);

        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom(teamName);
        equipe.setParticipantEntities(new ArrayList<>()); // Équipe vide au départ
        equipe.setPassEntity(pass);

        when(equipeComponent.findEntityByName(teamName)).thenReturn(equipe);
        when(participantComponent.existsByEmail(anyString())).thenReturn(false);
        when(participantComponent.existsByTelephone(anyString())).thenReturn(false);

        // WHEN
        EquipeResponse response = teamService.addParticipantToTeam(teamName, request);

        // THEN
        assertThat(response).isNotNull();
        // On vérifie que le participant a été ajouté à la liste de l'entité
        assertThat(equipe.getParticipantEntities()).hasSize(1);

        // Vérification des appels aux composants
        verify(equipeComponent, times(1)).findEntityByName(teamName);
        verify(participantComponent, times(1)).save(any(ParticipantEntity.class));
    }
    @Test
    void addParticipantToTeam_InvalidAge_ThrowsBadRequest() {
        // GIVEN : Un participant mineur (16 ans)
        ParticipantCreationRequest request = new ParticipantCreationRequest(
                "toto", "titi", 16, "toto@toto.com", "0123456789",
                LocalDate.of(2010, 1, 1), "pseudo", true
        );

        // WHEN / THEN
        assertThrows(ParticipantBadRequestException.class, () ->
                teamService.addParticipantToTeam("Team", request));

        verifyNoInteractions(equipeComponent);
    }

    @Test
    void addParticipantToTeam_TeamFull_ThrowsTeamFullRestException() throws EquipeNotFoundException {
        // GIVEN : Une équipe qui a déjà 10 participants
        String teamName = "FullTeam";
        ParticipantCreationRequest request = new ParticipantCreationRequest(
                "toto", "titi", 20, "toto@toto.com", "0123456789",
                LocalDate.of(2000, 1, 1), "pseudo", true
        );

        EquipeEntity fullEquipe = new EquipeEntity();
        // On simule une liste de 10 participants
        List<ParticipantEntity> mockList = mock(List.class);
        when(mockList.size()).thenReturn(10);
        fullEquipe.setParticipantEntities(mockList);

        when(equipeComponent.findEntityByName(teamName)).thenReturn(fullEquipe);

        // WHEN / THEN
        assertThrows(TeamFullRestException.class, () ->
                teamService.addParticipantToTeam(teamName, request));
    }

    @Test
    void addParticipantToTeam_InvalidEmailFormat_ThrowsBadRequest() {
        // GIVEN : Email sans @
        ParticipantCreationRequest request = new ParticipantCreationRequest(
                "toto", "titi", 20, "bad-email", "0123456789",
                LocalDate.of(2000, 1, 1), "pseudo", true
        );

        // WHEN / THEN
        assertThrows(ParticipantBadRequestException.class, () ->
                teamService.addParticipantToTeam("Team", request));
    }

    @Test
    void addParticipantToTeam_InvalidPhoneFormat_ThrowsBadRequest() {
        // GIVEN : Téléphone avec des lettres
        ParticipantCreationRequest request = new ParticipantCreationRequest(
                "toto", "titi", 20, "toto@toto.com", "01ABC67890",
                LocalDate.of(2000, 1, 1), "pseudo", true
        );

        // WHEN / THEN
        assertThrows(ParticipantBadRequestException.class, () ->
                teamService.addParticipantToTeam("Team", request));
    }
}
