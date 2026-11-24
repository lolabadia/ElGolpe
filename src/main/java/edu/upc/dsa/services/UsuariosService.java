package edu.upc.dsa.services;

import edu.upc.dsa.domain.models.Usuario;
import edu.upc.dsa.domain.repository.UsuarioManager;
import edu.upc.dsa.domain.repository.UsuarioManagerImpl;
import edu.upc.dsa.dto.LoginTO;
import edu.upc.dsa.dto.RegistroTO;
import edu.upc.dsa.dto.UsuarioTO;
import edu.upc.dsa.exceptions.CredencialesIncorrectas;
import edu.upc.dsa.exceptions.UsuarioYaExiste;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/elgolpe/usuarios", description = "Registre i login d'usuaris")
@Path("/elgolpe/usuarios")
public class UsuariosService {

    private UsuarioManager um;

    public UsuariosService() {
        this.um = UsuarioManagerImpl.getInstance();
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

    private boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }
}