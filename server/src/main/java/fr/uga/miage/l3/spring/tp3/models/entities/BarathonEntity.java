package fr.uga.miage.l3.spring.tp3.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collection;

@Entity
@Getter
@Setter
public class BarathonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ville;
    private Integer edition;
    private String theme;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    @OneToMany(mappedBy = "barathonEntity")
    private Collection<EquipeEntity> equipeEntities;

    @ManyToMany(mappedBy = "barathonEntities")
    private Collection<BarEntity> barEntities;

    @ManyToMany
    private Collection<OrganisateurEntity> organisateurEntities;
}
