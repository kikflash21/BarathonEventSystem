package fr.uga.miage.l3.spring.tp3.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
@Getter
@Setter
public class ChallengeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String intitule;
    private String description;
    private Integer points;
    private String hashtag;


    @OneToMany(mappedBy = "challengeEntity")
    private Collection<IncidentEntity> incidentEntities;

    @ManyToMany(mappedBy = "challengeEntities")
    private Collection<EquipeEntity> equipeEntities;
}
