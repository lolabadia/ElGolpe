package edu.upc.dsa.domain.repository;

import edu.upc.dsa.domain.models.Compra;
import edu.upc.dsa.domain.models.EntradaInventario;
import edu.upc.dsa.domain.models.Objeto;
import edu.upc.dsa.domain.models.Usuario;
import edu.upc.dsa.exceptions.MonedasInsuficientes;
import edu.upc.dsa.exceptions.ObjetoNoEncontrado;
import edu.upc.dsa.exceptions.UsuarioNoEncontrado;
import edu.upc.dsa.util.Conexion;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TiendaManagerImpl implements TiendaManager {
    final static Logger logger = Logger.getLogger(TiendaManagerImpl.class);
    private static TiendaManager instance;

    private TiendaManagerImpl() {
    }

    public static TiendaManager getInstance() {
        if (instance == null) instance = new TiendaManagerImpl();
        return instance;
    }

    @Override
    public List<Objeto> getObjetos() {
        logger.info("getObjetos");
        try (Connection c = Conexion.getConnection()) {
            ResultSet rs = c.prepareStatement("SELECT * FROM objeto ORDER BY id").executeQuery();
            List<Objeto> lista = new ArrayList<>();
            while (rs.next()) lista.add(leerObjeto(rs));
            return lista;
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    @Override
    public Objeto getObjeto(int id) {
        logger.info("getObjeto " + id);
        try (Connection c = Conexion.getConnection()) {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM objeto WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return leerObjeto(rs);
            return null;
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    @Override
    public Compra comprar(int usuarioId, int objetoId, int cantidad) throws UsuarioNoEncontrado, ObjetoNoEncontrado, MonedasInsuficientes {
        logger.info("comprar usuario " + usuarioId + ", objeto " + objetoId + ", cantidad " + cantidad);
        if (cantidad <= 0) {
            logger.error("cantidad invalida " + cantidad);
            throw new IllegalArgumentException("cantidad invalida");
        }
        Usuario u = UsuarioManagerImpl.getInstance().getUsuario(usuarioId);
        if (u == null) {
            logger.error("no existe el usuario " + usuarioId);
            throw new UsuarioNoEncontrado();
        }
        Objeto o = getObjeto(objetoId);
        if (o == null) {
            logger.error("no existe el objeto " + objetoId);
            throw new ObjetoNoEncontrado();
        }
        int precioTotal = o.getPrecio() * cantidad;
        if (u.getMonedas() < precioTotal) {
            logger.error("monedas insuficientes: tiene " + u.getMonedas() + ", necesita " + precioTotal);
            throw new MonedasInsuficientes();
        }

        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try (Connection c = Conexion.getConnection()) {
            // las tres operaciones van juntas: o se hacen todas o ninguna
            c.setAutoCommit(false);
            try {
                PreparedStatement ps = c.prepareStatement("UPDATE usuario SET monedas = monedas - ? WHERE id = ?");
                ps.setInt(1, precioTotal);
                ps.setInt(2, usuarioId);
                ps.executeUpdate();

                ps = c.prepareStatement("UPDATE entrada_inventario SET cantidad = cantidad + ? WHERE usuarioId = ? AND objetoId = ?");
                ps.setInt(1, cantidad);
                ps.setInt(2, usuarioId);
                ps.setInt(3, objetoId);
                if (ps.executeUpdate() == 0) {
                    ps = c.prepareStatement("INSERT INTO entrada_inventario (usuarioId, objetoId, cantidad) VALUES (?, ?, ?)");
                    ps.setInt(1, usuarioId);
                    ps.setInt(2, objetoId);
                    ps.setInt(3, cantidad);
                    ps.executeUpdate();
                }

                ps = c.prepareStatement(
                        "INSERT INTO compra (usuarioId, objetoId, cantidad, precioTotal, fecha) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, usuarioId);
                ps.setInt(2, objetoId);
                ps.setInt(3, cantidad);
                ps.setInt(4, precioTotal);
                ps.setString(5, fecha);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                Compra compra = new Compra(rs.getInt(1), usuarioId, objetoId, cantidad, precioTotal, fecha);

                c.commit();
                logger.info("compra realizada: " + compra);
                return compra;
            } catch (SQLException e) {
                c.rollback();
                logger.error("compra cancelada (rollback)");
                throw e;
            }
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    @Override
    public List<EntradaInventario> getInventario(int usuarioId) throws UsuarioNoEncontrado {
        logger.info("getInventario " + usuarioId);
        if (UsuarioManagerImpl.getInstance().getUsuario(usuarioId) == null) {
            logger.error("no existe el usuario " + usuarioId);
            throw new UsuarioNoEncontrado();
        }
        try (Connection c = Conexion.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "SELECT * FROM entrada_inventario WHERE usuarioId = ? ORDER BY objetoId");
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            List<EntradaInventario> inventario = new ArrayList<>();
            while (rs.next()) {
                inventario.add(new EntradaInventario(rs.getInt("usuarioId"), rs.getInt("objetoId"), rs.getInt("cantidad")));
            }
            return inventario;
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    @Override
    public int numCompras() {
        try (Connection c = Conexion.getConnection()) {
            ResultSet rs = c.prepareStatement("SELECT COUNT(*) FROM compra").executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    @Override
    public void clear() {
        logger.info("clear tienda");
        try (Connection c = Conexion.getConnection()) {
            // el catalogo (tabla objeto) no se toca
            c.prepareStatement("DELETE FROM entrada_inventario").executeUpdate();
            c.prepareStatement("DELETE FROM compra").executeUpdate();
            c.prepareStatement("ALTER TABLE compra AUTO_INCREMENT = 1").executeUpdate();
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    private Objeto leerObjeto(ResultSet rs) throws SQLException {
        return new Objeto(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"),
                rs.getInt("precio"), rs.getString("efecto"));
    }

    private RuntimeException errorBD(SQLException e) {
        logger.error("error de base de datos: " + e.getMessage());
        return new RuntimeException("error de base de datos: " + e.getMessage());
    }
}
