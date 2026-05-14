package fr.uga.miage.l3.spring.tp3.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

public record ParticipantCreationRequest(
        @JsonProperty("nom") String nom,
        @JsonProperty("prenom") String prenom,

        @JsonProperty("age")
        @Min(value = 18, message = "Le participant doit être majeur")
        int age,

        @JsonProperty("email") String email,
        @JsonProperty("telephone") String telephone,
        @JsonProperty("dateNaissance") LocalDate dateNaissance,
        @JsonProperty("pseudo") String pseudo,
        @JsonProperty("estSAM") boolean estSAM
) {}