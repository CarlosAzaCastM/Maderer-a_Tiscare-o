package dao;

import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp; // Importante para fechas exactas
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import modelo.Gasto;

public class GastoDAO {

    public boolean registrarGasto(Gasto g) {
        String sql = "INSERT INTO gastos (tipo_gasto, descripcion, monto, fecha_gasto, id_usuario) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, g.getTipoGasto());
            ps.setString(2, g.getDescripcion());
            ps.setDouble(3, g.getMonto());
            
            // Convertir el String de fecha a Timestamp para MySQL (YYYY-MM-DD HH:MM:SS)
            // Asumimos que g.getFechaGasto() viene en formato compatible o lo manejamos como String directo si MySQL lo acepta
            ps.setString(4, g.getFechaGasto()); 
            
            ps.setInt(5, g.getIdUsuario());
            
            ps.executeUpdate();
            return true;
            
        } catch (Exception e) {
            System.out.println("Error al registrar gasto: " + e.getMessage());
            return false;
        }
    }
    
     public List<Gasto> listarGastosPorUsuario(int idUsuario) {
        List<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT * FROM gastos WHERE id_usuario = ? ORDER BY fecha_gasto DESC";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Gasto g = new Gasto();
                g.setIdGasto(rs.getInt("id_gasto"));
                g.setTipoGasto(rs.getString("tipo_gasto"));
                g.setDescripcion(rs.getString("descripcion"));
                g.setMonto(rs.getDouble("monto"));
                g.setFechaGasto(rs.getString("fecha_gasto"));
                g.setIdUsuario(rs.getInt("id_usuario"));
                gastos.add(g);
            }
            
        } catch (SQLException e) {
            System.out.println("Error al listar gastos: " + e.getMessage());
        }
        return gastos;
    }
    
    // Método para listar gastos filtrados
    public List<Gasto> listarGastosFiltrados(int idUsuario, String condicion) {
        List<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT * FROM gastos WHERE id_usuario = ? " + condicion + " ORDER BY fecha_gasto DESC";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Gasto g = new Gasto();
                g.setIdGasto(rs.getInt("id_gasto"));
                g.setTipoGasto(rs.getString("tipo_gasto"));
                g.setDescripcion(rs.getString("descripcion"));
                g.setMonto(rs.getDouble("monto"));
                g.setFechaGasto(rs.getString("fecha_gasto"));
                g.setIdUsuario(rs.getInt("id_usuario"));
                gastos.add(g);
            }
            
        } catch (SQLException e) {
            System.out.println("Error al listar gastos filtrados: " + e.getMessage());
        }
        return gastos;
    }
    
    public double sumarGastosPorUsuarioYRango(int idUsuario, Date fechaInicio, Date fechaFin) {
        double total = 0.0;
        String sql = "SELECT SUM(monto) as total_gastos " +
                     "FROM gastos " +
                     "WHERE id_usuario = ? " +
                     "AND fecha_gasto BETWEEN ? AND ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            java.sql.Timestamp fechaInicioSql = new java.sql.Timestamp(fechaInicio.getTime());
            java.sql.Timestamp fechaFinSql = new java.sql.Timestamp(fechaFin.getTime());

            ps.setInt(1, idUsuario);
            ps.setTimestamp(2, fechaInicioSql);
            ps.setTimestamp(3, fechaFinSql);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total_gastos");
                if (rs.wasNull()) {
                    total = 0.0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al sumar gastos: " + e.getMessage());
            e.printStackTrace();
        }
        return total;
    }
}