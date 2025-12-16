package dao;

import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import modelo.EntradaInventario;

public class EntradaInventarioDAO {
    
    public List<EntradaInventario> obtenerEntradasPorFecha(Date fechaInicio, Date fechaFin) {
        List<EntradaInventario> entradas = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = Conexion.getConexion();
            String sql = "SELECT ei.*, p.nombre as nombre_producto, v.clase, v.medida, v.grosor " +
                         "FROM entradas_inventario ei " +
                         "INNER JOIN variantes v ON ei.id_variante = v.id_variante " +
                         "INNER JOIN productos p ON v.id_producto = p.id_producto " +
                         "WHERE ei.fecha_entrada BETWEEN ? AND ? " +
                         "ORDER BY ei.fecha_entrada DESC";
            
            ps = con.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(fechaInicio.getTime()));
            ps.setTimestamp(2, new Timestamp(fechaFin.getTime()));
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                EntradaInventario entrada = new EntradaInventario();
                entrada.setIdEntrada(rs.getInt("id_entrada"));
                entrada.setIdVariante(rs.getInt("id_variante"));
                entrada.setCantidadAgregada(rs.getInt("cantidad_agregada"));
                entrada.setFechaEntrada(rs.getTimestamp("fecha_entrada").toString());
                
                // IMPORTANTE: usar getDouble() no getString()
                entrada.setNuevoCostoCompra(rs.getDouble("nuevo_costo_compra"));
                entrada.setNuevoPrecioVenta(rs.getDouble("nuevo_precio_venta"));
                
                // Campos adicionales
                entrada.setNombreProducto(rs.getString("nombre_producto"));
                entrada.setClase(rs.getString("clase"));
                entrada.setMedida(rs.getString("medida"));
                entrada.setGrosor(rs.getString("grosor"));
                
                entradas.add(entrada);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener entradas de inventario: " + e.getMessage());
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
        
        return entradas;
    }
}