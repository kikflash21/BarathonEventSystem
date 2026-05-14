package fr.uga.miage.l3.spring.tp3.responses;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class EquipeResponse {
    private Long id;
    private String nom;
    private String couleur;
    private String slogan;

    @Builder.Default
    private Collection<ParticipantSimpleResponse> participants = new ArrayList<>();
    private boolean estActif;
}
