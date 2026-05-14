package fr.uga.miage.l3.spring.tp3.endpoints;


import fr.uga.miage.l3.spring.tp3.request.ParticipantCreationRequest;
import fr.uga.miage.l3.spring.tp3.responses.EquipeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Team", description = "Gestion des équipes")
@RequestMapping("/api/teams")
public interface TeamEndpoints {

    @Operation(description = "Ajouter un participant à une équipe")
    @ApiResponse(responseCode = "201", description = "Le participant a bien été créé et ajouté à l'équipe")
    @ApiResponse(responseCode = "400", description = "La requête d'entrée n'est pas conforme")
    @ApiResponse(responseCode = "404", description = "L'équipe n'existe pas")
    @ApiResponse(responseCode = "409", description = "Un participant avec cet email/téléphone existe déjà")
    @ApiResponse(responseCode = "422", description = "L'équipe est déjà complète")
    @PutMapping("/{teamsName}/participants")
    @ResponseStatus(HttpStatus.CREATED)
    EquipeResponse addParticipantToTeam(@PathVariable("teamsName") String teamsName, @RequestBody ParticipantCreationRequest request);
    // TODO 🚧 28/03/2026
}
