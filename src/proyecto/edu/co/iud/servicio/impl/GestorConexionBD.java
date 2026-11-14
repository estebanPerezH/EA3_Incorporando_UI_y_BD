package proyecto.edu.co.iud.servicio.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import proyecto.edu.co.iud.modelo.ResultadoConexion;
import proyecto.edu.co.iud.servicio.IConexionBDable;

/**
 * Encapsula la lógica JDBC para cargar el driver, establecer la conexión con DriverManager
 * y garantizar el cierre del recurso mediante try-with-resources.
 */
public class GestorConexionBD implements IConexionBDable {

    private static final int TIMEOUT_CONEXION_SEGUNDOS = 5;

    private static final Map<String, String> DRIVERS_POR_PREFIJO = Map.of(
            "jdbc:sqlite:", "org.sqlite.JDBC",
            "jdbc:mysql:", "com.mysql.cj.jdbc.Driver",
            "jdbc:mariadb:", "org.mariadb.jdbc.Driver",
            "jdbc:postgresql:", "org.postgresql.Driver",
            "jdbc:h2:", "org.h2.Driver",
            "jdbc:oracle:", "oracle.jdbc.OracleDriver",
            "jdbc:sqlserver:", "com.microsoft.sqlserver.jdbc.SQLServerDriver");

    @Override
    public ResultadoConexion probarConexion(String url, String usuario, String contrasena)
            throws ClassNotFoundException, SQLException {
        if (url == null || url.isBlank()) {
            throw new SQLException(
                    "La URL de conexión JDBC no puede estar vacía (ejemplo: jdbc:sqlite:inventario.db).");
        }

        String urlLimpia = url.trim();
        String usuarioLimpio = usuario == null ? "" : usuario.trim();
        String contrasenaSegura = contrasena == null ? "" : contrasena;

        String claseDriver = resolverClaseDriver(urlLimpia);
        if (claseDriver != null) {
            Class.forName(claseDriver);
        }

        DriverManager.setLoginTimeout(TIMEOUT_CONEXION_SEGUNDOS);

        try (Connection conexion = abrirConexion(urlLimpia, usuarioLimpio, contrasenaSegura)) {
            if (conexion == null || !verificarConexionActiva(conexion)) {
                throw new SQLException("El controlador no pudo validar una sesión activa con la base de datos.");
            }

            DatabaseMetaData metaDatos = conexion.getMetaData();
            String nombreMotor = metaDatos.getDatabaseProductName();
            String versionMotor = metaDatos.getDatabaseProductVersion();
            String nombreDriver = metaDatos.getDriverName();

            return new ResultadoConexion(
                    true,
                    "Conexión exitosa",
                    "La comunicación con la base de datos se estableció correctamente.",
                    nombreMotor,
                    versionMotor,
                    nombreDriver,
                    urlLimpia,
                    LocalDateTime.now());
        }
    }

    /**
     * Identifica el nombre cualificado del driver JDBC según el prefijo de la URL.
     *
     * @param url URL JDBC ingresada.
     * @return Nombre de la clase del driver conocido, o {@code null} si no aplica pre-carga explícita.
     */
    public String resolverClaseDriver(String url) {
        String urlMinuscula = url.toLowerCase(Locale.ROOT);
        for (Map.Entry<String, String> entrada : DRIVERS_POR_PREFIJO.entrySet()) {
            if (urlMinuscula.startsWith(entrada.getKey())) {
                return entrada.getValue();
            }
        }
        return null;
    }

    private Connection abrirConexion(String url, String usuario, String contrasena) throws SQLException {
        if (usuario.isEmpty() && contrasena.isEmpty()) {
            return DriverManager.getConnection(url);
        }
        return DriverManager.getConnection(url, usuario, contrasena);
    }

    private boolean verificarConexionActiva(Connection conexion) throws SQLException {
        try {
            return conexion.isValid(3);
        } catch (SQLFeatureNotSupportedException | AbstractMethodError ex) {
            return !conexion.isClosed();
        }
    }
}

