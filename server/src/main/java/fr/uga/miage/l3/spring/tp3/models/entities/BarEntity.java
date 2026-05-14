package fr.uga.miage.l3.spring.tp3.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Collection;

@Entity
@Getter
@Setter
public class BarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    private String adresse;
    private Integer capacite;
    private LocalTime heureOuverture;
    private LocalTime heureFermeture;

    @ManyToMany
    private Collection<BarathonEntity> barathonEntities;

    @ManyToOne
    private EpreuveEntity epreuveEntity;
}