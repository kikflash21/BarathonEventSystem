package fr.uga.miage.l3.spring.tp3.models.entities;


import fr.uga.miage.l3.spring.tp3.enums.RoleSuperviseur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
@Getter
@Setter
@DiscriminatorValue("supervisor")
public class SuperviseurEntity extends PersonneEntity {
    @Column(unique = true, length = 10)
    private String numeroUrgence;
    @Enumerated(EnumType.STRING)
    private RoleSuperviseur roleSuperviseur ;

    @OneToMany
    @JoinColumn(name = "superviseur_id")
    private Collection<IncidentEntity> incidentEntities;
}
