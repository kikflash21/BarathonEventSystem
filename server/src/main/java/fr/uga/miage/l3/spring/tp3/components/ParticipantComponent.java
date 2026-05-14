package fr.uga.miage.l3.spring.tp3.components;

import fr.uga.miage.l3.spring.tp3.models.entities.ParticipantEntity;
import fr.uga.miage.l3.spring.tp3.repositories.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class ParticipantComponent {
    private final ParticipantRepository participantRepository;

    public Collection<ParticipantEntity> getAllSamOfRedTeams(){
        return participantRepository.findAllByEstSAMFalseAndEquipeEntityPassEntityActifFalseAndEquipeEntityCouleur("rouge");

    }


    public boolean existsByEmail(String email) {
        return participantRepository.existsByEmail(email);
    }

    public boolean existsByTelephone(String telephone) {
        return participantRepository.existsByTelephone(telephone);
    }

    public ParticipantEntity save(ParticipantEntity participant) {
        return participantRepository.save(participant);
    }

}
