package ar.edu.unl.huellas.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

// clase que maneja la conexion en general (No heredable)
public final class ConnectionManager {

    /* Atributo donde manejaremos el pool de conexiones unico (Singleton). Al ser volatile nos aseguramos que todos los hilos vean
    el valor actualizado inmediatamente después de ser inicializado */
    private static volatile HikariDataSource dataSource;

    // Este constructor privado es para evitar que se puedan crear instancias de esta clase, porque estamos aplicando el patron singleton  en una clase de utilidad estatica.
    private ConnectionManager() {
    }

    // Metodo que se va a encargar de configurar y crear el pool de conexiones con HikariCP
    private static HikariDataSource init() {
        // obtengo la variable de entorno DB_HOST de mi archivo .env (donde tengo mis variables de entorno)
        String envHost = System.getenv("DB_HOST");
        HikariConfig config = new HikariConfig();

        // Seteamos para que se utilice el driver JDBC de PostgreSQL
        config.setDriverClassName("org.postgresql.Driver");

        // Si tenemos configurado variables de entorno en nuestro sistema (existe = no es null) y ademas no esta vacia. Se utilizan dichos datos
        if (envHost != null && !envHost.isBlank()) {
            // Se obtienen el resto de los parámetros, usando valores por defecto si no están definidos
            String port = System.getenv().getOrDefault("DB_PORT", "5432");
            String name = System.getenv().getOrDefault("DB_NAME", "huellas_db");
            String user = System.getenv().getOrDefault("DB_USER", "postgres");
            String password = System.getenv().getOrDefault("DB_PASSWORD", "");

            // Se construye la URL de conexión JDBC dinamicamente
            config.setJdbcUrl("jdbc:postgresql://" + envHost + ":" + port + "/" + name);
            config.setUsername(user);
            config.setPassword(password);
            config.setMaximumPoolSize(10); // el tamaño del pool de un maximo de 10
        } else {
            // Caso contrario, como no tenemos variables de entorno configurado en nuestro sistema, se busca el archivo de configuracion local que tenemos "db.properties"
            Properties props = new Properties();

            // intentamos abrir el archivo db.properties desde el classpath (src/main/resources)
            try (InputStream in = ConnectionManager.class.getResourceAsStream("/db.properties")) {
                if (in == null) {
                    // Si no se encuentra el archivo ni las variables de entorno, lanzamos un error.
                    throw new IllegalStateException(
                            "No se encontró db.properties en el classpath (src/main/resources) "
                                    + "ni variables de entorno DB_HOST/DB_PORT/DB_NAME/DB_USER/DB_PASSWORD.");
                }
                // Si se encontro las variables de entorno, se cargan las propiedades del archivo
                props.load(in);
            } catch (IOException e) {
                // Si no se encontro el archivo, se lanza un error de lectura del archivo
                throw new IllegalStateException("Error leyendo db.properties", e);
            }

            // configuramos los datos de conexion a partir del archivo .properties
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));

            // El tamaño maximo del pool tambien puede definirse en el archivo, pero lo definimos aca con valor por defecto "10"
            config.setMaximumPoolSize(
                    Integer.parseInt(props.getProperty("db.pool.maxSize", "10")));
        }

        // Le definimos un nombre identificador del pool que nos sera util para los logs y metricas
        config.setPoolName("huellas-pool");

        // Se crea y retorna el DataSource de Hikari con toda la configuración aplicada
        return new HikariDataSource(config);
    }

    // Metodo publico para obtener una conexión desde el pool
    public static Connection getConnection() throws SQLException {
        // Primera verificacion (sin bloqueo) para evitar sincronizar innecesariamente
        if (dataSource == null) {
            // Bloqueo por clase para evitar que dos hilos inicialicen el pool al mismo tiempo
            synchronized (ConnectionManager.class) {
                // Segunda verificacion dentro del bloque sincronizado (patron "double-checked locking")
                if (dataSource == null) {
                    dataSource = init();
                }
            }
        }
        // Se obtiene una conexión disponible del pool
        return dataSource.getConnection();
    }

    // Metodo para cerrar el pool de conexiones de forma ordenada
    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}