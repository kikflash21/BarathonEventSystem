package fr.uga.miage.l3.spring.tp3.mappers.domains;

import fr.uga.miage.l3.spring.tp3.models.domains.Epreuve;
import fr.uga.miage.l3.spring.tp3.models.domains.EpreuveParticipation;
import fr.uga.miage.l3.spring.tp3.responses.EpreuveParticipationResponse;
import fr.uga.miage.l3.spring.tp3.responses.EpreuveResponse;
import org.mapstruct.Mapper;

@Mapper(uses = {EquipeMapper.class})
public interface EpreuveMapper {

    EpreuveParticipationResponse toResponse(EpreuveParticipation epreuveParticipation);

    EpreuveResponse toResponse(Epreuve epreuve);
}
