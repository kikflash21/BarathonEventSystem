package fr.uga.miage.l3.spring.tp3.controllers;

import fr.uga.miage.l3.spring.tp3.endpoints.TeamEndpoints;
import fr.uga.miage.l3.spring.tp3.request.ParticipantCreationRequest;
import fr.uga.miage.l3.spring.tp3.responses.EquipeResponse;
import fr.uga.miage.l3.spring.tp3.services.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamController implements TeamEndpoints {

    private final TeamService teamService;

    @Override
    public EquipeResponse addParticipantToTeam(
            @PathVariable("teamsName") String teamsName,
            @Valid @RequestBody ParticipantCreationRequest request // @RequestBody est OBLIGATOIRE ICI
    ) {
        return teamService.addParticipantToTeam(teamsName, request);
    }
}