package edu.upc.dsa.domain.repository;

import edu.upc.dsa.domain.models.Usuario;
import edu.upc.dsa.exceptions.CredencialesIncorrectas;
import edu.upc.dsa.exceptions.UsuarioYaExiste;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class UsuarioManagerImpl implements UsuarioManager {
    final static Logger logger = Logger.getLogger(UsuarioManagerImpl.class);
    private static UsuarioManager instance;

    public static final int MONEDAS_INICIALES = 100;

    private HashMap<Integer, Usuario> usuarios;
    private HashMap<String, Usuario> usuariosPorNombre;
    private int siguienteId;

    private UsuarioManagerImpl() {
        this.usuarios = new HashMap<>();
        this.usuariosPorNombre = new HashMap<>();
        this.siguienteId = 1;
    }

    public static UsuarioManager getInstance() {
        if (instance == null) instance = new UsuarioManagerImpl();
        return instance;
    }

    @Override
    public Usuario registrar(String nombre, String email, String password) throws UsuarioYaExiste {
        logger.info("registrar usuario " + nombre + ", email " + email);
        if (usuariosPorNombre.containsKey(nombre)) {
            logger.error("ya existe un usuario con el nombre " + nombre);
            throw new UsuarioYaExiste();
        }
        Usuario u = new Usuario(siguienteId, nombre, email, password, MONEDAS_INICIALES);
        siguienteId++;
        usuarios.put(u.getId(), u);
        usuariosPorNombre.put(nombre, u);
        logger.info("usuario registrado: " + u);
        return u;
    }

    @Override
    public Usuario login(String nombre, String password) throws CredencialesIncorrectas {
        logger.info("login de " + nombre);
        Usuario u = usuariosPorNombre.get(nombre);
        if (u == null || !u.getPassword().equals(password)) {
            logger.error("login incorrecto de " + nombre);
            throw new CredencialesIncorrectas();
        }
        logger.info("login correcto, id " + u.getId());
        return u;
    }

    @Override
    public Usuario getUsuario(int id) {
        logger.info("getUsuario " + id);
        Usuario u = usuarios.get(id);
        logger.info("getUsuario retorna " + u);
        return u;
    }

    @Override
    public int numUsuarios() {
        return usuarios.size();
    }

    @Override
    public void clear() {
        logger.info("clear usuarios");
        usuarios.clear();
        usuariosPorNombre.clear();
        siguienteId = 1;
    }
}