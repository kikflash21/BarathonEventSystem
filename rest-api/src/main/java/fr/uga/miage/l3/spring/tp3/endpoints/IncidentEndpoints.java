package fr.uga.miage.l3.spring.tp3.endpoints;


import fr.uga.miage.l3.spring.tp3.request.IncidentChallengeCreationRequest;
import fr.uga.miage.l3.spring.tp3.responses.ChallengeIncidentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/api/incidents")
public interface IncidentEndpoints {

    @Operation(summary = "Rapporter un incident sur un challenge")
    @ApiResponse(responseCode = "201", description = "L'incident a bien été créé, et 1 point leur a été enlevé de leur challenge.")
    @ApiResponse(responseCode = "404", description = "L'équipe ou le challenge sur lequel nous voulons associer un incident n'existe pas.")
    @ApiResponse(responseCode = "409", description = "L'équipe sur laquelle nous voulons associer un incident n'est pas inscrite à ce challenge.")
    @PutMapping("/challenge/{challengeId}")
    @ResponseStatus(HttpStatus.CREATED) // Code de retour 201 garanti par Spring
    ChallengeIncidentResponse reportIncident(
            @PathVariable Long challengeId,
            @RequestBody IncidentChallengeCreationRequest request
    );



    // TODO 🚧 28/03/2026
}
