package fr.uga.miage.l3.spring.tp3.responses;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class EpreuveResponse {
    private Long id;
    private String type;
    private String nomPoste;
    @NotNull
    private Integer nombreParticipantMax;
    private Integer points;
}
