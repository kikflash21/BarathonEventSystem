package fr.uga.miage.l3.spring.tp3.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@DiscriminatorColumn(name= "type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class PersonneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    private String prenom;
    private Integer age;
    @Column(unique = true)
    private String email;
    @Column(unique = true,length = 10)
    private String telephone;
    private LocalDate dateNaissance;
}
