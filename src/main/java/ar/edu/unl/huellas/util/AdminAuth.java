package ar.edu.unl.huellas.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// clase utilitaria para validar o autenticar la informacion del admin para que corresponda a la del archivo admin.properties. La contraseña no esta hasheada para este caso.
public final class AdminAuth {
    private static volatile Properties props;

    private AdminAuth() {
    }

    private static Properties cargar() {
        if (props == null) {
            synchronized (AdminAuth.class) {
                if (props == null) {
                    Properties p = new Properties();
                    try (InputStream in = AdminAuth.class.getResourceAsStream("/admin.properties")) {
                        if (in != null) {
                            p.load(in);
                        }
                    } catch (IOException e) {
                        throw new IllegalStateException("Error leyendo admin.properties", e);
                    }
                    props = p;
                }
            }
        }
        return props;
    }

    public static boolean credencialesValidas(String usuario, String password) {
        Properties p = cargar();
        String usuarioEsperado = p.getProperty("admin.username", "admin");
        String passwordEsperado = p.getProperty("admin.password", "admin");
        return usuarioEsperado.equals(usuario) && passwordEsperado.equals(password);
    }
}