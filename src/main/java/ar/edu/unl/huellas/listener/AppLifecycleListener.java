package ar.edu.unl.huellas.listener;

import ar.edu.unl.huellas.db.ConnectionManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

// Listener para asegurar de cerrar el pool de conexiones de HikariCP cuando el server de app se detiene o se redeploya el WAR, para que de esta manera se eviten conexiones colgadas contra PostgreSQL
@WebListener
public class AppLifecycleListener implements ServletContextListener {

    // Metodo que se va a ejecutar cuando nuestra app se despliegue en el servidor
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    /* Metodo que se va a ejecutar cuando nuestra app se detenga o se redeploye. Cerrando el pool de conexiones mediante ConnectionManager para garantizar que todas
    las conexiones activas sean liberadas correctamente*/
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionManager.shutdown();
    }
}
