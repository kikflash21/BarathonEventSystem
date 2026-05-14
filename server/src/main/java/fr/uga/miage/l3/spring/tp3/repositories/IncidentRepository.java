package fr.uga.miage.l3.spring.tp3.repositories;

import fr.uga.miage.l3.spring.tp3.models.entities.IncidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IncidentRepository extends JpaRepository<IncidentEntity, Long> {

    int countDistinctByDateDeclarationBetween(LocalDateTime dateDeclarationAfter, LocalDateTime dateDeclarationBefore);

}
