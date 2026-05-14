package fr.uga.miage.l3.spring.tp3.Services;

import fr.uga.miage.l3.spring.tp3.components.EpreuveComponent;
import fr.uga.miage.l3.spring.tp3.components.EquipeComponent;
import fr.uga.miage.l3.spring.tp3.components.ParticipationComponent;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.EpreuveNotFoundRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.EquipeNotFoundRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.NoActiveTeamsRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.EpreuveNotFoundException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.EquipeNotFoundException;
import fr.uga.miage.l3.spring.tp3.mappers.entities.EpreuveEntityMapper;
import fr.uga.miage.l3.spring.tp3.models.domains.Epreuve;
import fr.uga.miage.l3.spring.tp3.models.domains.EpreuveParticipation;
import fr.uga.miage.l3.spring.tp3.models.domains.Equipe;
import fr.uga.miage.l3.spring.tp3.request.AddParticipationRequest;
import fr.uga.miage.l3.spring.tp3.services.EpreuveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class EpreuveServiceTest {

    @Autowired
    private EpreuveService epreuveService;

    @MockitoBean
    private EpreuveComponent epreuveComponent;

    @MockitoSpyBean
    private EpreuveEntityMapper epreuveEntityMapper;

    @MockitoBean
    private EquipeComponent equipeComponent;

    @MockitoBean
    private ParticipationComponent participationComponent;

    @Test
    void addParticipation_correct() throws EpreuveNotFoundException, EquipeNotFoundException {
        // given
        Long id = 1L; // Requis par l'appel de la méthode du service

        // Création Epreuve
        Epreuve epreuve = Epreuve
                .builder()
                .nombreParticipantMax(3)
                .build();

        // Création Equipe A
        Equipe equipeA = new Equipe();
        equipeA.setNom("PSG");
        equipeA.setEstActif(true);

        // Création Equipe B
        Equipe equipeB = new Equipe();
        equipeB.setNom("OM");
        equipeB.setEstActif(true);

        // Création participation attendue
        EpreuveParticipation participationAttendu = new EpreuveParticipation();
        participationAttendu.setEpreuve(epreuve);
        participationAttendu.setEquipeA(equipeA);
        participationAttendu.setEquipeB(equipeB);

        // Request
        AddParticipationRequest request = AddParticipationRequest
                .builder()
                .equipeA("PSG")
                .equipeB("OM")
                .build();

        // config des mocks
        when(epreuveComponent.findEpreuveById(id)).thenReturn(epreuve);
        when(equipeComponent.findActifTeamsByName("PSG")).thenReturn(equipeA);
        when(equipeComponent.findActifTeamsByName("OM")).thenReturn(equipeB);
        when(participationComponent.createParticipation(epreuve, equipeA, equipeB)).thenReturn(participationAttendu);

        // when
        EpreuveParticipation result = epreuveService.addParticipation(id, request);

        // then
        assertThat(result).isEqualTo(participationAttendu);
        verify(participationComponent).createParticipation(epreuve, equipeA, equipeB);
    }

    @Test
    void addParticipation_noActiveTeams() throws EquipeNotFoundException, EpreuveNotFoundException {
        // Given
        Long id = 1L;

        // Création Epreuve
        Epreuve epreuve = Epreuve
                .builder()
                .nombreParticipantMax(3)
                .build();

        // Création Equipe A
        Equipe equipeA = new Equipe();
        equipeA.setNom("PSG");
        equipeA.setEstActif(true);

        // Création Equipe B (Inactive pour faire échouer le service)
        Equipe equipeB = new Equipe();
        equipeB.setNom("OM");
        equipeB.setEstActif(false);

        // Request
        AddParticipationRequest request = AddParticipationRequest
                .builder()
                .equipeA("PSG")
                .equipeB("OM")
                .build();

        // config des mocks
        when(epreuveComponent.findEpreuveById(id)).thenReturn(epreuve);
        when(equipeComponent.findActifTeamsByName("PSG")).thenReturn(equipeA);
        when(equipeComponent.findActifTeamsByName("OM")).thenReturn(equipeB);

        // when + then
        assertThrows(NoActiveTeamsRestException.class,
                () -> epreuveService.addParticipation(id, request));

        verify(participationComponent, never()).createParticipation(any(), any(), any());
    }

    @Test
    void addParticipation_epreuveNotFound() throws EpreuveNotFoundException, EquipeNotFoundException {
        // given
        Long id = 1L;

        // Request
        AddParticipationRequest request = AddParticipationRequest
                .builder()
                .equipeA("PSG")
                .equipeB("OM")
                .build();

        // config des mocks
        when(epreuveComponent.findEpreuveById(id)).thenThrow(new EpreuveNotFoundException("message"));

        // when + then
        assertThrows(EpreuveNotFoundRestException.class, () -> epreuveService.addParticipation(id, request));

        verify(equipeComponent, never()).findActifTeamsByName(any());
        verify(participationComponent, never()).createParticipation(any(), any(), any());
    }

    @Test
    void addParticipation_equipeNotFound() throws EpreuveNotFoundException, EquipeNotFoundException {
        // given
        Long id = 1L;

        // Création Epreuve
        Epreuve epreuve = Epreuve
                .builder()
                .nombreParticipantMax(3)
                .build();

        // Request
        AddParticipationRequest request = AddParticipationRequest
                .builder()
                .equipeA("PSG")
                .equipeB("OM")
                .build();

        // config des mocks
        when(epreuveComponent.findEpreuveById(id)).thenReturn(epreuve);
        when(equipeComponent.findActifTeamsByName(anyString())).thenThrow(new EquipeNotFoundException("message"));

        // when + then
        assertThrows(EquipeNotFoundRestException.class, () -> epreuveService.addParticipation(id, request));

        verify(epreuveComponent, times(1)).findEpreuveById(id);
        verify(participationComponent, never()).createParticipation(any(), any(), any());
        verify(equipeComponent, times(1)).findActifTeamsByName(any());
    }
}