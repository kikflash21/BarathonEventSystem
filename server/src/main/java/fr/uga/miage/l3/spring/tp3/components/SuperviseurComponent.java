package fr.uga.miage.l3.spring.tp3.components;

import fr.uga.miage.l3.spring.tp3.enums.RoleSuperviseur;
import fr.uga.miage.l3.spring.tp3.enums.TypeEpreuve;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.SupervisorNotFoundException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.NotHaveIncidentBetweenException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.NotHaveSupervisorException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.NumberSupervisorException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.OverlapException;
import fr.uga.miage.l3.spring.tp3.models.entities.SuperviseurEntity;
import fr.uga.miage.l3.spring.tp3.repositories.IncidentRepository;
import fr.uga.miage.l3.spring.tp3.repositories.SuperviseurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SuperviseurComponent {
    private final SuperviseurRepository superviseurRepository;
    private final IncidentRepository incidentRepository;

    public Collection<SuperviseurEntity> getSecouristeToSuperviseFlechette() {
        return superviseurRepository.findAllByRoleSuperviseurAndIncidentEntitiesEpreuveEntityType(RoleSuperviseur.SECOURISTE, TypeEpreuve.FLECHETTES);
    }

    public Collection<SuperviseurEntity> getSecouristeToSuperviseBeerPong() {
        return superviseurRepository.findAllByRoleSuperviseurAndIncidentEntitiesEpreuveEntityType(RoleSuperviseur.SECOURISTE, TypeEpreuve.BEER_PONG);
    }

    public Collection<SuperviseurEntity> getTop5SupervisorsWithTheMostIncidents() throws NotHaveSupervisorException, NumberSupervisorException {
        List<SuperviseurEntity> all = superviseurRepository.findAll();
        if(all.isEmpty()) throw new NotHaveSupervisorException("Aucun superviseur n'existe");
        if(superviseurRepository.count() < 5) throw new NumberSupervisorException("Il faut au moins 5 superviseurs pour faire un top 5");
        return superviseurRepository.findTop5WithMostIncidents();
    }


    public boolean supervisorHaveIncidentBetween(Long idSupervisor, LocalDateTime start, LocalDateTime end) throws OverlapException, NotHaveIncidentBetweenException, SupervisorNotFoundException {
        if(end.isBefore(start)) throw new OverlapException("La date de fin ne peut pas être avant la date de début");
        if(incidentRepository.countDistinctByDateDeclarationBetween(start,end) == 0) throw new NotHaveIncidentBetweenException(start,end,"Aucun incident n'a été déclarer lors de cette période");
        SuperviseurEntity superviseurEntity = superviseurRepository.findById(idSupervisor)
                .orElseThrow(() -> new SupervisorNotFoundException(String.format("Le superviseur [%s] n'existe pas", idSupervisor), idSupervisor));
        return superviseurEntity.getIncidentEntities()
                .stream()
                .anyMatch(i -> i.getDateDeclaration().isBefore(end) && i.getDateDeclaration().isAfter(start));
    }
}
