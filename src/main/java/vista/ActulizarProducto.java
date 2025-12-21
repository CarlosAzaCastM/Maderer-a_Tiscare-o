
package vista;

import dao.EntradaDAO;
import dao.VarianteDAO;
import java.awt.Frame;
import javax.swing.JOptionPane;
import modelo.Variante;

public class ActulizarProducto extends javax.swing.JDialog {
    
    private Variante varianteSeleccionada;
    private JFrameInventario padre;
    
    // Logger para evitar errores de NetBeans
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ActulizarProducto.class.getName());

    // --- CONSTRUCTOR ---
    public ActulizarProducto(Frame parent, boolean modal, Variante variante) {
        super(parent, modal);
        this.padre = (JFrameInventario) parent;
        this.varianteSeleccionada = variante;
        initComponents();
        this.setLocationRelativeTo(null); // Centrar ventana
        
        cargarDatosActuales();
        
        txtCantidadAgregada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char car = evt.getKeyChar();
                if (!Character.isDigit(car)) {
                    evt.consume();
                }
            }
        });
        
        txtCostoCNuevo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
               char car = evt.getKeyChar();
        
            // Permitir números Y el punto, pero bloquear lo demás
            if ((car < '0' || car > '9') && car != '.') {
                evt.consume();
            }

            // Opcional: Evitar que escriban dos puntos (ej: 10..50)
            if (car == '.' && txtCostoCNuevo.getText().contains(".")) {
                evt.consume();
            }
            }
        });
        
        txtPrecioVNuevo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char car = evt.getKeyChar();
        
                // Permitir números Y el punto, pero bloquear lo demás
                if ((car < '0' || car > '9') && car != '.') {
                    evt.consume();
                }

                // Opcional: Evitar que escriban dos puntos (ej: 10..50)
                if (car == '.' && txtPrecioVNuevo.getText().contains(".")) {
                    evt.consume();
                }
            }
        });
    }

    private void cargarDatosActuales() {
        if (varianteSeleccionada != null) {
            // 1. Mostrar datos actuales en los LABELS (Info estática)
            txtProducto.setText(varianteSeleccionada.getNombreProducto());
            txtClase.setText(varianteSeleccionada.getClase());
            txtMedida.setText(varianteSeleccionada.getMedida());
            txtGrosor.setText(varianteSeleccionada.getGrosor());
            
            // Calculamos pies totales actuales para mostrar
            double piesTotales = varianteSeleccionada.getStockPiezas() * varianteSeleccionada.getPiesPorPieza();
            txtFtTotal.setText(String.format("%.2f", piesTotales));
            
            txtCantidad.setText(String.valueOf(varianteSeleccionada.getStockPiezas()));
            txtCostoC1.setText(String.valueOf(varianteSeleccionada.getCostoCompra()));
            txtPrecioV.setText(String.valueOf(varianteSeleccionada.getPrecioVenta()));
            
            // 2. Pre-llenar los TEXTFIELDS (Campos Editables)
            // Ponemos los precios actuales para que sea fácil editarlos si cambiaron poco
            txtCostoCNuevo.setText(String.valueOf(varianteSeleccionada.getCostoCompra()));
            txtPrecioVNuevo.setText(String.valueOf(varianteSeleccionada.getPrecioVenta()));
            
            // La cantidad a agregar empieza en 0 (porque vamos a sumar, no a reemplazar)
            txtCantidadAgregada.setText("0"); 
        }
    }
    
    private void ejecutarActualizacion() {
        try {
            // 1. Obtener datos de los inputs
            int cantidadAgregar = Integer.parseInt(txtCantidadAgregada.getText());
            double nuevoCosto = Double.parseDouble(txtCostoCNuevo.getText());
            double nuevoPrecio = Double.parseDouble(txtPrecioVNuevo.getText());
            
            if (cantidadAgregar < 0) {
                JOptionPane.showMessageDialog(this, "No puedes agregar cantidades negativas.");
                return;
            }

            // 2. LLAMADA OPTIMIZADA (Una sola conexión para todo)
            VarianteDAO dao = new VarianteDAO();
            boolean exito = dao.registrarEntradaYActualizar(
                    varianteSeleccionada.getId(), 
                    cantidadAgregar, 
                    nuevoCosto, 
                    nuevoPrecio
            );
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
                
                // --- TRUCO DE VELOCIDAD (Actualización Local) ---
                // En lugar de descargar toda la BD de nuevo, actualizamos el objeto en memoria RAM
                // Como 'varianteSeleccionada' es el mismo objeto que está en la lista del Padre,
                // solo actualizamos sus valores aquí:
                
                int stockAnterior = varianteSeleccionada.getStockPiezas();
                varianteSeleccionada.setStockPiezas(stockAnterior + cantidadAgregar);
                varianteSeleccionada.setCostoCompra(nuevoCosto);
                varianteSeleccionada.setPrecioVenta(nuevoPrecio);
                
                // Le decimos al padre que solo repinte la tabla (Instantáneo)
                // OJO: Asegúrate que 'filtrarTablaLocalmente' sea público en JFrameInventario
                padre.filtrarTablaLocalmente(""); 
                
                this.dispose(); // Cerrar ventana
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar en la base de datos.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor revisa que los números sean válidos.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
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
        jLabel13 = new javax.swing.JLabel();
        btnClose = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtPrecioV = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JLabel();
        txtProducto = new javax.swing.JLabel();
        txtClase = new javax.swing.JLabel();
        txtMedida = new javax.swing.JLabel();
        txtGrosor = new javax.swing.JLabel();
        txtFtTotal = new javax.swing.JLabel();
        txtPrecioVNuevo = new javax.swing.JTextField();
        txtCantidadAgregada = new javax.swing.JTextField();
        txtCostoCNuevo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtCostoC1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

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
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 96, 65, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(243, 243, 243));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Producto");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 96, 101, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(243, 243, 243));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Clase");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 86, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(243, 243, 243));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Medida");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 96, 86, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(243, 243, 243));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Grosor");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(426, 96, 86, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(243, 243, 243));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("ft Total");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, 86, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(243, 243, 243));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Precio V");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 100, 94, -1));

        btnActualizar.setBackground(new java.awt.Color(121, 140, 215));
        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarMouseClicked(evt);
            }
        });
        btnActualizar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(241, 241, 241));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Actualizar");
        btnActualizar.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 140, 30));

        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 270, 241, 45));

        btnClose.setBackground(new java.awt.Color(28, 28, 28));
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

        txtPrecioV.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtPrecioV.setForeground(new java.awt.Color(243, 243, 243));
        txtPrecioV.setText("??");
        jPanel1.add(txtPrecioV, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 140, 80, -1));

        txtCantidad.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtCantidad.setForeground(new java.awt.Color(243, 243, 243));
        txtCantidad.setText("??");
        jPanel1.add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 140, 44, -1));

        txtProducto.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtProducto.setForeground(new java.awt.Color(243, 243, 243));
        txtProducto.setText("??");
        jPanel1.add(txtProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 101, -1));

        txtClase.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtClase.setForeground(new java.awt.Color(243, 243, 243));
        txtClase.setText("??");
        jPanel1.add(txtClase, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 60, -1));

        txtMedida.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtMedida.setForeground(new java.awt.Color(243, 243, 243));
        txtMedida.setText("??");
        jPanel1.add(txtMedida, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, 80, -1));

        txtGrosor.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtGrosor.setForeground(new java.awt.Color(243, 243, 243));
        txtGrosor.setText("??");
        jPanel1.add(txtGrosor, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 140, 80, -1));

        txtFtTotal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtFtTotal.setForeground(new java.awt.Color(243, 243, 243));
        txtFtTotal.setText("??");
        jPanel1.add(txtFtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, 80, -1));
        jPanel1.add(txtPrecioVNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 180, 80, 40));
        jPanel1.add(txtCantidadAgregada, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 50, 40));
        jPanel1.add(txtCostoCNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 180, 80, 40));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(243, 243, 243));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Costo C");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 100, 86, -1));

        txtCostoC1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtCostoC1.setForeground(new java.awt.Color(243, 243, 243));
        txtCostoC1.setText("??");
        jPanel1.add(txtCostoC1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 140, 80, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(76, 199, 124));
        jLabel12.setText("+");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 200, 20, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseClicked
        ejecutarActualizacion();
    }//GEN-LAST:event_btnActualizarMouseClicked

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
                ActulizarProducto dialog = new ActulizarProducto(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel txtCantidad;
    private javax.swing.JTextField txtCantidadAgregada;
    private javax.swing.JLabel txtClase;
    private javax.swing.JLabel txtCostoC1;
    private javax.swing.JTextField txtCostoCNuevo;
    private javax.swing.JLabel txtFtTotal;
    private javax.swing.JLabel txtGrosor;
    private javax.swing.JLabel txtMedida;
    private javax.swing.JLabel txtPrecioV;
    private javax.swing.JTextField txtPrecioVNuevo;
    private javax.swing.JLabel txtProducto;
    // End of variables declaration//GEN-END:variables
}
