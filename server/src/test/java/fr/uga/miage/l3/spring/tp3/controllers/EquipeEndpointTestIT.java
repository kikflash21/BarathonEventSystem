package fr.uga.miage.l3.spring.tp3.controllers;

import fr.uga.miage.l3.spring.tp3.exceptions.ConflitErrorResponse;
import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.ParticipantEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.PassEntity;
import fr.uga.miage.l3.spring.tp3.repositories.EquipeRepository;
import fr.uga.miage.l3.spring.tp3.repositories.ParticipantRepository;
import fr.uga.miage.l3.spring.tp3.repositories.PassRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureWebTestClient
class EquipeEndpointTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private PassRepository passRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @AfterEach
    public void clear() {
        participantRepository.deleteAllInBatch();
        equipeRepository.deleteAllInBatch();
        passRepository.deleteAllInBatch();
    }

    @Test
    void checkAllEquipesAreValid_Success_Returns200() {
        // GIVEN : On prépare une équipe valide en base de données
        PassEntity pass = new PassEntity();

        pass.setActif(true); // Le pass doit être actif pour que le Component la trouve
        passRepository.save(pass);

        EquipeEntity equipe = new EquipeEntity();

        equipe.setNom("Team Alpha");
        equipe.setPassEntity(pass);
        equipeRepository.save(equipe);

        // On crée 2 participants majeurs, dont un SAM
        ParticipantEntity p1 = new ParticipantEntity();

        p1.setAge(20); // Majeur
        p1.setEstSAM(true); // Est SAM
        p1.setEquipeEntity(equipe);
        participantRepository.save(p1);

        ParticipantEntity p2 = new ParticipantEntity();

        p2.setAge(22); // Majeur
        p2.setEstSAM(false);
        p2.setEquipeEntity(equipe);
        participantRepository.save(p2);

        // WHEN / THEN : On appelle l'API et on s'attend à un 200 OK
        webTestClient
                .get()
                .uri("/api/equipe/checks")
                .exchange()
                .expectStatus().isOk(); // Vérifie que le HTTP status est 200
    }

    @Test
    void checkAllEquipesAreValid_WithMinor_Returns425() {
        // GIVEN : Une équipe avec un mineur
        PassEntity pass = new PassEntity();

        pass.setActif(true);
        passRepository.save(pass);

        EquipeEntity equipe = new EquipeEntity();

        equipe.setNom("Team Beta");
        equipe.setPassEntity(pass);
        equipeRepository.save(equipe);

        ParticipantEntity p1 = new ParticipantEntity();

        p1.setAge(20);
        p1.setEstSAM(true);
        p1.setEquipeEntity(equipe);
        participantRepository.save(p1);

        // LE MINEUR !
        ParticipantEntity p2 = new ParticipantEntity();

        p2.setAge(17); // Mineur
        p2.setEstSAM(false);
        p2.setEquipeEntity(equipe);
        participantRepository.save(p2);

        // WHEN / THEN : On s'attend à une erreur 425 et un message String
        webTestClient
                .get()
                .uri("/api/equipe/checks")
                .exchange()
                .expectStatus().isEqualTo(425)
                .expectBody(String.class)
                .value(response -> {
                    assertThat(response).contains("Un mineur s'est glissé dans l'équipe Team Beta");
                });
    }

    @Test
    void checkAllEquipesAreValid_NoSam_Returns409() {
        // GIVEN : Une équipe sans aucun SAM
        PassEntity pass = new PassEntity();

        pass.setActif(true);
        passRepository.save(pass);

        EquipeEntity equipe = new EquipeEntity();

        equipe.setNom("Team Gamma");
        equipe.setPassEntity(pass);
        equipeRepository.save(equipe);

        // Deux participants majeurs, mais AUCUN n'est SAM
        ParticipantEntity p1 = new ParticipantEntity();

        p1.setAge(22);
        p1.setEstSAM(false); // Pas de SAM
        p1.setEquipeEntity(equipe);
        participantRepository.save(p1);

        ParticipantEntity p2 = new ParticipantEntity();

        p2.setAge(25);
        p2.setEstSAM(false); // Pas de SAM
        p2.setEquipeEntity(equipe);
        participantRepository.save(p2);

        // WHEN / THEN : On s'attend à un statut 409 et à l'objet ConflitErrorResponse
        webTestClient
                .get()
                .uri("/api/equipe/checks")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT) // 409 Conflict
                .expectBody(ConflitErrorResponse.class)
                .value(errorResponse -> {
                    // On vérifie que la réponse contient bien la liste des erreurs comme prévu dans l'exception
                    assertThat(errorResponse.getErrors()).isNotNull();
                    boolean containsSamError = errorResponse.getErrors().stream()
                            .anyMatch(msg -> msg.contains("ne contient pas de SAM"));
                    assertThat(containsSamError).isTrue();
                });
    }

    // TODO 🚧 26/03/2026
}