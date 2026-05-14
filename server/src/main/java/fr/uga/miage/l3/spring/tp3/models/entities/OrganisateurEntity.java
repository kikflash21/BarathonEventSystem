package fr.uga.miage.l3.spring.tp3.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("manager")
@Getter
@Setter
public class OrganisateurEntity extends PersonneEntity {
     private String organisation;
     private String cv;
}
