package dao;

import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.DetalleVenta;
import modelo.Venta;

public class VentaDao {

    public boolean registrarVenta(Venta venta, List<DetalleVenta> detalles) {
        Connection con = null;
        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false); // INICIO TRANSACCIÓN

            // --- 1. OBTENER Y ACTUALIZAR FOLIO ---
            // Primero actualizamos para asegurar que nadie más tome este folio
            String sqlUpdateFolio = "UPDATE secuencia_ticket SET ultimo_folio = ultimo_folio + 1 WHERE id = 1";
            PreparedStatement psUpdateFolio = con.prepareStatement(sqlUpdateFolio);
            if (psUpdateFolio.executeUpdate() == 0) {
                // Si no actualizó nada (quizás la tabla está vacía), insertamos el primer registro
                // Ojo: Esto es solo un salvavidas por si la tabla secuencia_ticket está vacía
                Statement st = con.createStatement();
                st.executeUpdate("INSERT INTO secuencia_ticket (id, ultimo_folio) VALUES (1, 1)");
            }

            // Ahora recuperamos el folio que acabamos de generar
            String sqlGetFolio = "SELECT ultimo_folio FROM secuencia_ticket WHERE id = 1";
            PreparedStatement psGetFolio = con.prepareStatement(sqlGetFolio);
            ResultSet rsFolio = psGetFolio.executeQuery();
            
            int nuevoFolio = 0;
            if (rsFolio.next()) {
                nuevoFolio = rsFolio.getInt("ultimo_folio");
            } else {
                throw new SQLException("No se pudo generar el folio del ticket.");
            }

            // --- 2. INSERTAR LA VENTA (CON FOLIO) ---
            String sqlVenta = "INSERT INTO ventas (folio_ticket, subtotal, descuento_porcentaje, descuento_dinero, total, pago_efectivo, pago_tarjeta, id_usuario, estatus) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setInt(1, nuevoFolio); // <--- AQUÍ USAMOS EL FOLIO GENERADO
            psVenta.setDouble(2, venta.getSubtotal());
            psVenta.setInt(3, venta.getDescuentoPorcentaje());
            psVenta.setDouble(4, venta.getDescuentoDinero());
            psVenta.setDouble(5, venta.getTotal());
            psVenta.setDouble(6, venta.getPagoEfectivo());
            psVenta.setDouble(7, venta.getPagoTarjeta());
            psVenta.setInt(8, venta.getIdUsuario());
            psVenta.setString(9, "completada");
            
            psVenta.executeUpdate();
            
            // Obtener el ID interno de la venta (id_venta) para los detalles
            int idVenta = 0;
            ResultSet rs = psVenta.getGeneratedKeys();
            if (rs.next()) {
                idVenta = rs.getInt(1);
            }

            // --- 3. INSERTAR DETALLES Y RESTAR STOCK ---
            String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_variante, cantidad, precio_venta_historico, costo_compra_historico, importe) VALUES (?, ?, ?, ?, ?, ?)";
            String sqlStock = "UPDATE variantes SET stock_piezas = stock_piezas - ? WHERE id_variante = ?";
            
            PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
            PreparedStatement psStock = con.prepareStatement(sqlStock);

            for (DetalleVenta item : detalles) {
                // Detalle
                psDetalle.setInt(1, idVenta);
                psDetalle.setInt(2, item.getIdVariante());
                psDetalle.setInt(3, item.getCantidad());
                psDetalle.setDouble(4, item.getPrecioVenta());
                psDetalle.setDouble(5, item.getCostoCompra());
                psDetalle.setDouble(6, item.getSubtotal());
                psDetalle.addBatch();

                // Stock
                psStock.setInt(1, item.getCantidad());
                psStock.setInt(2, item.getIdVariante());
                psStock.addBatch();
            }

            psDetalle.executeBatch();
            psStock.executeBatch();

            // --- 4. CONFIRMAR TODO ---
            con.commit();
            
            // Opcional: Imprimir en consola qué folio se generó para debug
            System.out.println("Venta registrada con Folio Ticket: " + nuevoFolio);
            venta.setFolioTicket(nuevoFolio);
            return true;

        } catch (SQLException e) {
            System.out.println("Error en transacción venta: " + e.getMessage());
            try {
                if (con != null) con.rollback(); 
            } catch (SQLException ex) {
                System.out.println("Error en rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar: " + ex.getMessage());
            }
        }
    }
    
    public List<Venta> obtenerVentasPorFecha(java.util.Date fechaInicio, java.util.Date fechaFin, String estatus) {
    List<Venta> ventas = new ArrayList<>();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = Conexion.getConexion();
        // Hacemos JOIN con la tabla usuarios para obtener el nombre_completo
        String sql = "SELECT v.folio_ticket, v.fecha_venta, v.subtotal, v.descuento_dinero, v.total, " +
                     "v.pago_efectivo, v.pago_tarjeta, v.id_usuario, u.nombre_completo, v.estatus " +
                     "FROM ventas v " +
                     "LEFT JOIN usuarios u ON v.id_usuario = u.id_usuario " +
                     "WHERE v.fecha_venta BETWEEN ? AND ?";

        if (estatus != null && !estatus.isEmpty()) {
            sql += " AND v.estatus = ?";
        }

        sql += " ORDER BY v.fecha_venta DESC";

        ps = con.prepareStatement(sql);
        ps.setTimestamp(1, new java.sql.Timestamp(fechaInicio.getTime()));
        ps.setTimestamp(2, new java.sql.Timestamp(fechaFin.getTime()));

        if (estatus != null && !estatus.isEmpty()) {
            ps.setString(3, estatus);
        }

        rs = ps.executeQuery();

        while (rs.next()) {
            Venta venta = new Venta();
            venta.setFolioTicket(rs.getInt("folio_ticket"));
            venta.setFechaVenta(rs.getTimestamp("fecha_venta").toString());
            venta.setSubtotal(rs.getDouble("subtotal"));
            venta.setDescuentoDinero(rs.getDouble("descuento_dinero"));
            venta.setTotal(rs.getDouble("total"));
            venta.setPagoEfectivo(rs.getDouble("pago_efectivo"));
            venta.setPagoTarjeta(rs.getDouble("pago_tarjeta"));
            venta.setIdUsuario(rs.getInt("id_usuario"));
            venta.setNombreUsuario(rs.getString("nombre_completo")); // Usamos el nombre, no el ID
            venta.setEstatus(rs.getString("estatus"));
            ventas.add(venta);
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener ventas: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    return ventas;
}
    
    // Método para actualizar el estatus de una venta
    public boolean actualizarEstatusVenta(int idVenta, String nuevoEstatus) {
        Connection con = null;
        PreparedStatement ps = null;
        
        try {
            con = Conexion.getConexion();
            String sql = "UPDATE ventas SET estatus = ? WHERE id_venta = ?";
            
            ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstatus);
            ps.setInt(2, idVenta);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar estatus de venta: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
    
    // Método para obtener venta por folio
    public Venta obtenerVentaPorFolio(int folioTicket) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = Conexion.getConexion();
        // Hacemos JOIN con la tabla usuarios para obtener el nombre_completo
        String sql = "SELECT v.id_venta, v.folio_ticket, v.fecha_venta, v.subtotal, v.descuento_dinero, " +
                     "v.total, v.pago_efectivo, v.pago_tarjeta, v.id_usuario, u.nombre_completo, v.estatus " +
                     "FROM ventas v " +
                     "LEFT JOIN usuarios u ON v.id_usuario = u.id_usuario " +
                     "WHERE v.folio_ticket = ?";

        ps = con.prepareStatement(sql);
        ps.setInt(1, folioTicket);
        rs = ps.executeQuery();

        if (rs.next()) {
            Venta venta = new Venta();
            venta.setIdVenta(rs.getInt("id_venta")); // Para actualizar el estatus
            venta.setFolioTicket(rs.getInt("folio_ticket"));
            venta.setFechaVenta(rs.getTimestamp("fecha_venta").toString());
            venta.setSubtotal(rs.getDouble("subtotal"));
            venta.setDescuentoDinero(rs.getDouble("descuento_dinero"));
            venta.setTotal(rs.getDouble("total"));
            venta.setPagoEfectivo(rs.getDouble("pago_efectivo"));
            venta.setPagoTarjeta(rs.getDouble("pago_tarjeta"));
            venta.setIdUsuario(rs.getInt("id_usuario"));
            venta.setNombreUsuario(rs.getString("nombre_completo")); // Usamos el nombre, no el ID
            venta.setEstatus(rs.getString("estatus"));
            return venta;
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener venta por folio: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    return null;
}
}