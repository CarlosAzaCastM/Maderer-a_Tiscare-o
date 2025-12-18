
package vista;

import modelo.Gasto;


public class JDialogEditarGasto extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JDialogEditarGasto.class.getName());
    
    private Gasto itemActual;

    public JDialogEditarGasto(java.awt.Frame parent, boolean modal, Gasto gasto) {
        super(parent, modal);
        initComponents();
        this.itemActual = gasto;
        cargarDatosEnFormulario(); // LLAMAMOS A ESTE METODO NUEVO
    }

    private void cargarDatosEnFormulario() {
        if (itemActual != null) {
            txtDescripcion.setText(itemActual.getDescripcion());
            txtMonto.setText(String.valueOf(itemActual.getMonto()));
            jComboBoxTipoGasto.setSelectedItem(itemActual.getTipoGasto());
            
            // Manejo de Fecha (String a Date para el JDateChooser)
            try {
                // Asumiendo formato de BD "yyyy-MM-dd HH:mm:ss" o similar
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date fecha = sdf.parse(itemActual.getFechaGasto());
                jDateChooserFechaGasto.setDate(fecha);
            } catch (Exception e) {
                // Si falla el formato, poner fecha actual o manejar error
                jDateChooserFechaGasto.setDate(new java.util.Date());
            }
        }
    }

    private void ejecutarEdicion() {
        // 1. Validaciones básicas
        if (txtDescripcion.getText().isEmpty() || txtMonto.getText().isEmpty() || jDateChooserFechaGasto.getDate() == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        // 2. Pasar datos de los inputs al objeto itemActual
        itemActual.setDescripcion(txtDescripcion.getText());
        itemActual.setTipoGasto((String) jComboBoxTipoGasto.getSelectedItem());
        
        try {
            itemActual.setMonto(Double.parseDouble(txtMonto.getText()));
        } catch (NumberFormatException e) {
             javax.swing.JOptionPane.showMessageDialog(this, "El monto debe ser un número válido.");
             return;
        }

        // Convertir Date del Chooser a String para tu modelo
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fechaStr = sdf.format(jDateChooserFechaGasto.getDate());
        itemActual.setFechaGasto(fechaStr);

        // 3. Llamar al DAO
        dao.GastoDAO gastoDAO = new dao.GastoDAO();
        boolean exito = gastoDAO.actualizarGasto(itemActual);

        if (exito) {
            javax.swing.JOptionPane.showMessageDialog(this, "Gasto actualizado correctamente.");
            this.dispose(); // Cierra la ventana
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al actualizar en la base de datos.");
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnActualizarGasto = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        btnClose = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtMonto = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        jDateChooserFechaGasto = new com.toedter.calendar.JDateChooser();
        jComboBoxTipoGasto = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(28, 28, 28));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(243, 243, 243));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Editar Gasto");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 262, 39));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(243, 243, 243));
        jLabel2.setText("Tipo de gasto");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 150, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(243, 243, 243));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Descripción");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 100, 170, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(243, 243, 243));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Monto");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, 86, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(243, 243, 243));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Fecha gasto");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 100, 140, -1));

        btnActualizarGasto.setBackground(new java.awt.Color(121, 140, 215));
        btnActualizarGasto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarGasto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarGastoMouseClicked(evt);
            }
        });
        btnActualizarGasto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(241, 241, 241));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Actualizar");
        btnActualizarGasto.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 2, 130, 40));

        jPanel1.add(btnActualizarGasto, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 240, 241, 45));

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
        jPanel1.add(txtMonto, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 140, 130, 40));
        jPanel1.add(txtDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 130, 40));
        jPanel1.add(jDateChooserFechaGasto, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 140, 160, 40));

        jComboBoxTipoGasto.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jComboBoxTipoGasto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gasolina", "Empleado", "Navajas", "Diesel", "Renta", "Otro" }));
        jPanel1.add(jComboBoxTipoGasto, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 180, 40));

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

    private void btnActualizarGastoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarGastoMouseClicked
        ejecutarEdicion();
    }//GEN-LAST:event_btnActualizarGastoMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnCloseMouseClicked

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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JDialogEditarGasto dialog = new JDialogEditarGasto(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JPanel btnActualizarGasto;
    private javax.swing.JPanel btnClose;
    private javax.swing.JComboBox<String> jComboBoxTipoGasto;
    private com.toedter.calendar.JDateChooser jDateChooserFechaGasto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtMonto;
    // End of variables declaration//GEN-END:variables
}
