
package vista;

import dao.GastoDAO;
import dao.ReporteDAO;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import modelo.Usuario;

public class PanelReporteGanancias extends javax.swing.JPanel {
    private Usuario usuario;
    private boolean datosCargados = false;
    
    public PanelReporteGanancias(Usuario usuario) {
        this.usuario = usuario;
        initComponents();
        logicaInicial();
        jComboBoxTipoFiltro.addActionListener(e -> cambiarVisibilidadFiltros());
    }
    
    public void asegurarCargaDatos() {
        // Solo va a la BD si nunca ha cargado datos antes
        if (!datosCargados) {
            System.out.println("Cargando datos de Ganancia por primera vez...");
            datosCargados = true; // Marcamos como cargado
        }
    }
    
    private void logicaInicial() {
        jComboBoxMesesGanancia.setVisible(false);
        jComboBoxSemanasGastos.setVisible(false);
        jDateChooserGanancia.setVisible(false);
        jDateChooserFin.setVisible(false);
        jLabelInicio.setVisible(false);
        jLabelFin.setVisible(false);
        
        // Inicializar etiquetas
        jLabelGananciaBruta.setText("$0.00");
        jLabelGastos.setText("$0.00");
        jLabelGananciaNeta.setText("$0.00");
    }
    
    private void cambiarVisibilidadFiltros() {
        String filtroSeleccionado = (String) jComboBoxTipoFiltro.getSelectedItem();
        
        jComboBoxMesesGanancia.setVisible(false);
        jComboBoxSemanasGastos.setVisible(false);
        jDateChooserGanancia.setVisible(false);
        jDateChooserFin.setVisible(false);
        jLabelInicio.setVisible(false);
        jLabelFin.setVisible(false);
        
        switch (filtroSeleccionado) {
            case "Mes":
                jComboBoxMesesGanancia.setVisible(true);
                break;
            case "Semana":
                jComboBoxSemanasGastos.setVisible(true);
                break;
            case "Fecha":
                jDateChooserGanancia.setVisible(true);
                jDateChooserFin.setVisible(true);
                jLabelInicio.setVisible(true);
                jLabelFin.setVisible(true);
                break;
        }
    }
    
    private void calcularGanancias(Date fechaInicio, Date fechaFin) {
        try {
            ReporteDAO reporteDAO = new ReporteDAO();
            GastoDAO gastoDAO = new GastoDAO();
            
            // Calcular ganancia bruta
            double gananciaBruta = reporteDAO.calcularGananciaBruta(fechaInicio, fechaFin, usuario.getIdUsuario());
            
            // Calcular total de gastos
            double totalGastos = reporteDAO.calcularTotalGastos(fechaInicio, fechaFin, usuario.getIdUsuario());
            
            double totalVentas = reporteDAO.calcularTotalVentas(fechaInicio, fechaFin, usuario.getIdUsuario());
            
            // Calcular ganancia neta
            double gananciaNeta = gananciaBruta - totalGastos;
            
            // Formatear los números como moneda
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            
            // Actualizar etiquetas
            jLabelGananciaBruta.setText(formatter.format(gananciaBruta));
            jLabelGastos.setText(formatter.format(totalGastos));
            jLabelGananciaNeta.setText(formatter.format(gananciaNeta));
            jLabelVentaTotal.setText(formatter.format(totalVentas));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al calcular ganancias: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private Date[] obtenerRangoFechas() {
        Date fechaInicio = null;
        Date fechaFin = null;
        
        String tipoFiltro = (String) jComboBoxTipoFiltro.getSelectedItem();
        Calendar calendar = Calendar.getInstance();
        
        switch (tipoFiltro) {
            case "Mes":
                String mesSeleccionado = (String) jComboBoxMesesGanancia.getSelectedItem();
                switch (mesSeleccionado) {
                    case "Últimos 30 dias":
                        fechaFin = new Date();
                        calendar.setTime(fechaFin);
                        calendar.add(Calendar.DAY_OF_MONTH, -30);
                        fechaInicio = calendar.getTime();
                        break;
                        
                    case "mes pasado":
                        // Primer día del mes pasado
                        calendar.add(Calendar.MONTH, -1);
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        fechaInicio = calendar.getTime();
                        
                        // Último día del mes pasado
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                        fechaFin = calendar.getTime();
                        break;
                        
                    case "Últimos 60 dias":
                        fechaFin = new Date();
                        calendar.setTime(fechaFin);
                        calendar.add(Calendar.DAY_OF_MONTH, -60);
                        fechaInicio = calendar.getTime();
                        break;
                }
                break;
                
            case "Semana":
                String semanaSeleccionada = (String) jComboBoxSemanasGastos.getSelectedItem();
                fechaFin = new Date();
                calendar.setTime(fechaFin);
                
                switch (semanaSeleccionada) {
                    case "Últimos 7 dias":
                        calendar.add(Calendar.DAY_OF_MONTH, -7);
                        break;
                    case "Últimos 14 dias":
                        calendar.add(Calendar.DAY_OF_MONTH, -14);
                        break;
                }
                fechaInicio = calendar.getTime();
                break;
                
            case "Fecha":
                Date fechaSeleccionada = jDateChooserGanancia.getDate();
                Date fechaFinalSeleccionada = jDateChooserFin.getDate();
                if (fechaSeleccionada != null && fechaFinalSeleccionada != null) {
                    // Establecer fecha de inicio al inicio del día
                    calendar.setTime(fechaSeleccionada);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    fechaInicio = calendar.getTime();
                    
                    calendar.setTime(fechaFinalSeleccionada);
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    calendar.set(Calendar.MILLISECOND, 999);
                    fechaFin = calendar.getTime();
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha.");
                    return null;
                }
                break;
        }
        
        // Si no se seleccionó fecha específica, usar fecha actual como fin
        if (fechaFin == null) {
            fechaFin = new Date();
        }
        
        // Asegurarse de que la fecha de fin sea el final del día
        if (tipoFiltro != "Fecha") {
            calendar.setTime(fechaFin);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            fechaFin = calendar.getTime();
        }
        
        return new Date[]{fechaInicio, fechaFin};
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelGastos = new javax.swing.JPanel();
        jComboBoxTipoFiltro = new javax.swing.JComboBox<>();
        jComboBoxMesesGanancia = new javax.swing.JComboBox<>();
        jComboBoxSemanasGastos = new javax.swing.JComboBox<>();
        jDateChooserGanancia = new com.toedter.calendar.JDateChooser();
        btnFiltrarGanancia = new javax.swing.JPanel();
        jLabelFiltrarGastos = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabelGananciaNeta = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabelGananciaBruta = new javax.swing.JLabel();
        jLabelGastos = new javax.swing.JLabel();
        jLabelInicio = new javax.swing.JLabel();
        jLabelFin = new javax.swing.JLabel();
        jDateChooserFin = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jLabelVentaTotal = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelGastos.setBackground(new java.awt.Color(62, 44, 32));
        jPanelGastos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBoxTipoFiltro.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxTipoFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mes", "Semana", "Fecha" }));
        jPanelGastos.add(jComboBoxTipoFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 150, 30));

        jComboBoxMesesGanancia.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxMesesGanancia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Últimos 30 dias", "mes pasado", "Últimos 60 dias" }));
        jPanelGastos.add(jComboBoxMesesGanancia, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 40, 170, 30));

        jComboBoxSemanasGastos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jComboBoxSemanasGastos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Últimos 7 dias", "Últimos 14 dias" }));
        jPanelGastos.add(jComboBoxSemanasGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 40, 160, 30));
        jPanelGastos.add(jDateChooserGanancia, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, 180, 30));

        btnFiltrarGanancia.setBackground(new java.awt.Color(124, 146, 221));
        btnFiltrarGanancia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFiltrarGanancia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFiltrarGananciaMouseClicked(evt);
            }
        });

        jLabelFiltrarGastos.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelFiltrarGastos.setForeground(new java.awt.Color(239, 239, 239));
        jLabelFiltrarGastos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFiltrarGastos.setText("Filtrar");

        javax.swing.GroupLayout btnFiltrarGananciaLayout = new javax.swing.GroupLayout(btnFiltrarGanancia);
        btnFiltrarGanancia.setLayout(btnFiltrarGananciaLayout);
        btnFiltrarGananciaLayout.setHorizontalGroup(
            btnFiltrarGananciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnFiltrarGananciaLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabelFiltrarGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        btnFiltrarGananciaLayout.setVerticalGroup(
            btnFiltrarGananciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnFiltrarGananciaLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jLabelFiltrarGastos, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelGastos.add(btnFiltrarGanancia, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 30, 140, 40));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(245, 245, 245));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Ganancia Neta");
        jPanelGastos.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 440, 220, 70));

        jLabelGananciaNeta.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabelGananciaNeta.setForeground(new java.awt.Color(245, 245, 245));
        jLabelGananciaNeta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelGananciaNeta.setText("$");
        jPanelGastos.add(jLabelGananciaNeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 440, 390, 70));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(243, 117, 117));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("-");
        jPanelGastos.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 360, 40, 20));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(245, 245, 245));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Gastos");
        jPanelGastos.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 340, 220, 70));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(245, 245, 245));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Ventas Totales");
        jPanelGastos.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, 220, 70));

        jLabelGananciaBruta.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabelGananciaBruta.setForeground(new java.awt.Color(245, 245, 245));
        jLabelGananciaBruta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelGananciaBruta.setText("$");
        jPanelGastos.add(jLabelGananciaBruta, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 250, 380, 70));

        jLabelGastos.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabelGastos.setForeground(new java.awt.Color(245, 245, 245));
        jLabelGastos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelGastos.setText("$");
        jPanelGastos.add(jLabelGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 340, 380, 70));

        jLabelInicio.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelInicio.setForeground(new java.awt.Color(241, 241, 241));
        jLabelInicio.setText("Inicio:");
        jPanelGastos.add(jLabelInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 40, 90, -1));

        jLabelFin.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelFin.setForeground(new java.awt.Color(241, 241, 241));
        jLabelFin.setText("Fin:");
        jPanelGastos.add(jLabelFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 40, 50, -1));
        jPanelGastos.add(jDateChooserFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 40, 180, 30));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(245, 245, 245));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Ganancia Bruta");
        jPanelGastos.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 250, 220, 70));

        jLabelVentaTotal.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        jLabelVentaTotal.setForeground(new java.awt.Color(245, 245, 245));
        jLabelVentaTotal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelVentaTotal.setText("$");
        jPanelGastos.add(jLabelVentaTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 380, 70));

        add(jPanelGastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(-21, -3, 1280, 610));
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarGananciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFiltrarGananciaMouseClicked
        // Obtener rango de fechas según el filtro seleccionado
        Date[] rangoFechas = obtenerRangoFechas();
        
        if (rangoFechas != null) {
            Date fechaInicio = rangoFechas[0];
            Date fechaFin = rangoFechas[1];
            
            // Calcular ganancias
            calcularGanancias(fechaInicio, fechaFin);
        }
    }//GEN-LAST:event_btnFiltrarGananciaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnFiltrarGanancia;
    private javax.swing.JComboBox<String> jComboBoxMesesGanancia;
    private javax.swing.JComboBox<String> jComboBoxSemanasGastos;
    private javax.swing.JComboBox<String> jComboBoxTipoFiltro;
    private com.toedter.calendar.JDateChooser jDateChooserFin;
    private com.toedter.calendar.JDateChooser jDateChooserGanancia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelFiltrarGastos;
    private javax.swing.JLabel jLabelFin;
    private javax.swing.JLabel jLabelGananciaBruta;
    private javax.swing.JLabel jLabelGananciaNeta;
    private javax.swing.JLabel jLabelGastos;
    private javax.swing.JLabel jLabelInicio;
    private javax.swing.JLabel jLabelVentaTotal;
    private javax.swing.JPanel jPanelGastos;
    // End of variables declaration//GEN-END:variables
}
