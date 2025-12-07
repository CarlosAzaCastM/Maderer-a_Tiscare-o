package dao;

import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.Usuario;

public class UsuarioDAO {
    
    public Usuario login(String username, String password) {
        Usuario usuario = null;
        // Consulta SQL basada en tu imagen de la tabla 'usuarios'
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password); // Nota: En sistemas reales, aquí se compara el Hash
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setUsernameUsuario(rs.getString("username"));
                usuario.setPasswordUsuario(rs.getString("password"));
                usuario.setNombreCompletoUsuario(rs.getString("nombre_completo"));
                usuario.setRolUsuario(rs.getString("rol")); // 'admin' o 'empleado'
            }
            
        } catch (Exception e) {
            System.out.println("Error en login: " + e.getMessage());
        }
        
        return usuario; // Devuelve el usuario si existe, o null si falló
    }
}