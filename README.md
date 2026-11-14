# EA3 - Incorporando UI y BD (`ConexionTester`)

Aplicación de escritorio desarrollada en **Java 21 LTS (Swing + JDBC)** para la asignatura **Programación Orientada a Objetos II** (*Institución Universitaria Digital de Antioquia*).

Permite al usuario ingresar dinámicamente la configuración de una base de datos (**URL JDBC**, **usuario** y **contraseña**), validar la conectividad mediante `DriverManager.getConnection(...)`, garantizar el cierre automático de la sesión con `try-with-resources` y mostrar resultados visuales en verde o rojo (incluyendo el detalle técnico de `ClassNotFoundException` y `SQLException`) dentro de un `JTextArea`.

## Estructura del proyecto

```text
EA3_Incorporando_UI_y_BD/
├── evidencias/
│   ├── 01_inicial.png
│   ├── 02_conexion_exitosa_sqlite.png
│   ├── 03_error_sql_exception.png
│   ├── 04_error_class_not_found.png
│   ├── diagrama_uml_ea3.png
│   └── registro_pruebas_conexion.csv
├── lib/
│   └── sqlite-jdbc-3.39.4.1.jar
└── src/
    └── proyecto/edu/co/iud/
        ├── modelo/
        │   └── ResultadoConexion.java
        ├── servicio/
        │   ├── IConexionBDable.java
        │   └── impl/
        │       └── GestorConexionBD.java
        └── vista/
            └── ConexionTester.java
```

## Requisitos

- **Java JDK 21 LTS** (o superior).
- El driver oficial de SQLite (`lib/sqlite-jdbc-3.39.4.1.jar`) ya se incluye en el repositorio. Si desea probar MySQL o PostgreSQL, agregue el `.jar` correspondiente en `lib/`.

## Compilación y ejecución

### En Windows (PowerShell / CMD)

```powershell
javac -encoding UTF-8 -d bin (Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName })
java -cp "bin;lib/*" proyecto.edu.co.iud.vista.ConexionTester
```

### En Linux / macOS (Bash)

```bash
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -cp "bin:lib/*" proyecto.edu.co.iud.vista.ConexionTester
```

## Pruebas sugeridas en la aplicación

1. **Conexión exitosa (verde):** URL `jdbc:sqlite:inventario.db`, usuario vacío, contraseña vacía.
2. **Error de conexión `SQLException` (rojo):** URL `jdbc:sqlite:Z:/ruta_inexistente/error.db` o URL con protocolo desconocido `jdbc:desconocido:prueba`.
3. **Error de driver `ClassNotFoundException` (rojo):** URL `jdbc:mysql://localhost:3306/inventario` sin tener el conector de MySQL en `lib/`.

