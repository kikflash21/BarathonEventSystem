package fr.uga.miage.l3.spring.tp3.endpoints;

import fr.uga.miage.l3.spring.tp3.request.AddParticipationRequest;
import fr.uga.miage.l3.spring.tp3.responses.EpreuveParticipationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Epreuve", description = "teams management")
@RequestMapping("/api/epreuve")
public interface EpreuveEndpoints {


    @Operation(description = "Ajout d'une participation à une épreuve de 2 équipes")
    @ApiResponse(responseCode = "201", description = "L'ajout de la participation à bien été effectuée")
    @ApiResponse(responseCode = "404", description = "Une des équipes, ou l'épreuve concernée n'a pas été trouver")
    @ApiResponse(responseCode = "422", description = "Une des 2 équipes n'a pas un pass actif")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}")
    EpreuveParticipationResponse addParticipation(@PathVariable Long id, @RequestBody AddParticipationRequest request);
}
