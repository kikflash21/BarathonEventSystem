package fr.uga.miage.l3.spring.tp3.controllers;

import fr.uga.miage.l3.spring.tp3.models.entities.BarEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.BarathonEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.EpreuveEntity;
import fr.uga.miage.l3.spring.tp3.repositories.BarRepository;
import fr.uga.miage.l3.spring.tp3.repositories.BarathonRepository;
import fr.uga.miage.l3.spring.tp3.repositories.EpreuveRepository;
import fr.uga.miage.l3.spring.tp3.responses.BarResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureWebTestClient
public class BarathonEndpointTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BarathonRepository barathonRepository;

    @Autowired
    private BarRepository barRepository;

    @Autowired
    private EpreuveRepository epreuveRepository;

    @AfterEach
    public void clear() {
        barRepository.deleteAllInBatch();
        barathonRepository.deleteAllInBatch();
        epreuveRepository.deleteAllInBatch();
    }

    @Test
    void deleteBar_success() {
        // GIVEN
        BarathonEntity barathon = new BarathonEntity();
        barathonRepository.save(barathon);

        BarEntity bar = new BarEntity();
        bar.setNom("Bar Test");
        bar.setAdresse("1 rue toto");
        bar.setCapacite(120);
        bar.setHeureOuverture(LocalTime.of(10, 0));
        bar.setHeureFermeture(LocalTime.of(22, 0));
        bar.setBarathonEntities(new ArrayList<>(List.of(barathon)));
        barRepository.save(bar);

        // WHEN / THEN
        webTestClient
                .delete()
                .uri("/api/barathon/{barathonId}/bars/{barId}", barathon.getId(), bar.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BarResponse.class)
                .value(response -> {
                    assertThat(response.getNom()).isEqualTo("Bar Test");
                    assertThat(response.getAdresse()).isEqualTo("1 rue toto");
                    assertThat(response.getCapacite()).isEqualTo(120);
                });
    }

    @Test
    void deleteBar_barathonNotFound() {
        // GIVEN
        Long fakeBarathonId = 9999L;
        Long fakeBarId = 9999L;

        // WHEN / THEN
        webTestClient
                .delete()
                .uri("/api/barathon/{barathonId}/bars/{barId}", fakeBarathonId, fakeBarId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Map.class)
                .value(response -> {
                    assertThat(response.get("url")).isEqualTo("/api/barathon/" + fakeBarathonId + "/bars/" + fakeBarId);
                    assertThat(response.get("error").toString()).contains("9999");
                });
    }

    @Test
    void deleteBar_barNotFound() {
        // GIVEN
        BarathonEntity barathon = new BarathonEntity();
        barathonRepository.save(barathon);

        Long fakeBarId = 9999L;

        // WHEN / THEN
        webTestClient
                .delete()
                .uri("/api/barathon/{barathonId}/bars/{barId}", barathon.getId(), fakeBarId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Map.class)
                .value(response -> {
                    assertThat(response.get("url")).isEqualTo("/api/barathon/" + barathon.getId() + "/bars/" + fakeBarId);
                    assertThat(response.get("error").toString()).contains("9999");
                });
    }

    @Test
    void deleteBar_barAlreadyHaveEpreuve() {
        // GIVEN
        BarathonEntity barathon = new BarathonEntity();
        barathonRepository.save(barathon);

        EpreuveEntity epreuve = new EpreuveEntity();
        epreuve.setNombreParticipantMax(8);
        epreuveRepository.save(epreuve);

        BarEntity bar = new BarEntity();
        bar.setNom("Bar avec Epreuve");
        bar.setAdresse("2 rue tata");
        bar.setCapacite(50);
        bar.setHeureOuverture(LocalTime.of(10, 0));
        bar.setHeureFermeture(LocalTime.of(22, 0));
        bar.setBarathonEntities(new ArrayList<>(List.of(barathon)));
        bar.setEpreuveEntity(epreuve); // Bar déjà lié à une épreuve !
        barRepository.save(bar);

        // WHEN / THEN
        webTestClient
                .delete()
                .uri("/api/barathon/{barathonId}/bars/{barId}", barathon.getId(), bar.getId())
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(Map.class)
                .value(response -> {
                    assertThat(response.get("url")).isEqualTo("/api/barathon/" + barathon.getId() + "/bars/" + bar.getId());
                    assertThat(response.get("error").toString()).contains("épreuve");
                });
    }
}
