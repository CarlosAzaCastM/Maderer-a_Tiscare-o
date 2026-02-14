
package vista;


import dao.EntradaDAO; 
import dao.ProductoDAO;
import dao.VarianteDAO;
import java.awt.Frame;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.Producto;
import modelo.Variante;

public class FormularioProducto extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormularioProducto.class.getName());
    
    private JFrameInventario padre;
    
    // Constructor LIMPIO: Solo recibe padre y modal
    public FormularioProducto(Frame parent, boolean modal) {
        super(parent, modal);
        this.padre = (JFrameInventario) parent;
        initComponents();
        this.setLocationRelativeTo(null);
        inicializarFormulario();
    }
    
    private void inicializarFormulario() {
        // 1. Cargar los productos en el ComboBox
        ProductoDAO productoDAO = new ProductoDAO();
        List<Producto> listaProductos = productoDAO.listarProductos();

        jComboBoxProductos.removeAllItems(); // Limpiar antes de llenar

        for (Producto p : listaProductos) {
            // Aquí agregamos el nombre. 
            // NOTA: Si quieres manejar objetos completos, el modelo Producto debe tener toString()
            // Por ahora usaremos el nombre string para compatibilidad con tu código.
            jComboBoxProductos.addItem(p.getNombreProducto());
        }

        // 2. Configurar el estado inicial de la vista
        alternarVisibilidad();

        // 3. Agregar el "Escuchador" (Listener) al Checkbox
        jCheckBoxNuevoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alternarVisibilidad();
            }
        });
    }

    private void alternarVisibilidad() {
        boolean esNuevo = jCheckBoxNuevoProducto.isSelected();

        // Si es nuevo: Muestra caja de texto, oculta combo
        txtProducto.setVisible(esNuevo);
        txtProducto.setEnabled(esNuevo); // Opcional: deshabilitar para asegurar

        // Si no es nuevo: Oculta caja de texto, muestra combo
        jComboBoxProductos.setVisible(!esNuevo);
        jComboBoxProductos.setEnabled(!esNuevo);

        // Limpieza opcional para evitar confusiones visuales
        if (esNuevo) {
            txtProducto.requestFocus();
        } else {
            jComboBoxProductos.requestFocus();
        }
    }

    private void guardarProducto() {
        try {
            // 1. Validar y OBTENER EL NOMBRE según el Checkbox
            String nombreProducto;
            boolean esNuevoProducto = jCheckBoxNuevoProducto.isSelected();

            if (esNuevoProducto) {
                // Si es nuevo, tomamos lo que escribió en el TextField
                nombreProducto = txtProducto.getText().trim();
            } else {
                // Si ya existe, tomamos lo seleccionado en el ComboBox
                if (jComboBoxProductos.getSelectedItem() != null) {
                    nombreProducto = jComboBoxProductos.getSelectedItem().toString();
                } else {
                    nombreProducto = "";
                }
            }

            // Validación común
            if (nombreProducto.isEmpty() || txtMedida.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del producto y la Medida son obligatorios.");
                return;
            }
            // 2. Determinar la acción y obtener el ID del Producto Padre
            int idPadre;
            
            VarianteDAO varianteDAO = new VarianteDAO();
            
            if (esNuevoProducto) {
                // *** CASO 1: CREAR NUEVO PRODUCTO PADRE Y VARIANTE ***
                ProductoDAO productoDAO = new ProductoDAO();
                Producto nuevoPadre = new Producto();
                nuevoPadre.setNombreProducto(nombreProducto);

                // Registrar el nuevo producto padre
                idPadre = productoDAO.registrarProductoPadre(nuevoPadre);
                
                if (idPadre <= 0) {
                    JOptionPane.showMessageDialog(this, "Error: No se pudo crear el Producto Padre.");
                    return;
                }
                
            } else {
                // *** CASO 2: CREAR SOLO VARIANTE (El producto padre ya debe existir) ***
                
                // Buscar ID del Producto Padre existente
                idPadre = buscarIdProducto(nombreProducto, varianteDAO);
                
                if (idPadre == -1) {
                    // Si no existe, sugerimos usar el CheckBox
                    JOptionPane.showMessageDialog(this, 
                        "El producto '" + nombreProducto + "' no existe. " +
                        "Si es un producto nuevo, marca la casilla 'Nuevo Producto'.");
                    return;
                }
            }
            
            // --- Continuamos con la creación de la Variante ---
            Variante v = new Variante();
            v.setId(idPadre); // Seteamos el ID del padre (existente o nuevo)
            
            // 3. Setear Datos de la Variante (Esto es igual que tu lógica anterior)
            v.setClase(txtClase.getText().toUpperCase());
            v.setMedida(txtMedida.getText().toLowerCase());
            v.setGrosor(txtGrosor.getText());
            
            // Calcular Pies
            double pies = calcularPies(v.getMedida(), v.getGrosor());
            v.setPiesPorPieza(pies);

            // Costos y Stock
            double costo = txtCostoC.getText().isEmpty() ? 0 : Double.parseDouble(txtCostoC.getText());
            double precio = txtCostoV.getText().isEmpty() ? 0 : Double.parseDouble(txtCostoV.getText());
            int stock = txtCant.getText().isEmpty() ? 0 : Integer.parseInt(txtCant.getText());

            v.setCostoCompra(costo);
            v.setPrecioVenta(precio);
            v.setStockPiezas(stock);

            // 4. GUARDAR VARIANTE EN BD Y OBTENER EL NUEVO ID
            int nuevoIdVariante = varianteDAO.registrar(v); // Asumiendo que VarianteDAO.registrar funciona

            if (nuevoIdVariante > 0) {
                // 5. REGISTRAR ENTRADA EN HISTORIAL (Si hay stock inicial)
                if (stock > 0) {
                    EntradaDAO entradaDAO = new EntradaDAO();
                    entradaDAO.registrarEntrada(nuevoIdVariante, stock, costo, precio);
                }
                
                JOptionPane.showMessageDialog(this, "Producto y Variante creados exitosamente (ID Padre: " + idPadre + ").");
                padre.refrescarDatosDesdeBD();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar la variante en base de datos.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Revisa que los números sean válidos en Costo, Precio o Cantidad.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error general: " + e.getMessage());
        }
    }

    // --- Métodos Auxiliares ---
    private int buscarIdProducto(String nombre, VarianteDAO dao) {
        List<Variante> padres = dao.listarProductosPadre();
        for (Variante p : padres) {
            if (p.getNombreProducto().equalsIgnoreCase(nombre)) return p.getId();
        }
        return -1; 
    }

    private double calcularPies(String medida, String grosorTxt) {
        try {
            String[] partes = medida.toLowerCase().replace(" ", "").split("x");
            if (partes.length < 2) return 0;
            
            double ancho = Double.parseDouble(partes[0]);
            double largo = Double.parseDouble(partes[1]);
            double grosor = 0;
            
            if (grosorTxt.contains("/")) {
                String[] frac = grosorTxt.split("/");
                grosor = Double.parseDouble(frac[0]) / Double.parseDouble(frac[1]);
            } else {
                grosor = Double.parseDouble(grosorTxt);
            }
            return (grosor * ancho * largo) / 12.0;
        } catch (Exception e) { return 0; }
    }
    
    // ... AQUÍ ABAJO PEGA TU CÓDIGO GENERADO DE INITCOMPONENTS ...
    // ... SOLO RECUERDA QUITAR LA LÓGICA DE 'if(variante != null)' DEL CONSTRUCTOR QUE BORRASTE
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCant = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        txtClase = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtMedida = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtGrosor = new javax.swing.JTextField();
        txtCostoC = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCostoV = new javax.swing.JTextField();
        btnCrear = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btnClose = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jCheckBoxNuevoProducto = new javax.swing.JCheckBox();
        jComboBoxProductos = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(28, 28, 28));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(243, 243, 243));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nuevo Producto");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(249, 24, 262, 39));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(243, 243, 243));
        jLabel2.setText("Cant");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 127, 65, -1));

        txtCant.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantKeyTyped(evt);
            }
        });
        jPanel1.add(txtCant, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 165, 60, 33));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(243, 243, 243));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Producto");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 127, 119, -1));

        txtProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 119, 33));

        txtClase.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtClase, new org.netbeans.lib.awtextra.AbsoluteConstraints(219, 165, 81, 33));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(243, 243, 243));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Clase");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(214, 127, 86, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(243, 243, 243));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Medida");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 126, 86, -1));

        txtMedida.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtMedida, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 165, 94, 33));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(243, 243, 243));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Grosor");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(406, 127, 86, -1));

        txtGrosor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtGrosor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGrosorKeyTyped(evt);
            }
        });
        jPanel1.add(txtGrosor, new org.netbeans.lib.awtextra.AbsoluteConstraints(406, 165, 94, 33));

        txtCostoC.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoCKeyTyped(evt);
            }
        });
        jPanel1.add(txtCostoC, new org.netbeans.lib.awtextra.AbsoluteConstraints(506, 165, 94, 33));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(243, 243, 243));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Costo C");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(506, 127, 86, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(243, 243, 243));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Precio V");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(606, 127, 94, -1));

        txtCostoV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCostoV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoVKeyTyped(evt);
            }
        });
        jPanel1.add(txtCostoV, new org.netbeans.lib.awtextra.AbsoluteConstraints(606, 165, 94, 33));

        btnCrear.setBackground(new java.awt.Color(53, 146, 53));
        btnCrear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCrearMouseClicked(evt);
            }
        });
        btnCrear.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(238, 238, 238));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Crear");
        jLabel9.setToolTipText("");
        btnCrear.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, -1, -1));

        jPanel1.add(btnCrear, new org.netbeans.lib.awtextra.AbsoluteConstraints(228, 253, 241, 45));

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

        jCheckBoxNuevoProducto.setBackground(new java.awt.Color(28, 28, 28));
        jCheckBoxNuevoProducto.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jCheckBoxNuevoProducto.setForeground(new java.awt.Color(243, 243, 243));
        jCheckBoxNuevoProducto.setText("Nuevo Producto");
        jCheckBoxNuevoProducto.setOpaque(true);
        jPanel1.add(jCheckBoxNuevoProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(273, 84, -1, -1));

        jComboBoxProductos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(jComboBoxProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 164, 119, 34));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCrearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCrearMouseClicked
        guardarProducto();
    }//GEN-LAST:event_btnCrearMouseClicked

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnCloseMouseClicked

    private void txtCantKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantKeyTyped
        char car = evt.getKeyChar();
        if (!Character.isDigit(car)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCantKeyTyped

    private void txtCostoCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoCKeyTyped
        char car = evt.getKeyChar();
        
        // Permitir números Y el punto, pero bloquear lo demás
        if ((car < '0' || car > '9') && car != '.') {
            evt.consume();
        }
        
        // Opcional: Evitar que escriban dos puntos (ej: 10..50)
        if (car == '.' && txtCostoC.getText().contains(".")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCostoCKeyTyped

    private void txtCostoVKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoVKeyTyped
        char car = evt.getKeyChar();
        
        // Permitir números Y el punto, pero bloquear lo demás
        if ((car < '0' || car > '9') && car != '.') {
            evt.consume();
        }
        
        // Opcional: Evitar que escriban dos puntos (ej: 10..50)
        if (car == '.' && txtCostoV.getText().contains(".")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCostoVKeyTyped

    private void txtGrosorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGrosorKeyTyped
        char car = evt.getKeyChar();
        
        // Permitir números Y el punto, pero bloquear lo demás
        if ((car < '0' || car > '9') && car != '.') {
            evt.consume();
        }
        
        // Opcional: Evitar que escriban dos puntos (ej: 10..50)
        if (car == '.' && txtGrosor.getText().contains(".")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtGrosorKeyTyped

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
                FormularioProducto dialog = new FormularioProducto(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel btnClose;
    private javax.swing.JPanel btnCrear;
    private javax.swing.JCheckBox jCheckBoxNuevoProducto;
    private javax.swing.JComboBox<String> jComboBoxProductos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtCant;
    private javax.swing.JTextField txtClase;
    private javax.swing.JTextField txtCostoC;
    private javax.swing.JTextField txtCostoV;
    private javax.swing.JTextField txtGrosor;
    private javax.swing.JTextField txtMedida;
    private javax.swing.JTextField txtProducto;
    // End of variables declaration//GEN-END:variables
}
