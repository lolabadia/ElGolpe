package edu.upc.dsa.domain.repository;

import edu.upc.dsa.domain.models.EntradaInventario;
import edu.upc.dsa.domain.models.Museo;
import edu.upc.dsa.domain.models.Partida;
import edu.upc.dsa.dto.RankingTO;
import edu.upc.dsa.exceptions.MuseoNoEncontrado;
import edu.upc.dsa.exceptions.ObjetosNoDisponibles;
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
import java.util.Map;

public class PartidaManagerImpl implements PartidaManager {
    final static Logger logger = Logger.getLogger(PartidaManagerImpl.class);
    private static PartidaManager instance;

    private PartidaManagerImpl() {
    }

    public static PartidaManager getInstance() {
        if (instance == null) instance = new PartidaManagerImpl();
        return instance;
    }

    @Override
    public Partida registrarPartida(int usuarioId, int museoId, boolean exito, int tiempoSegundos, Map<Integer, Integer> objetosUsados)
            throws UsuarioNoEncontrado, MuseoNoEncontrado, ObjetosNoDisponibles {
        logger.info("registrarPartida usuario " + usuarioId + ", museo " + museoId + ", exito " + exito
                + ", tiempo " + tiempoSegundos + ", objetos " + objetosUsados);
        if (tiempoSegundos < 0) {
            logger.error("tiempo invalido " + tiempoSegundos);
            throw new IllegalArgumentException("tiempo invalido");
        }

        // 1. validar todo antes de modificar nada
        if (UsuarioManagerImpl.getInstance().getUsuario(usuarioId) == null) {
            logger.error("no existe el usuario " + usuarioId);
            throw new UsuarioNoEncontrado();
        }
        Museo m = MuseoManagerImpl.getInstance().getMuseo(museoId);
        if (m == null) {
            logger.error("no existe el museo " + museoId);
            throw new MuseoNoEncontrado();
        }
        List<EntradaInventario> inventario = TiendaManagerImpl.getInstance().getInventario(usuarioId);
        for (Map.Entry<Integer, Integer> usado : objetosUsados.entrySet()) {
            int tiene = 0;
            for (EntradaInventario e : inventario) {
                if (e.getObjetoId() == usado.getKey()) tiene = e.getCantidad();
            }
            if (tiene < usado.getValue()) {
                logger.error("el usuario no tiene " + usado.getValue() + " del objeto " + usado.getKey() + " (tiene " + tiene + ")");
                throw new ObjetosNoDisponibles();
            }
        }

        // 2. modificar, todo en una transaccion
        int monedasGanadas = exito ? m.getRecompensa() : 0;
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try (Connection c = Conexion.getConnection()) {
            c.setAutoCommit(false);
            try {
                PreparedStatement ps = c.prepareStatement("UPDATE usuario SET monedas = monedas + ? WHERE id = ?");
                ps.setInt(1, monedasGanadas);
                ps.setInt(2, usuarioId);
                ps.executeUpdate();

                // los objetos se gastan aunque pierda
                for (Map.Entry<Integer, Integer> usado : objetosUsados.entrySet()) {
                    ps = c.prepareStatement("UPDATE entrada_inventario SET cantidad = cantidad - ? WHERE usuarioId = ? AND objetoId = ?");
                    ps.setInt(1, usado.getValue());
                    ps.setInt(2, usuarioId);
                    ps.setInt(3, usado.getKey());
                    ps.executeUpdate();
                }
                ps = c.prepareStatement("DELETE FROM entrada_inventario WHERE usuarioId = ? AND cantidad <= 0");
                ps.setInt(1, usuarioId);
                ps.executeUpdate();

                ps = c.prepareStatement(
                        "INSERT INTO partida (usuarioId, museoId, exito, tiempoSegundos, monedasGanadas, fecha) VALUES (?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, usuarioId);
                ps.setInt(2, museoId);
                ps.setBoolean(3, exito);
                ps.setInt(4, tiempoSegundos);
                ps.setInt(5, monedasGanadas);
                ps.setString(6, fecha);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                Partida p = new Partida(rs.getInt(1), usuarioId, museoId, exito, tiempoSegundos, monedasGanadas, fecha);

                c.commit();
                logger.info("partida guardada: " + p);
                return p;
            } catch (SQLException e) {
                c.rollback();
                logger.error("partida cancelada (rollback)");
                throw e;
            }
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    @Override
    public List<RankingTO> getRanking(int limite) {
        logger.info("getRanking limite " + limite);
        try (Connection c = Conexion.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "SELECT u.nombre, COUNT(DISTINCT p.museoId) AS museosCompletados, SUM(p.tiempoSegundos) AS tiempoTotal"
                            + " FROM partida p JOIN usuario u ON u.id = p.usuarioId"
                            + " WHERE p.exito = TRUE"
                            + " GROUP BY u.id, u.nombre"
                            + " ORDER BY museosCompletados DESC, tiempoTotal ASC, u.id ASC"
                            + " LIMIT ?");
            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            List<RankingTO> ranking = new ArrayList<>();
            int posicion = 1;
            while (rs.next()) {
                ranking.add(new RankingTO(posicion, rs.getString("nombre"), rs.getInt("museosCompletados"), rs.getInt("tiempoTotal")));
                posicion++;
            }
            return ranking;
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    @Override
    public int numPartidas() {
        try (Connection c = Conexion.getConnection()) {
            ResultSet rs = c.prepareStatement("SELECT COUNT(*) FROM partida").executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    @Override
    public void clear() {
        logger.info("clear partidas");
        try (Connection c = Conexion.getConnection()) {
            // la tabla museo no se toca
            c.prepareStatement("DELETE FROM partida").executeUpdate();
            c.prepareStatement("ALTER TABLE partida AUTO_INCREMENT = 1").executeUpdate();
        } catch (SQLException e) {
            throw errorBD(e);
        }
    }

    private RuntimeException errorBD(SQLException e) {
        logger.error("error de base de datos: " + e.getMessage());
        return new RuntimeException("error de base de datos: " + e.getMessage());
    }
}