package fr.uga.miage.l3.spring.tp3.controllers;

import fr.uga.miage.l3.spring.tp3.components.EpreuveComponent;
import fr.uga.miage.l3.spring.tp3.models.entities.EpreuveEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.ParticipantEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.PassEntity;
import fr.uga.miage.l3.spring.tp3.repositories.*;
import fr.uga.miage.l3.spring.tp3.request.AddParticipationRequest;
import fr.uga.miage.l3.spring.tp3.responses.EpreuveParticipationResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class EpreuveEndpointTestIT {
    // TODO 🚧 26/03/2026

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EpreuveRepository epreuveRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private EpreuveParticipationRepository epreuveParticipationRepository;

    @Autowired
    private PassRepository passRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @MockitoSpyBean
    private EpreuveComponent epreuveComponent;

    @AfterEach
    public void clear(){

        epreuveParticipationRepository.deleteAllInBatch();
        participantRepository.deleteAllInBatch();
        equipeRepository.deleteAllInBatch();
        epreuveRepository.deleteAllInBatch();
        passRepository.deleteAllInBatch();
    }
    @Test
    void addParticipation_created(){
        // GIVEN

        // 1. Équipe A (PSG)
        PassEntity passEquipeA = new PassEntity();
        passEquipeA.setActif(true);
        passRepository.save(passEquipeA);

        EquipeEntity equipeA = new EquipeEntity();
        equipeA.setNom("PSG");
        equipeA.setPassEntity(passEquipeA);
        equipeRepository.save(equipeA);

        ParticipantEntity p1 = new ParticipantEntity();
        p1.setNom("Joueur1"); p1.setEmail("j1@psg.com"); p1.setTelephone("0600000001");
        p1.setEquipeEntity(equipeA);
        participantRepository.save(p1);

        ParticipantEntity p2 = new ParticipantEntity();
        p2.setNom("Joueur2"); p2.setEmail("j2@psg.com"); p2.setTelephone("0600000002");
        p2.setEquipeEntity(equipeA);
        participantRepository.save(p2);

        // 2. Équipe B (OM)
        PassEntity passEquipeB = new PassEntity();
        passEquipeB.setActif(true);
        passRepository.save(passEquipeB);

        EquipeEntity equipeB = new EquipeEntity();
        equipeB.setNom("OM");
        equipeB.setPassEntity(passEquipeB);
        equipeRepository.save(equipeB);

        ParticipantEntity p3 = new ParticipantEntity();
        p3.setNom("Joueur3"); p3.setEmail("j3@om.com"); p3.setTelephone("0600000003");
        p3.setEquipeEntity(equipeB);
        participantRepository.save(p3);

        ParticipantEntity p4 = new ParticipantEntity();
        p4.setNom("Joueur4"); p4.setEmail("j4@om.com"); p4.setTelephone("0600000004");
        p4.setEquipeEntity(equipeB);
        participantRepository.save(p4);

        // 3. Épreuve
        EpreuveEntity epreuve = new EpreuveEntity();
        epreuve.setNombreParticipantMax(10);
        epreuveRepository.save(epreuve);

        final AddParticipationRequest request = AddParticipationRequest
                .builder()
                .equipeA(equipeA.getNom())
                .equipeB(equipeB.getNom())
                .build();

        // WHEN / THEN
        webTestClient
                .post()
                .uri("/api/epreuve/{id}", epreuve.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated() // 201
                .expectBody(EpreuveParticipationResponse.class)
                .value(response -> {
                    assertThat(epreuveParticipationRepository.count()).isEqualTo(1);
                    assertThat(response.getEquipeA().getNom()).isEqualTo("PSG");
                    assertThat(response.getEquipeB().getNom()).isEqualTo("OM");
                    assertThat(response.getEpreuve().getId()).isEqualTo(epreuve.getId());
                });
    }

    @Test
    void addParticipation_noActivePass(){
        //Given
        ParticipantEntity p1 = new ParticipantEntity();
        ParticipantEntity p2 = new ParticipantEntity();
        ParticipantEntity p3 = new ParticipantEntity();
        ParticipantEntity p4 = new ParticipantEntity();

        participantRepository.save(p1);
        participantRepository.save(p2);
        participantRepository.save(p3);
        participantRepository.save(p4);

        EquipeEntity equipeA = new EquipeEntity();
        equipeA.setNom("PSG");
        equipeA.setParticipantEntities(List.of(p1, p2));

        PassEntity passEquipeA = new PassEntity();
        passEquipeA.setActif(false);
        passRepository.save(passEquipeA);

        equipeA.setPassEntity(passEquipeA);
        equipeRepository.save(equipeA);


        EquipeEntity equipeB = new EquipeEntity();
        equipeB.setNom("OM");
        equipeB.setParticipantEntities(List.of(p3, p4));


        PassEntity passEquipeB = new PassEntity();
        passEquipeB.setActif(false);
        passRepository.save(passEquipeB);

        equipeB.setPassEntity(passEquipeB);
        equipeRepository.save(equipeB);

        EpreuveEntity epreuve = new EpreuveEntity();
        epreuve.setNombreParticipantMax(10);
        epreuveRepository.save(epreuve);

        final AddParticipationRequest request = AddParticipationRequest
                .builder()
                .equipeA(equipeA.getNom())
                .equipeB(equipeB.getNom())
                .build();

        // When
        webTestClient
                .post()
                .uri("/api/epreuve/{id}", epreuve.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                //Then
                .expectStatus()
                .isEqualTo(422);
    }


    @Test
    void addParticipation_notFound() {
        //Given
        ParticipantEntity p1 = new ParticipantEntity();
        ParticipantEntity p2 = new ParticipantEntity();
        ParticipantEntity p3 = new ParticipantEntity();
        ParticipantEntity p4 = new ParticipantEntity();

        participantRepository.save(p1);
        participantRepository.save(p2);
        participantRepository.save(p3);
        participantRepository.save(p4);

        EquipeEntity equipeA = new EquipeEntity();
        equipeA.setNom("PSG");
        equipeA.setParticipantEntities(List.of(p1, p2));

        PassEntity passEquipeA = new PassEntity();
        passEquipeA.setActif(true);
        passRepository.save(passEquipeA);

        equipeA.setPassEntity(passEquipeA);
        equipeRepository.save(equipeA);


        EquipeEntity equipeB = new EquipeEntity();
        equipeB.setNom("OM");
        equipeB.setParticipantEntities(List.of(p3, p4));


        PassEntity passEquipeB = new PassEntity();
        passEquipeB.setActif(false);
        passRepository.save(passEquipeB);

        equipeB.setPassEntity(passEquipeB);
        equipeRepository.save(equipeB);

        EpreuveEntity epreuve = new EpreuveEntity();
        epreuve.setNombreParticipantMax(10);
        epreuveRepository.save(epreuve);

        final AddParticipationRequest request = AddParticipationRequest
                .builder()
                .equipeA(equipeA.getNom())
                .equipeB("CI")
                .build();

        // When
        webTestClient
                .post()
                //.uri("/api/epreuve/999")
                .uri("/api/epreuve/{id}", epreuve.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                //Then
                .expectStatus()
                .isEqualTo(404);
    }
}