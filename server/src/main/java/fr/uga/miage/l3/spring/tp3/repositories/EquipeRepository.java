package fr.uga.miage.l3.spring.tp3.repositories;

import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface EquipeRepository extends JpaRepository<EquipeEntity, Long> {

    @Query("select equi from EquipeEntity equi join EpreuveParticipationEntity ep on ep.vainqueur = equi join EpreuveEntity e on e.id = ep.epreuveEntity.id group by equi order by sum(e.points) DESC LIMIT 5")
    public Collection<EquipeEntity> findTop5OfEpreuve();

    Collection<EquipeEntity> findAllByPassEntityActifTrue();

    Optional<EquipeEntity> findByNom(String name);
}
