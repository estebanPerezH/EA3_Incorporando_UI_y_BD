package proyecto.edu.co.iud.servicio;

import java.sql.SQLException;
import proyecto.edu.co.iud.modelo.ResultadoConexion;

/**
 * Contrato de servicio para validar la configuración de una conexión JDBC.
 */
public interface IConexionBDable {

    /**
     * Intenta establecer y validar una conexión a la base de datos usando DriverManager.
     *
     * @param url        URL JDBC de conexión (por ejemplo: jdbc:sqlite:inventario.db).
     * @param usuario    Nombre de usuario de la base de datos (vacío si es SQLite).
     * @param contrasena Contraseña del usuario (vacía si es SQLite).
     * @return Resultado detallado de la prueba de conexión exitosa.
     * @throws ClassNotFoundException Si no se encuentra la clase del driver JDBC en el classpath.
     * @throws SQLException           Si ocurre un error de conexión, credenciales o URL inválida.
     */
    ResultadoConexion probarConexion(String url, String usuario, String contrasena)
            throws ClassNotFoundException, SQLException;
}

