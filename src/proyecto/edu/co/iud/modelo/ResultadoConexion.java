package proyecto.edu.co.iud.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa el resultado inmutable de una prueba de conexión JDBC.
 */
public record ResultadoConexion(
        String motor,
        String versionMotor,
        String driverUtilizado,
        String urlEvaluada,
        LocalDateTime fechaHora) {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Formatea la información diagnóstica de la conexión para mostrarla en el JTextArea.
     *
     * @return Texto formateado con el resumen técnico de la validación.
     */
    public String formatearResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("Conexión exitosa").append(System.lineSeparator());
        sb.append("--------------------------------------------------").append(System.lineSeparator());
        sb.append("Detalle       : La comunicación con la base de datos se estableció correctamente.")
                .append(System.lineSeparator());
        sb.append("URL           : ").append(urlEvaluada).append(System.lineSeparator());
        if (motor != null && !motor.isBlank()) {
            sb.append("Motor BD      : ").append(motor);
            if (versionMotor != null && !versionMotor.isBlank()) {
                sb.append(" (").append(versionMotor).append(")");
            }
            sb.append(System.lineSeparator());
        }
        if (driverUtilizado != null && !driverUtilizado.isBlank()) {
            sb.append("Driver JDBC   : ").append(driverUtilizado).append(System.lineSeparator());
        }
        sb.append("Fecha/Hora    : ").append(fechaHora.format(FORMATO_FECHA)).append(System.lineSeparator());
        sb.append("Recurso BD    : Cerrado automáticamente mediante try-with-resources.");
        return sb.toString();
    }
}

