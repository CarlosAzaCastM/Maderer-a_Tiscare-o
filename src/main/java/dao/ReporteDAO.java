package dao;

import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ReporteDAO {
    
    // Método para calcular ganancia bruta (total ventas - costo de productos)
    public double calcularGananciaBruta(Date fechaInicio, Date fechaFin, int idUsuario) {
        double gananciaBruta = 0.0;
        
        // Consulta que calcula:
        // 1. Suma de totales de ventas completadas en el período
        // 2. Suma de costos de compra de los productos vendidos
        String sql = "SELECT " +
             "    COALESCE(SUM(dv.cantidad * dv.precio_venta_historico), 0) as total_ventas, " + 
             "    COALESCE(SUM(dv.cantidad * dv.costo_compra_historico), 0) as total_costo " +
             "FROM ventas v " +
             "INNER JOIN detalle_venta dv ON v.id_venta = dv.id_venta " +
             "WHERE v.fecha_venta BETWEEN ? AND ? " +
             "AND v.estatus = 'completada' " +
             "AND v.id_usuario = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            java.sql.Timestamp fechaInicioSql = new java.sql.Timestamp(fechaInicio.getTime());
            java.sql.Timestamp fechaFinSql = new java.sql.Timestamp(fechaFin.getTime());
            
            ps.setTimestamp(1, fechaInicioSql);
            ps.setTimestamp(2, fechaFinSql);
            ps.setInt(3, idUsuario);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double totalVentas = rs.getDouble("total_ventas");
                double totalCosto = rs.getDouble("total_costo");
                gananciaBruta = totalVentas - totalCosto;
            }
            
        } catch (SQLException e) {
            System.out.println("Error al calcular ganancia bruta: " + e.getMessage());
            e.printStackTrace();
        }
        return gananciaBruta;
    }
    
    // Método para calcular total de ventas (solo para referencia)
    public double calcularTotalVentas(Date fechaInicio, Date fechaFin, int idUsuario) {
        double totalVentas = 0.0;
        String sql = "SELECT COALESCE(SUM(total), 0) as total_ventas " +
                     "FROM ventas " +
                     "WHERE fecha_venta BETWEEN ? AND ? " +
                     "AND estatus = 'completada' " +
                     "AND id_usuario = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            java.sql.Timestamp fechaInicioSql = new java.sql.Timestamp(fechaInicio.getTime());
            java.sql.Timestamp fechaFinSql = new java.sql.Timestamp(fechaFin.getTime());
            
            ps.setTimestamp(1, fechaInicioSql);
            ps.setTimestamp(2, fechaFinSql);
            ps.setInt(3, idUsuario);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalVentas = rs.getDouble("total_ventas");
            }
            
        } catch (SQLException e) {
            System.out.println("Error al calcular total ventas: " + e.getMessage());
            e.printStackTrace();
        }
        return totalVentas;
    }
    
    // Método para calcular total de costos de productos vendidos
    public double calcularTotalCostos(Date fechaInicio, Date fechaFin, int idUsuario) {
        double totalCostos = 0.0;
        String sql = "SELECT COALESCE(SUM(dv.cantidad * dv.costo_compra_historico), 0) as total_costo " +
                     "FROM detalle_venta dv " +
                     "INNER JOIN ventas v ON dv.id_venta = v.id_venta " +
                     "WHERE v.fecha_venta BETWEEN ? AND ? " +
                     "AND v.estatus = 'completada' " +
                     "AND v.id_usuario = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            java.sql.Timestamp fechaInicioSql = new java.sql.Timestamp(fechaInicio.getTime());
            java.sql.Timestamp fechaFinSql = new java.sql.Timestamp(fechaFin.getTime());
            
            ps.setTimestamp(1, fechaInicioSql);
            ps.setTimestamp(2, fechaFinSql);
            ps.setInt(3, idUsuario);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalCostos = rs.getDouble("total_costo");
            }
            
        } catch (SQLException e) {
            System.out.println("Error al calcular total costos: " + e.getMessage());
            e.printStackTrace();
        }
        return totalCostos;
    }
    
    // Método para calcular total de gastos
    public double calcularTotalGastos(Date fechaInicio, Date fechaFin, int idUsuario) {
        double totalGastos = 0.0;
        String sql = "SELECT COALESCE(SUM(monto), 0) as total_gastos " +
                     "FROM gastos " +
                     "WHERE fecha_gasto BETWEEN ? AND ? " +
                     "AND id_usuario = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            java.sql.Timestamp fechaInicioSql = new java.sql.Timestamp(fechaInicio.getTime());
            java.sql.Timestamp fechaFinSql = new java.sql.Timestamp(fechaFin.getTime());
            
            ps.setTimestamp(1, fechaInicioSql);
            ps.setTimestamp(2, fechaFinSql);
            ps.setInt(3, idUsuario);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalGastos = rs.getDouble("total_gastos");
            }
            
        } catch (SQLException e) {
            System.out.println("Error al calcular total de gastos: " + e.getMessage());
            e.printStackTrace();
        }
        return totalGastos;
    }
}