package fr.uga.miage.l3.spring.tp3.endpoints;

import fr.uga.miage.l3.spring.tp3.exceptions.ConflitErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Équipe", description = "Équipe management")
@RequestMapping("/api/equipe")
public interface EquipeEndpoints {


    @Operation(description = "Vérifie que toutes les équipes inscrites sont conforme")
    @ApiResponse(responseCode = "200", description = "Toutes les équipe sont valide")
    @ApiResponse(responseCode = "425", description = "Un mineur est présent dans l'équipe", content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "409", description = "Une des vérifications n'est pas correct", content = @Content(schema = @Schema(implementation = ConflitErrorResponse.class)))
    @GetMapping("/checks")
    @ResponseStatus(HttpStatus.OK)
    void checkAllEquipesAreValid();
}
