package fr.uga.miage.l3.spring.tp3.Services;


import fr.uga.miage.l3.spring.tp3.components.EquipeComponent;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.InvalidTeamRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.MinorParticipantRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.NoActiveTeamsRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.NoActiveTeamsException;
import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.ParticipantEntity;
import fr.uga.miage.l3.spring.tp3.services.EquipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class EquipeServiceTest {

    @Autowired
    private EquipeService equipeService;


    @MockitoBean
    private EquipeComponent equipeComponent;

    // -------------------------------------------------------------------------
    // TESTS POUR checkAllTeamsAreValid()
    // -------------------------------------------------------------------------

    @Test
    void checkAllTeamsAreValid_NoActiveTeams_ThrowsRestException() throws NoActiveTeamsException {
        // Given : Le composant lève l'exception technique
        when(equipeComponent.getAllTeamsWithPassActif()).thenThrow(new NoActiveTeamsException());

        // When / Then : Le service doit la capturer et lancer l'exception REST correspondante
        assertThrows(NoActiveTeamsRestException.class, () -> equipeService.checkAllTeamsAreValid());

        verify(equipeComponent, times(1)).getAllTeamsWithPassActif();
    }

    @Test
    void checkAllTeamsAreValid_WithMinor_ThrowsMinorRestException() throws NoActiveTeamsException {
        // Given : Une équipe avec un mineur
        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom("Team Mineur");

        ParticipantEntity mineur = new ParticipantEntity();
        mineur.setAge(17); // Mineur !

        ParticipantEntity majeur = new ParticipantEntity();
        majeur.setAge(20);
        majeur.setEstSAM(true);

        equipe.setParticipantEntities(List.of(mineur, majeur));

        when(equipeComponent.getAllTeamsWithPassActif()).thenReturn(List.of(equipe));

        // When / Then : La présence du mineur stoppe tout et lève l'exception
        assertThrows(MinorParticipantRestException.class, () -> equipeService.checkAllTeamsAreValid());

        verify(equipeComponent, times(1)).getAllTeamsWithPassActif();
    }

    @Test
    void checkAllTeamsAreValid_InvalidSizeAndNoSam_ThrowsInvalidTeamRestException() throws NoActiveTeamsException {
        // Given : Une équipe avec 1 seul membre (taille invalide) ET qui n'est pas SAM
        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom("Team Solo");

        ParticipantEntity majeurNonSam = new ParticipantEntity();
        majeurNonSam.setAge(25);
        majeurNonSam.setEstSAM(false); // Pas de SAM !

        equipe.setParticipantEntities(List.of(majeurNonSam));

        when(equipeComponent.getAllTeamsWithPassActif()).thenReturn(List.of(equipe));

        // When / Then : On s'attend à une InvalidTeamRestException qui regroupe les erreurs
        InvalidTeamRestException exception = assertThrows(InvalidTeamRestException.class, () -> equipeService.checkAllTeamsAreValid());



        verify(equipeComponent, times(1)).getAllTeamsWithPassActif();
    }

    @Test
    void checkAllTeamsAreValid_Success_DoesNotThrow() throws NoActiveTeamsException {
        // Given : Une équipe totalement valide
        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom("Team Valide");

        ParticipantEntity p1 = new ParticipantEntity();
        p1.setAge(22);
        p1.setEstSAM(true); // Il y a un SAM

        ParticipantEntity p2 = new ParticipantEntity();
        p2.setAge(25);
        p2.setEstSAM(false);

        // La taille est bonne (2 participants)
        equipe.setParticipantEntities(List.of(p1, p2));

        when(equipeComponent.getAllTeamsWithPassActif()).thenReturn(List.of(equipe));

        // When / Then : On s'attend à ce qu'aucune exception ne soit levée
        assertDoesNotThrow(() -> equipeService.checkAllTeamsAreValid());

        verify(equipeComponent, times(1)).getAllTeamsWithPassActif();
    }
}
