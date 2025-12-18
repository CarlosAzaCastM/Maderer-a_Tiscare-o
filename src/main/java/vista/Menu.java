
package vista;
import javax.swing.JFrame;
import modelo.Usuario;


public class Menu extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Menu.class.getName());

    private Usuario usuarioActual;

    public Menu(Usuario usuario) {
        initComponents();
        getContentPane().setBackground(new java.awt.Color(28, 28, 28));
        this.usuarioActual = usuario;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        aplicarPermisos();

        this.setTitle("Bienvenido: " + usuario.getNombreCompletoUsuario());
    }

    private void aplicarPermisos() {
        // Si el rol es 'empleado', ocultamos cosas sensibles
        if (usuarioActual != null && usuarioActual.getRolUsuario().equalsIgnoreCase("empleado")) {
            btnGastos.setVisible(false);  
            btnReportes.setVisible(false); 
            btnInventario.setVisible(false);
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgMenu = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnVenta = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnInventario = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnGastos = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnReportes = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btnCancelarVenta = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(1127, 640));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bgMenu.setBackground(new java.awt.Color(28, 28, 28));
        bgMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(193, 168, 120));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LA MEJOR CALIDAD AL MEJOR PRECIO");
        bgMenu.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 160, 420, 38));

        jLabel2.setFont(new java.awt.Font("Poor Richard", 1, 60)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(26, 87, 29));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Maderería Tiscareño");
        bgMenu.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 50, 513, 105));

        btnVenta.setBackground(new java.awt.Color(62, 44, 32));
        btnVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVenta.setMaximumSize(new java.awt.Dimension(347, 53));
        btnVenta.setMinimumSize(new java.awt.Dimension(347, 53));
        btnVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVentaMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(241, 241, 241));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Venta");
        jLabel4.setMaximumSize(new java.awt.Dimension(167, 32));
        jLabel4.setMinimumSize(new java.awt.Dimension(167, 32));
        jLabel4.setPreferredSize(new java.awt.Dimension(167, 32));

        javax.swing.GroupLayout btnVentaLayout = new javax.swing.GroupLayout(btnVenta);
        btnVenta.setLayout(btnVentaLayout);
        btnVentaLayout.setHorizontalGroup(
            btnVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnVentaLayout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );
        btnVentaLayout.setVerticalGroup(
            btnVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bgMenu.add(btnVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 260, -1, 50));

        btnInventario.setBackground(new java.awt.Color(62, 44, 32));
        btnInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInventario.setMaximumSize(new java.awt.Dimension(347, 53));
        btnInventario.setMinimumSize(new java.awt.Dimension(347, 53));
        btnInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnInventarioMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(241, 241, 241));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Inventario");
        jLabel5.setMaximumSize(new java.awt.Dimension(167, 32));
        jLabel5.setMinimumSize(new java.awt.Dimension(167, 32));
        jLabel5.setPreferredSize(new java.awt.Dimension(167, 32));

        javax.swing.GroupLayout btnInventarioLayout = new javax.swing.GroupLayout(btnInventario);
        btnInventario.setLayout(btnInventarioLayout);
        btnInventarioLayout.setHorizontalGroup(
            btnInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnInventarioLayout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
        );
        btnInventarioLayout.setVerticalGroup(
            btnInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bgMenu.add(btnInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 400, -1, 50));

        btnGastos.setBackground(new java.awt.Color(62, 44, 32));
        btnGastos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGastos.setMaximumSize(new java.awt.Dimension(347, 53));
        btnGastos.setMinimumSize(new java.awt.Dimension(347, 53));
        btnGastos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGastosMouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(241, 241, 241));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Gastos");
        jLabel6.setMaximumSize(new java.awt.Dimension(167, 32));
        jLabel6.setMinimumSize(new java.awt.Dimension(167, 32));
        jLabel6.setPreferredSize(new java.awt.Dimension(167, 32));

        javax.swing.GroupLayout btnGastosLayout = new javax.swing.GroupLayout(btnGastos);
        btnGastos.setLayout(btnGastosLayout);
        btnGastosLayout.setHorizontalGroup(
            btnGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnGastosLayout.createSequentialGroup()
                .addContainerGap(87, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86))
        );
        btnGastosLayout.setVerticalGroup(
            btnGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnGastosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        bgMenu.add(btnGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 470, -1, 50));

        btnReportes.setBackground(new java.awt.Color(62, 44, 32));
        btnReportes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReportes.setMaximumSize(new java.awt.Dimension(347, 53));
        btnReportes.setMinimumSize(new java.awt.Dimension(347, 53));
        btnReportes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportesMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(241, 241, 241));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Reportes");
        jLabel3.setMaximumSize(new java.awt.Dimension(167, 32));
        jLabel3.setMinimumSize(new java.awt.Dimension(167, 32));
        jLabel3.setPreferredSize(new java.awt.Dimension(167, 32));

        javax.swing.GroupLayout btnReportesLayout = new javax.swing.GroupLayout(btnReportes);
        btnReportes.setLayout(btnReportesLayout);
        btnReportesLayout.setHorizontalGroup(
            btnReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnReportesLayout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        btnReportesLayout.setVerticalGroup(
            btnReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnReportesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        bgMenu.add(btnReportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 540, -1, 50));

        btnLogout.setBackground(new java.awt.Color(28, 28, 28));
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imgs/cerrar-sesion.png"))); // NOI18N

        javax.swing.GroupLayout btnLogoutLayout = new javax.swing.GroupLayout(btnLogout);
        btnLogout.setLayout(btnLogoutLayout);
        btnLogoutLayout.setHorizontalGroup(
            btnLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnLogoutLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel7)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        btnLogoutLayout.setVerticalGroup(
            btnLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnLogoutLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel7)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        bgMenu.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 60, -1));

        btnCancelarVenta.setBackground(new java.awt.Color(62, 44, 32));
        btnCancelarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelarVenta.setMaximumSize(new java.awt.Dimension(347, 53));
        btnCancelarVenta.setMinimumSize(new java.awt.Dimension(347, 53));
        btnCancelarVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarVentaMouseClicked(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(241, 241, 241));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Cancelar Venta");

        javax.swing.GroupLayout btnCancelarVentaLayout = new javax.swing.GroupLayout(btnCancelarVenta);
        btnCancelarVenta.setLayout(btnCancelarVentaLayout);
        btnCancelarVentaLayout.setHorizontalGroup(
            btnCancelarVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnCancelarVentaLayout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );
        btnCancelarVentaLayout.setVerticalGroup(
            btnCancelarVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnCancelarVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bgMenu.add(btnCancelarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 330, -1, 50));

        getContentPane().add(bgMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1281, 650));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVentaMouseClicked
        JFrameVenta venta = new JFrameVenta(usuarioActual);
        venta.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVentaMouseClicked

    private void btnInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInventarioMouseClicked
        JFrameInventario inventarioFrame = new JFrameInventario(usuarioActual);
        inventarioFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnInventarioMouseClicked

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        InicioSesion iniciarSesion = new InicioSesion();
        iniciarSesion.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void btnGastosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGastosMouseClicked
        JFrameGasto gastoFrame = new JFrameGasto(usuarioActual);
        gastoFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnGastosMouseClicked

    private void btnCancelarVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarVentaMouseClicked
        JFrameCancelarVenta cancelarVentaFrame = new JFrameCancelarVenta(usuarioActual);
        cancelarVentaFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCancelarVentaMouseClicked

    private void btnReportesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportesMouseClicked
        JFrameReportes reportesFrame = new JFrameReportes(usuarioActual);
        reportesFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnReportesMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            Usuario userPrueba = new Usuario();
            userPrueba.setNombreCompletoUsuario("Modo Prueba");
            userPrueba.setRolUsuario("admin"); 
            new Menu(userPrueba).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bgMenu;
    private javax.swing.JPanel btnCancelarVenta;
    private javax.swing.JPanel btnGastos;
    private javax.swing.JPanel btnInventario;
    private javax.swing.JPanel btnLogout;
    private javax.swing.JPanel btnReportes;
    private javax.swing.JPanel btnVenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    // End of variables declaration//GEN-END:variables
}
