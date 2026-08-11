package ar.edu.unl.huellas.dao;

import ar.edu.unl.huellas.db.ConnectionManager;
import ar.edu.unl.huellas.model.Suscriptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// DAO para acceder los datos de la tabla suscriptores_newsletter, correspondiente a nuestra base de datos
public class NewsletterDAO {

    // metodo para guardar el mail. Devuelve true si se agrego, caso contrario false si ese mail ya esta registrado en la tabla
    public boolean guardar(Suscriptor suscriptor) throws SQLException {
        String sql = "INSERT INTO suscriptores_newsletter (email) VALUES (?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, suscriptor.getEmail());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                return false;
            }
            throw e;
        }
    }

    // Para poder traer o listar toda la informacion y volcarlo sobre el panel de administracion
    public List<Suscriptor> listarTodos() throws SQLException {
        String sql = "SELECT id, email, creado_en FROM suscriptores_newsletter ORDER BY creado_en DESC";
        List<Suscriptor> lista = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Suscriptor s = new Suscriptor();
                s.setId(rs.getInt("id"));
                s.setEmail(rs.getString("email"));
                s.setCreadoEn(rs.getTimestamp("creado_en").toLocalDateTime());
                lista.add(s);
            }
        }
        return lista;
    }
}

