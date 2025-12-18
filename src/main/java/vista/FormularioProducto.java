
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
    }

    private void guardarProducto() {
        try {
            // 1. Validar campos
            String nombreProducto = txtProducto.getText().trim();
            if (nombreProducto.isEmpty() || txtMedida.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Medida son obligatorios.");
                return;
            }

            // 2. Determinar la acción y obtener el ID del Producto Padre
            int idPadre;
            boolean esNuevoProducto = jCheckBoxNuevoProducto.isSelected(); // Lee el estado del CheckBox
            
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
            v.setMedida(txtMedida.getText());
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
                padre.cargarTabla("");
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(28, 28, 28));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(243, 243, 243));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nuevo Producto");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(243, 243, 243));
        jLabel2.setText("Cant");

        txtCant.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(243, 243, 243));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Producto");

        txtProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtClase.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(243, 243, 243));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Clase");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(243, 243, 243));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Medida");

        txtMedida.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(243, 243, 243));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Grosor");

        txtGrosor.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtCostoC.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(243, 243, 243));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Costo C");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(243, 243, 243));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Precio V");

        txtCostoV.setHorizontalAlignment(javax.swing.JTextField.CENTER);

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

        jCheckBoxNuevoProducto.setBackground(new java.awt.Color(28, 28, 28));
        jCheckBoxNuevoProducto.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jCheckBoxNuevoProducto.setForeground(new java.awt.Color(243, 243, 243));
        jCheckBoxNuevoProducto.setText("Nuevo Producto");
        jCheckBoxNuevoProducto.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(179, 179, 179)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCrear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBoxNuevoProducto, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(256, 256, 256))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtCant, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtClase, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGrosor, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCostoC, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCostoV)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jCheckBoxNuevoProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCostoC, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCant, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addGap(39, 39, 39))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtClase, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGrosor, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCostoV, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44)
                .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96))
        );

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
