package fr.uga.miage.l3.spring.tp3.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class EpreuveParticipationResponse {
    private EpreuveResponse epreuve;
    private EquipeResponse equipeA;
    private EquipeResponse equipeB;
    private EquipeResponse vainqueur;
    private String statut;
}
