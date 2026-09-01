package ar.edu.unl.huellas.dao;

import ar.edu.unl.huellas.db.ConnectionManager;
import ar.edu.unl.huellas.model.ArticuloBlog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO para acceder los datos de la tabla articulos_blog correspondiente a nuestra base de datos
public class BlogDAO {

    public List<ArticuloBlog> listarTodos() throws SQLException {
        String sql = "SELECT id, slug, etiqueta, titulo, resumen, contenido, imagen_url, publicado_en "
                + "FROM articulos_blog ORDER BY publicado_en DESC";
        List<ArticuloBlog> lista = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // Metodo para traer un articulo puntual a partir de su slug
    public Optional<ArticuloBlog> obtenerPorSlug(String slug) throws SQLException {
        String sql = "SELECT id, slug, etiqueta, titulo, resumen, contenido, imagen_url, publicado_en "
                + "FROM articulos_blog WHERE slug = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, slug);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }
        return Optional.empty();
    }

    // Metodo para traer un articulo puntual por id
    public Optional<ArticuloBlog> obtenerPorId(int id) throws SQLException {
        String sql = "SELECT id, slug, etiqueta, titulo, resumen, contenido, imagen_url, publicado_en "
                + "FROM articulos_blog WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }
        return Optional.empty();
    }

    // Metodo para comprobar si un slug ya esta en uso excluyendo opcionalmente el propio id, para el caso de edicion
    public boolean existeSlug(String slug, Integer idAExcluir) throws SQLException {
        String sql = "SELECT 1 FROM articulos_blog WHERE slug = ? AND (? IS NULL OR id <> ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, slug);
            if (idAExcluir == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, idAExcluir);
                ps.setInt(3, idAExcluir);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Metodo para inserta un nuevo articulo y devolver el id generado
    public int crear(ArticuloBlog a) throws SQLException {
        String sql = "INSERT INTO articulos_blog (slug, etiqueta, titulo, resumen, contenido, imagen_url, publicado_en) "
                + "VALUES (?, ?, ?, ?, ?, ?, COALESCE(?, CURRENT_DATE))";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getSlug());
            ps.setString(2, a.getEtiqueta());
            ps.setString(3, a.getTitulo());
            ps.setString(4, a.getResumen());
            ps.setString(5, a.getContenido());
            ps.setString(6, a.getImagenUrl());
            if (a.getPublicadoEn() != null) {
                ps.setDate(7, java.sql.Date.valueOf(a.getPublicadoEn()));
            } else {
                ps.setNull(7, java.sql.Types.DATE);
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo obtener el id generado para el articulo nuevo.");
    }

    // Metodo para actualizr un articulo existente identificado por su id
    public boolean actualizar(ArticuloBlog a) throws SQLException {
        String sql = "UPDATE articulos_blog SET slug = ?, etiqueta = ?, titulo = ?, resumen = ?, "
                + "contenido = ?, imagen_url = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getSlug());
            ps.setString(2, a.getEtiqueta());
            ps.setString(3, a.getTitulo());
            ps.setString(4, a.getResumen());
            ps.setString(5, a.getContenido());
            ps.setString(6, a.getImagenUrl());
            ps.setInt(7, a.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // Metodo para eliminar un articulo por id. Devuelve true si efectivamente elimino una fila
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM articulos_blog WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private ArticuloBlog mapear(ResultSet rs) throws SQLException {
        ArticuloBlog a = new ArticuloBlog();
        a.setId(rs.getInt("id"));
        a.setSlug(rs.getString("slug"));
        a.setEtiqueta(rs.getString("etiqueta"));
        a.setTitulo(rs.getString("titulo"));
        a.setResumen(rs.getString("resumen"));
        a.setContenido(rs.getString("contenido"));
        a.setImagenUrl(rs.getString("imagen_url"));
        a.setPublicadoEn(rs.getDate("publicado_en").toLocalDate());
        return a;
    }
}