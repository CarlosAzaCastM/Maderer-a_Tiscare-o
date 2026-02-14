package dao;

import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Producto;

public class ProductoDAO {

   
    public int registrarProductoPadre(Producto p) {
        String sql = "INSERT INTO productos (nombre) VALUES (?)";
        int idGenerado = -1;
        
        try (Connection con = Conexion.getConexion();
             // Usamos RETURN_GENERATED_KEYS para obtener el ID de la fila insertada
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, p.getNombreProducto());
            
            if (ps.executeUpdate() > 0) {
                // Obtener el ID generado por la base de datos
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error al registrar producto padre: " + e.getMessage());
        }
        return idGenerado;
    }
    
    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY nombre ASC";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setNombreProducto(rs.getString("nombre"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }
}