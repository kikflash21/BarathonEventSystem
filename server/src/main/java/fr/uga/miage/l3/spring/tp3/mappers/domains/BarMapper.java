package fr.uga.miage.l3.spring.tp3.mappers.domains;

import fr.uga.miage.l3.spring.tp3.models.entities.BarEntity;
import fr.uga.miage.l3.spring.tp3.responses.BarResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BarMapper {
    BarResponse toResponse(BarEntity barEntity);
}
