package edu.upc.dsa.util;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Da conexiones a MariaDB con los datos de conf/db.properties.
 * Si hay un db.properties en el classpath (src/test/resources), se usa ese: asi los tests van a elgolpe_test.
 */
public class Conexion {
    final static Logger logger = Logger.getLogger(Conexion.class);
    public static final String FICHERO = "conf/db.properties";

    private static Properties props;

    public static Connection getConnection() throws SQLException {
        if (props == null) props = cargarPropiedades();
        return DriverManager.getConnection(props.getProperty("url"), props.getProperty("usuario"), props.getProperty("password"));
    }

    /**
     * Comprueba que la base de datos responde. Escribe un ERROR claro si no.
     */
    public static boolean comprobar() {
        try (Connection c = getConnection()) {
            logger.info("conectado a la base de datos " + props.getProperty("url"));
            return true;
        } catch (SQLException e) {
            logger.error("No se puede conectar a la base de datos: " + e.getMessage()
                    + ". Revisa que MariaDB este arrancado y los datos de " + FICHERO);
            return false;
        }
    }

    private static Properties cargarPropiedades() throws SQLException {
        Properties p = new Properties();
        InputStream in = Conexion.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            if (in == null) in = new FileInputStream(FICHERO);
            p.load(in);
            in.close();
        } catch (IOException e) {
            throw new SQLException("no se puede leer " + FICHERO + " (copia conf/db.properties.example a conf/db.properties)");
        }
        return p;
    }
}
