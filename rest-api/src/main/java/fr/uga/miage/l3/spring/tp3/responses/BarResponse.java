package fr.uga.miage.l3.spring.tp3.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class BarResponse {
    private Long id;
    private String nom;
    private String adresse;
    private Integer capacite;
    private LocalTime heureOuverture;
    private LocalTime heureFermeture;
}
