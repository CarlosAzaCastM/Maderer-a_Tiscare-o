package vista;

import dao.EntradaInventarioDAO;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.EntradaInventario;
import modelo.Usuario;

public class PanelReporteInventario extends javax.swing.JPanel {
    private Usuario usuarioActual;
    private DefaultTableModel tableModelInventario;
    private EntradaInventarioDAO entradaInventarioDao;
    private boolean datosCargados = false;
    
    public PanelReporteInventario(Usuario usuario) {
        initComponents();
        usuarioActual = usuario;
        entradaInventarioDao = new EntradaInventarioDAO();
        
        configurarTabla();
        logicaInicial();
        
        jComboBoxTipoFiltroInventario.addActionListener(e -> cambiarVisibilidadFiltros());
    }
    
    public void asegurarCargaDatos() {
        // Solo va a la BD si nunca ha cargado datos antes
        if (!datosCargados) {
            cargarEntradasUltimosDias(7);
            datosCargados = true; // Marcamos como cargado
        }
    }
    
    private void configurarTabla() {
        tableModelInventario = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Columnas para las entradas de inventario
        String[] columnas = {
            "Cantidad", "Producto", "Clase", "Medida", "Grosor", 
            "Nuevo Costo", "Nuevo Precio", "Fecha"
        };
        tableModelInventario.setColumnIdentifiers(columnas);
        jTableEntradasInventario.setModel(tableModelInventario);
        
        // Ajustar el ancho de las columnas
        jTableEntradasInventario.getColumnModel().getColumn(0).setPreferredWidth(80);  // Cantidad
        jTableEntradasInventario.getColumnModel().getColumn(1).setPreferredWidth(150); // Producto
        jTableEntradasInventario.getColumnModel().getColumn(2).setPreferredWidth(100); // Clase
        jTableEntradasInventario.getColumnModel().getColumn(3).setPreferredWidth(100); // Medida
        jTableEntradasInventario.getColumnModel().getColumn(4).setPreferredWidth(100); // Grosor  
        jTableEntradasInventario.getColumnModel().getColumn(5).setPreferredWidth(100); // Nuevo Costo
        jTableEntradasInventario.getColumnModel().getColumn(6).setPreferredWidth(100); // Nuevo Precio
        jTableEntradasInventario.getColumnModel().getColumn(7).setPreferredWidth(150); // Fecha
        
        // Aumentar la altura de las filas
        jTableEntradasInventario.setRowHeight(40);
        
        // Aumentar el tamaño de la fuente
        jTableEntradasInventario.setFont(new java.awt.Font("Segoe UI", 0, 16));
        jTableEntradasInventario.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 16));
        
        // Centrar el texto en algunas columnas
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jTableEntradasInventario.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Cantidad
        
        // Alinear a la derecha las columnas numéricas
        javax.swing.table.DefaultTableCellRenderer rightRenderer = new javax.swing.table.DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jTableEntradasInventario.getColumnModel().getColumn(5).setCellRenderer(rightRenderer); // Nuevo Costo
        jTableEntradasInventario.getColumnModel().getColumn(6).setCellRenderer(rightRenderer); // Nuevo Precio
        
        // Centrar la fecha
        jTableEntradasInventario.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // Fecha
    }
    
    private void cambiarVisibilidadFiltros() {
        String filtroSeleccionado = (String) jComboBoxTipoFiltroInventario.getSelectedItem();
        
        jComboBoxMesesInventario.setVisible(false);
        jComboBoxSemanasInventario.setVisible(false);
        jDateChooserInicio.setVisible(false);
        jDateChooserFin.setVisible(false);
        jLabel2.setVisible(false);
        jLabel3.setVisible(false);
        
        switch (filtroSeleccionado) {
            case "Mes":
                jComboBoxMesesInventario.setVisible(true);
                break;
            case "Semana":
                jComboBoxSemanasInventario.setVisible(true);
                break;
            case "Fecha":
                jDateChooserInicio.setVisible(true);
                jDateChooserFin.setVisible(true);
                break;
        }
    }
    
    private void cargarEntradasUltimosDias(int dias) {
        Calendar calendario = Calendar.getInstance();
        
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.SECOND, 59);
        Date fechaFin = calendario.getTime();
        
        // Ahora restamos los días para encontrar el inicio
        calendario.add(Calendar.DAY_OF_YEAR, -dias);
        
        // Ajustamos el inicio al primer segundo de ese día
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        Date fechaInicio = calendario.getTime();
        
        List<EntradaInventario> entradas = entradaInventarioDao.obtenerEntradasPorFecha(fechaInicio, fechaFin);
        actualizarTablaInventario(entradas);
    }
    
    private void cargarEntradasPorFechaExacta(Date fechaI, Date fechaF) {
        Calendar calendario = Calendar.getInstance();
        
        calendario.setTime(fechaI);
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        Date fechaInicio = calendario.getTime();
        
        // Establecer fin del día
        calendario.setTime(fechaF);
        calendario.set(Calendar.HOUR_OF_DAY, 23);
        calendario.set(Calendar.MINUTE, 59);
        calendario.set(Calendar.SECOND, 59);
        Date fechaFin = calendario.getTime();
        
        List<EntradaInventario> entradas = entradaInventarioDao.obtenerEntradasPorFecha(fechaInicio, fechaFin);
        actualizarTablaInventario(entradas);
    }
    
    private void cargarEntradasMesPasado() {
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
        
        List<EntradaInventario> entradas = entradaInventarioDao.obtenerEntradasPorFecha(fechaInicio, fechaFin);
        actualizarTablaInventario(entradas);
    }
    
    private void actualizarTablaInventario(List<EntradaInventario> entradas) {
        // Limpiar la tabla
        tableModelInventario.setRowCount(0);
        
        // Formato para la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        // Formato para moneda
        java.text.DecimalFormat df = new java.text.DecimalFormat("$#,##0.00");
        
        // Agregar las entradas a la tabla
        for (EntradaInventario entrada : entradas) {
            try {
                Date fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entrada.getFechaEntrada());
                String fechaFormateada = sdf.format(fecha);
                
                Object[] fila = {
                    entrada.getCantidadAgregada(),
                    entrada.getNombreProducto() != null ? entrada.getNombreProducto() : "N/A",
                    entrada.getClase() != null ? entrada.getClase() : "N/A",
                    entrada.getMedida() != null ? entrada.getMedida() : "N/A",
                    entrada.getGrosor() != null ? entrada.getGrosor() : "N/A",
                    df.format(entrada.getNuevoCostoCompra()),
                    df.format(entrada.getNuevoPrecioVenta()),
                    fechaFormateada
                };
                tableModelInventario.addRow(fila);
            } catch (Exception e) {
                // Si hay error en el formato de fecha, usar el string original
                Object[] fila = {
                    entrada.getCantidadAgregada(),
                    entrada.getNombreProducto() != null ? entrada.getNombreProducto() : "N/A",
                    entrada.getClase() != null ? entrada.getClase() : "N/A",
                    entrada.getMedida() != null ? entrada.getMedida() : "N/A",
                    entrada.getGrosor() != null ? entrada.getGrosor() : "N/A",
                    df.format(entrada.getNuevoCostoCompra()),
                    df.format(entrada.getNuevoPrecioVenta()),
                    entrada.getFechaEntrada()
                };
                tableModelInventario.addRow(fila);
            }
        }
        jTableEntradasInventario.repaint();
    }
    
    private void logicaInicial() {
        jComboBoxMesesInventario.setVisible(false);
        jComboBoxSemanasInventario.setVisible(false);
        jDateChooserInicio.setVisible(false);
        jDateChooserFin.setVisible(false);
        jLabel2.setVisible(false);
        jLabel3.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelInventario = new javax.swing.JPanel();
        jComboBoxSemanasInventario = new javax.swing.JComboBox<>();
        jComboBoxTipoFiltroInventario = new javax.swing.JComboBox<>();
        jDateChooserInicio = new com.toedter.calendar.JDateChooser();
        btnFiltrarInventario = new javax.swing.JPanel();
        jLabelCancelar2 = new javax.swing.JLabel();
        jComboBoxMesesInventario = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableEntradasInventario = new javax.swing.JTable();
        jDateChooserFin = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelInventario.setBackground(new java.awt.Color(62, 44, 32));
        jPanelInventario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBoxSemanasInventario.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxSemanasInventario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Últimos 7 dias", "Últimos 14 dias" }));
        jPanelInventario.add(jComboBoxSemanasInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 40, 160, 30));

        jComboBoxTipoFiltroInventario.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxTipoFiltroInventario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mes", "Semana", "Fecha" }));
        jPanelInventario.add(jComboBoxTipoFiltroInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 150, 30));
        jPanelInventario.add(jDateChooserInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 40, 180, 30));

        btnFiltrarInventario.setBackground(new java.awt.Color(124, 146, 221));
        btnFiltrarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFiltrarInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFiltrarInventarioMouseClicked(evt);
            }
        });

        jLabelCancelar2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelCancelar2.setForeground(new java.awt.Color(239, 239, 239));
        jLabelCancelar2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCancelar2.setText("Filtrar");

        javax.swing.GroupLayout btnFiltrarInventarioLayout = new javax.swing.GroupLayout(btnFiltrarInventario);
        btnFiltrarInventario.setLayout(btnFiltrarInventarioLayout);
        btnFiltrarInventarioLayout.setHorizontalGroup(
            btnFiltrarInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnFiltrarInventarioLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabelCancelar2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        btnFiltrarInventarioLayout.setVerticalGroup(
            btnFiltrarInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnFiltrarInventarioLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jLabelCancelar2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelInventario.add(btnFiltrarInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 30, 140, 40));

        jComboBoxMesesInventario.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxMesesInventario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Últimos 30 dias", "mes pasado", "Últimos 60 dias" }));
        jPanelInventario.add(jComboBoxMesesInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 170, 30));

        jTableEntradasInventario.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTableEntradasInventario);

        jPanelInventario.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 85, 1030, 450));
        jPanelInventario.add(jDateChooserFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 40, 180, 30));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(241, 241, 241));
        jLabel2.setText("Inicio");
        jPanelInventario.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 90, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(241, 241, 241));
        jLabel3.setText("Fin:");
        jPanelInventario.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 40, 90, 30));

        add(jPanelInventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 580));
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFiltrarInventarioMouseClicked
         String tipoFiltro = (String) jComboBoxTipoFiltroInventario.getSelectedItem();

        switch (tipoFiltro) {
            case "Mes":
                String mesSeleccionado = (String) jComboBoxMesesInventario.getSelectedItem();
                switch (mesSeleccionado) {
                    case "Últimos 30 dias":
                        cargarEntradasUltimosDias(30);
                        break;
                    case "mes pasado":
                        cargarEntradasMesPasado();
                        break;
                    case "Últimos 60 dias":
                        cargarEntradasUltimosDias(60);
                        break;
                }
                break;

            case "Semana":
                String semanaSeleccionada = (String) jComboBoxSemanasInventario.getSelectedItem();
                switch (semanaSeleccionada) {
                    case "Últimos 7 dias":
                        cargarEntradasUltimosDias(7);
                        break;
                    case "Últimos 14 dias":
                        cargarEntradasUltimosDias(14);
                        break;
                }
                break;

            case "Fecha":
                Date fechaSeleccionada = jDateChooserInicio.getDate();
                Date fechaSeleccionadaFin = jDateChooserFin.getDate();
                if (fechaSeleccionada != null && fechaSeleccionadaFin != null) {
                    if (fechaSeleccionada.after(fechaSeleccionadaFin)) {
                        JOptionPane.showMessageDialog(this, "La fecha de inicio no puede ser posterior a la final.");
                    } else {
                        cargarEntradasPorFechaExacta(fechaSeleccionada, fechaSeleccionadaFin);
                    }     
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha.");
                }
                break;
        }
    }//GEN-LAST:event_btnFiltrarInventarioMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnFiltrarInventario;
    private javax.swing.JComboBox<String> jComboBoxMesesInventario;
    private javax.swing.JComboBox<String> jComboBoxSemanasInventario;
    private javax.swing.JComboBox<String> jComboBoxTipoFiltroInventario;
    private com.toedter.calendar.JDateChooser jDateChooserFin;
    private com.toedter.calendar.JDateChooser jDateChooserInicio;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelCancelar2;
    private javax.swing.JPanel jPanelInventario;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableEntradasInventario;
    // End of variables declaration//GEN-END:variables
}
