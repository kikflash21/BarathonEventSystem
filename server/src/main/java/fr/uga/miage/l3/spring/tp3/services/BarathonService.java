package fr.uga.miage.l3.spring.tp3.services;

import fr.uga.miage.l3.spring.tp3.components.BarathonComponent;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.BarAlreadyHaveEpreuveRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.BarNotFoundRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.rest.BarathonNotFoundRestException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarAlreadyHaveEpreuveException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarNotFoundException;
import fr.uga.miage.l3.spring.tp3.exceptions.technical.BarathonNotFoundException;
import fr.uga.miage.l3.spring.tp3.mappers.domains.BarMapper;
import fr.uga.miage.l3.spring.tp3.responses.BarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarathonService {
    private final BarathonComponent barathonComponent;
    private final BarMapper barMapper;

    public BarResponse deleteBarFromBarathon(Long barathonId, Long barId){
        try {
            return barMapper.toResponse(barathonComponent.deleteBarFromBarathon(barathonId, barId));
        }
        catch (BarathonNotFoundException e){
            throw new BarathonNotFoundRestException(e);
        }
        catch (BarNotFoundException e){
            throw new BarNotFoundRestException(e);
        }
        catch (BarAlreadyHaveEpreuveException e){
            throw new BarAlreadyHaveEpreuveRestException(e);
        }


    }
}
