package fr.uga.miage.l3.spring.tp3.responses;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public final class ParticipantResponse {
    private Long id;
    private String nom;
    private String prenom;
    private Integer age;
    private String email;
    private String telephone;
    private LocalDate dateNaissance;
    private String pseudo;
    private boolean estSAM;
}
