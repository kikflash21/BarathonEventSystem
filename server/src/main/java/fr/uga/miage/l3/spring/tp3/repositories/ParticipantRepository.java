package fr.uga.miage.l3.spring.tp3.repositories;

import fr.uga.miage.l3.spring.tp3.models.entities.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {
    Collection<ParticipantEntity> findAllByEstSAMFalseAndEquipeEntityPassEntityActifFalseAndEquipeEntityCouleur(String nom);

    boolean existsByEmail(String email);
    boolean existsByTelephone(String telephone);
}
