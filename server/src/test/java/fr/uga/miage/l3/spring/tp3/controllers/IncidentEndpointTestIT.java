package fr.uga.miage.l3.spring.tp3.controllers;


import fr.uga.miage.l3.spring.tp3.models.entities.ChallengeEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.repositories.ChallengeRepository;
import fr.uga.miage.l3.spring.tp3.repositories.EquipeRepository;
import fr.uga.miage.l3.spring.tp3.repositories.IncidentRepository;
import fr.uga.miage.l3.spring.tp3.request.IncidentChallengeCreationRequest;
import fr.uga.miage.l3.spring.tp3.responses.ChallengeIncidentResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureWebTestClient
public class IncidentEndpointTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @AfterEach
    public void clear() {
        // L'ordre est important à cause des clés étrangères
        incidentRepository.deleteAll();
        equipeRepository.deleteAll();
        challengeRepository.deleteAll();
    }

    @Test
    void reportIncident_Success_Returns201() {
        // GIVEN : On prépare un Challenge
        ChallengeEntity challenge = new ChallengeEntity();
        challenge.setIntitule("Le meilleur challenge");
        challenge.setDescription("trop bien ce challenge");
        challenge.setPoints(5); // Le challenge commence à 5 points
        challenge.setHashtag("#LeChallengeRelou");
        challengeRepository.save(challenge);

        // On prépare une équipe et on l'inscrit au challenge
        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom("myTeams");
        equipe.setChallengeEntities(new ArrayList<>(List.of(challenge)));
        equipeRepository.save(equipe);

        // La requête REST
        IncidentChallengeCreationRequest request = new IncidentChallengeCreationRequest(
                "myTeams",
                LocalDateTime.of(2024, 12, 25, 10, 30, 0),
                "le motif de l'incident"
        );

        // WHEN / THEN
        webTestClient.put()
                .uri("/api/incidents/challenge/{id}", challenge.getId())
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated() // 201 Created
                .expectBody(ChallengeIncidentResponse.class)
                .value(response -> {
                    // Vérification de la réponse
                    assertThat(response.getIntitule()).isEqualTo("Le meilleur challenge");
                    assertThat(response.getDescription()).isEqualTo("trop bien ce challenge");
                    assertThat(response.getHashtag()).isEqualTo("#LeChallengeRelou");

                    // On vérifie que 1 point a bien été enlevé ! (5 - 1 = 4)
                    assertThat(response.getPoints()).isEqualTo(4);

                    // On vérifie que la liste des équipes contient bien notre équipe
                    assertThat(response.getEquipes()).isNotNull();
                    assertThat(response.getEquipes()).contains("myTeams");
                });

        // Vérification en base de données : l'incident a bien été créé
        assertThat(incidentRepository.count()).isEqualTo(1);
    }

    @Test
    void reportIncident_ChallengeNotFound_Returns404() {
        // GIVEN : L'équipe existe, mais pas le challenge visé
        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom("myTeams");
        equipeRepository.save(equipe);

        IncidentChallengeCreationRequest request = new IncidentChallengeCreationRequest(
                "myTeams",
                LocalDateTime.now(),
                "motif"
        );

        Long fakeChallengeId = 9999L;

        // WHEN / THEN
        webTestClient.put()
                .uri("/api/incidents/challenge/{id}", fakeChallengeId)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound() // 404 Not Found
                .expectBody(Map.class) // On s'attend à recevoir notre Map JSON
                .value(response -> {
                    assertThat(response.get("url")).isEqualTo("/api/incidents/challenge/" + fakeChallengeId);
                    // On ajoute .toString() ici 👇
                    assertThat(response.get("error").toString()).contains("Le challenge n'existe pas");
                });
    }

    @Test
    void reportIncident_TeamNotFound_Returns404() {
        // GIVEN : Le challenge existe, mais l'équipe "equipeFantome" n'existe pas en base
        ChallengeEntity challenge = new ChallengeEntity();
        challenge.setIntitule("Challenge Solo");
        challengeRepository.save(challenge);

        IncidentChallengeCreationRequest request = new IncidentChallengeCreationRequest(
                "equipeFantome",
                LocalDateTime.now(),
                "motif"
        );

        // WHEN / THEN
        webTestClient.put()
                .uri("/api/incidents/challenge/{id}", challenge.getId())
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound() // 404 Not Found
                .expectBody(Map.class)
                .value(response -> {
                    assertThat(response.get("url")).isEqualTo("/api/incidents/challenge/" + challenge.getId());
                    assertThat(response.get("error")).isNotNull(); // Le message venant de EquipeNotFoundException
                });
    }

    @Test
    void reportIncident_TeamNotRegistered_Returns409() {
        // GIVEN : Le challenge existe ET l'équipe existe, MAIS l'équipe n'est pas inscrite
        ChallengeEntity challenge = new ChallengeEntity();
        challenge.setIntitule("Challenge Select VIP");
        challengeRepository.save(challenge);

        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom("myTeams");
        // On NE met PAS le challenge dans sa liste !
        equipeRepository.save(equipe);

        IncidentChallengeCreationRequest request = new IncidentChallengeCreationRequest(
                "myTeams",
                LocalDateTime.now(),
                "Triche !"
        );

        // WHEN / THEN
        webTestClient.put()
                .uri("/api/incidents/challenge/{id}", challenge.getId())
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(409) // 409 Conflict
                .expectBody(Map.class)
                .value(response -> {
                    assertThat(response.get("url")).isEqualTo("/api/incidents/challenge/" + challenge.getId());

                    assertThat(response.get("error").toString()).contains("n'est pas inscrite au challenge");
                });

        // On s'assure que rien n'a été sauvegardé par erreur
        assertThat(incidentRepository.count()).isEqualTo(0);
    }
}
