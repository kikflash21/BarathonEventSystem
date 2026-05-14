package fr.uga.miage.l3.spring.tp3.models.entities;

import fr.uga.miage.l3.spring.tp3.enums.TypeEpreuve;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
@Getter
@Setter
public class EpreuveEntity {
      @Id
      @GeneratedValue(strategy = GenerationType.AUTO)
      private Long id;
      @Enumerated(EnumType.STRING)
      private TypeEpreuve type;
      private String nomPoste;
      @Column(nullable = false)
      private Integer nombreParticipantMax;
      private Integer points;

      @OneToMany(mappedBy = "epreuveEntity")
      private Collection<IncidentEntity> incidentEntities;

      @OneToMany(mappedBy = "epreuveEntity")
      private Collection<BarEntity> barEntities;

      @OneToMany(mappedBy = "epreuveEntity")
      private Collection<EpreuveParticipationEntity> epreuveParticipationEntities;

}
