package edu.upc.dsa.services;

import edu.upc.dsa.domain.repository.PartidaManager;
import edu.upc.dsa.domain.repository.PartidaManagerImpl;
import edu.upc.dsa.dto.RankingTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/elgolpe/ranking", description = "Ranking de jugadores")
@Path("/elgolpe/ranking")
public class RankingService {

    private PartidaManager pm;

    public RankingService() {
        this.pm = PartidaManagerImpl.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Ranking por museos completados y tiempo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ranking", response = RankingTO.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "El limite tiene que estar entre 1 y 50")
    })
    public Response getRanking(@QueryParam("limite") @DefaultValue("10") int limite) {
        if (limite < 1 || limite > 50) {
            return Response.status(400).build();
        }
        List<RankingTO> ranking = pm.getRanking(limite);
        GenericEntity<List<RankingTO>> entity = new GenericEntity<List<RankingTO>>(ranking) {};
        return Response.status(200).entity(entity).build();
    }
}