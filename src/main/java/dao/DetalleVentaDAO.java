package dao;

import config.Conexion;
import modelo.DetalleVentaHistorico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO {
    
    public List<DetalleVentaHistorico> obtenerDetallesPorVenta(int idVenta) {
        List<DetalleVentaHistorico> detalles = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = Conexion.getConexion();
            // Consulta para obtener detalles con información de variantes y productos
            String sql = "SELECT dv.*, v.clase, v.medida, v.grosor, p.nombre as nombre_producto "
                       + "FROM detalle_venta dv "
                       + "LEFT JOIN variantes v ON dv.id_variante = v.id_variante "
                       + "LEFT JOIN productos p ON v.id_producto = p.id_producto "
                       + "WHERE dv.id_venta = ?";
            
            ps = con.prepareStatement(sql);
            ps.setInt(1, idVenta);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                DetalleVentaHistorico detalle = new DetalleVentaHistorico();
                detalle.setIdDetalle(rs.getInt("id_detalle"));
                detalle.setIdVenta(rs.getInt("id_venta"));
                detalle.setIdVariante(rs.getInt("id_variante"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setPrecioVentaHistorico(rs.getDouble("precio_venta_historico"));
                detalle.setCostoCompraHistorico(rs.getDouble("costo_compra_historico"));
                detalle.setImporte(rs.getDouble("importe"));
                
                // Información de la variante
                detalle.setClase(rs.getString("clase"));
                detalle.setMedida(rs.getString("medida"));
                detalle.setGrosor(rs.getString("grosor"));
                detalle.setNombreProducto(rs.getString("nombre_producto"));
                
                detalles.add(detalle);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener detalles de venta: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        
        return detalles;
    }
}