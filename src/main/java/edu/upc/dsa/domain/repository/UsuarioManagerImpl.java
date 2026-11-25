package edu.upc.dsa.domain.repository;

import edu.upc.dsa.domain.models.Usuario;
import edu.upc.dsa.exceptions.CredencialesIncorrectas;
import edu.upc.dsa.exceptions.UsuarioYaExiste;
import edu.upc.dsa.util.Conexion;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioManagerImpl implements UsuarioManager {
    final static Logger logger = Logger.getLogger(UsuarioManagerImpl.class);
    private static UsuarioManager instance;

    public static final int MONEDAS_INICIALES = 100;

    private UsuarioManagerImpl() {
    }

    public static UsuarioManager getInstance() {
        if (instance == null) instance = new UsuarioManagerImpl();
        return instance;
    }

    @Override
    public Usuario registrar(String nombre, String email, String password) throws UsuarioYaExiste {
        logger.info("registrar usuario " + nombre + ", email " + email);
        try (Connection c = Conexion.getConnection()) {
            if (buscarPorNombre(c, nombre) != null) {
                logger.error("ya existe un usuario con el nombre " + nombre);
                throw new UsuarioYaExiste();
            }
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO usuario (nombre, email, password, monedas) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setInt(4, MONEDAS_INICIALES);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            Usuario u = new Usuario(rs.getInt(1), nombre, email, password, MONEDAS_INICIALES);
            logger.info("usuario registrado: " + u);
            return u;
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    @Override
    public Usuario login(String nombre, String password) throws CredencialesIncorrectas {
        logger.info("login de " + nombre);
        try (Connection c = Conexion.getConnection()) {
            Usuario u = buscarPorNombre(c, nombre);
            if (u == null || !u.getPassword().equals(password)) {
                logger.error("login incorrecto de " + nombre);
                throw new CredencialesIncorrectas();
            }
            logger.info("login correcto, id " + u.getId());
            return u;
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    @Override
    public Usuario getUsuario(int id) {
        logger.info("getUsuario " + id);
        try (Connection c = Conexion.getConnection()) {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM usuario WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Usuario u = null;
            if (rs.next()) u = leerUsuario(rs);
            logger.info("getUsuario retorna " + u);
            return u;
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    @Override
    public int numUsuarios() {
        try (Connection c = Conexion.getConnection()) {
            ResultSet rs = c.prepareStatement("SELECT COUNT(*) FROM usuario").executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    @Override
    public void clear() {
        logger.info("clear usuarios");
        try (Connection c = Conexion.getConnection()) {
            // primero las tablas que dependen de usuario
            c.prepareStatement("DELETE FROM entrada_inventario").executeUpdate();
            c.prepareStatement("DELETE FROM compra").executeUpdate();
            c.prepareStatement("DELETE FROM partida").executeUpdate();
            c.prepareStatement("DELETE FROM usuario").executeUpdate();
            c.prepareStatement("ALTER TABLE usuario AUTO_INCREMENT = 1").executeUpdate();
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    private Usuario buscarPorNombre(Connection c, String nombre) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT * FROM usuario WHERE nombre = ?");
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return leerUsuario(rs);
        return null;
    }

    private Usuario leerUsuario(ResultSet rs) throws SQLException {
        return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("email"),
                rs.getString("password"), rs.getInt("monedas"));
    }

    private RuntimeException errorBD(SQLException e) {
        logger.error("error de base de datos: " + e.getMessage());
        return new RuntimeException("error de base de datos: " + e.getMessage());
    }
}
