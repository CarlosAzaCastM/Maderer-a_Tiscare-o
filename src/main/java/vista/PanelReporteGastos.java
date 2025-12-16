
package vista;

import dao.GastoDAO;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Gasto;
import modelo.Usuario;


public class PanelReporteGastos extends javax.swing.JPanel {
    private Usuario usuario;
    private DefaultTableModel modeloTabla;
    
    public PanelReporteGastos(Usuario usuario) {
        this.usuario = usuario;
        initComponents();
        logicaInicial();
        configurarTabla();
        cargarGastosIniciales();
        jComboBoxTipoGastos.addActionListener(e -> cambiarVisibilidadFiltros());
    }
    
    private void logicaInicial(){
        jComboBoxMesesGastos.setVisible(false);
        jComboBoxSemanasGastos.setVisible(false);
        jDateChooserGastos.setVisible(false);
        
    }
    
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        
        // Definir las columnas según tu BD (sin id_gasto)
        String[] columnas = {"Tipo Gasto", "Descripción", "Monto", "Fecha Gasto", "ID Usuario"};
        modeloTabla.setColumnIdentifiers(columnas);
        jTableGastos.setModel(modeloTabla);
        
        jTableGastos.setRowHeight(40);
        
        // Ajustar el ancho de las columnas
        jTableGastos.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTableGastos.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTableGastos.getColumnModel().getColumn(2).setPreferredWidth(80);
        jTableGastos.getColumnModel().getColumn(3).setPreferredWidth(120);
        jTableGastos.getColumnModel().getColumn(4).setPreferredWidth(80);
    }
    
    private void cargarGastosIniciales() {
        // Cargar todos los gastos inicialmente
        cargarGastos(null);
    }
    
    private void cargarGastos(String condicion) {
        GastoDAO gastoDAO = new GastoDAO();
        List<Gasto> gastos;
        
        if (condicion == null) {
            gastos = gastoDAO.listarGastosPorUsuario(usuario.getIdUsuario());
        } else {
            gastos = gastoDAO.listarGastosFiltrados(usuario.getIdUsuario(), condicion);
        }
        
        cargarGastosEnTabla(gastos);
    }
    
    private void cargarGastosEnTabla(List<Gasto> gastos) {
        // Limpiar el modelo
        modeloTabla.setRowCount(0);
        
        // Agregar cada gasto como una fila en el modelo
        for (Gasto g : gastos) {
            Object[] fila = {
                g.getTipoGasto(),
                g.getDescripcion(),
                g.getMonto(),
                g.getFechaGasto(),
                g.getIdUsuario()
            };
            modeloTabla.addRow(fila);
        }
    }
    
    private void cargarEntradasUltimosDias(int dias) {
        String condicion = "AND fecha_gasto >= DATE_SUB(CURDATE(), INTERVAL " + dias + " DAY)";
        cargarGastos(condicion);
    }

    private void cargarEntradasMesPasado() {
        String condicion = "AND fecha_gasto >= DATE_SUB(DATE_SUB(CURDATE(), INTERVAL DAYOFMONTH(CURDATE())-1 DAY), INTERVAL 1 MONTH) " +
                           "AND fecha_gasto < DATE_SUB(CURDATE(), INTERVAL DAYOFMONTH(CURDATE())-1 DAY)";
        cargarGastos(condicion);
    }

    private void cargarEntradasPorFechaExacta(Date fecha) {
        // Convertir fecha a formato MySQL
        java.sql.Date sqlDate = new java.sql.Date(fecha.getTime());
        String condicion = "AND DATE(fecha_gasto) = '" + sqlDate + "'";
        cargarGastos(condicion);
    }

    // Método adicional para filtrar por tipo de gasto si lo necesitas
    private void cargarGastosPorTipo(String tipo) {
        String condicion = "AND tipo_gasto = '" + tipo + "'";
        cargarGastos(condicion);
    }
    
    private void cambiarVisibilidadFiltros() {
        String filtroSeleccionado = (String) jComboBoxTipoGastos.getSelectedItem();
        
        jComboBoxMesesGastos.setVisible(false);
        jComboBoxSemanasGastos.setVisible(false);
        jDateChooserGastos.setVisible(false);
        
        switch (filtroSeleccionado) {
            case "Mes":
                jComboBoxMesesGastos.setVisible(true);
                break;
            case "Semana":
                jComboBoxSemanasGastos.setVisible(true);
                break;
            case "Fecha":
                jDateChooserGastos.setVisible(true);
                break;
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelGastos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableGastos = new javax.swing.JTable();
        jComboBoxTipoGastos = new javax.swing.JComboBox<>();
        jComboBoxMesesGastos = new javax.swing.JComboBox<>();
        jComboBoxSemanasGastos = new javax.swing.JComboBox<>();
        jDateChooserGastos = new com.toedter.calendar.JDateChooser();
        btnFiltrarGastos = new javax.swing.JPanel();
        jLabelFiltrarGastos = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelGastos.setBackground(new java.awt.Color(62, 44, 32));
        jPanelGastos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jTableGastos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTableGastos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTableGastos);

        jPanelGastos.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 999, 470));

        jComboBoxTipoGastos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxTipoGastos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mes", "Semana", "Fecha" }));
        jPanelGastos.add(jComboBoxTipoGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 150, 30));

        jComboBoxMesesGastos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxMesesGastos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Últimos 30 dias", "mes pasado", "Últimos 60 dias" }));
        jPanelGastos.add(jComboBoxMesesGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 40, 170, 30));

        jComboBoxSemanasGastos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxSemanasGastos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Últimos 7 dias", "Últimos 14 dias" }));
        jPanelGastos.add(jComboBoxSemanasGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 40, 160, 30));
        jPanelGastos.add(jDateChooserGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 40, 180, 30));

        btnFiltrarGastos.setBackground(new java.awt.Color(124, 146, 221));
        btnFiltrarGastos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFiltrarGastos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFiltrarGastosMouseClicked(evt);
            }
        });

        jLabelFiltrarGastos.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelFiltrarGastos.setForeground(new java.awt.Color(239, 239, 239));
        jLabelFiltrarGastos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFiltrarGastos.setText("Filtrar");

        javax.swing.GroupLayout btnFiltrarGastosLayout = new javax.swing.GroupLayout(btnFiltrarGastos);
        btnFiltrarGastos.setLayout(btnFiltrarGastosLayout);
        btnFiltrarGastosLayout.setHorizontalGroup(
            btnFiltrarGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnFiltrarGastosLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabelFiltrarGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        btnFiltrarGastosLayout.setVerticalGroup(
            btnFiltrarGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnFiltrarGastosLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jLabelFiltrarGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelGastos.add(btnFiltrarGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 30, 140, 40));

        add(jPanelGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(-11, -13, 1270, 620));
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarGastosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFiltrarGastosMouseClicked
         String tipoFiltro = (String) jComboBoxTipoGastos.getSelectedItem();

        switch (tipoFiltro) {
            case "Mes":
                String mesSeleccionado = (String) jComboBoxMesesGastos.getSelectedItem();
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
                String semanaSeleccionada = (String) jComboBoxSemanasGastos.getSelectedItem();
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
                Date fechaSeleccionada = jDateChooserGastos.getDate();
                if (fechaSeleccionada != null) {
                    cargarEntradasPorFechaExacta(fechaSeleccionada);
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha.");
                }
                break;
        }
    }//GEN-LAST:event_btnFiltrarGastosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnFiltrarGastos;
    private javax.swing.JComboBox<String> jComboBoxMesesGastos;
    private javax.swing.JComboBox<String> jComboBoxSemanasGastos;
    private javax.swing.JComboBox<String> jComboBoxTipoGastos;
    private com.toedter.calendar.JDateChooser jDateChooserGastos;
    private javax.swing.JLabel jLabelFiltrarGastos;
    private javax.swing.JPanel jPanelGastos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableGastos;
    // End of variables declaration//GEN-END:variables
}
