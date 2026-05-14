package fr.uga.miage.l3.spring.tp3.repositories;

import fr.uga.miage.l3.spring.tp3.models.entities.EpreuveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface EpreuveRepository extends JpaRepository<EpreuveEntity, Long> {

    Collection<EpreuveEntity> findAllByIncidentEntitiesEmpty();
}
