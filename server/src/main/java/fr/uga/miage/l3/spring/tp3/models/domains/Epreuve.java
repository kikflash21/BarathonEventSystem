package fr.uga.miage.l3.spring.tp3.models.domains;

import fr.uga.miage.l3.spring.tp3.enums.TypeEpreuve;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Epreuve {
    private Long id;
    private TypeEpreuve type;
    private String nomPoste;
    @NotNull
    private Integer nombreParticipantMax;
    private Integer points;
}
