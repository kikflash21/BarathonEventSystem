package fr.uga.miage.l3.spring.tp3.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
@Getter
@Setter
@DiscriminatorValue("participant")
public class ParticipantEntity extends PersonneEntity {
    private String pseudo;
    private boolean estSAM;

    @ManyToOne
    private EquipeEntity equipeEntity;

    @ManyToMany(mappedBy = "participantEntities")
    private Collection<IncidentEntity> incidentEntities;
}
