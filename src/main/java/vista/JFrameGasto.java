
package vista;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import modelo.Usuario;

public class JFrameGasto extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFrameGasto.class.getName());
    private Usuario usuarioActual;
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String fechaFormateada = LocalDate.now().format(formato);
    
    
    public JFrameGasto(Usuario usuario) {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        labelFechaActual.setText(fechaFormateada);
        usuarioActual = usuario;
        iniciarLogica();
    }
    
    private void iniciarLogica() {
        // 1. Configuración inicial
        jDateChooser.setVisible(false); // Porque el checkbox inicia seleccionado
        txtDescripcionCorta.setVisible(false); // Oculto al inicio
        jScrollPane1.setVisible(true); // TextArea visible al inicio (para notas generales)
        
        // 2. Evento del ComboBox
        jComboBox1.addActionListener(e -> cambiarInterfazSegunTipo());
    }
    
    private void cambiarInterfazSegunTipo() {
        String tipoSeleccionado = (String) jComboBox1.getSelectedItem();
        
        if ("Empleado".equals(tipoSeleccionado)) {
            // Caso Empleado
            jLabelDescripcion_EmpleadoNombre.setText("Nombre Empleado");
            jScrollPane1.setVisible(false); // Ocultar area grande
            txtDescripcionCorta.setVisible(true); // Mostrar campo corto
            txtDescripcionCorta.setText("");
            
        } else if ("Otro".equals(tipoSeleccionado)) {
            // Caso Otro
            jLabelDescripcion_EmpleadoNombre.setText("Especifique Gasto");
            jScrollPane1.setVisible(false);
            txtDescripcionCorta.setVisible(true);
            txtDescripcionCorta.setText("");
            
        } else {
            // Casos normales (Gasolina, Renta, etc.)
            jLabelDescripcion_EmpleadoNombre.setText("Descripción / Notas");
            jScrollPane1.setVisible(true); // Usamos el grande para detalles
            txtDescripcionCorta.setVisible(false);
            jTextArea1.setText("");
        }
    }
    
    private void limpiarFormulario() {
        txtGastoMonto.setText("");
        jTextArea1.setText("");
        txtDescripcionCorta.setText("");
        jComboBox1.setSelectedIndex(0);
        jCheckBoxFechaActual.setSelected(true);
        jDateChooser.setVisible(false);
        jScrollPane1.setVisible(true); // Restaurar vista por defecto
        txtDescripcionCorta.setVisible(false);
        jLabelDescripcion_EmpleadoNombre.setText("Descripción");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        btnHome = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        labelFechaActual = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        btnRegistrarGasto = new javax.swing.JPanel();
        jLabelRegistrar = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtGastoMonto = new javax.swing.JTextField();
        jLabelFecha = new javax.swing.JLabel();
        jCheckBoxFechaActual = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabelTipoGasto = new javax.swing.JLabel();
        jLabelGasto = new javax.swing.JLabel();
        jLabelDescripcion_EmpleadoNombre = new javax.swing.JLabel();
        jDateChooser = new com.toedter.calendar.JDateChooser();
        txtDescripcionCorta = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(28, 28, 28));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTitulo.setBackground(new java.awt.Color(193, 166, 120));
        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(193, 166, 120));
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("Registrar Gastos");
        jPanel1.add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(501, 20, 300, -1));

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

        jPanel2.setBackground(new java.awt.Color(62, 44, 32));

        labelFechaActual.setFont(new java.awt.Font("Segoe UI", 3, 25)); // NOI18N
        labelFechaActual.setForeground(new java.awt.Color(246, 246, 246));
        labelFechaActual.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFechaActual.setText("??");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelFechaActual, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(labelFechaActual))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 20, 280, 40));

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gasolina", "Empleado", "Navajas", "Diesel", "Renta", "Otro" }));
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 180, 40));

        btnRegistrarGasto.setBackground(new java.awt.Color(42, 128, 47));
        btnRegistrarGasto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrarGasto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarGastoMouseClicked(evt);
            }
        });

        jLabelRegistrar.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelRegistrar.setForeground(new java.awt.Color(239, 239, 239));
        jLabelRegistrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelRegistrar.setText("Registrar");

        javax.swing.GroupLayout btnRegistrarGastoLayout = new javax.swing.GroupLayout(btnRegistrarGasto);
        btnRegistrarGasto.setLayout(btnRegistrarGastoLayout);
        btnRegistrarGastoLayout.setHorizontalGroup(
            btnRegistrarGastoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnRegistrarGastoLayout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(jLabelRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );
        btnRegistrarGastoLayout.setVerticalGroup(
            btnRegistrarGastoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnRegistrarGastoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(btnRegistrarGasto, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 510, 270, 60));

        jPanel3.setBackground(new java.awt.Color(62, 44, 32));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtGastoMonto.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtGastoMonto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGastoMonto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGastoMontoKeyTyped(evt);
            }
        });
        jPanel3.add(txtGastoMonto, new org.netbeans.lib.awtextra.AbsoluteConstraints(876, 64, 180, 40));

        jLabelFecha.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelFecha.setForeground(new java.awt.Color(241, 241, 241));
        jLabelFecha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFecha.setText("Fecha");
        jPanel3.add(jLabelFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(845, 116, 240, -1));

        jCheckBoxFechaActual.setBackground(new java.awt.Color(62, 44, 32));
        jCheckBoxFechaActual.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jCheckBoxFechaActual.setForeground(new java.awt.Color(246, 246, 246));
        jCheckBoxFechaActual.setSelected(true);
        jCheckBoxFechaActual.setText("Fecha actual");
        jCheckBoxFechaActual.addChangeListener(this::jCheckBoxFechaActualStateChanged);
        jPanel3.add(jCheckBoxFechaActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(904, 154, -1, -1));

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(422, 64, 310, 170));

        jLabelTipoGasto.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelTipoGasto.setForeground(new java.awt.Color(241, 241, 241));
        jLabelTipoGasto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTipoGasto.setText("Tipo de gasto");
        jPanel3.add(jLabelTipoGasto, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 17, 240, 50));

        jLabelGasto.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelGasto.setForeground(new java.awt.Color(241, 241, 241));
        jLabelGasto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelGasto.setText("Gasto");
        jPanel3.add(jLabelGasto, new org.netbeans.lib.awtextra.AbsoluteConstraints(845, 8, 240, 50));

        jLabelDescripcion_EmpleadoNombre.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelDescripcion_EmpleadoNombre.setForeground(new java.awt.Color(241, 241, 241));
        jLabelDescripcion_EmpleadoNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDescripcion_EmpleadoNombre.setText("Descripción");
        jPanel3.add(jLabelDescripcion_EmpleadoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 8, 240, 50));
        jPanel3.add(jDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(885, 189, 170, 39));

        txtDescripcionCorta.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel3.add(txtDescripcionCorta, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 190, 40));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 1170, 490));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        Menu menu = new Menu(usuarioActual);
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnHomeMouseClicked

    private void jCheckBoxFechaActualStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxFechaActualStateChanged
        if(jCheckBoxFechaActual.isSelected()){
            jDateChooser.setVisible(false);
        }
        else{
            jDateChooser.setVisible(true);
        }
    }//GEN-LAST:event_jCheckBoxFechaActualStateChanged

    private void btnRegistrarGastoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarGastoMouseClicked
        try {
            // 1. Validar Monto
            double monto = Double.parseDouble(txtGastoMonto.getText());
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor a 0.");
                return;
            }

            // 2. Obtener Descripción (del campo activo)
            String descripcion = "";
            if (txtDescripcionCorta.isVisible()) {
                descripcion = txtDescripcionCorta.getText();
            } else {
                descripcion = jTextArea1.getText();
            }
            
            if (descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debes especificar el gasto o empleado.");
                return;
            }

            // 3. Obtener Fecha
            String fechaFinal = "";
            if (jCheckBoxFechaActual.isSelected()) {
                // Formato MySQL: YYYY-MM-DD HH:MM:SS
                fechaFinal = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else {
                if (jDateChooser.getDate() == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona una fecha.");
                    return;
                }
                // Convertir fecha del chooser
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd 00:00:00");
                fechaFinal = sdf.format(jDateChooser.getDate());
            }

            // 4. Crear Objeto
            modelo.Gasto gasto = new modelo.Gasto();
            gasto.setTipoGasto((String) jComboBox1.getSelectedItem());
            gasto.setDescripcion(descripcion);
            gasto.setMonto(monto);
            gasto.setFechaGasto(fechaFinal);
            gasto.setIdUsuario(usuarioActual.getIdUsuario());

            // 5. Guardar
            dao.GastoDAO dao = new dao.GastoDAO();
            if (dao.registrarGasto(gasto)) {
                JOptionPane.showMessageDialog(this, "Gasto registrado correctamente.");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar en base de datos.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El monto debe ser un número válido.");
        }
    }//GEN-LAST:event_btnRegistrarGastoMouseClicked

    private void txtGastoMontoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGastoMontoKeyTyped
        char car = evt.getKeyChar();
        
        // Permitir números Y el punto, pero bloquear lo demás
        if ((car < '0' || car > '9') && car != '.') {
            evt.consume();
        }
        
        // Opcional: Evitar que escriban dos puntos (ej: 10..50)
        if (car == '.' && txtGastoMonto.getText().contains(".")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtGastoMontoKeyTyped

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
            new JFrameGasto(userPrueba).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnHome;
    private javax.swing.JPanel btnRegistrarGasto;
    private javax.swing.JCheckBox jCheckBoxFechaActual;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelDescripcion_EmpleadoNombre;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelGasto;
    private javax.swing.JLabel jLabelRegistrar;
    private javax.swing.JLabel jLabelTipoGasto;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel labelFechaActual;
    private javax.swing.JTextField txtDescripcionCorta;
    private javax.swing.JTextField txtGastoMonto;
    // End of variables declaration//GEN-END:variables
}
