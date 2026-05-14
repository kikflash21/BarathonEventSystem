package fr.uga.miage.l3.spring.tp3.exceptions.handlers;

import fr.uga.miage.l3.spring.tp3.exceptions.ConflitErrorResponse;
import fr.uga.miage.l3.spring.tp3.exceptions.ErrorResponse;
import fr.uga.miage.l3.spring.tp3.exceptions.SimpleErrorResponse;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.support.HttpRequestHandlerServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class BarathonExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.TOO_EARLY)
    public String handleMinorParticipantException(MinorParticipantRestException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(EpreuveNotFoundRestException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoTeamException(RuntimeException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidTeamRestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ConflitErrorResponse handleInvalidTeamException(HttpServletRequest req, InvalidTeamRestException ex) {
        return ConflitErrorResponse
                .builder()
                .uri(req.getRequestURI())
                .errorMessage("un clonflit exist dans une équipe, il faut revoir cette équipe pour commencer l'éventement")
                .errors(ex.getErrors())
                .build();
    }

    @ExceptionHandler(ParticipantBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParticipantBadRequest(ParticipantBadRequestException ex, HttpServletRequest request) {
        return SimpleErrorResponse.builder()
                .uri(request.getRequestURI())
                .errorMessage(ex.getMessage())
                .build();
    }

    @ExceptionHandler(ParticipantAlreadyExistsRestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleParticipantConflict(ParticipantAlreadyExistsRestException ex, HttpServletRequest request) {
        return SimpleErrorResponse.builder()
                .uri(request.getRequestURI())
                .errorMessage(ex.getMessage())
                .build();
    }

    @ExceptionHandler(TeamFullRestException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleTeamFull(TeamFullRestException ex, HttpServletRequest request) {
        return SimpleErrorResponse.builder()
                .uri(request.getRequestURI())
                .errorMessage(ex.getMessage())
                .build();
    }


    @ExceptionHandler({ChallengeNotFoundRestException.class, EquipeNotFoundRestException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleIncidentNotFound(RuntimeException ex, HttpServletRequest request) {
        return Map.of(
                "url", request.getRequestURI(),
                "error", ex.getMessage()
        );
    }

    // Gère le 409 : L'équipe n'est pas inscrite à ce challenge
    @ExceptionHandler(TeamNotRegisteredToChallengeRestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleTeamNotRegistered(TeamNotRegisteredToChallengeRestException ex, HttpServletRequest request) {
        return Map.of(
                "url", request.getRequestURI(),
                "error", ex.getMessage()
        );
    }

    // Gère le 422 : Une des équipes n'a pas de pass actif pour participer à l'épreuve
    @ExceptionHandler(NoActiveTeamsRestException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleNoActiveTeams(NoActiveTeamsRestException ex, HttpServletRequest request) {
        return SimpleErrorResponse.builder()
                .uri(request.getRequestURI())
                .errorMessage(ex.getMessage())
                .build();
    }

    //Handler pour BarathonEndpoint

    // Gère le 404 : Le bar ou le barathon n'existe pas
    @ExceptionHandler({BarathonNotFoundRestException.class, BarNotFoundRestException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleBarOrBarathonNotFound(RuntimeException ex, HttpServletRequest request) {
        return Map.of(
                "url", request.getRequestURI(),
                "error", ex.getMessage()
        );
    }

    // Gère le 422 : Le bar à supprimer a déjà une participation à une épreuve
    @ExceptionHandler(BarAlreadyHaveEpreuveRestException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, String> handleBarAlreadyHaveEpreuve(BarAlreadyHaveEpreuveRestException ex, HttpServletRequest request) {
        return Map.of(
                "url", request.getRequestURI(),
                "error", ex.getMessage()
        );
    }

}
