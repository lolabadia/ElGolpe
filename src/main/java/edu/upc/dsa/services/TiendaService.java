package edu.upc.dsa.services;

import edu.upc.dsa.domain.models.Objeto;
import edu.upc.dsa.domain.repository.TiendaManager;
import edu.upc.dsa.domain.repository.TiendaManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/elgolpe/tienda", description = "Catalogo de la tienda")
@Path("/elgolpe/tienda")
public class TiendaService {

    private TiendaManager tm;

    public TiendaService() {
        this.tm = TiendaManagerImpl.getInstance();
    }

    @GET
    @Path("/objetos")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Listar los objetos de la tienda")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de objetos", response = Objeto.class, responseContainer = "List")
    })
    public Response getObjetos() {
        List<Objeto> objetos = tm.getObjetos();
        GenericEntity<List<Objeto>> entity = new GenericEntity<List<Objeto>>(objetos) {};
        return Response.status(200).entity(entity).build();
    }
}