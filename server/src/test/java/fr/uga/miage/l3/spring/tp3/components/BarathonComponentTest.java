package fr.uga.miage.l3.spring.tp3.components;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarAlreadyHaveEpreuveException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarNotFoundException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarathonNotFoundException;
import fr.uga.miage.l3.spring.tp3.models.entities.BarEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.BarathonEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.EpreuveEntity;
import fr.uga.miage.l3.spring.tp3.repositories.BarRepository;
import fr.uga.miage.l3.spring.tp3.repositories.BarathonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BarathonComponentTest {

    @Autowired
    private BarathonComponent barathonComponent;

    @MockitoBean
    private BarathonRepository barathonRepository;

    @MockitoBean
    private BarRepository barRepository;

    @Test
    void deleteBarFromBarathon_success() throws BarathonNotFoundException, BarNotFoundException, BarAlreadyHaveEpreuveException {
        // GIVEN
        Long barathonId = 1L;
        Long barId = 1L;

        BarathonEntity barathonEntity = new BarathonEntity();
        barathonEntity.setBarEntities(new ArrayList<>());

        BarEntity barEntity = new BarEntity();
        barEntity.setId(barId);
        barEntity.setNom("Bar Test");
        barEntity.setEpreuveEntity(null); // pas d'épreuve
        barathonEntity.getBarEntities().add(barEntity);

        when(barathonRepository.findById(barathonId)).thenReturn(Optional.of(barathonEntity));
        when(barRepository.findById(barId)).thenReturn(Optional.of(barEntity));
        when(barathonRepository.save(barathonEntity)).thenReturn(barathonEntity);

        // WHEN
        BarEntity result = barathonComponent.deleteBarFromBarathon(barathonId, barId);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Bar Test");
        verify(barathonRepository, times(1)).findById(barathonId);
        verify(barRepository, times(1)).findById(barId);
        verify(barathonRepository, times(1)).save(barathonEntity);
    }

    @Test
    void deleteBarFromBarathon_barathonNotFound() {
        // GIVEN
        Long barathonId = 999L;
        Long barId = 1L;

        when(barathonRepository.findById(barathonId)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(BarathonNotFoundException.class,
                () -> barathonComponent.deleteBarFromBarathon(barathonId, barId));

        verify(barathonRepository, times(1)).findById(barathonId);
        verify(barRepository, never()).findById(any());
        verify(barathonRepository, never()).save(any());
    }

    @Test
    void deleteBarFromBarathon_barNotFound() {
        // GIVEN
        Long barathonId = 1L;
        Long barId = 999L;

        BarathonEntity barathonEntity = new BarathonEntity();
        barathonEntity.setBarEntities(new ArrayList<>());

        when(barathonRepository.findById(barathonId)).thenReturn(Optional.of(barathonEntity));
        when(barRepository.findById(barId)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(BarNotFoundException.class,
                () -> barathonComponent.deleteBarFromBarathon(barathonId, barId));

        verify(barathonRepository, times(1)).findById(barathonId);
        verify(barRepository, times(1)).findById(barId);
        verify(barathonRepository, never()).save(any());
    }

    @Test
    void deleteBarFromBarathon_barAlreadyHaveEpreuve() {
        // GIVEN
        Long barathonId = 1L;
        Long barId = 1L;

        BarathonEntity barathonEntity = new BarathonEntity();
        barathonEntity.setBarEntities(new ArrayList<>());

        EpreuveEntity epreuveEntity = new EpreuveEntity();

        BarEntity barEntity = new BarEntity();
        barEntity.setId(barId);
        barEntity.setEpreuveEntity(epreuveEntity); // bar lié à une épreuve !

        when(barathonRepository.findById(barathonId)).thenReturn(Optional.of(barathonEntity));
        when(barRepository.findById(barId)).thenReturn(Optional.of(barEntity));

        // WHEN / THEN
        assertThrows(BarAlreadyHaveEpreuveException.class,
                () -> barathonComponent.deleteBarFromBarathon(barathonId, barId));

        verify(barathonRepository, times(1)).findById(barathonId);
        verify(barRepository, times(1)).findById(barId);
        verify(barathonRepository, never()).save(any()); // on ne doit jamais sauvegarder
    }
}