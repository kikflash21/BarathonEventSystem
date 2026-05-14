package fr.uga.miage.l3.spring.tp3.components;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.ChallengeNotFoundException;
import fr.uga.miage.l3.spring.tp3.models.entities.ChallengeEntity;
import fr.uga.miage.l3.spring.tp3.repositories.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChallengeComponent {

    private final ChallengeRepository challengeRepository;

    public ChallengeEntity findById(Long id) throws ChallengeNotFoundException {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException("Le challenge n'existe pas", id));
    }

    public ChallengeEntity save(ChallengeEntity challenge) {
        return challengeRepository.save(challenge);
    }
}