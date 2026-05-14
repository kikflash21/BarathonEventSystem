package fr.uga.miage.l3.spring.tp3.controllers;

import fr.uga.miage.l3.spring.tp3.endpoints.BarathonEndpoints;
import fr.uga.miage.l3.spring.tp3.responses.BarResponse;
import fr.uga.miage.l3.spring.tp3.services.BarathonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BarathonController implements BarathonEndpoints {
    private final BarathonService barathonService;

    @Override
    public BarResponse deleteBar(Long barathonId, Long barId){
        return barathonService.deleteBarFromBarathon(barathonId, barId);
    }
}
