package fr.uga.miage.l3.spring.tp3.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder

@EqualsAndHashCode(callSuper = true)
public class SimpleErrorResponse extends ErrorResponse {

}
