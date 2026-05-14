package fr.uga.miage.l3.spring.tp3.models.domains;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Collection;

@Data
public class Equipe {
    private Long id;
    private String nom;
    private String couleur;
    private String slogan;
    @Size(min = 2, max = 10)
    private @Valid Collection<Participant> participants;
    private boolean estActif;
}
