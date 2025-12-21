package dao;

import config.Conexion;
import modelo.Variante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class VarianteDAO {
    
    public List<Variante> obtenerTodoElInventario() {
        List<Variante> lista = new ArrayList<>();
        // Hacemos el JOIN para tener ya el nombre del producto cargado en memoria
        String sql = "SELECT v.*, p.nombre FROM variantes v " +
                     "INNER JOIN productos p ON v.id_producto = p.id_producto " +
                     "ORDER BY p.nombre, v.clase, v.medida, v.grosor";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Variante v = new Variante();
                v.setId(rs.getInt("id_variante"));
                v.setNombreProducto(rs.getString("nombre")); // Ya tenemos el nombre aquí
                v.setClase(rs.getString("clase"));
                v.setMedida(rs.getString("medida"));
                v.setGrosor(rs.getString("grosor"));
                v.setPiesPorPieza(rs.getDouble("pies_por_pieza"));
                v.setCostoCompra(rs.getDouble("costo_compra"));
                v.setPrecioVenta(rs.getDouble("precio_venta"));
                v.setStockPiezas(rs.getInt("stock_piezas"));
                v.setStockMinimo(rs.getInt("stock_minimo"));
                lista.add(v);
            }
        } catch (Exception e) {
            System.out.println("Error cargando caché inventario: " + e.getMessage());
        }
        return lista;
    }

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
    
    // --- MÉTODOS PARA FILTROS EN CASCADA (VENTAS) ---

    // 1. Traer nombres de productos únicos (Tabla, Polin, etc.)
    public List<String> obtenerNombresProductos() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT nombre FROM productos ORDER BY nombre";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(rs.getString("nombre"));
        } catch (Exception e) { System.out.println("Error nombres: " + e.getMessage()); }
        return lista;
    }

    // 2. Traer Clases basado en el Producto seleccionado
    public List<String> obtenerClasesPorProducto(String nombreProducto) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT v.clase FROM variantes v " +
                     "INNER JOIN productos p ON v.id_producto = p.id_producto " +
                     "WHERE p.nombre = ? ORDER BY v.clase";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreProducto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getString("clase"));
        } catch (Exception e) { System.out.println("Error clases: " + e.getMessage()); }
        return lista;
    }

    // 3. Traer Medidas basado en Producto y Clase
    public List<String> obtenerMedidas(String nombreProducto, String clase) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT v.medida FROM variantes v " +
                     "INNER JOIN productos p ON v.id_producto = p.id_producto " +
                     "WHERE p.nombre = ? AND v.clase = ? ORDER BY v.medida";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreProducto);
            ps.setString(2, clase);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getString("medida"));
        } catch (Exception e) { System.out.println("Error medidas: " + e.getMessage()); }
        return lista;
    }

    // 4. Traer Grosores basado en Producto, Clase y Medida
    public List<String> obtenerGrosores(String nombreProducto, String clase, String medida) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT v.grosor FROM variantes v " +
                     "INNER JOIN productos p ON v.id_producto = p.id_producto " +
                     "WHERE p.nombre = ? AND v.clase = ? AND v.medida = ? ORDER BY v.grosor";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreProducto);
            ps.setString(2, clase);
            ps.setString(3, medida);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(rs.getString("grosor"));
        } catch (Exception e) { System.out.println("Error grosores: " + e.getMessage()); }
        return lista;
    }

    // 5. ¡EL IMPORTANTE! Buscar la variante exacta con todos los filtros
    public Variante buscarVarianteEspecifica(String nombre, String clase, String medida, String grosor) {
        Variante v = null;
        String sql = "SELECT v.*, p.nombre FROM variantes v " +
                     "INNER JOIN productos p ON v.id_producto = p.id_producto " +
                     "WHERE p.nombre = ? AND v.clase = ? AND v.medida = ? AND v.grosor = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, clase);
            ps.setString(3, medida);
            ps.setString(4, grosor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                v = new Variante();
                v.setId(rs.getInt("id_variante"));
                v.setNombreProducto(rs.getString("nombre"));
                v.setClase(rs.getString("clase"));
                v.setMedida(rs.getString("medida"));
                v.setGrosor(rs.getString("grosor"));
                v.setPiesPorPieza(rs.getDouble("pies_por_pieza"));
                v.setCostoCompra(rs.getDouble("costo_compra"));
                v.setPrecioVenta(rs.getDouble("precio_venta"));
                v.setStockPiezas(rs.getInt("stock_piezas"));
            }
        } catch (Exception e) { System.out.println("Error buscando variante: " + e.getMessage()); }
        return v;
    }
    
    public boolean registrarEntradaYActualizar(int idVariante, int cantidad, double nuevoCosto, double nuevoPrecio) {
        Connection con = null;
        PreparedStatement psEntrada = null;
        PreparedStatement psUpdate = null;
        
        String sqlEntrada = "INSERT INTO entradas_inventario (id_variante, cantidad_agregada, nuevo_costo_compra, nuevo_precio_venta) VALUES (?, ?, ?, ?)";
        
        // Sumamos stock y actualizamos precios
        String sqlUpdate = "UPDATE variantes SET stock_piezas = stock_piezas + ?, costo_compra = ?, precio_venta = ? WHERE id_variante = ?";
        
        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false); // INICIO DE LA TRANSACCIÓN (Pone pausa al guardado automático)

            // 1. Insertar en Historial
            psEntrada = con.prepareStatement(sqlEntrada);
            psEntrada.setInt(1, idVariante);
            psEntrada.setInt(2, cantidad);
            psEntrada.setDouble(3, nuevoCosto);
            psEntrada.setDouble(4, nuevoPrecio);
            psEntrada.executeUpdate();

            // 2. Actualizar Stock y Precios
            psUpdate = con.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, cantidad);
            psUpdate.setDouble(2, nuevoCosto);
            psUpdate.setDouble(3, nuevoPrecio);
            psUpdate.setInt(4, idVariante);
            int filas = psUpdate.executeUpdate();

            if (filas > 0) {
                con.commit(); // CONFIRMAR CAMBIOS (Todo salió bien)
                return true;
            } else {
                con.rollback(); // Cancelar si no se actualizó la variante
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error en transacción de entrada: " + e.getMessage());
            try {
                if (con != null) con.rollback(); // Cancelar todo si hubo error
            } catch (Exception ex) { }
            return false;
        } finally {
            // Cerramos recursos manualmente
            try {
                if (psEntrada != null) psEntrada.close();
                if (psUpdate != null) psUpdate.close();
                if (con != null) con.close();
            } catch (Exception ex) { }
        }
    }
    
    
}

