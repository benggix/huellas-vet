package ar.edu.unl.huellas.dao;

import ar.edu.unl.huellas.db.ConnectionManager;
import ar.edu.unl.huellas.model.ConsultaContacto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// DAO para poder acceder los datos de la tabla consultas_contacto correspondiente a nuestra base de datos
public class ContactoDAO {
    // metodo para guardar
    public void guardar(ConsultaContacto consulta) throws SQLException {
        String sql = "INSERT INTO consultas_contacto (nombre, email, asunto, mensaje) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, consulta.getNombre());
            ps.setString(2, consulta.getEmail());
            ps.setString(3, consulta.getAsunto());
            ps.setString(4, consulta.getMensaje());
            ps.executeUpdate();
        }
    }

    // Metodo para obtener el listado de toda la informacion de contacto desde el panel de administracion
    public List<ConsultaContacto> listarTodos() throws SQLException {
        String sql = "SELECT id, nombre, email, asunto, mensaje, creado_en "
                + "FROM consultas_contacto ORDER BY creado_en DESC";
        List<ConsultaContacto> lista = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ConsultaContacto c = new ConsultaContacto();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setEmail(rs.getString("email"));
                c.setAsunto(rs.getString("asunto"));
                c.setMensaje(rs.getString("mensaje"));
                c.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());
                lista.add(c);
            }
        }
        return lista;
    }
}

