package fr.uga.miage.l3.spring.tp3.models.domains;

import fr.uga.miage.l3.spring.tp3.enums.StatutParticipation;
import lombok.Data;

@Data
public class EpreuveParticipation {
    private Epreuve epreuve;
    private Equipe equipeA;
    private Equipe equipeB;
    private Equipe vainqueur;
    private StatutParticipation statut;
}
