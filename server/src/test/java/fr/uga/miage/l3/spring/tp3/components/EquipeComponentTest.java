package fr.uga.miage.l3.spring.tp3.components;


import fr.uga.miage.l3.spring.tp3.exceptions.technical.EquipeNotFoundException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.NoActiveTeamsException;
import fr.uga.miage.l3.spring.tp3.mappers.entities.EquipeEntityMapper;
import fr.uga.miage.l3.spring.tp3.models.domains.Equipe;
import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.repositories.EquipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class EquipeComponentTest {


    @Autowired
    private EquipeComponent equipeComponent;

    // On mock les dépendances du composant
    @MockitoBean
    private EquipeRepository equipeRepository;

    @MockitoBean
    private EquipeEntityMapper equipeEntityMapper;

    // -------------------------------------------------------------------------
    // Tests pour getAllTeamsWithPassActif()
    // -------------------------------------------------------------------------

    @Test
    void getAllTeamsWithPassActif_WhenNoTeams_ThrowsException() {
        // Given : Le mock du repo renvoie une liste vide
        when(equipeRepository.findAllByPassEntityActifTrue()).thenReturn(Collections.emptyList());

        // When / Then : On s'attend à ce que l'exception soit levée
        assertThrows(NoActiveTeamsException.class, () -> equipeComponent.getAllTeamsWithPassActif());

        // Vérification optionnelle : s'assurer que le repo a bien été appelé
        verify(equipeRepository, times(1)).findAllByPassEntityActifTrue();
    }

    @Test
    void getAllTeamsWithPassActif_WhenTeamsExist_ReturnsCollection() throws NoActiveTeamsException {
        // Given : On utilise "new" et on configure un nom (pas d'ID en dur)
        EquipeEntity equipeMock = new EquipeEntity();
        equipeMock.setNom("Team Active");

        when(equipeRepository.findAllByPassEntityActifTrue()).thenReturn(List.of(equipeMock));

        // When : On appelle la méthode
        Collection<EquipeEntity> result = equipeComponent.getAllTeamsWithPassActif();

        // Then : On vérifie que le résultat n'est pas vide et contient notre équipe mockée par son nom
        assertThat(result).hasSize(1);
        assertThat(result.iterator().next().getNom()).isEqualTo("Team Active");
    }

    // -------------------------------------------------------------------------
    // Tests pour findActifTeamsByName(String name)
    // -------------------------------------------------------------------------

    @Test
    void findActifTeamsByName_WhenNotFound_ThrowsException() {
        // Given : Le mock du repo ne trouve rien
        when(equipeRepository.findByNom(anyString())).thenReturn(Optional.empty());

        // When / Then : On s'attend à l'exception EquipeNotFoundException
        assertThrows(EquipeNotFoundException.class, () -> equipeComponent.findActifTeamsByName("Team Inconnue"));

        verify(equipeEntityMapper, never()).toEquipe(any());
    }

    @Test
    void findActifTeamsByName_WhenFound_ReturnsMappedEquipe() throws EquipeNotFoundException {
        // Given
        String teamName = "Les vainqueurs";

        // 1. On prépare l'entité que le repo va renvoyer avec un constructeur vide + setter
        EquipeEntity entityMock = new EquipeEntity();
        entityMock.setNom(teamName);

        // 2. On prépare le modèle de domaine avec un constructeur vide + setter
        Equipe equipeDomainMock = new Equipe();
        equipeDomainMock.setNom(teamName);

        // On configure  mocks
        when(equipeRepository.findByNom(teamName)).thenReturn(Optional.of(entityMock));
        when(equipeEntityMapper.toEquipe(entityMock)).thenReturn(equipeDomainMock);

        // When
        Equipe result = equipeComponent.findActifTeamsByName(teamName);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo(teamName);

        // On s'assure que les deux méthodes (repo ET mapper) ont bien été appelées
        verify(equipeRepository, times(1)).findByNom(teamName);
        verify(equipeEntityMapper, times(1)).toEquipe(entityMock);
    }
}