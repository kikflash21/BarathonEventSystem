package fr.uga.miage.l3.spring.tp3.controllers;

import fr.uga.miage.l3.spring.tp3.endpoints.EquipeEndpoints;
import fr.uga.miage.l3.spring.tp3.services.EquipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EquipeController implements EquipeEndpoints {
    private final EquipeService equipeService;

    @Override
    public void checkAllEquipesAreValid() {
        this.equipeService.checkAllTeamsAreValid();
    }

}
