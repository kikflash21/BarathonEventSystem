package fr.uga.miage.l3.spring.tp3.mappers.domains;

import fr.uga.miage.l3.spring.tp3.models.entities.ChallengeEntity;
import fr.uga.miage.l3.spring.tp3.responses.ChallengeIncidentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChallengeMapper {

    @Mapping(target = "equipes", expression = "java(entity.getEquipeEntities() != null ? entity.getEquipeEntities().stream().map(e -> e.getNom()).toList() : java.util.Collections.emptyList())")
    ChallengeIncidentResponse toResponse(ChallengeEntity entity);

}