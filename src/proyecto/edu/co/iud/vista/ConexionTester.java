package proyecto.edu.co.iud.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import proyecto.edu.co.iud.modelo.ResultadoConexion;
import proyecto.edu.co.iud.servicio.IConexionBDable;
import proyecto.edu.co.iud.servicio.impl.GestorConexionBD;

/**
 * Interfaz gráfica Swing para ingresar la configuración de una base de datos
 * y validar dinámicamente la conexión mediante JDBC.
 */
public final class ConexionTester extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Color COLOR_PRIMARIO = new Color(0, 43, 73);
    private static final Color COLOR_BOTON = new Color(21, 94, 158);
    private static final Color COLOR_FONDO_VENTANA = new Color(245, 247, 250);
    private static final Color COLOR_EXITO_TEXTO = new Color(21, 128, 61);
    private static final Color COLOR_EXITO_FONDO = new Color(240, 253, 244);
    private static final Color COLOR_ERROR_TEXTO = new Color(185, 28, 28);
    private static final Color COLOR_ERROR_FONDO = new Color(254, 242, 242);
    private static final Color COLOR_NEUTRO_TEXTO = new Color(51, 65, 85);

    private final transient IConexionBDable gestorConexion;

    private JTextField txtUrl;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnProbarConexion;
    private JTextArea txaResultado;

    /**
     * Constructor por defecto con la implementación JDBC estándar.
     */
    public ConexionTester() {
        this(new GestorConexionBD());
    }

    /**
     * Constructor con inyección de dependencias para desacoplar la lógica JDBC de la vista.
     *
     * @param gestorConexion Servicio encargado de probar la conexión JDBC.
     */
    public ConexionTester(IConexionBDable gestorConexion) {
        this.gestorConexion = gestorConexion;
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("EA3 - Validador de Conexión JDBC | IU Digital");
        setSize(720, 640);
        setMinimumSize(new Dimension(640, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(COLOR_FONDO_VENTANA);
    }

    private void inicializarComponentes() {
        add(crearPanelEncabezado(), BorderLayout.NORTH);

        JPanel panelContenido = new JPanel(new BorderLayout(12, 12));
        panelContenido.setOpaque(false);
        panelContenido.setBorder(new EmptyBorder(4, 16, 8, 16));
        panelContenido.add(crearPanelFormulario(), BorderLayout.NORTH);
        panelContenido.add(crearPanelResultados(), BorderLayout.CENTER);
        add(panelContenido, BorderLayout.CENTER);

        add(crearPanelPie(), BorderLayout.SOUTH);

        btnProbarConexion.addActionListener(e -> ejecutarPruebaConexion());
        getRootPane().setDefaultButton(btnProbarConexion);
    }

    private JPanel crearPanelEncabezado() {
        JPanel panelEncabezado = new JPanel(new BorderLayout(4, 4));
        panelEncabezado.setBackground(COLOR_PRIMARIO);
        panelEncabezado.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel lblTitulo = new JLabel("Validador de Conexión a Base de Datos (JDBC)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel(
                "Ingrese la URL, usuario y contraseña para comprobar la conexión mediante DriverManager",
                SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(203, 213, 225));

        panelEncabezado.add(lblTitulo, BorderLayout.CENTER);
        panelEncabezado.add(lblSubtitulo, BorderLayout.SOUTH);
        return panelEncabezado;
    }

    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Configuración de Conexión JDBC"),
                new EmptyBorder(10, 12, 10, 12)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font fuenteEtiqueta = new Font("Segoe UI", Font.BOLD, 13);
        Font fuenteCampo = new Font("Segoe UI", Font.PLAIN, 13);

        // Fila 0: URL de conexión
        JLabel lblUrl = new JLabel("URL de conexión:");
        lblUrl.setFont(fuenteEtiqueta);
        txtUrl = new JTextField(30);
        txtUrl.setFont(fuenteCampo);
        txtUrl.setToolTipText("Ejemplo: jdbc:sqlite:inventario.db");
        JLabel lblAyudaUrl = new JLabel("Ej.: jdbc:sqlite:inventario.db  |  jdbc:mysql://localhost:3306/bd");
        lblAyudaUrl.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblAyudaUrl.setForeground(new Color(100, 116, 139));

        agregarFilaFormulario(panelFormulario, gbc, 0, lblUrl, txtUrl);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 6, 6, 6);
        panelFormulario.add(lblAyudaUrl, gbc);
        gbc.insets = new Insets(6, 6, 6, 6);

        // Fila 2: Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(fuenteEtiqueta);
        txtUsuario = new JTextField(30);
        txtUsuario.setFont(fuenteCampo);
        txtUsuario.setToolTipText("Dejar vacío si utiliza SQLite");
        agregarFilaFormulario(panelFormulario, gbc, 2, lblUsuario, txtUsuario);

        // Fila 3: Contraseña
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(fuenteEtiqueta);
        txtContrasena = new JPasswordField(30);
        txtContrasena.setFont(fuenteCampo);
        txtContrasena.setToolTipText("Dejar vacío si utiliza SQLite");
        agregarFilaFormulario(panelFormulario, gbc, 3, lblContrasena, txtContrasena);

        JLabel lblNotaSqlite = new JLabel("Nota: deje usuario y contraseña vacíos para conexiones SQLite.");
        lblNotaSqlite.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNotaSqlite.setForeground(new Color(100, 116, 139));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 6, 8, 6);
        panelFormulario.add(lblNotaSqlite, gbc);

        // Fila 5: Botón Probar conexión
        btnProbarConexion = new JButton("Probar conexión");
        btnProbarConexion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnProbarConexion.setBackground(COLOR_BOTON);
        btnProbarConexion.setForeground(Color.WHITE);
        btnProbarConexion.setOpaque(true);
        btnProbarConexion.setFocusPainted(false);
        btnProbarConexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProbarConexion.setPreferredSize(new Dimension(180, 36));

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(6, 6, 4, 6);
        panelFormulario.add(btnProbarConexion, gbc);

        return panelFormulario;
    }

    private void agregarFilaFormulario(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            JLabel etiqueta,
            javax.swing.JComponent campo) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(etiqueta, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campo, gbc);
    }

    private JPanel crearPanelResultados() {
        JPanel panelResultados = new JPanel(new BorderLayout(6, 6));
        panelResultados.setBackground(Color.WHITE);
        panelResultados.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Resultado de la prueba"),
                new EmptyBorder(8, 10, 10, 10)));

        txaResultado = new JTextArea(10, 52);
        txaResultado.setEditable(false);
        txaResultado.setLineWrap(true);
        txaResultado.setWrapStyleWord(true);
        txaResultado.setFont(new Font("Consolas", Font.PLAIN, 13));
        txaResultado.setForeground(COLOR_NEUTRO_TEXTO);
        txaResultado.setBackground(new Color(248, 250, 252));
        txaResultado.setBorder(new EmptyBorder(8, 8, 8, 8));
        txaResultado.setText("Complete la configuración superior y presione \"Probar conexión\" para validar el acceso JDBC.");

        JScrollPane scrollResultados = new JScrollPane(txaResultado);
        scrollResultados.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelResultados.add(scrollResultados, BorderLayout.CENTER);
        return panelResultados;
    }

    private JPanel crearPanelPie() {
        JPanel panelPie = new JPanel(new BorderLayout());
        panelPie.setBackground(new Color(235, 240, 246));
        panelPie.setBorder(new EmptyBorder(6, 16, 6, 16));

        JLabel lblPie = new JLabel(
                "Control de excepciones: ClassNotFoundException y SQLException | Cierre seguro con try-with-resources",
                SwingConstants.CENTER);
        lblPie.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPie.setForeground(new Color(71, 85, 105));
        panelPie.add(lblPie, BorderLayout.CENTER);
        return panelPie;
    }

    /**
     * Lee los campos de configuración ingresados por el usuario, delega la validación JDBC
     * al servicio desacoplado y presenta el éxito o error capturado en el área de texto.
     */
    private void ejecutarPruebaConexion() {
        String url = txtUrl.getText().trim();
        String usuario = txtUsuario.getText().trim();
        char[] passwordChars = txtContrasena.getPassword();
        String contrasena = new String(passwordChars);
        Arrays.fill(passwordChars, '\0');

        try {
            ResultadoConexion resultado = gestorConexion.probarConexion(url, usuario, contrasena);
            mostrarMensajeExito(resultado);
        } catch (ClassNotFoundException ex) {
            mostrarMensajeError("Fallo de conexión (Driver JDBC no encontrado)",
                    "No se encontró la clase del controlador: " + ex.getMessage()
                            + System.lineSeparator()
                            + "Asegúrese de incluir el archivo .jar del driver en la carpeta lib/.");
        } catch (SQLException ex) {
            mostrarMensajeError("Fallo de conexión", ex.getMessage());
        }
    }

    private void mostrarMensajeExito(ResultadoConexion resultado) {
        txaResultado.setForeground(COLOR_EXITO_TEXTO);
        txaResultado.setBackground(COLOR_EXITO_FONDO);
        txaResultado.setText(resultado.formatearResumen());
        txaResultado.setCaretPosition(0);
    }

    private void mostrarMensajeError(String encabezado, String detalleTecnico) {
        txaResultado.setForeground(COLOR_ERROR_TEXTO);
        txaResultado.setBackground(COLOR_ERROR_FONDO);
        txaResultado.setText(encabezado + System.lineSeparator()
                + "--------------------------------------------------" + System.lineSeparator()
                + "Error técnico : " + detalleTecnico);
        txaResultado.setCaretPosition(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConexionTester ventana = new ConexionTester();
            ventana.setVisible(true);
        });
    }
}
