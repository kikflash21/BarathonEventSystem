package fr.uga.miage.l3.spring.tp3.mappers.entities;

import fr.uga.miage.l3.spring.tp3.models.entities.EquipeEntity;
import fr.uga.miage.l3.spring.tp3.models.entities.ParticipantEntity;
import fr.uga.miage.l3.spring.tp3.models.domains.Equipe;
import fr.uga.miage.l3.spring.tp3.models.domains.Participant;
import fr.uga.miage.l3.spring.tp3.request.ParticipantCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipeEntityMapper {

    @Mapping(target = "participants", source = "participantEntities")
    @Mapping(source = ".", target = "estActif", defaultExpression = "java(isActif(entity))")
    Equipe toEquipe(EquipeEntity entity);

    Participant toParticipant(ParticipantEntity participantEntity);

    ParticipantEntity toEntity(ParticipantCreationRequest request);

    default Boolean isActif(EquipeEntity entity) {
        return entity.getPassEntity().isActif();
    }
}