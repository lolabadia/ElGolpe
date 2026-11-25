package edu.upc.dsa.domain.repository;

import edu.upc.dsa.domain.models.Museo;
import edu.upc.dsa.util.Conexion;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Minimo para el issue #10. La lista de museos (GET /museos) y los vigilantes son del issue #9.
public class MuseoManagerImpl implements MuseoManager {
    final static Logger logger = Logger.getLogger(MuseoManagerImpl.class);
    private static MuseoManager instance;

    private MuseoManagerImpl() {
    }

    public static MuseoManager getInstance() {
        if (instance == null) instance = new MuseoManagerImpl();
        return instance;
    }

    @Override
    public Museo getMuseo(int id) {
        logger.info("getMuseo " + id);
        try (Connection c = Conexion.getConnection()) {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM museo WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Museo m = null;
            if (rs.next()) {
                m = new Museo(rs.getInt("id"), rs.getString("nombre"), rs.getInt("dificultad"), rs.getInt("ancho"),
                        rs.getInt("alto"), rs.getString("mapa"), rs.getInt("recompensa"), rs.getString("nombreObra"));
            }
            logger.info("getMuseo retorna " + m);
            return m;
        } catch (SQLException e) {
            logger.error("error de base de datos: " + e.getMessage());
            throw new RuntimeException("error de base de datos: " + e.getMessage());
        }
    }
}