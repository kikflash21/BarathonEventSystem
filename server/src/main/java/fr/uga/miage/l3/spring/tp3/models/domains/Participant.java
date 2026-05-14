package fr.uga.miage.l3.spring.tp3.models.domains;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Participant {
    private Long id;
    private String nom;
    private String prenom;
    @Min(18)
    private Integer age;
    @Email
    private String email;
    @Size(max = 10)
    private String telephone;
    private LocalDate dateNaissance;
    private String pseudo;
    private boolean estSAM;
}
