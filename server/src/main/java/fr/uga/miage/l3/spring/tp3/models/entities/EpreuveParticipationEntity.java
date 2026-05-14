package fr.uga.miage.l3.spring.tp3.models.entities;

import fr.uga.miage.l3.spring.tp3.enums.StatutParticipation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EpreuveParticipationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String preuveUrl;
    @Enumerated(EnumType.ORDINAL)
    private StatutParticipation statut;
    private LocalDateTime horodatage;
    @ManyToOne
    private EpreuveEntity epreuveEntity;
    @ManyToOne
    private EquipeEntity equipeAEntity;
    @ManyToOne
    private EquipeEntity equipeBEntity;
    @ManyToOne
    private EquipeEntity vainqueur;
  }