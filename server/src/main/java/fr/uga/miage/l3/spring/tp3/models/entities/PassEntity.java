package fr.uga.miage.l3.spring.tp3.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String QRCode;
    private boolean actif;

    @OneToOne(mappedBy = "passEntity")
    private EquipeEntity equipeEntity;
}
