package edu.upc.dsa.services;

import edu.upc.dsa.domain.models.Compra;
import edu.upc.dsa.domain.models.EntradaInventario;
import edu.upc.dsa.domain.models.Objeto;
import edu.upc.dsa.domain.models.Usuario;
import edu.upc.dsa.domain.repository.TiendaManager;
import edu.upc.dsa.domain.repository.TiendaManagerImpl;
import edu.upc.dsa.domain.repository.UsuarioManager;
import edu.upc.dsa.domain.repository.UsuarioManagerImpl;
import edu.upc.dsa.dto.CompraRespuestaTO;
import edu.upc.dsa.dto.CompraTO;
import edu.upc.dsa.dto.InventarioTO;
import edu.upc.dsa.dto.LoginTO;
import edu.upc.dsa.dto.RegistroTO;
import edu.upc.dsa.dto.UsuarioTO;
import edu.upc.dsa.exceptions.CredencialesIncorrectas;
import edu.upc.dsa.exceptions.MonedasInsuficientes;
import edu.upc.dsa.exceptions.ObjetoNoEncontrado;
import edu.upc.dsa.exceptions.UsuarioNoEncontrado;
import edu.upc.dsa.exceptions.UsuarioYaExiste;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/elgolpe/usuarios", description = "Registre i login d'usuaris")
@Path("/elgolpe/usuarios")
public class UsuariosService {

    private UsuarioManager um;
    private TiendaManager tm;

    public UsuariosService() {
        this.um = UsuarioManagerImpl.getInstance();
        this.tm = TiendaManagerImpl.getInstance();
    }

    @POST
    @Path("/registro")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Crear una cuenta nueva")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario creado", response = UsuarioTO.class),
            @ApiResponse(code = 400, message = "Falta algun campo"),
            @ApiResponse(code = 409, message = "Ya existe un usuario con ese nombre")
    })
    public Response registro(RegistroTO r) {
        if (r == null || vacio(r.getNombre()) || vacio(r.getEmail()) || vacio(r.getPassword())) {
            return Response.status(400).build();
        }
        try {
            Usuario u = um.registrar(r.getNombre(), r.getEmail(), r.getPassword());
            return Response.status(201).entity(new UsuarioTO(u)).build();
        } catch (UsuarioYaExiste e) {
            return Response.status(409).build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Iniciar sesion")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login correcto", response = UsuarioTO.class),
            @ApiResponse(code = 400, message = "Falta el nombre o la password"),
            @ApiResponse(code = 401, message = "Nombre o password incorrectos")
    })
    public Response login(LoginTO l) {
        if (l == null || vacio(l.getNombre()) || vacio(l.getPassword())) {
            return Response.status(400).build();
        }
        try {
            Usuario u = um.login(l.getNombre(), l.getPassword());
            return Response.status(200).entity(new UsuarioTO(u)).build();
        } catch (CredencialesIncorrectas e) {
            return Response.status(401).build();
        }
    }

    @POST
    @Path("/{id}/compras")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Comprar un objeto de la tienda")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Compra realizada", response = CompraRespuestaTO.class),
            @ApiResponse(code = 400, message = "Falta el objetoId o la cantidad no es valida"),
            @ApiResponse(code = 402, message = "Monedas insuficientes"),
            @ApiResponse(code = 404, message = "No existe el usuario o el objeto")
    })
    public Response comprar(@PathParam("id") int id, CompraTO c) {
        if (c == null || c.getObjetoId() == null || c.getCantidad() <= 0) {
            return Response.status(400).build();
        }
        try {
            Compra compra = tm.comprar(id, c.getObjetoId(), c.getCantidad());
            int monedas = um.getUsuario(id).getMonedas();
            return Response.status(201).entity(new CompraRespuestaTO(compra, monedas)).build();
        } catch (UsuarioNoEncontrado | ObjetoNoEncontrado e) {
            return Response.status(404).build();
        } catch (MonedasInsuficientes e) {
            return Response.status(402).build();
        }
    }

    @GET
    @Path("/{id}/inventario")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Ver el inventario de un usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Inventario del usuario", response = InventarioTO.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "No existe el usuario")
    })
    public Response getInventario(@PathParam("id") int id) {
        try {
            List<InventarioTO> lista = new ArrayList<>();
            for (EntradaInventario e : tm.getInventario(id)) {
                Objeto o = tm.getObjeto(e.getObjetoId());
                lista.add(new InventarioTO(e.getObjetoId(), o.getNombre(), e.getCantidad()));
            }
            GenericEntity<List<InventarioTO>> entity = new GenericEntity<List<InventarioTO>>(lista) {};
            return Response.status(200).entity(entity).build();
        } catch (UsuarioNoEncontrado e) {
            return Response.status(404).build();
        }
    }

    private boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }
}