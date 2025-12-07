package dao;

import config.Conexion;
import modelo.Variante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VarianteDAO {

    public List<Variante> listarInventario(String busqueda) {
        List<Variante> lista = new ArrayList<>();
        
        // SQL: Unimos variantes con productos para obtener el nombre (Ej: "Tabla")
        String sql = "SELECT v.id_variante, p.nombre, v.clase, v.medida, v.grosor, " +
                     "v.pies_por_pieza, v.costo_compra, v.precio_venta, v.stock_piezas " +
                     "FROM variantes v " +
                     "INNER JOIN productos p ON v.id_producto = p.id_producto " +
                     "WHERE p.nombre LIKE ? OR v.medida LIKE ? " +
                     "ORDER BY p.nombre, v.clase, v.medida"; // Ordenado para que se vea bonito

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            // Buscamos por nombre o por medida
            ps.setString(1, "%" + busqueda + "%");
            ps.setString(2, "%" + busqueda + "%");
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Variante v = new Variante();
                v.setId(rs.getInt("id_variante"));
                v.setNombreProducto(rs.getString("nombre"));
                v.setClase(rs.getString("clase"));
                v.setMedida(rs.getString("medida"));
                v.setGrosor(rs.getString("grosor"));
                v.setPiesPorPieza(rs.getDouble("pies_por_pieza"));
                v.setCostoCompra(rs.getDouble("costo_compra"));
                v.setPrecioVenta(rs.getDouble("precio_venta"));
                v.setStockPiezas(rs.getInt("stock_piezas"));
                
                lista.add(v);
            }
        } catch (Exception e) {
            System.out.println("Error en DAO: " + e.getMessage());
        }
        return lista;
    }
    
    // 1. Método para obtener la lista de productos padre para el ComboBox
    public List<Variante> listarProductosPadre() {
        List<Variante> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Variante v = new Variante();
                v.setId(rs.getInt("id_producto")); // Usamos el ID de variante temporalmente para guardar el ID producto
                v.setNombreProducto(rs.getString("nombre"));
                lista.add(v);
            }
        } catch (Exception e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    // 2. Método para GUARDAR (Nuevo)
    // Modificado para devolver el ID generado (int) en lugar de boolean
    public int registrar(Variante v) {
        String sql = "INSERT INTO variantes (id_producto, clase, medida, grosor, pies_por_pieza, costo_compra, precio_venta, stock_piezas) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Usamos RETURN_GENERATED_KEYS para obtener el ID auto-incrementable
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, v.getId()); 
            ps.setString(2, v.getClase());
            ps.setString(3, v.getMedida());
            ps.setString(4, v.getGrosor());
            ps.setDouble(5, v.getPiesPorPieza());
            ps.setDouble(6, v.getCostoCompra());
            ps.setDouble(7, v.getPrecioVenta());
            ps.setInt(8, v.getStockPiezas());
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Retorna el ID nuevo (ej: 58)
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al registrar: " + e.getMessage());
        }
        return -1; // Retorna -1 si falló
    }

    // 3. Método para EDITAR (Modificar precios)
    public boolean modificar(Variante v) {
        String sql = "UPDATE variantes SET clase=?, medida=?, grosor=?, pies_por_pieza=?, costo_compra=?, precio_venta=? WHERE id_variante=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, v.getClase());
            ps.setString(2, v.getMedida());
            ps.setString(3, v.getGrosor());
            ps.setDouble(4, v.getPiesPorPieza());
            ps.setDouble(5, v.getCostoCompra());
            ps.setDouble(6, v.getPrecioVenta());
            ps.setInt(7, v.getId()); // ID de la variante a editar
            
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error al modificar: " + e.getMessage());
            return false;
        }
    }
    
    // 4. Método para ACTUALIZAR STOCK Y PRECIOS (Usado en el modal Actualizar)
    public boolean actualizarStockYPrecios(int idVariante, int cantidadAgregar, double nuevoCosto, double nuevoPrecio) {
        // La lógica es: Stock Nuevo = Stock Viejo + Cantidad Agregada
        String sql = "UPDATE variantes SET "
                   + "stock_piezas = stock_piezas + ?, "
                   + "costo_compra = ?, "
                   + "precio_venta = ? "
                   + "WHERE id_variante = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, cantidadAgregar);
            ps.setDouble(2, nuevoCosto);
            ps.setDouble(3, nuevoPrecio);
            ps.setInt(4, idVariante);
            
            int filas = ps.executeUpdate();
            return filas > 0;
            
        } catch (Exception e) {
            System.out.println("Error al actualizar stock/precios: " + e.getMessage());
            return false;
        }
    }
    
    
    
}

