package dao;

import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}