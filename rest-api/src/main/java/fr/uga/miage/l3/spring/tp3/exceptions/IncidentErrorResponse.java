package fr.uga.miage.l3.spring.tp3.exceptions;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IncidentErrorResponse {
    private String url;
    private String error;
}
