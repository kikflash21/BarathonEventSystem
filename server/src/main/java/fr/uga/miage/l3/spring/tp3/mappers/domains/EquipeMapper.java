package fr.uga.miage.l3.spring.tp3.mappers.domains;

import fr.uga.miage.l3.spring.tp3.models.domains.Equipe;
import fr.uga.miage.l3.spring.tp3.models.entities.ParticipantEntity;
import fr.uga.miage.l3.spring.tp3.responses.EquipeResponse;
import fr.uga.miage.l3.spring.tp3.responses.ParticipantSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EquipeMapper {



    EquipeResponse toResponse(Equipe equipe);

    ParticipantSimpleResponse toParticipantSimple(ParticipantEntity participant);
}
