package fr.uga.miage.l3.spring.tp3.repositories;

import fr.uga.miage.l3.spring.tp3.models.entities.PassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassRepository extends JpaRepository<PassEntity, Long> {
}
