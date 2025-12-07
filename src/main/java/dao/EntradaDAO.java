package dao;

import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EntradaDAO {

    public boolean registrarEntrada(int idVariante, int cantidad, double costo, double precio) {
        String sql = "INSERT INTO entradas_inventario (id_variante, cantidad_agregada, nuevo_costo_compra, nuevo_precio_venta) VALUES (?, ?, ?, ?)";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idVariante);
            ps.setInt(2, cantidad);
            ps.setDouble(3, costo);
            ps.setDouble(4, precio);
            
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al registrar entrada: " + e.getMessage());
            return false;
        }
    }
}