package fr.uga.miage.l3.spring.tp3.repositories;

import fr.uga.miage.l3.spring.tp3.models.entities.BarathonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarathonRepository extends JpaRepository<BarathonEntity, Long> {

}
