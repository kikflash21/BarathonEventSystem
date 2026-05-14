package fr.uga.miage.l3.spring.tp3.components;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.EpreuveNotFoundException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.EquipeNotFoundException;
import fr.uga.miage.l3.spring.tp3.mappers.entities.EpreuveEntityMapper;
import fr.uga.miage.l3.spring.tp3.models.entities.EpreuveEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.EpreuveParticipationEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.models.domains.Epreuve;
import fr.uga.miage.l3.spring.tp3.models.domains.EpreuveParticipation;
import fr.uga.miage.l3.spring.tp3.models.domains.Equipe;
import fr.uga.miage.l3.spring.tp3.repositories.EpreuveParticipationRepository;
import fr.uga.miage.l3.spring.tp3.repositories.EpreuveRepository;
import fr.uga.miage.l3.spring.tp3.repositories.EquipeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ParticipationComponent {
    private final EpreuveParticipationRepository epreuveParticipationRepository;
    private final EpreuveRepository epreuveRepository;
    private final EquipeRepository equipeRepository;
    private final EpreuveEntityMapper epreuveEntityMapper;


    public EpreuveParticipation createParticipation(Epreuve epreuve, @Valid Equipe equipeA,@Valid Equipe equipeB) throws EpreuveNotFoundException, EquipeNotFoundException {
        EpreuveEntity epreuveEntity = epreuveRepository.findById(epreuve.getId())
                .orElseThrow(() -> new EpreuveNotFoundException(String.format("L'épreuve [%s] n'existe pas", epreuve.getId())));
        EquipeEntity equipeAEntity = equipeRepository.findById(equipeA.getId()).orElseThrow(() -> new EquipeNotFoundException(String.format("L'équipe avec l'id [%s] n'existe pas", equipeA.getId())));
        EquipeEntity equipeBEntity = equipeRepository.findById(equipeB.getId()).orElseThrow(() -> new EquipeNotFoundException(String.format("L'équipe avec l'id [%s] n'existe pas", equipeB.getId())));

        EpreuveParticipationEntity epreuveParticipationEntity = EpreuveParticipationEntity.builder()
                .epreuveEntity(epreuveEntity)
                .equipeAEntity(equipeAEntity)
                .equipeBEntity(equipeBEntity)
                .horodatage(LocalDateTime.now())
                .build();

        return epreuveEntityMapper.toEpreuveParticipation(epreuveParticipationRepository.save(epreuveParticipationEntity));
    }
}
