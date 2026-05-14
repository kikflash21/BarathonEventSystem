package fr.uga.miage.l3.spring.tp3.mappers.entities;

import fr.uga.miage.l3.spring.tp3.models.entities.EpreuveEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.EpreuveParticipationEntity;
import fr.uga.miage.l3.spring.tp3.models.domains.Epreuve;
import fr.uga.miage.l3.spring.tp3.models.domains.EpreuveParticipation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EpreuveEntityMapper {

    Epreuve toEpreuve(EpreuveEntity entity);


    @Mapping(source = "equipeAEntity", target = "equipeA")
    @Mapping(source = "equipeBEntity", target = "equipeB")
    @Mapping(source = "epreuveEntity", target = "epreuve")
    EpreuveParticipation toEpreuveParticipation(EpreuveParticipationEntity epreuveParticipationEntity);
}

