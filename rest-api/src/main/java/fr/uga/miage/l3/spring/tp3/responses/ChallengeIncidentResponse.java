package fr.uga.miage.l3.spring.tp3.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeIncidentResponse {
    private String intitule;
    private String description;
    private Integer points;
    private String hashtag;
    private List<String> equipes;
}
