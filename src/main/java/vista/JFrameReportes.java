package vista;

import modelo.Usuario;
import javax.swing.event.ChangeEvent; // Importante
import javax.swing.event.ChangeListener;

public class JFrameReportes extends javax.swing.JFrame {
    private PanelReporteVentas panelVentas;
    private PanelReporteInventario panelInventario;
    private PanelReporteGastos panelGastos;
    private PanelReporteGanancias panelGanancia;
    private Usuario usuarioActual;
    
    // Agrega el logger
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFrameReportes.class.getName());

    public JFrameReportes(Usuario usuario) {
        initComponents();
        usuarioActual = usuario;
        // Crear los paneles
        panelVentas = new PanelReporteVentas(usuario, this);
        panelInventario = new PanelReporteInventario(usuario);
        panelGastos = new PanelReporteGastos(usuario);
        panelGanancia = new PanelReporteGanancias(usuario);

        // Agregar al JTabbedPane
        jTabbedPane1.addTab("Ventas", panelVentas);
        jTabbedPane1.addTab("Inventario", panelInventario);
        jTabbedPane1.addTab("Gastos", panelGastos);
        jTabbedPane1.addTab("Ganancias", panelGanancia);
        
        jTabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cargarPestañaActual();
            }
        });
        
        this.setExtendedState(MAXIMIZED_BOTH);
        
        panelVentas.asegurarCargaDatos();
    }
    
    private void cargarPestañaActual() {
        int indice = jTabbedPane1.getSelectedIndex();
        
        switch (indice) {
            case 0: // Ventas
                panelVentas.asegurarCargaDatos();
                break;
            case 1: // Inventario
                panelInventario.asegurarCargaDatos();
                break;
            case 2: // Gastos
                panelGastos.asegurarCargaDatos();
                break;
            case 3: // Ganancias
                panelGanancia.asegurarCargaDatos();
                break;
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backgroundInicio = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        btnHome = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        backgroundInicio.setBackground(new java.awt.Color(28, 28, 28));
        backgroundInicio.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(193, 168, 120));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Reportes");
        backgroundInicio.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 367, 38));

        jTabbedPane1.setBackground(new java.awt.Color(89, 66, 50));
        jTabbedPane1.setForeground(new java.awt.Color(248, 248, 248));
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        backgroundInicio.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 1250, 600));

        btnHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome.setOpaque(false);
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imgs/home.png"))); // NOI18N

        javax.swing.GroupLayout btnHomeLayout = new javax.swing.GroupLayout(btnHome);
        btnHome.setLayout(btnHomeLayout);
        btnHomeLayout.setHorizontalGroup(
            btnHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnHomeLayout.setVerticalGroup(
            btnHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        backgroundInicio.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(backgroundInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 1290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(backgroundInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 716, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        Menu menu = new Menu(usuarioActual);
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnHomeMouseClicked

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
            new JFrameReportes(userPrueba).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundInicio;
    private javax.swing.JPanel btnHome;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
