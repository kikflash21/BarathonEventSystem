package fr.uga.miage.l3.spring.tp3.endpoints;


import fr.uga.miage.l3.spring.tp3.responses.BarResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Bar", description = "bar management")
@RequestMapping("/api/barathon")
public interface BarathonEndpoints {
    // TODO 🚧 28/03/2026

    @Operation(description = "Permettre à un Bar de se désinscrire du Barathon")
    @ApiResponse(responseCode = "200", description = "Le bar a bien été supprimé de l'édition du Barathon")
    @ApiResponse(responseCode = "404", description = "Le Bar à supprimer n'existe pas")
    @ApiResponse(responseCode = "404", description = "Le Barathon ou l'on veut supprimer un bar associé n'existe pas")
    @ApiResponse(responseCode = "422", description = "Le bar à supprimer a déjà une participation à une épreuve")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{barathonId}/bars/{barId}")
    BarResponse deleteBar(@PathVariable Long barathonId, @PathVariable Long barId);

}
