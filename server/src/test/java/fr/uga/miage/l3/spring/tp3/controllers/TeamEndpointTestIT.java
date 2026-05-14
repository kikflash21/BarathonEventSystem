package fr.uga.miage.l3.spring.tp3.controllers;


import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.ParticipantEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.PassEntity;
import fr.uga.miage.l3.spring.tp3.repositories.EquipeRepository;
import fr.uga.miage.l3.spring.tp3.repositories.ParticipantRepository;
import fr.uga.miage.l3.spring.tp3.repositories.PassRepository;
import fr.uga.miage.l3.spring.tp3.request.ParticipantCreationRequest;
import fr.uga.miage.l3.spring.tp3.responses.EquipeResponse;
import fr.uga.miage.l3.spring.tp3.responses.ParticipantSimpleResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureWebTestClient
public class TeamEndpointTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private PassRepository passRepository;

    @AfterEach
    public void clear() {
        // On nettoie la base de données entre chaque test
        participantRepository.deleteAllInBatch();
        equipeRepository.deleteAllInBatch();
        passRepository.deleteAllInBatch();
    }

    @Test
    void addParticipantToTeam_Success_Returns201() {
        // 1. GIVEN : Sauvegarder le Pass PUIS l'équipe
        PassEntity pass = new PassEntity();
        pass.setActif(true);
        passRepository.save(pass);

        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom("MyTeams");
        equipe.setCouleur("rouge");
        equipe.setSlogan("rouge c'est bien non ?");
        equipe.setPassEntity(pass);
        equipeRepository.save(equipe);

        ParticipantEntity p1 = new ParticipantEntity();
        p1.setNom("tato");
        p1.setPrenom("tati");
        p1.setEstSAM(false);
        p1.setEmail("tato@test.com");
        p1.setTelephone("0611111111");
        p1.setEquipeEntity(equipe);
        participantRepository.save(p1);

        ParticipantCreationRequest request = new ParticipantCreationRequest(
                "toto", "titi", 18, "toto@toto.com", "0789345678",
                LocalDate.of(2006, 3, 26), "Le bruyant", true
        );

        // 4. WHEN / THEN
        webTestClient.put()
                .uri("/api/teams/MyTeams/participants")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated() // 201
                .expectBody(EquipeResponse.class)
                .value(response -> {
                    assertThat(response.getId()).isNotNull();
                    assertThat(response.getNom()).isEqualTo("MyTeams");
                    assertThat(response.getCouleur()).isEqualTo("rouge");
                    assertThat(response.getSlogan()).isEqualTo("rouge c'est bien non ?");

                    // On vérifie que le Mapper a bien fait son travail grâce au Pass
                    assertThat(response.isEstActif()).isTrue();

                    assertThat(response.getParticipants()).isNotNull();
                    List<ParticipantSimpleResponse> parts = new ArrayList<>(response.getParticipants());
                    assertThat(parts.size()).isEqualTo(2);

                    assertThat(parts.get(0).getNom()).isEqualTo("tato");
                    assertThat(parts.get(0).getPrenom()).isEqualTo("tati");
                    assertThat(parts.get(0).isEstSAM()).isFalse();

                    assertThat(parts.get(1).getNom()).isEqualTo("toto");
                    assertThat(parts.get(1).getPrenom()).isEqualTo("titi");
                    assertThat(parts.get(1).isEstSAM()).isTrue();
                });

        assertThat(participantRepository.count()).isEqualTo(2);
    }

    @Test
    void addParticipantToTeam_TeamNotFound_Returns404() {
        // GIVEN : Aucune équipe en base
        ParticipantCreationRequest request = new ParticipantCreationRequest(
                "toto", "titi", 18, "toto@toto.com", "0789345678",
                LocalDate.of(2006, 3, 26), "Le bruyant", true
        );

        // WHEN / THEN
        webTestClient.put()
                .uri("/api/teams/EquipeFantome/participants")
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound(); // Vérifie que le gestionnaire d'erreur renvoie bien 404

        assertThat(participantRepository.count()).isEqualTo(0);
    }

    @Test
    void addParticipantToTeam_InvalidRequest_Returns400() {
        // GIVEN : Une équipe existe
        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom("MyTeams");
        equipeRepository.save(equipe);

        // Requête invalide : participant mineur (16 ans)
        ParticipantCreationRequest request = new ParticipantCreationRequest(
                "toto", "titi", 16, "toto@toto.com", "0789345678",
                LocalDate.of(2008, 3, 26), "Le bruyant", true
        );

        // WHEN / THEN
        webTestClient.put()
                .uri("/api/teams/MyTeams/participants")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest(); // Vérifie que le validateur du service renvoie 400

        assertThat(participantRepository.count()).isEqualTo(0);
    }

    @Test
    void addParticipantToTeam_EmailOrPhoneAlreadyExists_Returns409() {
        // GIVEN : Une équipe et un participant existant
        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom("MyTeams");
        equipeRepository.save(equipe);

        ParticipantEntity p1 = new ParticipantEntity();
        p1.setNom("tato");
        p1.setEmail("deja-utilise@test.com"); // Email à tester
        p1.setTelephone("0611111111");
        p1.setEquipeEntity(equipe);
        participantRepository.save(p1);

        // Requête avec le même email
        ParticipantCreationRequest request = new ParticipantCreationRequest(
                "toto", "titi", 18, "deja-utilise@test.com", "0789345678",
                LocalDate.of(2006, 3, 26), "Le bruyant", true
        );

        // WHEN / THEN
        webTestClient.put()
                .uri("/api/teams/MyTeams/participants")
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(409); // Conflict

        assertThat(participantRepository.count()).isEqualTo(1); // L'ajout a échoué, on reste à 1
    }

    @Test
    void addParticipantToTeam_TeamFull_Returns422() {
        // GIVEN : Une équipe avec déjà 10 participants
        EquipeEntity equipe = new EquipeEntity();
        equipe.setNom("FullTeam");
        equipeRepository.save(equipe);

        // Ajout manuel de 10 participants en base
        for (int i = 0; i < 10; i++) {
            ParticipantEntity p = new ParticipantEntity();
            p.setNom("Nom" + i);
            p.setEmail("email" + i + "@test.com");
            p.setTelephone("060000000" + i);
            p.setEquipeEntity(equipe);
            participantRepository.save(p);
        }

        // Requête pour ajouter le 11ème
        ParticipantCreationRequest request = new ParticipantCreationRequest(
                "Onzieme", "Participant", 20, "onzieme@test.com", "0799999999",
                LocalDate.of(2000, 1, 1), "TropTard", false
        );

        // WHEN / THEN
        webTestClient.put()
                .uri("/api/teams/FullTeam/participants")
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(422); // Unprocessable Entity

        assertThat(participantRepository.count()).isEqualTo(10); // Toujours 10
    }



}