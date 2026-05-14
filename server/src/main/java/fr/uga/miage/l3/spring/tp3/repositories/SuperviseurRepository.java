package fr.uga.miage.l3.spring.tp3.repositories;

import fr.uga.miage.l3.spring.tp3.enums.RoleSuperviseur;
import fr.uga.miage.l3.spring.tp3.enums.TypeEpreuve;
import fr.uga.miage.l3.spring.tp3.models.entities.SuperviseurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface SuperviseurRepository extends JpaRepository<SuperviseurEntity, Long> {

    Collection<SuperviseurEntity> findAllByRoleSuperviseurAndIncidentEntitiesEpreuveEntityType(RoleSuperviseur roleSuperviseur, TypeEpreuve typeEpreuve);

    @Query("select s as numberIncidents from  SuperviseurEntity s join s.incidentEntities i group by s.id order by count(i) ASC LIMIT 5")
    Collection<SuperviseurEntity> findTop5WithMostIncidents();

    Long id(Long id);
}
