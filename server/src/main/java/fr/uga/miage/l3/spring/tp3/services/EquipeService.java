package fr.uga.miage.l3.spring.tp3.services;

import fr.uga.miage.l3.spring.tp3.components.EquipeComponent;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.InvalidTeamRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.NoActiveTeamsException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.NoActiveTeamsRestException;
import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.ParticipantEntity;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.MinorParticipantRestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EquipeService {
    private final EquipeComponent equipeComponent;

    public void checkAllTeamsAreValid() {
        Collection<EquipeEntity> teamsList = null;
        try {
            teamsList = this.equipeComponent.getAllTeamsWithPassActif();

            ArrayList<String> errors = new ArrayList<>();

            teamsList.forEach(equipe -> {
                if (equipe.getParticipantEntities()
                        .stream()
                        .anyMatch(participantEntity -> participantEntity.getAge() < 18)
                ) {
                    throw new MinorParticipantRestException(
                            String.format("Un mineur s'est glissé dans l'équipe %s, veuillez aller directement à la case prison.", equipe.getNom())
                    );
                }

                if (equipe.getParticipantEntities().size() > 10 || equipe.getParticipantEntities().size() < 2) {
                    errors.add(String.format(
                            "L'équipe %s contient %s participant(s) : nombre de participants attendu entre 2 et 10.",
                            equipe.getNom(),
                            equipe.getParticipantEntities().size()
                    ));
                }

                if (equipe.getParticipantEntities()
                        .stream()
                        .noneMatch(ParticipantEntity::isEstSAM)
                ) {
                    errors.add(String.format("L'équipe %s ne contient pas de SAM.", equipe.getNom()));
                }

            });

            if (!errors.isEmpty()) {
                throw new InvalidTeamRestException("Equipe(s) invalide(s).", errors);
            }
        } catch (NoActiveTeamsException e) {
            throw new NoActiveTeamsRestException("Aucune équipe n'est active pour le barathon, il ne peut donc pas avoir lieux",e);
        }
    }
}
