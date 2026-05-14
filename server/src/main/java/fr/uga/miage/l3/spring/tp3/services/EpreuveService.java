package fr.uga.miage.l3.spring.tp3.services;

import fr.uga.miage.l3.spring.tp3.components.EpreuveComponent;
import fr.uga.miage.l3.spring.tp3.components.EquipeComponent;
import fr.uga.miage.l3.spring.tp3.components.ParticipationComponent;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.EpreuveNotFoundRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.EquipeNotFoundRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.NoActiveTeamsRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.EpreuveNotFoundException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.EquipeNotFoundException;
import fr.uga.miage.l3.spring.tp3.models.domains.Epreuve;
import fr.uga.miage.l3.spring.tp3.models.domains.EpreuveParticipation;
import fr.uga.miage.l3.spring.tp3.models.domains.Equipe;
import fr.uga.miage.l3.spring.tp3.request.AddParticipationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EpreuveService {
    private final EpreuveComponent epreuveComponent;
    private final EquipeComponent equipeComponent;
    private final ParticipationComponent participationComponent;

    @Transactional
    public EpreuveParticipation addParticipation(Long id, AddParticipationRequest request) {
        try {
            Epreuve epreuve = epreuveComponent.findEpreuveById(id);
            Equipe equipeA = equipeComponent.findActifTeamsByName(request.equipeA());
            Equipe equipeB = equipeComponent.findActifTeamsByName(request.equipeB());

            if(!equipeA.isEstActif() || !equipeB.isEstActif() ){
                throw new NoActiveTeamsRestException("Une des 2 équipes n'est pas active");
            }

            return participationComponent.createParticipation(epreuve, equipeA,equipeB);
        } catch (EpreuveNotFoundException e) {
            throw new EpreuveNotFoundRestException(e);
        } catch (EquipeNotFoundException e) {
            throw new EquipeNotFoundRestException(e);
        }
    }
}
