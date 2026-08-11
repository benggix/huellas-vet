package ar.edu.unl.huellas.dao;

import ar.edu.unl.huellas.db.ConnectionManager;
import ar.edu.unl.huellas.model.Mascota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// DAO para acceder los datos de la tabla mascotas correspondiente a nuestra base de datos
public class MascotaDAO {
    // metodo para buscar una mascota por su nombre + el contador del dueño para no duplicar el mismo cliente en cada turno que reserva. Si no existe, la crea. Devuelve el id para usarlo como clave foránea en la tabla de turnos
    public int buscarOCrear(Mascota mascota) throws SQLException {
        String buscar = "SELECT id FROM mascotas WHERE nombre = ? AND dueno_contacto = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(buscar)) {
            ps.setString(1, mascota.getNombre());
            ps.setString(2, mascota.getDuenoContacto());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        String insertar = "INSERT INTO mascotas (nombre, especie, dueno_nombre, dueno_contacto) "
                + "VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertar)) {
            ps.setString(1, mascota.getNombre());
            ps.setString(2, mascota.getEspecie());
            ps.setString(3, mascota.getDuenoNombre());
            ps.setString(4, mascota.getDuenoContacto());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
