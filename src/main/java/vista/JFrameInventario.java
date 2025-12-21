package vista;

import dao.VarianteDAO;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import modelo.Usuario;
import modelo.Variante;

public class JFrameInventario extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFrameInventario.class.getName());

    DefaultTableModel modelo;
    private Usuario usuarioActual;
    
    // OPTIMIZACIÓN: Esta lista guardará todo el inventario en RAM
    private List<Variante> inventarioCache;

    public JFrameInventario(Usuario usuario) {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        usuarioActual = usuario;
        
        configurarTabla(); 
        
        // 1. Carga inicial: Traemos todo de la BD una sola vez
        refrescarDatosDesdeBD();
        
        // OPCIONAL: Agregar un listener para que busque mientras escribes (tiempo real)
        // Esto solo es posible gracias a que la búsqueda es local y rápida.
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarTablaLocalmente(txtBuscar.getText());
            }
        });
    }

    private void configurarTabla() {
        String[] titulos = {"Cant", "Producto", "Clase", "Medida", "Grosor", "Ft Total", "Costo C", "Costo V", "min"};
        modelo = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTableInventario.setModel(modelo);
        jTableInventario.setRowHeight(35);
        jTableInventario.getColumnModel().getColumn(0).setPreferredWidth(50); 
        jTableInventario.getColumnModel().getColumn(1).setPreferredWidth(150);
        
        jTableInventario.getColumnModel().getColumn(8).setMinWidth(0);
        jTableInventario.getColumnModel().getColumn(8).setMaxWidth(0);
        jTableInventario.getColumnModel().getColumn(8).setWidth(0);
        
        aplicarColoresTabla();
    }
    
    private void aplicarColoresTabla() {
        DefaultTableCellRenderer renderizador = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // 1. Alineación
                if (column == 0 || column == 1) { 
                    setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                }

                // 2. Si está seleccionado, mantener colores de selección
                if (isSelected) {
                    return c;
                }

                // 3. Lógica de color por stock
                try {
                    // Obtener valores del modelo (no de la vista)
                    int modelRow = table.convertRowIndexToModel(row);

                    Object valStockObj = table.getModel().getValueAt(modelRow, 0); // Columna Cantidad
                    Object valMinObj = table.getModel().getValueAt(modelRow, 8); // Columna stock mínimo (oculta)

                    // Verificar que no sean nulos
                    if (valStockObj != null && valMinObj != null) {
                        int stockActual = Integer.parseInt(valStockObj.toString());
                        int stockMinimo = Integer.parseInt(valMinObj.toString());

                        // DEBUG: Puedes agregar esto temporalmente para verificar
                        // System.out.println("Fila " + row + ": Stock=" + stockActual + ", Min=" + stockMinimo);

                        if (stockActual < stockMinimo) {
                            c.setForeground(Color.RED);
                            c.setBackground(new Color(255, 235, 235));
                        } else {
                            c.setForeground(Color.BLACK);
                            c.setBackground(table.getBackground());
                        }
                    }
                } catch (Exception e) {
                    // Si hay error, mantener colores por defecto
                    c.setForeground(table.getForeground());
                    c.setBackground(table.getBackground());
                }

                return c;
            }
        };

        // Aplicar renderizador a todas las columnas visibles
        for (int i = 0; i < 8; i++) {
            jTableInventario.getColumnModel().getColumn(i).setCellRenderer(renderizador);
        }
    }

    // MÉTODO 1: Va a la nube y descarga todo (Lento, usar solo al inicio o al guardar cambios)
    public void refrescarDatosDesdeBD() {
        VarianteDAO dao = new VarianteDAO();
        // Usamos el método optimizado que creamos en el paso anterior
        this.inventarioCache = dao.obtenerTodoElInventario();
        
        // Al terminar de descargar, mostramos todo en la tabla
        filtrarTablaLocalmente("");
    }

    // MÉTODO 2: Filtra la lista en memoria (Instantáneo, usar en Búsqueda)
    public void filtrarTablaLocalmente(String textoBusqueda) {
    if (inventarioCache == null) return;

    modelo.setRowCount(0); // Limpiar tabla visual
    
    String busquedaMinuscula = textoBusqueda.toLowerCase();

    for (Variante v : inventarioCache) {
        String nombreCompleto = (v.getNombreProducto() + " " + v.getMedida() + " " + v.getClase()).toLowerCase();
        
        if (busquedaMinuscula.isEmpty() || nombreCompleto.contains(busquedaMinuscula)) {
            
            double piesTotales = v.getStockPiezas() * v.getPiesPorPieza();

            // CORRECCIÓN: Ahora son 9 elementos (0-8) en lugar de 8
            Object[] fila = new Object[9]; // Cambiado de 8 a 9
            fila[0] = v.getStockPiezas(); 
            fila[1] = v.getNombreProducto();
            fila[2] = v.getClase();
            fila[3] = v.getMedida();
            fila[4] = v.getGrosor();
            fila[5] = String.format("%.2f ft", piesTotales);     
            fila[6] = String.format("$ %.2f", v.getCostoCompra()); 
            fila[7] = String.format("$ %.2f", v.getPrecioVenta()); 
            fila[8] = v.getStockMinimo(); // ¡IMPORTANTE! Agregar el stock mínimo

            modelo.addRow(fila);
        }
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
        filtrarTablaLocalmente(txtBuscar.getText());
    }//GEN-LAST:event_btnBuscarMouseClicked

    private void btnAgregarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarProductoMouseClicked
        FormularioProducto modal = new FormularioProducto(this, true);
        modal.setVisible(true);
        refrescarDatosDesdeBD();
    }//GEN-LAST:event_btnAgregarProductoMouseClicked

    private void btnActualizarProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarProductoMouseClicked
        int fila = jTableInventario.getSelectedRow();
        
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla primero.");
            return;
        }
        
        // MEJOR SOLUCIÓN: Vamos a obtener los datos clave de la fila seleccionada y buscar en el cache.
        String prodSel = (String) modelo.getValueAt(fila, 1);
        String claseSel = (String) modelo.getValueAt(fila, 2);
        String medidaSel = (String) modelo.getValueAt(fila, 3);
        String grosorSel = (String) modelo.getValueAt(fila, 4);
        
        Variante vSeleccionada = null;
        if (inventarioCache != null) {
            for (Variante v : inventarioCache) {
                if (v.getNombreProducto().equals(prodSel) && 
                    v.getClase().equals(claseSel) &&
                    v.getMedida().equals(medidaSel) &&
                    v.getGrosor().equals(grosorSel)) {
                    vSeleccionada = v;
                    break;
                }
            }
        }

        if (vSeleccionada != null) {
            ActulizarProducto modal = new ActulizarProducto(this, true, vSeleccionada);
            modal.setVisible(true);
            
            // IMPORTANTE: Refrescar BD tras editar
            refrescarDatosDesdeBD();
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
