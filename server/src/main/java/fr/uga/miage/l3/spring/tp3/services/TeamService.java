package fr.uga.miage.l3.spring.tp3.services;


import fr.uga.miage.l3.spring.tp3.components.EquipeComponent;
import fr.uga.miage.l3.spring.tp3.components.ParticipantComponent;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.EquipeNotFoundRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.ParticipantAlreadyExistsRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.ParticipantBadRequestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.TeamFullRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.EquipeNotFoundException;
import fr.uga.miage.l3.spring.tp3.mappers.domains.EquipeMapper;
import fr.uga.miage.l3.spring.tp3.mappers.entities.EquipeEntityMapper;
import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.ParticipantEntity;
import fr.uga.miage.l3.spring.tp3.request.ParticipantCreationRequest;
import fr.uga.miage.l3.spring.tp3.responses.EquipeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final EquipeComponent equipeComponent;
    private final ParticipantComponent participantComponent;
    private final EquipeEntityMapper entityMapper;
    private final EquipeMapper domainMapper;

    @Transactional
    public EquipeResponse addParticipantToTeam(String teamName, ParticipantCreationRequest request) {

        // ==========================================
        // 0. VALIDATION MANUELLE DE LA REQUÊTE (400)
        // ==========================================
        if (request.nom() == null || request.nom().trim().isEmpty()) {
            throw new ParticipantBadRequestException("Le nom est obligatoire.");
        }
        if (request.prenom() == null || request.prenom().trim().isEmpty()) {
            throw new ParticipantBadRequestException("Le prénom est obligatoire.");
        }
        if (request.age() < 18) {
            throw new ParticipantBadRequestException("Le participant doit être majeur (18 ans ou plus).");
        }
        // Vérification basique du format de l'email avec une expression régulière (Regex)
        if (request.email() == null || !request.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ParticipantBadRequestException("L'email est invalide ou manquant.");
        }
        // Vérification que le téléphone contient exactement 10 chiffres
        if (request.telephone() == null || !request.telephone().matches("\\d{10}")) {
            throw new ParticipantBadRequestException("Le téléphone doit contenir exactement 10 chiffres.");
        }
        if (request.dateNaissance() == null) {
            throw new ParticipantBadRequestException("La date de naissance est obligatoire.");
        }
        if (request.pseudo() == null || request.pseudo().trim().isEmpty()) {
            throw new ParticipantBadRequestException("Le pseudo est obligatoire.");
        }


        // ==========================================
        // 1. RECHERCHE DE L'ÉQUIPE (404)
        // ==========================================
        EquipeEntity equipe;
        try {
            equipe = equipeComponent.findEntityByName(teamName);
        } catch (EquipeNotFoundException e) {
            // On attrape l'erreur technique et on lance votre erreur REST !
            throw new EquipeNotFoundRestException(e);
        }


        // ==========================================
        // 2. RÈGLES MÉTIER DU BARATHON (422 & 409)
        // ==========================================
        if (equipe.getParticipantEntities().size() >= 10) {
            throw new TeamFullRestException(String.format("L'équipe %s est déjà complète.", teamName));
        }

        if (participantComponent.existsByEmail(request.email())) {
            throw new ParticipantAlreadyExistsRestException("L'email " + request.email() + " est déjà utilisé.");
        }
        if (participantComponent.existsByTelephone(request.telephone())) {
            throw new ParticipantAlreadyExistsRestException("Le téléphone " + request.telephone() + " est déjà utilisé.");
        }


        // ==========================================
        // 3. SAUVEGARDE ET MAPPING (201)
        // ==========================================
        ParticipantEntity newParticipant = entityMapper.toEntity(request);
        newParticipant.setEquipeEntity(equipe);

        // SUPPRIMEZ le bloc avec le System.currentTimeMillis() !!!

        participantComponent.save(newParticipant);

        if (equipe.getParticipantEntities() == null) {
            equipe.setParticipantEntities(new ArrayList<>());
        }
        equipe.getParticipantEntities().add(newParticipant);

        return domainMapper.toResponse(entityMapper.toEquipe(equipe));
    }
}
