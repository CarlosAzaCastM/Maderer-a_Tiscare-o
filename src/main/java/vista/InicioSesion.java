
package vista;


public class InicioSesion extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(InicioSesion.class.getName());

    public InicioSesion() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backgroundInicio = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnIniciarSesion = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtContraseña = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        backgroundInicio.setBackground(new java.awt.Color(28, 28, 28));
        backgroundInicio.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(193, 168, 120));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LA MEJOR CALIDAD AL MEJOR PRECIO");
        backgroundInicio.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 160, 367, 38));

        jLabel2.setFont(new java.awt.Font("Poor Richard", 1, 60)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(26, 87, 29));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Maderería Tiscareño");
        backgroundInicio.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 50, 513, 105));

        txtUsuario.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        backgroundInicio.add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 290, 310, 50));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(239, 239, 239));
        jLabel3.setText("Usuario");
        backgroundInicio.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 240, 120, 40));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(239, 239, 239));
        jLabel4.setText("Contraseña");
        backgroundInicio.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 360, 160, 40));

        btnIniciarSesion.setBackground(new java.awt.Color(62, 44, 32));
        btnIniciarSesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIniciarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnIniciarSesionMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(239, 239, 239));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Iniciar");

        javax.swing.GroupLayout btnIniciarSesionLayout = new javax.swing.GroupLayout(btnIniciarSesion);
        btnIniciarSesion.setLayout(btnIniciarSesionLayout);
        btnIniciarSesionLayout.setHorizontalGroup(
            btnIniciarSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnIniciarSesionLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        btnIniciarSesionLayout.setVerticalGroup(
            btnIniciarSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnIniciarSesionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        backgroundInicio.add(btnIniciarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 520, 170, 50));

        txtContraseña.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        backgroundInicio.add(txtContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 410, 310, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundInicio, javax.swing.GroupLayout.DEFAULT_SIZE, 1281, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundInicio, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarSesionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIniciarSesionMouseClicked
        // 1. Obtener datos
        String user = txtUsuario.getText();
        String pass = new String(txtContraseña.getPassword()); // JPasswordField requiere esto
        
        if (user.isEmpty() || pass.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor llena todos los campos.");
            return;
        }
        
        // 2. Consultar BD
        dao.UsuarioDAO dao = new dao.UsuarioDAO();
        modelo.Usuario usuarioLogueado = dao.login(user, pass);
        
        if (usuarioLogueado != null) {
            // 3. ¡Éxito! Abrir el menú pasándole el usuario
            Menu menu = new Menu(usuarioLogueado);
            menu.setVisible(true);
            this.dispose(); // Cerrar ventana de login
        } else {
            // 4. Error
            javax.swing.JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
        }
    }//GEN-LAST:event_btnIniciarSesionMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
         /* Set the Nimbus look and feel */
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException | InstantiationException |
             IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        logger.log(java.util.logging.Level.SEVERE, null, ex);
    }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new InicioSesion().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundInicio;
    private javax.swing.JPanel btnIniciarSesion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField txtContraseña;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
