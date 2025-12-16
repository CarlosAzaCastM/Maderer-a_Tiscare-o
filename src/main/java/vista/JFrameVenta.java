
package vista;

import dao.VarianteDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.DetalleVenta;
import modelo.Usuario;
import modelo.Variante;



public class JFrameVenta extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFrameVenta.class.getName());
    
    private Usuario usuarioActual; 
    private DefaultTableModel modeloTabla;
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   // Lista para el carrito actual
    private List<DetalleVenta> carrito = new ArrayList<>();
    
    String fechaFormateada = LocalDate.now().format(formato);
    
    private modelo.Venta ventaFinalizada = null;

    public JFrameVenta(Usuario usuario) {
        initComponents();
        this.modeloTabla = (DefaultTableModel) jTableVenta.getModel();
        configurarTabla();
        usuarioActual = usuario;
        labelFecha.setText(fechaFormateada);
        iniciarLogicaCombos();
        this.setExtendedState(MAXIMIZED_BOTH);
    }
    
    private void iniciarLogicaCombos() {
        // Cargar el primer combo (Productos)
        cargarComboProductos();

        // Agregar "Escuchadores" (Listeners) para la cascada
        // Cuando cambia Producto -> Carga Clases
        jComboBoxProducto.addActionListener(e -> cargarComboClases());
        
        // Cuando cambia Clase -> Carga Medidas
        jComboBoxClase.addActionListener(e -> cargarComboMedidas());
        
        // Cuando cambia Medida -> Carga Grosores
        jComboBoxMedida.addActionListener(e -> cargarComboGrosores());
    }
    
    private void cargarComboProductos() {
        jComboBoxProducto.removeAllItems();
        VarianteDAO dao = new VarianteDAO();
        List<String> lista = dao.obtenerNombresProductos();
        for (String s : lista) jComboBoxProducto.addItem(s);
        // Al cargar productos, reseteamos los siguientes
        jComboBoxProducto.setSelectedIndex(-1); // Ninguno seleccionado al inicio
    }

    private void cargarComboClases() {
        jComboBoxClase.removeAllItems();
        if (jComboBoxProducto.getSelectedIndex() == -1) return;
        
        String producto = (String) jComboBoxProducto.getSelectedItem();
        VarianteDAO dao = new VarianteDAO();
        List<String> lista = dao.obtenerClasesPorProducto(producto);
        for (String s : lista) jComboBoxClase.addItem(s);
        jComboBoxClase.setSelectedIndex(-1);
    }

    private void cargarComboMedidas() {
        jComboBoxMedida.removeAllItems();
        if (jComboBoxClase.getSelectedIndex() == -1) return;

        String producto = (String) jComboBoxProducto.getSelectedItem();
        String clase = (String) jComboBoxClase.getSelectedItem();
        
        VarianteDAO dao = new VarianteDAO();
        List<String> lista = dao.obtenerMedidas(producto, clase);
        for (String s : lista) jComboBoxMedida.addItem(s);
        jComboBoxMedida.setSelectedIndex(-1);
    }

    private void cargarComboGrosores() {
        jComboBoxGrosor.removeAllItems();
        if (jComboBoxMedida.getSelectedIndex() == -1) return;

        String producto = (String) jComboBoxProducto.getSelectedItem();
        String clase = (String) jComboBoxClase.getSelectedItem();
        String medida = (String) jComboBoxMedida.getSelectedItem();

        VarianteDAO dao = new VarianteDAO();
        List<String> lista = dao.obtenerGrosores(producto, clase, medida);
        for (String s : lista) jComboBoxGrosor.addItem(s);
        if(lista.size() > 0) jComboBoxGrosor.setSelectedIndex(0); // Autoseleccionar el primero si hay
    }

    private void configurarTabla() {
        String[] titulos = {"Cantidad", "Producto", "Grosor", "Medida", "ft total", "Precio U.", "Subtotal"};
        modeloTabla = new DefaultTableModel(null, titulos){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTableVenta.setModel(modeloTabla);
        
        jTableVenta.setRowHeight(30);
    }

    // Método para agregar desde el ComboBox y Botón (+)
    private void agregarAlCarrito(Variante v, int cant) {
        boolean productoExistente = false;

        // 1. Buscamos si el producto ya está en el carrito
        for (DetalleVenta item : carrito) {
            if (item.getIdVariante() == v.getId()) {
                // SI EXISTE: Verificamos si al sumar pasamos el stock
                int nuevaCantidadTotal = item.getCantidad() + cant;

                if (nuevaCantidadTotal > v.getStockPiezas()) {
                    javax.swing.JOptionPane.showMessageDialog(this, 
                        "No puedes agregar más. Stock máximo: " + v.getStockPiezas() + 
                        "\nYa tienes " + item.getCantidad() + " en el carrito.");
                    return; // Detenemos aquí, no agregamos nada
                }

                // Si hay stock, actualizamos la cantidad del ítem existente
                // Usamos el setter que recalcula automáticamente
                item.setCantidad(nuevaCantidadTotal);
                productoExistente = true;
                break; // Terminamos el bucle
            }
        }

        // 2. SI NO EXISTE: Lo agregamos como nuevo ítem
        if (!productoExistente) {
            // Verificación inicial de stock
            if (cant > v.getStockPiezas()) {
                 javax.swing.JOptionPane.showMessageDialog(this, "Stock insuficiente.");
                 return;
            }
            DetalleVenta nuevoItem = new DetalleVenta(v, cant);
            carrito.add(nuevoItem);
        }

        // 3. Refrescamos la tabla visual
        actualizarTablaVisual();
    }

    public void actualizarTablaVisual() {
        modeloTabla.setRowCount(0);
        double totalVenta = 0;
        for (DetalleVenta item : carrito) {
            modeloTabla.addRow(new Object[]{
                item.getCantidad(), item.getNombre(), item.getGrosor(), item.getMedida(), item.getFtTotal(),
                item.getPrecioVenta(), item.getSubtotal()
            });
            totalVenta += item.getSubtotal();
        }
        txtTotal.setText(String.format("$ %.2f", totalVenta)); // Label del Total
        actualizarTotalesConDescuento();
    }
    
    private void limpiarVenta() {
        carrito.clear();
        actualizarTablaVisual(); 
        txtDescuento.setText("");
        txtEfectivo.setText("");
        txtTarjeta.setText("");
        jComboBoxProducto.setSelectedIndex(-1);
        
        // RESETEAR ESTADO PARA NUEVA VENTA
        this.ventaFinalizada = null;
        jLabelRegistrar.setText("Registrar"); // Vuelve a decir Registrar
        btnAgregarProductoVenta.setEnabled(true);
        btnBorrar.setEnabled(true);
    }
    
    private void actualizarTotalesConDescuento() {
        double subtotal = 0;
        for (DetalleVenta item : carrito) {
            subtotal += item.getSubtotal();
        }

        int porcentajeDesc = 0;
        try {
            String textoDesc = txtDescuento.getText();
            if (!textoDesc.isEmpty()) {
                porcentajeDesc = Integer.parseInt(textoDesc);
            }
        } catch (NumberFormatException e) {
            // Si escriben letras, ignoramos el descuento
        }

        double descuentoDinero = subtotal * (porcentajeDesc / 100.0);
        double total = subtotal - descuentoDinero;

        // AQUÍ APLICAMOS TU REGLA DE REDONDEO (.4)
        double totalRedondeado = redondearPersonalizado(total);

        // Actualizamos el label visualmente
        txtTotal.setText(String.format("$ %.2f", totalRedondeado));
    }

    // Tu lógica de redondeo especial
    private double redondearPersonalizado(double valor) {
        long parteEntera = (long) valor;
        double decimales = valor - parteEntera;

        if (decimales >= 0.4) {
            return Math.ceil(valor); // Sube al siguiente entero (Ej: 10.4 -> 11.0)
        } else {
            return Math.floor(valor); // Baja al entero actual (Ej: 10.3 -> 10.0)
        }
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backgroundInicio = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnHome = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        labelFecha = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableVenta = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnRegistrarVenta = new javax.swing.JPanel();
        jLabelRegistrar = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtDescuento = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        txtTotal = new javax.swing.JLabel();
        txtEfectivo = new javax.swing.JTextField();
        txtTarjeta = new javax.swing.JTextField();
        btnDescuento = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jComboBoxProducto = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jComboBoxClase = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jComboBoxMedida = new javax.swing.JComboBox<>();
        btnAgregarProductoVenta = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jComboBoxGrosor = new javax.swing.JComboBox<>();
        btnEditar = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        btnBorrar = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        backgroundInicio.setBackground(new java.awt.Color(28, 28, 28));
        backgroundInicio.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(193, 168, 120));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Venta");
        backgroundInicio.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, 367, -1));

        btnHome.setBackground(new java.awt.Color(62, 44, 32));
        btnHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHomeMouseClicked(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imgs/home.png"))); // NOI18N

        javax.swing.GroupLayout btnHomeLayout = new javax.swing.GroupLayout(btnHome);
        btnHome.setLayout(btnHomeLayout);
        btnHomeLayout.setHorizontalGroup(
            btnHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnHomeLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel2)
                .addContainerGap(64, Short.MAX_VALUE))
        );
        btnHomeLayout.setVerticalGroup(
            btnHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        backgroundInicio.add(btnHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 150, 40));

        jPanel1.setBackground(new java.awt.Color(62, 44, 32));

        labelFecha.setFont(new java.awt.Font("Segoe UI", 3, 25)); // NOI18N
        labelFecha.setForeground(new java.awt.Color(246, 246, 246));
        labelFecha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFecha.setText("??");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(labelFecha)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        backgroundInicio.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 10, 280, 40));

        jTableVenta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTableVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableVenta);

        backgroundInicio.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, 1060, 370));

        jPanel2.setBackground(new java.awt.Color(62, 44, 32));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(241, 241, 241));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Efectivo");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 65, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(241, 241, 241));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Tarjeta");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, 65, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(241, 241, 241));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Total");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(967, 10, 90, -1));

        btnRegistrarVenta.setBackground(new java.awt.Color(42, 128, 47));
        btnRegistrarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrarVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarVentaMouseClicked(evt);
            }
        });

        jLabelRegistrar.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabelRegistrar.setForeground(new java.awt.Color(241, 241, 241));
        jLabelRegistrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelRegistrar.setText("Registrar");

        javax.swing.GroupLayout btnRegistrarVentaLayout = new javax.swing.GroupLayout(btnRegistrarVenta);
        btnRegistrarVenta.setLayout(btnRegistrarVentaLayout);
        btnRegistrarVentaLayout.setHorizontalGroup(
            btnRegistrarVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnRegistrarVentaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabelRegistrar)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        btnRegistrarVentaLayout.setVerticalGroup(
            btnRegistrarVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnRegistrarVentaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelRegistrar)
                .addContainerGap())
        );

        jPanel2.add(btnRegistrarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 30, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(241, 241, 241));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Descuento %");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(477, 10, 100, -1));

        txtDescuento.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDescuento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 120, 38));

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        txtTotal.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        txtTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 40, 170, 40));

        txtEfectivo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtEfectivo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtEfectivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 130, 38));

        txtTarjeta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTarjeta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtTarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 130, 38));

        btnDescuento.setBackground(new java.awt.Color(209, 76, 76));
        btnDescuento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDescuentoMouseClicked(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(239, 239, 239));
        jLabel9.setText("Aplicar");

        javax.swing.GroupLayout btnDescuentoLayout = new javax.swing.GroupLayout(btnDescuento);
        btnDescuento.setLayout(btnDescuentoLayout);
        btnDescuentoLayout.setHorizontalGroup(
            btnDescuentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnDescuentoLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(17, 17, 17))
        );
        btnDescuentoLayout.setVerticalGroup(
            btnDescuentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnDescuentoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(btnDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 30, 80, 30));

        backgroundInicio.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 530, 1120, 90));

        jPanel3.setBackground(new java.awt.Color(62, 44, 32));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(241, 241, 241));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Cant");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 70, -1));

        txtCantidad.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel3.add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 90, 28));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(241, 241, 241));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Producto");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 110, -1));

        jPanel3.add(jComboBoxProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 140, 30));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(241, 241, 241));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Clase");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 80, -1));

        jPanel3.add(jComboBoxClase, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, 110, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(241, 241, 241));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Medida");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 110, -1));

        jPanel3.add(jComboBoxMedida, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 30, 140, 30));

        btnAgregarProductoVenta.setBackground(new java.awt.Color(42, 128, 47));
        btnAgregarProductoVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarProductoVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarProductoVentaMouseClicked(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(242, 242, 242));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("+");

        javax.swing.GroupLayout btnAgregarProductoVentaLayout = new javax.swing.GroupLayout(btnAgregarProductoVenta);
        btnAgregarProductoVenta.setLayout(btnAgregarProductoVentaLayout);
        btnAgregarProductoVentaLayout.setHorizontalGroup(
            btnAgregarProductoVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnAgregarProductoVentaLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        btnAgregarProductoVentaLayout.setVerticalGroup(
            btnAgregarProductoVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnAgregarProductoVentaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel3.add(btnAgregarProductoVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 20, 80, 40));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(241, 241, 241));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Grosor");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 110, -1));

        jPanel3.add(jComboBoxGrosor, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 30, 140, 30));

        backgroundInicio.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 910, 70));

        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditar.setOpaque(false);
        btnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditarMouseClicked(evt);
            }
        });

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imgs/editar.png"))); // NOI18N

        javax.swing.GroupLayout btnEditarLayout = new javax.swing.GroupLayout(btnEditar);
        btnEditar.setLayout(btnEditarLayout);
        btnEditarLayout.setHorizontalGroup(
            btnEditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        btnEditarLayout.setVerticalGroup(
            btnEditarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        backgroundInicio.add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 200, 80, 80));

        btnBorrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBorrar.setOpaque(false);
        btnBorrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBorrarMouseClicked(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/vista/imgs/borrar.png"))); // NOI18N

        javax.swing.GroupLayout btnBorrarLayout = new javax.swing.GroupLayout(btnBorrar);
        btnBorrar.setLayout(btnBorrarLayout);
        btnBorrarLayout.setHorizontalGroup(
            btnBorrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnBorrarLayout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );
        btnBorrarLayout.setVerticalGroup(
            btnBorrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        backgroundInicio.add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 320, 70, 70));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundInicio, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHomeMouseClicked
        if (ventaFinalizada != null) {
            JOptionPane.showMessageDialog(this, 
                "Venta ya registrada. Imprime el ticket o inicia una nueva venta.");
            return;
        }
        Menu menu = new Menu(usuarioActual);
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnHomeMouseClicked

    private void btnEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseClicked
        if (ventaFinalizada != null) {
            JOptionPane.showMessageDialog(this, 
                "Venta ya registrada. Imprime el ticket o inicia una nueva venta.");
            return;
        }
        int fila = jTableVenta.getSelectedRow();
        if (fila >= 0) {
            DetalleVenta item = carrito.get(fila);
            // Abrimos el diálogo pasándole el item
            JDialogEditarVenta dialog = new JDialogEditarVenta(this, true, item);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para editar.");
        }
    }//GEN-LAST:event_btnEditarMouseClicked

    private void btnAgregarProductoVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarProductoVentaMouseClicked
        if (ventaFinalizada != null) {
            JOptionPane.showMessageDialog(this, 
                "Venta ya registrada. Imprime el ticket o inicia una nueva venta.");
            return;
        }

        // Resto del código original...
        // 1. Validar selecciones
        if (jComboBoxProducto.getSelectedIndex() == -1 || jComboBoxClase.getSelectedIndex() == -1 || 
            jComboBoxMedida.getSelectedIndex() == -1 || jComboBoxGrosor.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona todas las características del producto.");
            return;
        }
        
        // 2. Validar cantidad
        int cantidad = 0;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ingresa una cantidad válida mayor a 0.");
            return;
        }

        // 3. Buscar la variante exacta en BD
        String prod = (String) jComboBoxProducto.getSelectedItem();
        String clase = (String) jComboBoxClase.getSelectedItem();
        String medida = (String) jComboBoxMedida.getSelectedItem();
        String grosor = (String) jComboBoxGrosor.getSelectedItem();

        VarianteDAO dao = new VarianteDAO();
        Variante v = dao.buscarVarianteEspecifica(prod, clase, medida, grosor);

        if (v != null) {
            // 4. Verificar stock suficiente (Opcional pero recomendado)
            if (v.getStockPiezas() < cantidad) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponibles: " + v.getStockPiezas());
                return;
            }
            // 5. Agregar al carrito
            agregarAlCarrito(v, cantidad);
            txtCantidad.setText(""); // Limpiar cantidad
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se encontró el producto específico en la BD.");
        }
    }//GEN-LAST:event_btnAgregarProductoVentaMouseClicked

    private void btnBorrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarMouseClicked
        if (ventaFinalizada != null) {
            JOptionPane.showMessageDialog(this, 
                "Venta ya registrada. Imprime el ticket o inicia una nueva venta.");
            return;
        }
        int fila = jTableVenta.getSelectedRow();
        if (fila >= 0) {
            carrito.remove(fila); // Borrar de la lista lógica
            actualizarTablaVisual(); // Refrescar tabla y total
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para borrar.");
        }
    }//GEN-LAST:event_btnBorrarMouseClicked

    private void btnRegistrarVentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarVentaMouseClicked
    if (ventaFinalizada != null) {
         try {
            System.out.println("=== IMPRESIÓN (venta ya registrada) ===");
            System.out.println("Folio: " + ventaFinalizada.getFolioTicket());
            System.out.println("Productos en carrito: " + carrito.size());
            
            modelo.ImpresionTicket printer = new modelo.ImpresionTicket();
            printer.imprimirTicket(ventaFinalizada, carrito, usuarioActual);
        } catch(Exception ex) { 
            System.out.println("Error impresión: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        // Preguntar si quiere limpiar para nueva venta
        int resp = JOptionPane.showConfirmDialog(this, 
            "¿Iniciar nueva venta?", "Venta Finalizada", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            limpiarVenta();
        }
        return;
    }

    // --- CASO 2: NUEVA VENTA (Lógica normal) ---
    if (carrito.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Carrito vacío.");
        return;
    }

    try {
        // 1. Cálculos
        double subtotal = 0;
        for (DetalleVenta item : carrito) subtotal += item.getSubtotal();
        
        String descTxt = txtDescuento.getText();
        int descPorc = descTxt.isEmpty() ? 0 : Integer.parseInt(descTxt);
        double descDinero = subtotal * (descPorc / 100.0);
        
        double totalReal = redondearPersonalizado(subtotal - descDinero);

        // 2. Validar pagos
        String efecTxt = txtEfectivo.getText();
        String tarjTxt = txtTarjeta.getText();
        double efectivo = efecTxt.isEmpty() ? 0 : Double.parseDouble(efecTxt);
        double tarjeta = tarjTxt.isEmpty() ? 0 : Double.parseDouble(tarjTxt);
        double totalPagado = efectivo + tarjeta;

        if (Math.abs(totalPagado - totalReal) > 0.01) {
            if (totalPagado < totalReal) {
                JOptionPane.showMessageDialog(this, 
                    String.format("Pago insuficiente. Faltan: $%.2f", (totalReal - totalPagado)));
            } else {
                JOptionPane.showMessageDialog(this, 
                    String.format("El pago excede el total. Sobran: $%.2f\nAjusta el monto en efectivo o tarjeta.", 
                    (totalPagado - totalReal)));
            }
            return;
        }

        // 3. Crear Objeto Venta
        modelo.Venta venta = new modelo.Venta();
        venta.setSubtotal(subtotal);
        venta.setDescuentoPorcentaje(descPorc);
        venta.setDescuentoDinero(descDinero);
        venta.setTotal(totalReal);
        venta.setPagoEfectivo(efectivo);
        venta.setPagoTarjeta(tarjeta);
        venta.setIdUsuario(usuarioActual.getIdUsuario());

        // 4. Guardar en BD
        dao.VentaDao dao = new dao.VentaDao();
        if (dao.registrarVenta(venta, carrito)) {
            JOptionPane.showMessageDialog(this, "Venta Exitosa. Imprimiendo tickets...");
            
            // Guardar la venta en memoria
            this.ventaFinalizada = venta;
            jLabelRegistrar.setText("Imprimir");
            btnAgregarProductoVenta.setEnabled(false);
            btnBorrar.setEnabled(false);
            
            // 5. IMPRIMIR TICKET
            try {
                modelo.ImpresionTicket printer = new modelo.ImpresionTicket();
                printer.imprimirTicket(venta, carrito, usuarioActual);
            } catch(Exception ex) {
                System.out.println("Error impresión: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar la venta en BD.");
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Verifica que los montos sean números válidos.");
    }
        
    }//GEN-LAST:event_btnRegistrarVentaMouseClicked

    private void btnDescuentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDescuentoMouseClicked
         if (ventaFinalizada != null) {
            JOptionPane.showMessageDialog(this, 
                "No puedes modificar el descuento después de registrar la venta.");
            return;
        }
        actualizarTotalesConDescuento();
    }//GEN-LAST:event_btnDescuentoMouseClicked

  
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
            new JFrameVenta(userPrueba).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundInicio;
    private javax.swing.JPanel btnAgregarProductoVenta;
    private javax.swing.JPanel btnBorrar;
    private javax.swing.JPanel btnDescuento;
    private javax.swing.JPanel btnEditar;
    private javax.swing.JPanel btnHome;
    private javax.swing.JPanel btnRegistrarVenta;
    private javax.swing.JComboBox<String> jComboBoxClase;
    private javax.swing.JComboBox<String> jComboBoxGrosor;
    private javax.swing.JComboBox<String> jComboBoxMedida;
    private javax.swing.JComboBox<String> jComboBoxProducto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelRegistrar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableVenta;
    private javax.swing.JLabel labelFecha;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtDescuento;
    private javax.swing.JTextField txtEfectivo;
    private javax.swing.JTextField txtTarjeta;
    private javax.swing.JLabel txtTotal;
    // End of variables declaration//GEN-END:variables
}
