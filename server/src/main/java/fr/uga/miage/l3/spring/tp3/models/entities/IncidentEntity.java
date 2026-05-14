package fr.uga.miage.l3.spring.tp3.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Getter
@Setter
public class IncidentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime dateDeclaration;
    private String motif;

    @ManyToOne
    private ChallengeEntity challengeEntity;

    @ManyToOne
    private EpreuveEntity epreuveEntity;

    @ManyToMany
    private Collection<ParticipantEntity> participantEntities;
}