package fr.uga.miage.l3.spring.tp3.components;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.EpreuveNotFoundException;
import fr.uga.miage.l3.spring.tp3.mappers.entities.EpreuveEntityMapper;
import fr.uga.miage.l3.spring.tp3.models.domains.Epreuve;
import fr.uga.miage.l3.spring.tp3.models.entities.EpreuveEntity;
import fr.uga.miage.l3.spring.tp3.repositories.EpreuveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EpreuveComponentTest {

    @Autowired
    private EpreuveComponent epreuveComponent;

    @MockitoBean
    private EpreuveRepository epreuveRepository;

    @MockitoBean
    private EpreuveEntityMapper epreuveEntityMapper;

    @Test
    void findEpreuve() throws EpreuveNotFoundException {
        // Given
        Long id = 1L;

        EpreuveEntity epreuveEntity = new EpreuveEntity();
        epreuveEntity.setNombreParticipantMax(9);

        Epreuve epreuveExpected = Epreuve
                .builder()
                .build();

        // Configuration des mocks
        when(epreuveRepository.findById(id)).thenReturn(Optional.of(epreuveEntity));
        when(epreuveEntityMapper.toEpreuve(epreuveEntity)).thenReturn(epreuveExpected);

        // When
        Epreuve result = epreuveComponent.findEpreuveById(id);

        // Then
        assertEquals(epreuveExpected, result);


        verify(epreuveRepository, times(1)).findById(id);
        verify(epreuveEntityMapper, times(1)).toEpreuve(epreuveEntity);
    }

    @Test
    void findEpreuve_notFound() {
        // Given
        Long id = 999L;

        when(epreuveRepository.findById(id)).thenReturn(Optional.empty());

        // When - then
        assertThrows(EpreuveNotFoundException.class, () -> epreuveComponent.findEpreuveById(id));

        verify(epreuveEntityMapper, never()).toEpreuve(any());
    }
}