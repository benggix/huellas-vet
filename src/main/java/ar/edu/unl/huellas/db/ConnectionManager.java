package ar.edu.unl.huellas.db;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

// clase que maneja la conexion en general (No heredable). Ahora la configuracion se arma en DbConfig
public final class ConnectionManager {

  // Referencia al pool una vez creado. Solo la usa shutdown() para saber si el pool llego a crearse, sin forzar su creacion. Es volatile para que el hilo que cierra la app vea el valor escrito por el hilo que inicializo el pool
  private static volatile HikariDataSource created;

  // Usamos la implementacion Holder (initialization-on-demand holder) para que la JVM no cargue esta clase interna hasta que alguien acceda a INSTANCE por primera vez, ademas garantiza que la inicializacion estatica se ejecute una sola vez y de forma segura entre hilos. Por eso no hace falta synchronized ni volatile en el acceso
  private static final class Holder {
    private static final HikariDataSource INSTANCE = createPool();
  }

  // Constructor privado es para evitar que se puedan crear instancias de esta clase, porque es una clase de utilidad estatica.
  private ConnectionManager() {
  }

  // Metodo factory privado para crear el pool de conexiones con HikariCP a partir de la configuracion de DbConfig y se guarda la referencia para poder cerrarlo despues
  private static HikariDataSource createPool() {
    HikariDataSource ds = new HikariDataSource(DbConfig.load());
    created = ds;
    return ds;
  }

  // Metodo de acceso publico para obtener una conexión desde el pool (el pool se crea en el primer uso)
  public static Connection getConnection() throws SQLException {
    return Holder.INSTANCE.getConnection();
  }

  // Metodo de limpieza para cerrar el pool de conexiones de forma ordenada. Si el pool nunca se creo, no hace nada (no lo crea solo para cerrarlo)
  public static void shutdown() {
    HikariDataSource ds = created;
    if (ds != null) {
      ds.close();
    }
  }
}