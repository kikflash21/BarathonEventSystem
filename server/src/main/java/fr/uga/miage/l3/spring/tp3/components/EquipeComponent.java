package fr.uga.miage.l3.spring.tp3.components;

import fr.uga.miage.l3.spring.tp3.exceptions.technical.EquipeNotFoundException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.NoActiveTeamsException;
import fr.uga.miage.l3.spring.tp3.mappers.entities.EquipeEntityMapper;
import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.models.domains.Equipe;
import fr.uga.miage.l3.spring.tp3.repositories.EquipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class EquipeComponent {
    private final EquipeRepository equipeRepository;
    private final EquipeEntityMapper equipeEntityMapper;

    public Collection<EquipeEntity> getAllTeamsWithPassActif() throws NoActiveTeamsException {
        Collection<EquipeEntity> allTeamsWithActifPass = this.equipeRepository.findAllByPassEntityActifTrue();
        if (allTeamsWithActifPass.isEmpty()) throw new NoActiveTeamsException();
        return allTeamsWithActifPass;
    }

    public Equipe findActifTeamsByName(String name) throws EquipeNotFoundException {
        return equipeEntityMapper.toEquipe(
                equipeRepository.findByNom(name)
                .orElseThrow(() -> new EquipeNotFoundException("L'équipe [%s] n'existe pas"))
        );
    }

    public EquipeEntity findEntityByName(String name) throws EquipeNotFoundException {
        return equipeRepository.findByNom(name)
                .orElseThrow(() -> new EquipeNotFoundException(String.format("L'équipe [%s] n'existe pas", name)));
    }
}
