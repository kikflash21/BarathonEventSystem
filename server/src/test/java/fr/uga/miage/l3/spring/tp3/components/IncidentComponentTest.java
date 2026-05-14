package fr.uga.miage.l3.spring.tp3.components;

import fr.uga.miage.l3.spring.tp3.models.entities.IncidentEntity;
import fr.uga.miage.l3.spring.tp3.repositories.IncidentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class IncidentComponentTest {

    @Autowired
    private IncidentComponent incidentComponent;

    @MockitoBean
    private IncidentRepository incidentRepository;

    @Test
    void getIncidentBetween17Octobre22HAnd23h59_ReturnsCount() {
        // Given
        LocalDateTime start = LocalDateTime.of(2025, 10, 17, 22, 0);
        LocalDateTime end = LocalDateTime.of(2025, 10, 17, 23, 59);
        int expectedCount = 5;

        when(incidentRepository.countDistinctByDateDeclarationBetween(start, end)).thenReturn(expectedCount);

        // When
        int result = incidentComponent.getIncidentBetween17Octobre22HAnd23h59();

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(incidentRepository, times(1)).countDistinctByDateDeclarationBetween(start, end);
    }

    @Test
    void save_ReturnsSavedIncident() {
        // Given
        IncidentEntity incidentToSave = new IncidentEntity();
        incidentToSave.setMotif("Test motif");

        // On simule le fait que le repository retourne l'entité sauvegardée
        when(incidentRepository.save(any(IncidentEntity.class))).thenReturn(incidentToSave);

        // When
        IncidentEntity result = incidentComponent.save(incidentToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMotif()).isEqualTo("Test motif");
        verify(incidentRepository, times(1)).save(incidentToSave);
    }
}