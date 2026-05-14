package fr.uga.miage.l3.spring.tp3.components;

import fr.uga.miage.l3.spring.tp3.enums.RoleSuperviseur;
import fr.uga.miage.l3.spring.tp3.enums.TypeEpreuve;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.*;
import fr.uga.miage.l3.spring.tp3.models.entities.EpreuveEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.SuperviseurEntity;
import fr.uga.miage.l3.spring.tp3.repositories.EpreuveRepository;
import fr.uga.miage.l3.spring.tp3.repositories.SuperviseurRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import fr.uga.miage.l3.spring.tp3.models.entities.IncidentEntity;
import fr.uga.miage.l3.spring.tp3.repositories.IncidentRepository;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class SuperviseurComponentTest {
    // DONE 🚧 26/03/2026

    @Autowired
    private SuperviseurComponent superviseurComponent;

    @Autowired
    private SuperviseurRepository superviseurRepository;

    @Autowired
    private EpreuveRepository epreuveRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @Transactional
    @Test
    void getSecouristeToSuperviseurFlechette(){

        // GIVEN
        EpreuveEntity epreuveEntity = new EpreuveEntity();
        epreuveEntity.setType(TypeEpreuve.FLECHETTES);
        epreuveEntity.setNombreParticipantMax(6);
        epreuveRepository.save(epreuveEntity);

        IncidentEntity incidentCible = new IncidentEntity();
        incidentCible.setEpreuveEntity(epreuveEntity);
        incidentRepository.save(incidentCible);

        IncidentEntity incidentBruit = new IncidentEntity();
        incidentBruit.setEpreuveEntity(epreuveEntity);
        incidentRepository.save(incidentBruit);

        SuperviseurEntity secouristeCible = new SuperviseurEntity();
        secouristeCible.setRoleSuperviseur(RoleSuperviseur.SECOURISTE);
        secouristeCible.setIncidentEntities(List.of(incidentCible));
        superviseurRepository.save(secouristeCible);

        SuperviseurEntity mediateurBruit = new SuperviseurEntity();
        mediateurBruit.setRoleSuperviseur(RoleSuperviseur.MEDIATEUR);
        mediateurBruit.setIncidentEntities(List.of(incidentBruit));
        superviseurRepository.save(mediateurBruit);


        // WHEN
        Collection<SuperviseurEntity> result = superviseurComponent.getSecouristeToSuperviseFlechette();


        // THEN
        SuperviseurEntity superviseur = result.iterator().next();
        assertEquals(1, result.size(), "La liste doit contenir exactement 1 superviseur");
        assertEquals(RoleSuperviseur.SECOURISTE, superviseur.getRoleSuperviseur(), "Le superviseur doit avoir le role de secouriste");
    }

    @Transactional
    @Test
    void getSecouristeToSuperviseurFlechette_NotFound(){

        //GIVEN
        EpreuveEntity epreuveEntity = new EpreuveEntity();
        epreuveEntity.setType(TypeEpreuve.FLECHETTES);
        epreuveEntity.setNombreParticipantMax(5);
        epreuveRepository.save(epreuveEntity);

        IncidentEntity incidentEntity = new IncidentEntity();
        incidentEntity.setEpreuveEntity(epreuveEntity);
        incidentRepository.save(incidentEntity);

        SuperviseurEntity superviseurEntity = new SuperviseurEntity();
        superviseurEntity.setRoleSuperviseur(RoleSuperviseur.MEDIATEUR);
        superviseurEntity.setIncidentEntities(List.of(incidentEntity));
        superviseurRepository.save(superviseurEntity);


        // THEN
        Collection<SuperviseurEntity> result = superviseurComponent.getSecouristeToSuperviseFlechette();

        // WHEN
        assertTrue(result.isEmpty(), "La collection devrait etre vide");
    }

    @Transactional
    @Test
    void getSecouristeToSuperviseBeerPong() {
        // --- GIVEN ---

        //  Création de l'épreuve cible (BEER PONG)
        EpreuveEntity epreuveBeerPong = new EpreuveEntity();
        epreuveBeerPong.setType(TypeEpreuve.BEER_PONG);
        epreuveBeerPong.setNombreParticipantMax(4);
        epreuveRepository.save(epreuveBeerPong);

        //  Création de l'incident pour le SECOURISTE
        IncidentEntity incidentCible = new IncidentEntity();
        incidentCible.setDateDeclaration(LocalDateTime.now());
        incidentCible.setEpreuveEntity(epreuveBeerPong);
        incidentRepository.save(incidentCible);

        //  Création de l'incident pour le MEDIATEUR
        IncidentEntity incidentBruit = new IncidentEntity();
        incidentBruit.setDateDeclaration(LocalDateTime.now());
        incidentBruit.setEpreuveEntity(epreuveBeerPong);
        incidentRepository.save(incidentBruit);

        //  Création du superviseur CIBLE (Secouriste lié à l'incident cible)
        SuperviseurEntity secouristeCible = new SuperviseurEntity();
        secouristeCible.setNom("Doe");
        secouristeCible.setPrenom("John");
        secouristeCible.setEmail("john.doe@test.com");
        secouristeCible.setTelephone("0600000001");
        secouristeCible.setNumeroUrgence("0700000001");
        secouristeCible.setRoleSuperviseur(RoleSuperviseur.SECOURISTE);
        secouristeCible.setIncidentEntities(List.of(incidentCible)); // On lie l'incident 1
        superviseurRepository.save(secouristeCible);

        //  Un médiateur sur l'incident bruit
        SuperviseurEntity mediateurBruit = new SuperviseurEntity();
        mediateurBruit.setNom("Smith");
        mediateurBruit.setPrenom("Jane");
        mediateurBruit.setEmail("jane.smith@test.com");
        mediateurBruit.setTelephone("0600000002");
        mediateurBruit.setNumeroUrgence("0700000002");
        mediateurBruit.setRoleSuperviseur(RoleSuperviseur.MEDIATEUR);
        mediateurBruit.setIncidentEntities(List.of(incidentBruit)); // On lie l'incident 2
        superviseurRepository.save(mediateurBruit);

        // --- WHEN ---
        Collection<SuperviseurEntity> result = superviseurComponent.getSecouristeToSuperviseBeerPong();

        // --- THEN ---
        assertEquals(1, result.size(), "La liste doit contenir exactement 1 superviseur");

        SuperviseurEntity superviseurTrouve = result.iterator().next();
        // On vérifie avec l'email car l'ID n'est plus prévisible
        assertEquals("john.doe@test.com", superviseurTrouve.getEmail(), "L'email du superviseur trouvé doit être celui du secouriste cible");
        assertEquals(RoleSuperviseur.SECOURISTE, superviseurTrouve.getRoleSuperviseur(), "Le rôle doit bien être SECOURISTE");

    }

    @Transactional
    @Test
    void getSecouristeToSuperviseBeerPong_NotFound() {
        // --- GIVEN ---
        // On crée une configuration où AUCUN secouriste ne supervise un Beer Pong.

        //  Une épreuve de BEER PONG, mais supervisée par un MÉDIATEUR
        EpreuveEntity epreuveBeerPong = new EpreuveEntity();
        epreuveBeerPong.setType(TypeEpreuve.BEER_PONG);
        epreuveBeerPong.setNombreParticipantMax(4);
        epreuveRepository.save(epreuveBeerPong);

        IncidentEntity incidentBeerPong = new IncidentEntity();
        incidentBeerPong.setDateDeclaration(LocalDateTime.now());
        incidentBeerPong.setEpreuveEntity(epreuveBeerPong);
        incidentRepository.save(incidentBeerPong);

        SuperviseurEntity mediateur = new SuperviseurEntity();
        mediateur.setNom("Med");
        mediateur.setPrenom("Iateur");
        mediateur.setEmail("mediateur.notfound@test.com"); // Emails uniques !
        mediateur.setTelephone("0600000033");
        mediateur.setNumeroUrgence("0700000033");
        mediateur.setRoleSuperviseur(RoleSuperviseur.MEDIATEUR);
        mediateur.setIncidentEntities(List.of(incidentBeerPong));
        superviseurRepository.save(mediateur);

        //  Un SECOURISTE, mais qui supervise une épreuve de FLÉCHETTES
        EpreuveEntity epreuveFlechettes = new EpreuveEntity();
        epreuveFlechettes.setType(TypeEpreuve.FLECHETTES);
        epreuveFlechettes.setNombreParticipantMax(4);
        epreuveRepository.save(epreuveFlechettes);

        IncidentEntity incidentFlechettes = new IncidentEntity();
        incidentFlechettes.setDateDeclaration(LocalDateTime.now());
        incidentFlechettes.setEpreuveEntity(epreuveFlechettes);
        incidentRepository.save(incidentFlechettes);

        SuperviseurEntity secouriste = new SuperviseurEntity();
        secouriste.setNom("Sec");
        secouriste.setPrenom("Ouriste");
        secouriste.setEmail("secouriste.notfound@test.com"); // Emails uniques !
        secouriste.setTelephone("0600000044");
        secouriste.setNumeroUrgence("0700000044");
        secouriste.setRoleSuperviseur(RoleSuperviseur.SECOURISTE);
        secouriste.setIncidentEntities(List.of(incidentFlechettes));
        superviseurRepository.save(secouriste);

        // --- WHEN ---
        Collection<SuperviseurEntity> result = superviseurComponent.getSecouristeToSuperviseBeerPong();

        // --- THEN ---
        // On vérifie que la liste est strictement vide
        assertTrue(result.isEmpty(), "La collection devrait être vide car aucun secouriste ne supervise un Beer Pong");
    }

    @Transactional
    @Test
    void supervisorHaveIncidentBetween_overlapException(){

        // GIVEN
        LocalDateTime debut = LocalDateTime.of(2026, 03, 29, 12, 00, 00);
        LocalDateTime fin = LocalDateTime.of(2026, 03, 28, 12, 00, 00);

        // WHEN - THEN
        assertThrows(OverlapException.class, ()->superviseurComponent.supervisorHaveIncidentBetween(1L, debut, fin));
    }

    @Transactional
    @Test
    void supervisorHaveIncidentBetween_notHaveIncidentException(){
        // Given
        LocalDateTime debut = LocalDateTime.of(2026, 03, 28, 12, 00, 00);
        LocalDateTime fin = LocalDateTime.of(2026, 03, 29, 12, 00, 00);

        // WHEN - THEN
        assertThrows(NotHaveIncidentBetweenException.class, ()->superviseurComponent.supervisorHaveIncidentBetween(1L, debut, fin));
    }

    @Transactional
    @Test
    void supervisorHaveIncidentBetween_supervisorNotFoundException(){
        // Given
        IncidentEntity incident = new IncidentEntity();
        incident.setDateDeclaration(LocalDateTime.of(2026, 03, 28, 15, 00, 00));
        incidentRepository.save(incident);

        LocalDateTime debut = LocalDateTime.of(2026, 03, 28, 12, 00, 00);
        LocalDateTime fin = LocalDateTime.of(2026, 03, 29, 12, 00, 00);

        // When - Then
        // On cherche un ID qui n'existe pas pour lever l'exception
        assertThrows(SupervisorNotFoundException.class, ()->superviseurComponent.supervisorHaveIncidentBetween(999L, debut, fin));
    }

    @Transactional
    @Test
    void supervisorHaveIncidentBetween_true() throws OverlapException, NotHaveIncidentBetweenException, SupervisorNotFoundException
    {
        // Given
        IncidentEntity incident = new IncidentEntity();
        incident.setDateDeclaration(LocalDateTime.of(2026, 03, 28, 15, 00, 00));
        incidentRepository.save(incident);

        SuperviseurEntity superviseur = new SuperviseurEntity();
        superviseur.setIncidentEntities(List.of(incident));
        superviseurRepository.save(superviseur);

        LocalDateTime debut = LocalDateTime.of(2026, 03, 28, 12, 00, 00);
        LocalDateTime fin = LocalDateTime.of(2026, 03, 29, 12, 00, 00);

        // When
        boolean result = superviseurComponent.supervisorHaveIncidentBetween(superviseur.getId(), debut, fin);

        // Then
        assertTrue(result);
    }

    @Transactional
    @Test
    void supervisorHaveIncidentBetween_false() throws OverlapException, NotHaveIncidentBetweenException, SupervisorNotFoundException
    {
        // Given
        IncidentEntity incidentCible = new IncidentEntity();
        incidentCible.setDateDeclaration(LocalDateTime.of(2026, 03, 30, 12, 00, 00));
        incidentRepository.save(incidentCible);

        IncidentEntity incidentBruit = new IncidentEntity();
        incidentBruit.setDateDeclaration(LocalDateTime.of(2026, 03, 29, 11, 00, 00));
        incidentRepository.save(incidentBruit);

        SuperviseurEntity superviseur = new SuperviseurEntity();
        superviseur.setIncidentEntities(List.of(incidentCible));
        superviseurRepository.save(superviseur);

        LocalDateTime debut = LocalDateTime.of(2026, 03, 28, 12, 00, 00);
        LocalDateTime fin = LocalDateTime.of(2026, 03, 29, 12, 00, 00);

        // When
        boolean result = superviseurComponent.supervisorHaveIncidentBetween(superviseur.getId(), debut, fin);

        // Then
        assertFalse(result);
    }




    @Transactional
    @Test
    void getTop5SupervisorsWithTheMostIncidents_ThrowsNotHaveSupervisorException() {
        // GIVEN
        // La base de données est vide au début de l'exécution du test

        // WHEN / THEN
        assertThrows(NotHaveSupervisorException.class, () -> superviseurComponent.getTop5SupervisorsWithTheMostIncidents());
    }

    @Transactional
    @Test
    void getTop5SupervisorsWithTheMostIncidents_ThrowsNumberSupervisorException() {
        // GIVEN
        // On crée seulement 3 superviseurs (il en faut au moins 5).
        // Pas besoin d'incidents ici, car l'exception "count() < 5" se déclenche en premier !
        SuperviseurEntity sup1 = new SuperviseurEntity();
        sup1.setEmail("sup1@test.com");
        superviseurRepository.save(sup1);

        SuperviseurEntity sup2 = new SuperviseurEntity();
        sup2.setEmail("sup2@test.com");
        superviseurRepository.save(sup2);

        SuperviseurEntity sup3 = new SuperviseurEntity();
        sup3.setEmail("sup3@test.com");
        superviseurRepository.save(sup3);

        // WHEN / THEN
        assertThrows(NumberSupervisorException.class, () -> superviseurComponent.getTop5SupervisorsWithTheMostIncidents());
    }

    @Transactional
    @Test
    void getTop5SupervisorsWithTheMostIncidents_Success() throws NotHaveSupervisorException, NumberSupervisorException {
        // GIVEN
        // On crée 6 superviseurs et on leur donne à CHACUN un incident unique
        // pour que le INNER JOIN de la requête SQL fonctionne.

        // --- Superviseur 1 ---
        IncidentEntity inc1 = new IncidentEntity();
        inc1.setDateDeclaration(LocalDateTime.now());
        incidentRepository.save(inc1);

        SuperviseurEntity sup1 = new SuperviseurEntity();
        sup1.setNom("Nom1");
        sup1.setEmail("email1@test.com");
        sup1.setTelephone("0610000001");
        sup1.setNumeroUrgence("0710000001");
        sup1.setIncidentEntities(List.of(inc1));
        superviseurRepository.save(sup1);

        // --- Superviseur 2 ---
        IncidentEntity inc2 = new IncidentEntity();
        inc2.setDateDeclaration(LocalDateTime.now());
        incidentRepository.save(inc2);

        SuperviseurEntity sup2 = new SuperviseurEntity();
        sup2.setNom("Nom2");
        sup2.setEmail("email2@test.com");
        sup2.setTelephone("0610000002");
        sup2.setNumeroUrgence("0710000002");
        sup2.setIncidentEntities(List.of(inc2));
        superviseurRepository.save(sup2);

        // --- Superviseur 3 ---
        IncidentEntity inc3 = new IncidentEntity();
        inc3.setDateDeclaration(LocalDateTime.now());
        incidentRepository.save(inc3);

        SuperviseurEntity sup3 = new SuperviseurEntity();
        sup3.setNom("Nom3");
        sup3.setEmail("email3@test.com");
        sup3.setTelephone("0610000003");
        sup3.setNumeroUrgence("0710000003");
        sup3.setIncidentEntities(List.of(inc3));
        superviseurRepository.save(sup3);

        // --- Superviseur 4 ---
        IncidentEntity inc4 = new IncidentEntity();
        inc4.setDateDeclaration(LocalDateTime.now());
        incidentRepository.save(inc4);

        SuperviseurEntity sup4 = new SuperviseurEntity();
        sup4.setNom("Nom4");
        sup4.setEmail("email4@test.com");
        sup4.setTelephone("0610000004");
        sup4.setNumeroUrgence("0710000004");
        sup4.setIncidentEntities(List.of(inc4));
        superviseurRepository.save(sup4);

        // --- Superviseur 5 ---
        IncidentEntity inc5 = new IncidentEntity();
        inc5.setDateDeclaration(LocalDateTime.now());
        incidentRepository.save(inc5);

        SuperviseurEntity sup5 = new SuperviseurEntity();
        sup5.setNom("Nom5");
        sup5.setEmail("email5@test.com");
        sup5.setTelephone("0610000005");
        sup5.setNumeroUrgence("0710000005");
        sup5.setIncidentEntities(List.of(inc5));
        superviseurRepository.save(sup5);

        // --- Superviseur 6 ---
        IncidentEntity inc6 = new IncidentEntity();
        inc6.setDateDeclaration(LocalDateTime.now());
        incidentRepository.save(inc6);

        SuperviseurEntity sup6 = new SuperviseurEntity();
        sup6.setNom("Nom6");
        sup6.setEmail("email6@test.com");
        sup6.setTelephone("0610000006");
        sup6.setNumeroUrgence("0710000006");
        sup6.setIncidentEntities(List.of(inc6));
        superviseurRepository.save(sup6);

        // WHEN
        Collection<SuperviseurEntity> result = superviseurComponent.getTop5SupervisorsWithTheMostIncidents();

        // THEN
        assertEquals(5, result.size(), "La liste doit contenir exactement 5 superviseurs");
    }

}