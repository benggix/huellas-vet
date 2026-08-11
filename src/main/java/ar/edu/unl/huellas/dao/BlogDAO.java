package ar.edu.unl.huellas.dao;

import ar.edu.unl.huellas.db.ConnectionManager;
import ar.edu.unl.huellas.model.ArticuloBlog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// DAO para acceder los datos de la tabla articulos_blog correspondiente a nuestra base de datos
public class BlogDAO {

    public List<ArticuloBlog> listarTodos() throws SQLException {
        String sql = "SELECT id, etiqueta, titulo, resumen, publicado_en "
                + "FROM articulos_blog ORDER BY publicado_en DESC";
        List<ArticuloBlog> lista = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ArticuloBlog a = new ArticuloBlog();
                a.setId(rs.getInt("id"));
                a.setEtiqueta(rs.getString("etiqueta"));
                a.setTitulo(rs.getString("titulo"));
                a.setResumen(rs.getString("resumen"));
                a.setPublicadoEn(rs.getDate("publicado_en").toLocalDate());
                lista.add(a);
            }
        }
        return lista;
    }
}
