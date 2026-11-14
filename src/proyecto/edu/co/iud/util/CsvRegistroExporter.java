package proyecto.edu.co.iud.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import proyecto.edu.co.iud.modelo.ResultadoConexion;

/**
 * Utilidad para exportar el historial de pruebas de conexión a formato CSV (UTF-8 con BOM).
 */
public final class CsvRegistroExporter {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private CsvRegistroExporter() {
    }

    /**
     * Exporta una lista de resultados de prueba de conexión a un archivo CSV separado por punto y coma.
     *
     * @param archivo    Archivo CSV de destino.
     * @param resultados Lista de resultados recopilados.
     * @throws IOException Si ocurre un error de escritura en disco.
     */
    public static void exportarHistorial(File archivo, List<ResultadoConexion> resultados) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(archivo);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                BufferedWriter writer = new BufferedWriter(osw)) {

            // Marca BOM UTF-8 para compatibilidad directa con Microsoft Excel
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);

            writer.write("FechaHora;Exitosa;Estado;MotorBD;VersionMotor;DriverJDBC;URL;Detalle");
            writer.newLine();

            for (ResultadoConexion resultado : resultados) {
                String linea = String.join(";",
                        escaparCsv(resultado.fechaHora().format(FORMATO_FECHA)),
                        escaparCsv(resultado.exitosa() ? "SI" : "NO"),
                        escaparCsv(resultado.estado()),
                        escaparCsv(resultado.motor()),
                        escaparCsv(resultado.versionMotor()),
                        escaparCsv(resultado.driverUtilizado()),
                        escaparCsv(resultado.urlEvaluada()),
                        escaparCsv(resultado.detalle()));
                writer.write(linea);
                writer.newLine();
            }
        }
    }

    private static String escaparCsv(String valor) {
        if (valor == null) {
            return "";
        }
        String limpio = valor.replace("\r\n", " ").replace('\n', ' ').replace('\r', ' ');
        if (limpio.contains(";") || limpio.contains("\"")) {
            return "\"" + limpio.replace("\"", "\"\"") + "\"";
        }
        return limpio;
    }
}

