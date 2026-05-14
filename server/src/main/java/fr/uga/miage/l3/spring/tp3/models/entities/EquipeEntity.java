package fr.uga.miage.l3.spring.tp3.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
@Getter
@Setter
public class EquipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String nom;
    private String couleur;
    private String slogan;

    @ManyToOne
    private BarathonEntity barathonEntity;

    @OneToMany(mappedBy = "equipeEntity")
    @Size(min = 2, max = 10)
    private Collection<ParticipantEntity> participantEntities;

    @ManyToMany
    private Collection<ChallengeEntity> challengeEntities;

    @OneToOne
    private PassEntity passEntity;
}
