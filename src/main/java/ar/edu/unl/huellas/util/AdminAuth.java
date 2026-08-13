package ar.edu.unl.huellas.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Properties;

// clase utilitaria para validar o autenticar la informacion del admin para que corresponda a la del archivo admin.properties. La contraseña se guardara hasheada con PBKDF2-HMAC-SHA256 + salt (o sea no reversible), ya no mas en texto plano.
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

    // Metodo para recalcular el hash PBKDF2 de la contraseña ingresada, usando el mismo salt e iteraciones guardadas, y se lo compara contra el hash almacenado, no en texto plano.
    private static boolean passwordValida(String passwordIngresada, String saltB64, String hashEsperadoB64, int iteraciones) {
        try {
            byte[] salt = Base64.getDecoder().decode(saltB64);
            byte[] hashEsperado = Base64.getDecoder().decode(hashEsperadoB64);

            PBEKeySpec spec = new PBEKeySpec(passwordIngresada.toCharArray(), salt, iteraciones, hashEsperado.length * 8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hashCalculado = factory.generateSecret(spec).getEncoded();

            // Comparación en tiempo constante para evitar timing attacks
            return MessageDigest.isEqual(hashCalculado, hashEsperado);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean credencialesValidas(String usuario, String password) {
        if (usuario == null || password == null) {
            return false;
        }
        Properties p = cargar();
        String usuarioEsperado = p.getProperty("admin.username", "admin");
        String saltB64 = p.getProperty("admin.password.salt", "");
        String hashB64 = p.getProperty("admin.password.hash", "");
        int iteraciones = Integer.parseInt(p.getProperty("admin.password.iterations", "210000"));

        boolean usuarioOk = MessageDigest.isEqual(
                usuarioEsperado.getBytes(), usuario.getBytes());
        boolean passwordOk = passwordValida(password, saltB64, hashB64, iteraciones);

        return usuarioOk && passwordOk;
    }
}