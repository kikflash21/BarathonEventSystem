package fr.uga.miage.l3.spring.tp3.components;

import fr.uga.miage.l3.spring.tp3.models.entities.IncidentEntity;
import fr.uga.miage.l3.spring.tp3.repositories.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class IncidentComponent {
    private final IncidentRepository incidentRepository;

    public int getIncidentBetween17Octobre22HAnd23h59(){
        return incidentRepository.countDistinctByDateDeclarationBetween(
                LocalDateTime.of(2025,10,17,22,0),
                LocalDateTime.of(2025,10,17,23,59)
        );
    }

    public IncidentEntity save(IncidentEntity incidentEntity) {
        return incidentRepository.save(incidentEntity);
    }


}
