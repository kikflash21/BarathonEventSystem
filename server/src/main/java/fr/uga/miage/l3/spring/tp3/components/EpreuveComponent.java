package fr.uga.miage.l3.spring.tp3.components;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.EpreuveNotFoundException;
import fr.uga.miage.l3.spring.tp3.mappers.entities.EpreuveEntityMapper;
import fr.uga.miage.l3.spring.tp3.models.domains.Epreuve;
import fr.uga.miage.l3.spring.tp3.repositories.EpreuveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EpreuveComponent {
    private final EpreuveRepository epreuveRepository;
    private final EpreuveEntityMapper epreuveEntityMapper;

    public Epreuve findEpreuveById(Long id) throws EpreuveNotFoundException {
        return epreuveEntityMapper.toEpreuve(
                epreuveRepository
                        .findById(id)
                        .orElseThrow(() -> new EpreuveNotFoundException(String.format("L'épreuve [%s] n'existe pas", id))
                        )
        );
    }
}
