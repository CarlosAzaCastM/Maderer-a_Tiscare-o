package vista;

import dao.EntradaDAO;
import dao.VarianteDAO;
import javax.swing.JOptionPane;
import modelo.DetalleVenta;
import modelo.Variante;

public class JDialogEditarVenta extends javax.swing.JDialog {
    
    private DetalleVenta itemActual;
    private JFrameVenta padre;
    
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JDialogEditarVenta.class.getName());

    public JDialogEditarVenta(java.awt.Frame parent, boolean modal, DetalleVenta item) {
        super(parent, modal);
        this.padre = (JFrameVenta) parent;
        this.itemActual = item;
        initComponents();
        this.setLocationRelativeTo(null);
        
        cargarDatosActuales();
    }
    
    private void cargarDatosActuales() {
        if (itemActual != null) {
            // Etiquetas informativas
            txtProducto.setText(itemActual.getNombre());
            txtMedida.setText(itemActual.getMedida());
            txtGrosor.setText(itemActual.getGrosor());
            txtClase.setText(itemActual.getClase());
            
            // Campos editables
            txtCantidad.setText(String.valueOf(itemActual.getCantidad()));
            txtPrecio.setText(String.valueOf(itemActual.getPrecioVenta()));
            
            // Calcular pies actual
            txtFtTotal.setText(String.format("%.2f", itemActual.getFtTotal()));
        }
    }
    
    private void ejecutarEdicion() {
        try {
            int nuevaCant = Integer.parseInt(txtCantidad.getText());
            double nuevoPrecio = Double.parseDouble(txtPrecio.getText());

            if (nuevaCant <= 0 || nuevoPrecio < 0) {
                JOptionPane.showMessageDialog(this, "Datos inválidos.");
                return;
            }
            
            String prod = itemActual.getNombre();
            String clase = itemActual.getClase();
            String medida = itemActual.getMedida();
            String grosor = itemActual.getGrosor();

            VarianteDAO dao = new VarianteDAO();
            Variante v = dao.buscarVarianteEspecifica(prod, clase, medida, grosor);
            
            if (v.getStockPiezas() < nuevaCant) {
                JOptionPane.showMessageDialog(this, 
                    "Stock insuficiente. Disponibles: " + v.getStockPiezas());
                return;
            }

            // Actualizamos el objeto en memoria (el carrito)
            // Usamos los setters que ahora recalcularán automáticamente el ftTotal y subtotal
            itemActual.setCantidad(nuevaCant);
            itemActual.setPrecioVenta(nuevoPrecio);
            
            // Avisamos al padre que se refresque
            padre.actualizarTablaVisual();
            this.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en números: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        btnClose = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JLabel();
        txtClase = new javax.swing.JLabel();
        txtMedida = new javax.swing.JLabel();
        txtGrosor = new javax.swing.JLabel();
        txtFtTotal = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(28, 28, 28));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(243, 243, 243));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Actualizar Producto");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 262, 39));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(243, 243, 243));
        jLabel2.setText("Cant");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 65, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(243, 243, 243));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Producto");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 101, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(243, 243, 243));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Clase");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 100, 86, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(243, 243, 243));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Medida");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 100, 86, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(243, 243, 243));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Grosor");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, 86, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(243, 243, 243));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("ft ");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 100, 86, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(243, 243, 243));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Precio ");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 100, 80, -1));

        btnActualizar.setBackground(new java.awt.Color(121, 140, 215));
        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarMouseClicked(evt);
            }
        });
        btnActualizar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(241, 241, 241));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Actualizar");
        btnActualizar.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 2, 130, 40));

        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 240, 241, 45));

        btnClose.setBackground(new java.awt.Color(28, 28, 28));
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imgs/backIcon.png"))); // NOI18N

        javax.swing.GroupLayout btnCloseLayout = new javax.swing.GroupLayout(btnClose);
        btnClose.setLayout(btnCloseLayout);
        btnCloseLayout.setHorizontalGroup(
            btnCloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnCloseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        btnCloseLayout.setVerticalGroup(
            btnCloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnCloseLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 16, -1, -1));

        txtProducto.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtProducto.setForeground(new java.awt.Color(243, 243, 243));
        txtProducto.setText("??");
        jPanel1.add(txtProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 101, -1));

        txtClase.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtClase.setForeground(new java.awt.Color(243, 243, 243));
        txtClase.setText("??");
        jPanel1.add(txtClase, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 140, 60, -1));

        txtMedida.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtMedida.setForeground(new java.awt.Color(243, 243, 243));
        txtMedida.setText("??");
        jPanel1.add(txtMedida, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 140, 80, -1));

        txtGrosor.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtGrosor.setForeground(new java.awt.Color(243, 243, 243));
        txtGrosor.setText("??");
        jPanel1.add(txtGrosor, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, 80, -1));

        txtFtTotal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtFtTotal.setForeground(new java.awt.Color(243, 243, 243));
        txtFtTotal.setText("??");
        jPanel1.add(txtFtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 140, 90, -1));
        jPanel1.add(txtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 140, 80, 40));
        jPanel1.add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 50, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 849, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseClicked
        ejecutarEdicion();
    }//GEN-LAST:event_btnActualizarMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnCloseMouseClicked


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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JDialogEditarVenta dialog = new JDialogEditarVenta(new javax.swing.JFrame(), true, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnActualizar;
    private javax.swing.JPanel btnClose;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JLabel txtClase;
    private javax.swing.JLabel txtFtTotal;
    private javax.swing.JLabel txtGrosor;
    private javax.swing.JLabel txtMedida;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JLabel txtProducto;
    // End of variables declaration//GEN-END:variables
}
