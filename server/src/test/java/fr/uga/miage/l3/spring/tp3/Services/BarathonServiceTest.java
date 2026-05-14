package fr.uga.miage.l3.spring.tp3.Services;

import fr.uga.miage.l3.spring.tp3.components.BarathonComponent;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.BarAlreadyHaveEpreuveRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.BarNotFoundRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.BarathonNotFoundRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarAlreadyHaveEpreuveException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarNotFoundException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarathonNotFoundException;
import fr.uga.miage.l3.spring.tp3.mappers.domains.BarMapper;
import fr.uga.miage.l3.spring.tp3.models.entities.BarEntity;
import fr.uga.miage.l3.spring.tp3.responses.BarResponse;
import fr.uga.miage.l3.spring.tp3.services.BarathonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BarathonServiceTest {

    @Autowired
    private BarathonService barathonService;

    @MockitoBean
    private BarathonComponent barathonComponent;

    @MockitoSpyBean
    private BarMapper barMapper;

    @Test
    void deleteBar_success() throws BarathonNotFoundException, BarNotFoundException, BarAlreadyHaveEpreuveException {
        // GIVEN
        Long barathonId = 1L;
        Long barId = 1L;

        BarEntity barEntity = new BarEntity();
        barEntity.setId(barId);
        barEntity.setNom("Bar Test");
        barEntity.setAdresse("1 rue toto");
        barEntity.setCapacite(120);
        barEntity.setHeureOuverture(LocalTime.of(10, 0));
        barEntity.setHeureFermeture(LocalTime.of(22, 0));

        when(barathonComponent.deleteBarFromBarathon(barathonId, barId)).thenReturn(barEntity);

        // WHEN
        BarResponse result = barathonService.deleteBarFromBarathon(barathonId, barId);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(barId);
        assertThat(result.getNom()).isEqualTo("Bar Test");
        verify(barathonComponent, times(1)).deleteBarFromBarathon(barathonId, barId);
    }

    @Test
    void deleteBar_barathonNotFound() throws BarathonNotFoundException, BarNotFoundException, BarAlreadyHaveEpreuveException {
        // GIVEN
        Long barathonId = 99L;
        Long barId = 1L;

        when(barathonComponent.deleteBarFromBarathon(barathonId, barId))
                .thenThrow(new BarathonNotFoundException("Le barathon [99] n'existe pas"));

        // WHEN / THEN
        assertThrows(BarathonNotFoundRestException.class,
                () -> barathonService.deleteBarFromBarathon(barathonId, barId));

        verify(barathonComponent, times(1)).deleteBarFromBarathon(barathonId, barId);
    }

    @Test
    void deleteBar_barNotFound() throws BarathonNotFoundException, BarNotFoundException, BarAlreadyHaveEpreuveException {
        // GIVEN
        Long barathonId = 1L;
        Long barId = 99L;

        when(barathonComponent.deleteBarFromBarathon(barathonId, barId))
                .thenThrow(new BarNotFoundException("Le bar [99] n'existe pas"));

        // WHEN / THEN
        assertThrows(BarNotFoundRestException.class,
                () -> barathonService.deleteBarFromBarathon(barathonId, barId));

        verify(barathonComponent, times(1)).deleteBarFromBarathon(barathonId, barId);
    }

    @Test
    void deleteBar_barAlreadyHaveEpreuve() throws BarathonNotFoundException, BarNotFoundException, BarAlreadyHaveEpreuveException {
        // GIVEN
        Long barathonId = 1L;
        Long barId = 1L;

        when(barathonComponent.deleteBarFromBarathon(barathonId, barId))
                .thenThrow(new BarAlreadyHaveEpreuveException("Le bar à supprimer a déjà une participation à une épreuve"));

        // WHEN / THEN
        assertThrows(BarAlreadyHaveEpreuveRestException.class,
                () -> barathonService.deleteBarFromBarathon(barathonId, barId));

        verify(barathonComponent, times(1)).deleteBarFromBarathon(barathonId, barId);
    }
}