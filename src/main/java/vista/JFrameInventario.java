
package vista;

import dao.VarianteDAO;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import modelo.Usuario;
import modelo.Variante;

public class JFrameInventario extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFrameInventario.class.getName());

    DefaultTableModel modelo;
    List<Variante> listaActual;
    private Usuario usuarioActual;

    public JFrameInventario(Usuario usuario) {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        usuarioActual = usuario;
        configurarTabla(); // 1. Configuramos las columnas
        cargarTabla("");
    }

    private void configurarTabla() {
        String[] titulos = {"Cant", "Producto", "Clase", "Medida", "Grosor", "Ft Total", "Costo C", "Costo V"};
        modelo = new DefaultTableModel(null, titulos) {
            // Esto hace que el usuario no pueda editar las celdas directamente (solo lectura)
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTableInventario.setModel(modelo);
        
        jTableInventario.setRowHeight(35);
        
        // Opcional: Ajustar ancho de columnas específicas (Ej: Cantidad más pequeña)
        jTableInventario.getColumnModel().getColumn(0).setPreferredWidth(50); // Cant
        jTableInventario.getColumnModel().getColumn(1).setPreferredWidth(150); // Producto
    }

    // Método reutilizable para llenar datos
    public void cargarTabla(String buscar) {
        VarianteDAO dao = new VarianteDAO();
        listaActual = dao.listarInventario(buscar);

        modelo.setRowCount(0); // Limpiar tabla antes de llenar

        for (Variante v : listaActual) {
            // Cálculo visual de pies totales
            double piesTotales = v.getStockPiezas() * v.getPiesPorPieza();

            Object[] fila = new Object[8];
            fila[0] = v.getStockPiezas(); 
            fila[1] = v.getNombreProducto();
            fila[2] = v.getClase();
            fila[3] = v.getMedida();
            fila[4] = v.getGrosor();
            
            // FORMATOS DE NÚMEROS
            fila[5] = String.format("%.2f ft", piesTotales);     // Ej: "23.33 ft"
            fila[6] = String.format("$ %.2f", v.getCostoCompra()); // Ej: "$ 20.50"
            fila[7] = String.format("$ %.2f", v.getPrecioVenta()); // Ej: "$ 25.00"

            modelo.addRow(fila);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableInventario = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        btnAgregarProducto = new javax.swing.JPanel();
        labelMas = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnActualizarProducto = new javax.swing.JPanel();
        labelMenos = new javax.swing.JLabel();
        btnHome = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(28, 28, 28));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(193, 166, 120));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(193, 166, 120));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Inventario");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, 181, 42));

        txtBuscar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 70, 184, -1));

        btnBuscar.setBackground(new java.awt.Color(28, 28, 28));
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBuscarMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(236, 236, 236));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Buscar");

        javax.swing.GroupLayout btnBuscarLayout = new javax.swing.GroupLayout(btnBuscar);
        btnBuscar.setLayout(btnBuscarLayout);
        btnBuscarLayout.setHorizontalGroup(
            btnBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnBuscarLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        btnBuscarLayout.setVerticalGroup(
            btnBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 60, -1, -1));

        jTableInventario.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTableInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTableInventario);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, 907, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(235, 235, 235));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Agregar Producto");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 560, 180, 37));

        btnAgregarProducto.setBackground(new java.awt.Color(113, 221, 156));
        btnAgregarProducto.setToolTipText("");
        btnAgregarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarProductoMouseClicked(evt);
            }
        });

        labelMas.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        labelMas.setForeground(new java.awt.Color(235, 235, 235));
        labelMas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMas.setText("+");

        javax.swing.GroupLayout btnAgregarProductoLayout = new javax.swing.GroupLayout(btnAgregarProducto);
        btnAgregarProducto.setLayout(btnAgregarProductoLayout);
        btnAgregarProductoLayout.setHorizontalGroup(
            btnAgregarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnAgregarProductoLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(labelMas)
                .addContainerGap(71, Short.MAX_VALUE))
        );
        btnAgregarProductoLayout.setVerticalGroup(
            btnAgregarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnAgregarProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelMas, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel1.add(btnAgregarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 600, 160, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(235, 235, 235));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Actualizar Producto");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 560, 180, 37));

        btnActualizarProducto.setBackground(new java.awt.Color(121, 140, 215));
        btnActualizarProducto.setToolTipText("");
        btnActualizarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarProductoMouseClicked(evt);
            }
        });

        labelMenos.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        labelMenos.setForeground(new java.awt.Color(235, 235, 235));
        labelMenos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelMenos.setText("actualizar");

        javax.swing.GroupLayout btnActualizarProductoLayout = new javax.swing.GroupLayout(btnActualizarProducto);
        btnActualizarProducto.setLayout(btnActualizarProductoLayout);
        btnActualizarProductoLayout.setHorizontalGroup(
            btnActualizarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnActualizarProductoLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(labelMenos, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        btnActualizarProductoLayout.setVerticalGroup(
            btnActualizarProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnActualizarProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelMenos, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel1.add(btnActualizarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 600, 160, 40));

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

        jPanel1.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1227, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 713, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarMouseClicked
        cargarTabla(txtBuscar.getText());
    }//GEN-LAST:event_btnBuscarMouseClicked

    private void btnAgregarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarProductoMouseClicked
        FormularioProducto modal = new FormularioProducto(this, true);
        modal.setVisible(true);
    }//GEN-LAST:event_btnAgregarProductoMouseClicked

    private void btnActualizarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarProductoMouseClicked
        // 1. Verificar si seleccionó una fila
        int fila = jTableInventario.getSelectedRow();
        
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla primero.");
            return;
        }
        
        // 2. Obtener el objeto de la lista en memoria
        // (Asegúrate que tu listaActual esté declarada globalmente como hicimos antes)
        if (listaActual != null && fila < listaActual.size()) {
            Variante vSeleccionada = listaActual.get(fila);
            
            // 3. Abrir el modal pasándole el objeto
            ActulizarProducto modal = new ActulizarProducto(this, true, vSeleccionada);
            modal.setVisible(true);
        }
    }//GEN-LAST:event_btnActualizarProductoMouseClicked

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        Menu menu = new Menu(usuarioActual);
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnHomeMouseClicked

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
            new JFrameInventario(userPrueba).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnActualizarProducto;
    private javax.swing.JPanel btnAgregarProducto;
    private javax.swing.JPanel btnBuscar;
    private javax.swing.JPanel btnHome;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableInventario;
    private javax.swing.JLabel labelMas;
    private javax.swing.JLabel labelMenos;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
