package fr.uga.miage.l3.spring.tp3.components;


import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarAlreadyHaveEpreuveException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarNotFoundException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarathonNotFoundException;
import fr.uga.miage.l3.spring.tp3.models.entities.BarEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.BarathonEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.EpreuveEntity;
import fr.uga.miage.l3.spring.tp3.repositories.BarRepository;
import fr.uga.miage.l3.spring.tp3.repositories.BarathonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class BarathonComponent  {
    private final BarathonRepository barathonRepository;
    private final BarRepository barRepository;

    public BarEntity deleteBarFromBarathon(Long barathonId, Long barId) throws BarathonNotFoundException, BarNotFoundException, BarAlreadyHaveEpreuveException {

        //recuperation et verification de barathon
        BarathonEntity barathonEntity = barathonRepository.findById(barathonId).orElseThrow( () ->
                new BarathonNotFoundException(String.format("Le barathon [%s] n'existe pas", barathonId)));


        //recuperation et verification de bar
        BarEntity barEntity = barRepository.findById(barId).orElseThrow( () ->
                new BarNotFoundException(String.format("Le bar [%s] n'existe pas", barId)));

        //Verification de participation du bar à une epreuve
        EpreuveEntity epreuveBar =  barEntity.getEpreuveEntity();
        if (epreuveBar != null){
            throw new BarAlreadyHaveEpreuveException("Le bar à supprimer a déjà une participation à une épreuve");
        }

        Collection<BarEntity> bars = barathonEntity.getBarEntities();
        bars.removeIf(b -> b.getId().equals(barId));
        barathonRepository.save(barathonEntity);
        return barEntity;
    }

}
