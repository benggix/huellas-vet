package ar.edu.unl.huellas.db;

import com.zaxxer.hikari.HikariConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// clase que arma la configuracion de HikariCP leyendo variables de entorno o, si no existen, el archivo db.properties.
final class DbConfig {

  // Constructor privado para evitar instancias, ya que es una clase de utilidad estatica
  private DbConfig() {
  }

  // Metodo que lee el entorno y devuelve el HikariConfig ya armado, listo para crear el pool
  static HikariConfig load() {
    // obtengo la variable de entorno DB_HOST de mi archivo .env (que es donde tengo mis variables de entorno)
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
      config.setMaximumPoolSize(5); // lo reduzco para bajar consumo
      config.setMinimumIdle(1); // para no mantener conexiones de mas abiertas que esta sin uso

    } else {
      // Caso contrario, como no tenemos variables de entorno configurado en nuestro sistema, se busca el archivo de configuracion local que tenemos "db.properties"
      Properties props = new Properties();

      // intentamos abrir el archivo db.properties desde el classpath (src/main/resources)
      try (InputStream in = DbConfig.class.getResourceAsStream("/db.properties")) {
        if (in == null) {
          // Si no se encuentra el archivo ni las variables de entorno, lanzamos un error.
          throw new IllegalStateException(
                  "No se encontró db.properties en el classpath (src/main/resources) "
                          + "ni variables de entorno DB_HOST/DB_PORT/DB_NAME/DB_USER/DB_PASSWORD.");
        }
        // Si se encontro el archivo, se cargan las propiedades
        props.load(in);
      } catch (IOException e) {
        // Si no se pudo leer el archivo, se lanza un error de lectura
        throw new IllegalStateException("Error leyendo db.properties", e);
      }

      // configuramos los datos de conexion a partir del archivo .properties
      config.setJdbcUrl(props.getProperty("db.url"));
      config.setUsername(props.getProperty("db.user"));
      config.setPassword(props.getProperty("db.password"));

      // El tamaño maximo del pool tambien puede definirse en el archivo, pero lo definimos aca con valor por defecto "5"
      config.setMaximumPoolSize(
              Integer.parseInt(props.getProperty("db.pool.maxSize", "5")));
      config.setMinimumIdle(1);
    }

    // Le definimos un nombre identificador del pool que nos sera util para los logs y metricas
    config.setPoolName("huellas-pool");

    /* Con el patron Holder, si la creacion del pool falla la clase queda inutilizable hasta reiniciar la app (la JVM no reintenta).
    Con -1, Hikari no exige una conexion al crear el pool: si la BD todavia no responde, getConnection() falla con SQLException
    (que los DAO ya manejan) y se recupera sola cuando la BD vuelve, igual que antes */
    config.setInitializationFailTimeout(-1);

    return config;
  }
}