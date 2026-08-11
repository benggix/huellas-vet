package ar.edu.unl.huellas.dao;

import ar.edu.unl.huellas.db.ConnectionManager;
import ar.edu.unl.huellas.model.Servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// DAO para poder acceder a los datos de la tabla servicios
public class ServicioDAO {
    // metodo para listar todos los servicios que ofrece la clinica huellass
    public List<Servicio> listarTodos() throws SQLException {
        String sql = "SELECT id, codigo, titulo, descripcion, detalle, orden "
                + "FROM servicios ORDER BY orden ASC";
        List<Servicio> lista = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Servicio s = new Servicio();
                s.setId(rs.getInt("id"));
                s.setCodigo(rs.getString("codigo"));
                s.setTitulo(rs.getString("titulo"));
                s.setDescripcion(rs.getString("descripcion"));
                s.setDetalle(rs.getString("detalle"));
                s.setOrden(rs.getInt("orden"));
                lista.add(s);
            }
        }
        return lista;
    }
}
