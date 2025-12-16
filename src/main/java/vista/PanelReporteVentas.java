
package vista;

import dao.VentaDao;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import modelo.Usuario;
import modelo.Venta;

public class PanelReporteVentas extends javax.swing.JPanel {
    private Usuario usuarioActual;
    private DefaultTableModel tableModel;
    private VentaDao ventaDao;
    
    public PanelReporteVentas(Usuario usuario) {
        initComponents();
        
        usuarioActual = usuario;
        ventaDao = new VentaDao();
        
        configurarTabla();
        logicaInicial();
        
        cargarVentasUltimosDias(7);
        
        jComboBoxTipoFiltroVentas.addActionListener(e -> cambiarVisibilidadFiltros());
        
        
        agregarListenerDobleClic();
    }
    
     private void agregarListenerDobleClic() {
    jTableVentas.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getClickCount() == 2) { // Doble clic
                int filaSeleccionada = jTableVentas.getSelectedRow();
                if (filaSeleccionada != -1) {
                    // Obtener el folio de la venta (columna 0)
                    int folioTicket = (int) tableModel.getValueAt(filaSeleccionada, 0);
                    // Obtener la venta completa por folio
                    Venta venta = ventaDao.obtenerVentaPorFolio(folioTicket);
                    if (venta != null) {
                        abrirDetalleVenta(venta);
                    }
                }
            }
        }
    });
}

private void abrirDetalleVenta(Venta venta) {
    // Abrir JFrameDetalleVenta pasando la venta
    JFrameDetalleVenta detalleVenta = new JFrameDetalleVenta(usuarioActual, venta);
    detalleVenta.setVisible(true);
    // Opcional: puedes mantener esta ventana abierta o cerrarla
    // this.dispose();
}

    
    private void configurarTabla() {
    // Configurar el modelo de la tabla
    tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Hacer que la tabla no sea editable
        }
    };
    
    // Definir las columnas con TODAS las columnas excepto id_venta y descuento_porcentaje
    String[] columnas = {
        "Folio", 
        "Fecha", 
        "Subtotal", 
        "Descuento", 
        "Total", 
        "Efectivo", 
        "Tarjeta", 
        "Usuario",  // Ahora mostrará el nombre completo
        "Estatus"
    };
    tableModel.setColumnIdentifiers(columnas);
    jTableVentas.setModel(tableModel);
    
    // Ajustar el ancho de las columnas
    jTableVentas.getColumnModel().getColumn(0).setPreferredWidth(80);   // Folio
    jTableVentas.getColumnModel().getColumn(1).setPreferredWidth(150);  // Fecha
    jTableVentas.getColumnModel().getColumn(2).setPreferredWidth(100);  // Subtotal
    jTableVentas.getColumnModel().getColumn(3).setPreferredWidth(100);  // Descuento
    jTableVentas.getColumnModel().getColumn(4).setPreferredWidth(100);  // Total
    jTableVentas.getColumnModel().getColumn(5).setPreferredWidth(100);  // Efectivo
    jTableVentas.getColumnModel().getColumn(6).setPreferredWidth(100);  // Tarjeta
    jTableVentas.getColumnModel().getColumn(7).setPreferredWidth(150);  // Usuario - más ancho para el nombre
    jTableVentas.getColumnModel().getColumn(8).setPreferredWidth(100);  // Estatus
    
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
    // Crear un renderizador personalizado para la columna de estatus (columna 8)
    jTableVentas.getColumnModel().getColumn(8).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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

    // Centrar el folio (columna 0)
    javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jTableVentas.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Folio (columna 0)
    
    // Centrar la fecha (columna 1)
    jTableVentas.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Fecha (columna 1)
    
    // Alinear a la derecha las columnas numéricas
    javax.swing.table.DefaultTableCellRenderer rightRenderer = new javax.swing.table.DefaultTableCellRenderer();
    rightRenderer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jTableVentas.getColumnModel().getColumn(2).setCellRenderer(rightRenderer); // Subtotal
    jTableVentas.getColumnModel().getColumn(3).setCellRenderer(rightRenderer); // Descuento
    jTableVentas.getColumnModel().getColumn(4).setCellRenderer(rightRenderer); // Total
    jTableVentas.getColumnModel().getColumn(5).setCellRenderer(rightRenderer); // Efectivo
    jTableVentas.getColumnModel().getColumn(6).setCellRenderer(rightRenderer); // Tarjeta
    
    // Centrar el nombre del usuario (columna 7)
    jTableVentas.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // Usuario (nombre)
}
    
    private void cambiarVisibilidadFiltros() {
        String filtroSeleccionado = (String) jComboBoxTipoFiltroVentas.getSelectedItem();
        
        // Ocultar todos primero
        jComboBoxMesesVentas.setVisible(false);
        jComboBoxSemanasVentas.setVisible(false);
        jDateChooserVentas.setVisible(false);
        txtFolioTicketVentas.setVisible(false);
        labelFolioTicket.setVisible(false);
        
        // Mostrar solo el correspondiente
        switch (filtroSeleccionado) {
            case "Mes":
                jComboBoxMesesVentas.setVisible(true);
                break;
            case "Semana":
                jComboBoxSemanasVentas.setVisible(true);
                break;
            case "Fecha":
                jDateChooserVentas.setVisible(true);
                break;
            case "Folio":
                txtFolioTicketVentas.setVisible(true);
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
    
    // Formato para moneda
    java.text.DecimalFormat df = new java.text.DecimalFormat("$#,##0.00");
    
    // Agregar las ventas a la tabla
    for (Venta venta : ventas) {
        try {
            Date fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(venta.getFechaVenta());
            String fechaFormateada = sdf.format(fecha);
            
            Object[] fila = {
                venta.getFolioTicket(),
                fechaFormateada,
                df.format(venta.getSubtotal()),
                df.format(venta.getDescuentoDinero()),
                df.format(venta.getTotal()),
                df.format(venta.getPagoEfectivo()),
                df.format(venta.getPagoTarjeta()),
                venta.getNombreUsuario() != null ? venta.getNombreUsuario() : "N/A", // Usamos el nombre, no el ID
                venta.getEstatus()
            };
            tableModel.addRow(fila);
        } catch (Exception e) {
            // Si hay error en el formato de fecha, usar el string original
            Object[] fila = {
                venta.getFolioTicket(),
                venta.getFechaVenta(),
                df.format(venta.getSubtotal()),
                df.format(venta.getDescuentoDinero()),
                df.format(venta.getTotal()),
                df.format(venta.getPagoEfectivo()),
                df.format(venta.getPagoTarjeta()),
                venta.getNombreUsuario() != null ? venta.getNombreUsuario() : "N/A", // Usamos el nombre, no el ID
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
        jComboBoxMesesVentas.setVisible(false);
        jComboBoxSemanasVentas.setVisible(false);
        jDateChooserVentas.setVisible(false);
        txtFolioTicketVentas.setVisible(false);
        labelFolioTicket.setVisible(false);
    }


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelVentas = new javax.swing.JPanel();
        jComboBoxTipoFiltroVentas = new javax.swing.JComboBox<>();
        txtFolioTicketVentas = new javax.swing.JTextField();
        labelFolioTicket = new javax.swing.JLabel();
        jComboBoxMesesVentas = new javax.swing.JComboBox<>();
        jComboBoxSemanasVentas = new javax.swing.JComboBox<>();
        jDateChooserVentas = new com.toedter.calendar.JDateChooser();
        btnFiltrarVentas = new javax.swing.JPanel();
        jLabelCancelar1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableVentas = new javax.swing.JTable();
        btnCancelarVenta = new javax.swing.JPanel();
        jLabelCancelar = new javax.swing.JLabel();
        btnCompletarVenta = new javax.swing.JPanel();
        jLabelCompletar = new javax.swing.JLabel();

        jPanelVentas.setBackground(new java.awt.Color(62, 44, 32));
        jPanelVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelVentas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBoxTipoFiltroVentas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxTipoFiltroVentas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Folio", "Mes", "Semana", "Fecha" }));
        jPanelVentas.add(jComboBoxTipoFiltroVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 150, 30));

        txtFolioTicketVentas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanelVentas.add(txtFolioTicketVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 110, 30));

        labelFolioTicket.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelFolioTicket.setForeground(new java.awt.Color(238, 238, 238));
        labelFolioTicket.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFolioTicket.setText("Folio ticket");
        jPanelVentas.add(labelFolioTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 110, -1));

        jComboBoxMesesVentas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxMesesVentas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Últimos 30 dias", "mes pasado", "Últimos 60 dias" }));
        jPanelVentas.add(jComboBoxMesesVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 170, 30));

        jComboBoxSemanasVentas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxSemanasVentas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Últimos 7 dias", "Últimos 14 dias" }));
        jPanelVentas.add(jComboBoxSemanasVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 160, 30));
        jPanelVentas.add(jDateChooserVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, 180, 30));

        btnFiltrarVentas.setBackground(new java.awt.Color(124, 146, 221));
        btnFiltrarVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFiltrarVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFiltrarVentasMouseClicked(evt);
            }
        });

        jLabelCancelar1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelCancelar1.setForeground(new java.awt.Color(239, 239, 239));
        jLabelCancelar1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCancelar1.setText("Filtrar");

        javax.swing.GroupLayout btnFiltrarVentasLayout = new javax.swing.GroupLayout(btnFiltrarVentas);
        btnFiltrarVentas.setLayout(btnFiltrarVentasLayout);
        btnFiltrarVentasLayout.setHorizontalGroup(
            btnFiltrarVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnFiltrarVentasLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabelCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        btnFiltrarVentasLayout.setVerticalGroup(
            btnFiltrarVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnFiltrarVentasLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jLabelCancelar1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelVentas.add(btnFiltrarVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 10, 140, 40));

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

        jPanelVentas.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 980, 440));

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
                .addGap(23, 23, 23)
                .addComponent(jLabelCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        btnCancelarVentaLayout.setVerticalGroup(
            btnCancelarVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnCancelarVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelVentas.add(btnCancelarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 220, 170, -1));

        btnCompletarVenta.setBackground(new java.awt.Color(56, 135, 56));
        btnCompletarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCompletarVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCompletarVentaMouseClicked(evt);
            }
        });

        jLabelCompletar.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelCompletar.setForeground(new java.awt.Color(239, 239, 239));
        jLabelCompletar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCompletar.setText("Completar");

        javax.swing.GroupLayout btnCompletarVentaLayout = new javax.swing.GroupLayout(btnCompletarVenta);
        btnCompletarVenta.setLayout(btnCompletarVentaLayout);
        btnCompletarVentaLayout.setHorizontalGroup(
            btnCompletarVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnCompletarVentaLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabelCompletar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        btnCompletarVentaLayout.setVerticalGroup(
            btnCompletarVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnCompletarVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelCompletar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelVentas.add(btnCompletarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 340, 170, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1262, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanelVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 1256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 6, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 602, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanelVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 6, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFiltrarVentasMouseClicked
        String tipoFiltro = (String) jComboBoxTipoFiltroVentas.getSelectedItem();

        switch (tipoFiltro) {
            case "Mes":
            String mesSeleccionado = (String) jComboBoxMesesVentas.getSelectedItem();
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
            String semanaSeleccionada = (String) jComboBoxSemanasVentas.getSelectedItem();
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
            Date fechaSeleccionada = jDateChooserVentas.getDate();
            if (fechaSeleccionada != null) {
                cargarVentasPorFechaExacta(fechaSeleccionada);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha.");
            }
            break;

            case "Folio":
            String folioTexto = txtFolioTicketVentas.getText().trim();
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
    }//GEN-LAST:event_btnFiltrarVentasMouseClicked

    private void btnCancelarVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarVentaMouseClicked
        int filaSeleccionada = jTableVentas.getSelectedRow();

        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor, seleccione una venta de la tabla.");
            return;
        }

        // Obtener el folio de la venta seleccionada (columna 0)
        int folio = (int) tableModel.getValueAt(filaSeleccionada, 0);
        // Obtener el estatus actual (columna 8)
        String estatusActual = (String) tableModel.getValueAt(filaSeleccionada, 8);

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
                    // Actualizar la tabla en la columna 8
                    tableModel.setValueAt("cancelada", filaSeleccionada, 8);
                    javax.swing.JOptionPane.showMessageDialog(this, "Venta cancelada exitosamente.");
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Error al cancelar la venta.");
                }
            }
        }
    }//GEN-LAST:event_btnCancelarVentaMouseClicked

    private void btnCompletarVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCompletarVentaMouseClicked
        int filaSeleccionada = jTableVentas.getSelectedRow();

        if (filaSeleccionada == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor, seleccione una venta de la tabla.");
            return;
        }

        // Obtener el folio de la venta seleccionada (columna 0)
        int folio = (int) tableModel.getValueAt(filaSeleccionada, 0);
        // Obtener el estatus actual (columna 8)
        String estatusActual = (String) tableModel.getValueAt(filaSeleccionada, 8);

        // Verificar si ya está completada
        if ("completada".equals(estatusActual)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Esta venta ya está completada.");
            return;
        }

        // Confirmar completar
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de marcar como completada la venta con folio " + folio + "?",
            "Confirmar Completar",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            // Obtener la venta completa
            Venta venta = ventaDao.obtenerVentaPorFolio(folio);

            if (venta != null) {
                boolean exito = ventaDao.actualizarEstatusVenta(venta.getIdVenta(), "completada");

                if (exito) {
                    // Actualizar la tabla en la columna 8
                    tableModel.setValueAt("completada", filaSeleccionada, 8);
                    javax.swing.JOptionPane.showMessageDialog(this, "Venta completada exitosamente.");
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Error al completar la venta.");
                }
            }
        }
    }//GEN-LAST:event_btnCompletarVentaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnCancelarVenta;
    private javax.swing.JPanel btnCompletarVenta;
    private javax.swing.JPanel btnFiltrarVentas;
    private javax.swing.JComboBox<String> jComboBoxMesesVentas;
    private javax.swing.JComboBox<String> jComboBoxSemanasVentas;
    private javax.swing.JComboBox<String> jComboBoxTipoFiltroVentas;
    private com.toedter.calendar.JDateChooser jDateChooserVentas;
    private javax.swing.JLabel jLabelCancelar;
    private javax.swing.JLabel jLabelCancelar1;
    private javax.swing.JLabel jLabelCompletar;
    private javax.swing.JPanel jPanelVentas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableVentas;
    private javax.swing.JLabel labelFolioTicket;
    private javax.swing.JTextField txtFolioTicketVentas;
    // End of variables declaration//GEN-END:variables
}
