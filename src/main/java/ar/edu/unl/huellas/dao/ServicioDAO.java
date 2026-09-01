package ar.edu.unl.huellas.dao;

import ar.edu.unl.huellas.db.ConnectionManager;
import ar.edu.unl.huellas.model.Servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DAO para poder acceder a los datos de la tabla servicios
public class ServicioDAO {
    // metodo para listar todos los servicios que ofrece la clinica huellass
    public List<Servicio> listarTodos() throws SQLException {
        String sql = "SELECT id, codigo, titulo, descripcion, detalle, imagen_url, orden "
                + "FROM servicios ORDER BY orden ASC";
        List<Servicio> lista = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // metodo para traer un servicio puntual por id
    public Optional<Servicio> obtenerPorId(int id) throws SQLException {
        String sql = "SELECT id, codigo, titulo, descripcion, detalle, imagen_url, orden "
                + "FROM servicios WHERE id = ?";
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

    // Metodo para comprobar si un codigo ya esta en uso
    public boolean existeCodigo(String codigo, Integer idAExcluir) throws SQLException {
        String sql = "SELECT 1 FROM servicios WHERE codigo = ? AND (? IS NULL OR id <> ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
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

    // metodo para insertar un nuevo servicio y devuelver el id generado
    public int crear(Servicio s) throws SQLException {
        String sql = "INSERT INTO servicios (codigo, titulo, descripcion, detalle, imagen_url, orden) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getCodigo());
            ps.setString(2, s.getTitulo());
            ps.setString(3, s.getDescripcion());
            ps.setString(4, s.getDetalle());
            ps.setString(5, s.getImagenUrl());
            ps.setInt(6, s.getOrden() != null ? s.getOrden() : 0);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo obtener el id generado para el servicio nuevo.");
    }

    // Metodo para actualiza un servicio existente identificado por su id
    public boolean actualizar(Servicio s) throws SQLException {
        String sql = "UPDATE servicios SET codigo = ?, titulo = ?, descripcion = ?, detalle = ?, "
                + "imagen_url = ?, orden = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getCodigo());
            ps.setString(2, s.getTitulo());
            ps.setString(3, s.getDescripcion());
            ps.setString(4, s.getDetalle());
            ps.setString(5, s.getImagenUrl());
            ps.setInt(6, s.getOrden() != null ? s.getOrden() : 0);
            ps.setInt(7, s.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // Metodo para eliminar un servicio por id. Devuelve true si efectivamente elimino una fila
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM servicios WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Servicio mapear(ResultSet rs) throws SQLException {
        Servicio s = new Servicio();
        s.setId(rs.getInt("id"));
        s.setCodigo(rs.getString("codigo"));
        s.setTitulo(rs.getString("titulo"));
        s.setDescripcion(rs.getString("descripcion"));
        s.setDetalle(rs.getString("detalle"));
        s.setImagenUrl(rs.getString("imagen_url"));
        s.setOrden(rs.getInt("orden"));
        return s;
    }
}