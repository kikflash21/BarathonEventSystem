package fr.uga.miage.l3.spring.tp3.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class ParticipantSimpleResponse {
    private final String nom;
    private final String prenom;

    @JsonProperty("estSam")
    private final boolean estSAM;
}
