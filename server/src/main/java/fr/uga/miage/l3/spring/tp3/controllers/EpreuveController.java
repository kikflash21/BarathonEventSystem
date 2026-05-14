package fr.uga.miage.l3.spring.tp3.controllers;

import fr.uga.miage.l3.spring.tp3.endpoints.EpreuveEndpoints;
import fr.uga.miage.l3.spring.tp3.mappers.domains.EpreuveMapper;
import fr.uga.miage.l3.spring.tp3.request.AddParticipationRequest;
import fr.uga.miage.l3.spring.tp3.responses.EpreuveParticipationResponse;
import fr.uga.miage.l3.spring.tp3.services.EpreuveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EpreuveController implements EpreuveEndpoints {
    private final EpreuveService epreuveService;
    private final EpreuveMapper epreuveMapper;


    @Override
    public EpreuveParticipationResponse addParticipation(Long id, AddParticipationRequest request) {
        return epreuveMapper.toResponse(epreuveService.addParticipation(id, request));
    }
}
