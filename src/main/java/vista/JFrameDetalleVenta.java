package vista;

import dao.DetalleVentaDAO;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import modelo.DetalleVentaHistorico;
import modelo.Usuario;
import modelo.Venta;

public class JFrameDetalleVenta extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFrameDetalleVenta.class.getName());
    private Usuario usuarioActual;
    private Venta ventaSeleccionada;
    private DefaultTableModel tableModel;
    private DetalleVentaDAO detalleVentaDao;
    private JFrameReportes framePadre;
    
    public JFrameDetalleVenta(Usuario usuario, Venta venta, JFrameReportes padre) {
        initComponents();
        usuarioActual = usuario;
        ventaSeleccionada = venta;
        this.framePadre = padre;
        detalleVentaDao = new DetalleVentaDAO();
        
        configurarTabla();
        cargarDetallesVenta();
        
        this.setExtendedState(MAXIMIZED_BOTH);
        
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                regresarAlPadre();
            }
        });
    }
    
    private void regresarAlPadre() {
        if (framePadre != null) {
            framePadre.setVisible(true); // ¡Solo la mostramos! No recargamos nada.
        } else {
            // Fallback por si entramos directo (pruebas)
            new JFrameReportes(usuarioActual).setVisible(true);
        }
        this.dispose(); // Destruimos solo el detalle
    }
    
    private void configurarTabla() {
        // Configurar el modelo de la tabla
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        
        // Definir las columnas para el detalle de venta
        String[] columnas = {
            "Cantidad",
            "Producto", 
            "Clase", 
            "Medida", 
            "Grosor", 
            "Precio Unitario", 
            "Importe",
            "Costo Histórico"
        };
        tableModel.setColumnIdentifiers(columnas);
        jTableDetalleVenta.setModel(tableModel);
        
        // Ajustar el ancho de las columnas
        jTableDetalleVenta.getColumnModel().getColumn(0).setPreferredWidth(80);  // Cantidad
        jTableDetalleVenta.getColumnModel().getColumn(1).setPreferredWidth(150); // Producto
        jTableDetalleVenta.getColumnModel().getColumn(2).setPreferredWidth(100); // Clase
        jTableDetalleVenta.getColumnModel().getColumn(3).setPreferredWidth(100); // Medida
        jTableDetalleVenta.getColumnModel().getColumn(4).setPreferredWidth(100); // Grosor
        jTableDetalleVenta.getColumnModel().getColumn(5).setPreferredWidth(120); // Precio
        jTableDetalleVenta.getColumnModel().getColumn(6).setPreferredWidth(120); // Importe
        jTableDetalleVenta.getColumnModel().getColumn(7).setPreferredWidth(120); // Costo
        
        // Aumentar la altura de las filas
        jTableDetalleVenta.setRowHeight(40);
        
        // Aumentar el tamaño de la fuente
        jTableDetalleVenta.setFont(new java.awt.Font("Segoe UI", 0, 16));
        jTableDetalleVenta.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 16));
    }
    
    private void cargarDetallesVenta() {
        if (ventaSeleccionada != null) {
            // Actualizar título con el folio
            jLabelTitulo.setText("Detalle Venta - Folio: " + ventaSeleccionada.getFolioTicket());
            
            // Obtener detalles de la venta
            List<DetalleVentaHistorico> detalles = detalleVentaDao.obtenerDetallesPorVenta(ventaSeleccionada.getIdVenta());
            
            // Limpiar tabla
            tableModel.setRowCount(0);
            
            // Formato para moneda
            DecimalFormat df = new DecimalFormat("$#,##0.00");
            
            // Agregar detalles a la tabla
            for (DetalleVentaHistorico detalle : detalles) {
                Object[] fila = {
                    detalle.getCantidad(),
                    detalle.getNombreProducto() != null ? detalle.getNombreProducto() : "N/A",
                    detalle.getClase() != null ? detalle.getClase() : "N/A",
                    detalle.getMedida() != null ? detalle.getMedida() : "N/A",
                    detalle.getGrosor() != null ? detalle.getGrosor() : "N/A",
                    df.format(detalle.getPrecioVentaHistorico()),
                    df.format(detalle.getImporte()),
                    df.format(detalle.getCostoCompraHistorico())
                };
                tableModel.addRow(fila);
            }
            
            // Mostrar resumen de la venta en la consola (o podrías agregar labels)
            System.out.println("Detalles cargados para venta ID: " + ventaSeleccionada.getIdVenta() + 
                             ", Folio: " + ventaSeleccionada.getFolioTicket());
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        btnHome = new javax.swing.JPanel();
        jPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDetalleVenta = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(28, 28, 28));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTitulo.setBackground(new java.awt.Color(193, 166, 120));
        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(193, 166, 120));
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("Detalle Venta");
        jPanel1.add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 520, -1));

        btnHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome.setOpaque(false);
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout btnHomeLayout = new javax.swing.GroupLayout(btnHome);
        btnHome.setLayout(btnHomeLayout);
        btnHomeLayout.setHorizontalGroup(
            btnHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );
        btnHomeLayout.setVerticalGroup(
            btnHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel1.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 70));

        jPanel.setBackground(new java.awt.Color(62, 44, 32));
        jPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTableDetalleVenta.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTableDetalleVenta);

        jPanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 1070, 490));

        jPanel1.add(jPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 1240, 560));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imgs/backIcon.png"))); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 64, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 847, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        regresarAlPadre();
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
            // Para prueba, puedes pasar null como venta
            new JFrameDetalleVenta(userPrueba, null, null).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnHome;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableDetalleVenta;
    // End of variables declaration//GEN-END:variables
}
