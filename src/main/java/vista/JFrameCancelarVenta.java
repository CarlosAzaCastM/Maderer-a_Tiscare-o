
package vista;

import dao.VentaDao;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import modelo.Usuario;
import modelo.Venta;


public class JFrameCancelarVenta extends javax.swing.JFrame {
     private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFrameCancelarVenta.class.getName());
    private Usuario usuarioActual;
    private VentaDao ventaDao;
    private DefaultTableModel tableModel;

    public JFrameCancelarVenta(Usuario usuario) {
        initComponents();
        usuarioActual = usuario;
        ventaDao = new VentaDao();
        this.setExtendedState(MAXIMIZED_BOTH);
        
        configurarTabla();
        logicaInicial();
        
        cargarVentasUltimosDias(7);
        
        jComboBoxTipoFiltro.addActionListener(e -> cambiarVisibilidadFiltros());
    }
    
    private void configurarTabla() {
        // Configurar el modelo de la tabla
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        
        // Definir las columnas
        String[] columnas = {"Folio", "Fecha", "Estatus"};
        tableModel.setColumnIdentifiers(columnas);
        jTableVentas.setModel(tableModel);
        
        // Ajustar el ancho de las columnas
        jTableVentas.getColumnModel().getColumn(0).setPreferredWidth(150);  // Folio - más ancho
        jTableVentas.getColumnModel().getColumn(1).setPreferredWidth(250);  // Fecha - más ancho
        jTableVentas.getColumnModel().getColumn(2).setPreferredWidth(150);  // Estatus - más ancho
        
        // Aumentar la altura de las filas
        jTableVentas.setRowHeight(40);
        
        // Aumentar el tamaño de la fuente de la tabla
        jTableVentas.setFont(new java.awt.Font("Segoe UI", 0, 18));
        
        // Ajustar el tamaño de la fuente del encabezado
        jTableVentas.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 18));
        jTableVentas.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 40));
        
        configurarColoresTabla();
    }
    
    private void configurarColoresTabla() {
    // Crear un renderizador personalizado para la columna de estatus (columna 2, no 4)
    jTableVentas.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            java.awt.Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Obtener el valor del estatus
            String estatus = value != null ? value.toString().toLowerCase() : "";

            // Configurar colores según el estatus
            if (isSelected) {
                // Mantener los colores de selección
                cellComponent.setForeground(table.getSelectionForeground());
                cellComponent.setBackground(table.getSelectionBackground());
            } else {
                // Aplicar colores según el estatus
                switch (estatus) {
                    case "completada":
                        cellComponent.setForeground(new java.awt.Color(46, 125, 50)); // Verde
                        cellComponent.setBackground(table.getBackground());
                        break;
                    case "cancelada":
                        cellComponent.setForeground(new java.awt.Color(211, 47, 47)); // Rojo
                        cellComponent.setBackground(table.getBackground());
                        break;
                    case "pendiente":
                        cellComponent.setForeground(new java.awt.Color(245, 124, 0)); // Naranja
                        cellComponent.setBackground(table.getBackground());
                        break;
                    default:
                        cellComponent.setForeground(table.getForeground());
                        cellComponent.setBackground(table.getBackground());
                        break;
                }
            }

            // Centrar el texto en la columna de estatus
            ((javax.swing.JLabel) cellComponent).setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            return cellComponent;
        }
    });

    // También puedes alinear otras columnas si lo deseas
    // Centrar el folio (columna 0)
    javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jTableVentas.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Folio (columna 0)
    
    // Centrar la fecha (columna 1)
    jTableVentas.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Fecha (columna 1)
    
}
    
    private void cambiarVisibilidadFiltros() {
        String filtroSeleccionado = (String) jComboBoxTipoFiltro.getSelectedItem();
        
        // Ocultar todos primero
        jComboBoxMeses.setVisible(false);
        jComboBoxSemanas.setVisible(false);
        jDateChooser.setVisible(false);
        txtFolioTicket.setVisible(false);
        labelFolioTicket.setVisible(false);
        
        // Mostrar solo el correspondiente
        switch (filtroSeleccionado) {
            case "Mes":
                jComboBoxMeses.setVisible(true);
                break;
            case "Semana":
                jComboBoxSemanas.setVisible(true);
                break;
            case "Fecha":
                jDateChooser.setVisible(true);
                break;
            case "Folio":
                txtFolioTicket.setVisible(true);
                labelFolioTicket.setVisible(true);
                break;
        }
    }
    
    private void cargarVentasUltimosDias(int dias) {
        Calendar calendario = Calendar.getInstance();
        Date fechaFin = calendario.getTime();
        
        calendario.add(Calendar.DAY_OF_YEAR, -dias);
        Date fechaInicio = calendario.getTime();
        
        List<Venta> ventas = ventaDao.obtenerVentasPorFecha(fechaInicio, fechaFin, null);
        actualizarTabla(ventas);
    }
    
    private void cargarVentasPorFechaExacta(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        
        // Establecer inicio del día
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        Date fechaInicio = calendario.getTime();
        
        // Establecer fin del día
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.SECOND, 59);
        Date fechaFin = calendario.getTime();
        
        List<Venta> ventas = ventaDao.obtenerVentasPorFecha(fechaInicio, fechaFin, null);
        actualizarTabla(ventas);
    }
    
    private void cargarVentaPorFolio(int folio) {
        Venta venta = ventaDao.obtenerVentaPorFolio(folio);
        if (venta != null) {
            List<Venta> ventas = new ArrayList<>();
            ventas.add(venta);
            actualizarTabla(ventas);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "No se encontró una venta con el folio " + folio);
            // Limpiar la tabla
            tableModel.setRowCount(0);
        }
    }
    
    private void actualizarTabla(List<Venta> ventas) {
        // Limpiar la tabla
        tableModel.setRowCount(0);
        
        // Formato para la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        // Agregar las ventas a la tabla
        for (Venta venta : ventas) {
            try {
                Date fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(venta.getFechaVenta());
                String fechaFormateada = sdf.format(fecha);
                
                Object[] fila = {
                    venta.getFolioTicket(),
                    fechaFormateada,
                    venta.getEstatus()
                };
                tableModel.addRow(fila);
            } catch (Exception e) {
                // Si hay error en el formato de fecha, usar el string original
                Object[] fila = {
                    venta.getFolioTicket(),
                    venta.getFechaVenta(),
                    venta.getEstatus()
                };
                tableModel.addRow(fila);
            }
        }
        jTableVentas.repaint();
    }
    
    private void cargarMesPasado() {
        Calendar calendario = Calendar.getInstance();
        
        // Ir al mes anterior
        calendario.add(Calendar.MONTH, -1);
        
        // Obtener el primer día del mes anterior
        calendario.set(Calendar.DAY_OF_MONTH, 1);
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        Date fechaInicio = calendario.getTime();
        
        // Obtener el último día del mes anterior
        calendario.set(Calendar.DAY_OF_MONTH, calendario.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.SECOND, 59);
        Date fechaFin = calendario.getTime();
        
        List<Venta> ventas = ventaDao.obtenerVentasPorFecha(fechaInicio, fechaFin, null);
        actualizarTabla(ventas);
    }
    
    private void logicaInicial() {
        jComboBoxMeses.setVisible(false);
        jComboBoxSemanas.setVisible(false);
        jDateChooser.setVisible(false);
        txtFolioTicket.setVisible(false);
        labelFolioTicket.setVisible(false);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        btnHome = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnCancelarVenta = new javax.swing.JPanel();
        jLabelCancelar = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableVentas = new javax.swing.JTable();
        jComboBoxTipoFiltro = new javax.swing.JComboBox<>();
        jDateChooser = new com.toedter.calendar.JDateChooser();
        jComboBoxSemanas = new javax.swing.JComboBox<>();
        jComboBoxMeses = new javax.swing.JComboBox<>();
        btnFiltrar = new javax.swing.JPanel();
        jLabelCancelar1 = new javax.swing.JLabel();
        labelFolioTicket = new javax.swing.JLabel();
        txtFolioTicket = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(28, 28, 28));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTitulo.setBackground(new java.awt.Color(193, 166, 120));
        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(193, 166, 120));
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("Cancelar Venta");
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

        jPanel3.setBackground(new java.awt.Color(62, 44, 32));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCancelarVenta.setBackground(new java.awt.Color(205, 92, 92));
        btnCancelarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelarVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelarVentaMouseClicked(evt);
            }
        });

        jLabelCancelar.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelCancelar.setForeground(new java.awt.Color(239, 239, 239));
        jLabelCancelar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCancelar.setText("Cancelar");

        javax.swing.GroupLayout btnCancelarVentaLayout = new javax.swing.GroupLayout(btnCancelarVenta);
        btnCancelarVenta.setLayout(btnCancelarVentaLayout);
        btnCancelarVentaLayout.setHorizontalGroup(
            btnCancelarVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnCancelarVentaLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabelCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        btnCancelarVentaLayout.setVerticalGroup(
            btnCancelarVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnCancelarVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(btnCancelarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 220, 220, -1));

        jScrollPane1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jTableVentas.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        jTableVentas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTableVentas);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 780, 460));

        jComboBoxTipoFiltro.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxTipoFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Folio", "Mes", "Semana", "Fecha" }));
        jPanel3.add(jComboBoxTipoFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 150, 30));
        jPanel3.add(jDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, 180, 30));

        jComboBoxSemanas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxSemanas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Últimos 7 dias", "Últimos 14 dias" }));
        jPanel3.add(jComboBoxSemanas, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 160, 30));

        jComboBoxMeses.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxMeses.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Últimos 30 dias", "mes pasado", "Últimos 60 dias" }));
        jPanel3.add(jComboBoxMeses, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 170, 30));

        btnFiltrar.setBackground(new java.awt.Color(124, 146, 221));
        btnFiltrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFiltrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFiltrarMouseClicked(evt);
            }
        });

        jLabelCancelar1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelCancelar1.setForeground(new java.awt.Color(239, 239, 239));
        jLabelCancelar1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCancelar1.setText("Filtrar");

        javax.swing.GroupLayout btnFiltrarLayout = new javax.swing.GroupLayout(btnFiltrar);
        btnFiltrar.setLayout(btnFiltrarLayout);
        btnFiltrarLayout.setHorizontalGroup(
            btnFiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnFiltrarLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabelCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        btnFiltrarLayout.setVerticalGroup(
            btnFiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnFiltrarLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jLabelCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.add(btnFiltrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 10, 140, 40));

        labelFolioTicket.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelFolioTicket.setForeground(new java.awt.Color(238, 238, 238));
        labelFolioTicket.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFolioTicket.setText("Folio ticket");
        jPanel3.add(labelFolioTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 110, -1));

        txtFolioTicket.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel3.add(txtFolioTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 110, 30));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 1240, 560));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1284, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 711, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        Menu menu = new Menu(usuarioActual);
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnCancelarVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarVentaMouseClicked
         int filaSeleccionada = jTableVentas.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Por favor, seleccione una venta de la tabla.");
        return;
    }
    
    // Obtener el folio de la venta seleccionada
    int folio = (int) tableModel.getValueAt(filaSeleccionada, 0);
    String estatusActual = (String) tableModel.getValueAt(filaSeleccionada, 2); // CORREGIDO: columna 2, no 4
    
    // Verificar si ya está cancelada
    if ("cancelada".equals(estatusActual)) {
        javax.swing.JOptionPane.showMessageDialog(this, "Esta venta ya está cancelada.");
        return;
    }
    
    // Confirmar cancelación
    int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
        this, 
        "¿Está seguro de cancelar la venta con folio " + folio + "?", 
        "Confirmar Cancelación", 
        javax.swing.JOptionPane.YES_NO_OPTION
    );
    
    if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
        // Obtener la venta completa
        Venta venta = ventaDao.obtenerVentaPorFolio(folio);
        
        if (venta != null) {
            boolean exito = ventaDao.actualizarEstatusVenta(venta.getIdVenta(), "cancelada");
            
            if (exito) {
                // Actualizar la tabla - CORREGIDO: columna 2, no 4
                tableModel.setValueAt("cancelada", filaSeleccionada, 2);
                javax.swing.JOptionPane.showMessageDialog(this, "Venta cancelada exitosamente.");
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Error al cancelar la venta.");
            }
        }
    }
    }//GEN-LAST:event_btnCancelarVentaMouseClicked

    private void btnFiltrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFiltrarMouseClicked
        String tipoFiltro = (String) jComboBoxTipoFiltro.getSelectedItem();
        
        switch (tipoFiltro) {
            case "Mes":
                String mesSeleccionado = (String) jComboBoxMeses.getSelectedItem();
                switch (mesSeleccionado) {
                    case "Últimos 30 dias":
                        cargarVentasUltimosDias(30);
                        break;
                    case "mes pasado":
                        cargarMesPasado();
                        break;
                    case "Últimos 60 dias":
                        cargarVentasUltimosDias(60);
                        break;
                }
                break;
                
            case "Semana":
                String semanaSeleccionada = (String) jComboBoxSemanas.getSelectedItem();
                switch (semanaSeleccionada) {
                    case "Últimos 7 dias":
                        cargarVentasUltimosDias(7);
                        break;
                    case "Últimos 14 dias":
                        cargarVentasUltimosDias(14);
                        break;
                }
                break;
                
            case "Fecha":
                Date fechaSeleccionada = jDateChooser.getDate();
                if (fechaSeleccionada != null) {
                    cargarVentasPorFechaExacta(fechaSeleccionada);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha.");
                }
                break;
                
            case "Folio":
                String folioTexto = txtFolioTicket.getText().trim();
                if (!folioTexto.isEmpty()) {
                    try {
                        int folio = Integer.parseInt(folioTexto);
                        cargarVentaPorFolio(folio);
                    } catch (NumberFormatException e) {
                        javax.swing.JOptionPane.showMessageDialog(this, "Por favor, ingrese un número de folio válido.");
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Por favor, ingrese un número de folio.");
                }
                break;
        }
    }//GEN-LAST:event_btnFiltrarMouseClicked

    private void txtFolioTicketActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // Si el usuario presiona Enter en el campo de folio, ejecutar el filtro
        btnFiltrarMouseClicked(null);
    }     

    
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
            new JFrameCancelarVenta(userPrueba).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnCancelarVenta;
    private javax.swing.JPanel btnFiltrar;
    private javax.swing.JPanel btnHome;
    private javax.swing.JComboBox<String> jComboBoxMeses;
    private javax.swing.JComboBox<String> jComboBoxSemanas;
    private javax.swing.JComboBox<String> jComboBoxTipoFiltro;
    private com.toedter.calendar.JDateChooser jDateChooser;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelCancelar;
    private javax.swing.JLabel jLabelCancelar1;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableVentas;
    private javax.swing.JLabel labelFolioTicket;
    private javax.swing.JTextField txtFolioTicket;
    // End of variables declaration//GEN-END:variables
}
