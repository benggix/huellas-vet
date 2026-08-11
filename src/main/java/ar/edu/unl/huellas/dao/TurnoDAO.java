package ar.edu.unl.huellas.dao;

import ar.edu.unl.huellas.db.ConnectionManager;
import ar.edu.unl.huellas.model.Turno;
import ar.edu.unl.huellas.model.TurnoDetalle;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


// DAO para poder acceder a datos de la tabla turnos

public class TurnoDAO {
    // Metodo de restricción en la fecha y hora unicas, es para fijarnos antes de insertar los datos para poder devolver un mensaje.
    public boolean existeSuperposicion(LocalDate fecha, LocalTime hora) throws SQLException {
        String sql = "SELECT 1 FROM turnos WHERE fecha = ? AND hora = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            ps.setTime(2, Time.valueOf(hora));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }


    // Metodo para reservar un turno. Devuelve true si se guardo, si ya existe dicho turno en dicha hora/fecha. Se detectara si hubo suporsicion por si dos solicitudes llegan al mismo tiempo.
    public boolean guardar(Turno turno) throws SQLException {
        String sql = "INSERT INTO turnos (mascota_id, servicio, fecha, hora, notas) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, turno.getMascotaId());
            ps.setString(2, turno.getServicio());
            ps.setDate(3, Date.valueOf(turno.getFecha()));
            ps.setTime(4, Time.valueOf(turno.getHora()));
            ps.setString(5, turno.getNotas());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // Código 23505 = unique_violation en PostgreSQL (choque de horario)
            if ("23505".equals(e.getSQLState())) {
                return false;
            }
            throw e;
        }
    }
    
    // Para traer todos los datos del dueño junto a la mascota para el panel de administración. */
    public List<TurnoDetalle> listarTodosConMascota() throws SQLException {
        String sql = "SELECT t.id, t.servicio, t.fecha, t.hora, t.notas, "
                + "m.nombre AS mascota_nombre, m.especie, m.dueno_nombre, m.dueno_contacto "
                + "FROM turnos t JOIN mascotas m ON m.id = t.mascota_id "
                + "ORDER BY t.fecha DESC, t.hora DESC";
        List<TurnoDetalle> lista = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TurnoDetalle t = new TurnoDetalle();
                t.setId(rs.getInt("id"));
                t.setServicio(rs.getString("servicio"));
                t.setFecha(rs.getDate("fecha").toLocalDate());
                t.setHora(rs.getTime("hora").toLocalTime());
                t.setNotas(rs.getString("notas"));
                t.setMascotaNombre(rs.getString("mascota_nombre"));
                t.setEspecie(rs.getString("especie"));
                t.setDuenoNombre(rs.getString("dueno_nombre"));
                t.setDuenoContacto(rs.getString("dueno_contacto"));
                lista.add(t);
            }
        }
        return lista;
    }
}
