package modelo;

public class DetalleVenta {
    private int idDetalle;
    private int idVenta;
    private int idVariante;
    private String nombre;
    private String medida;
    private String clase;
    private String grosor;
    private double ftTotal;
    private int cantidad;
    private double precioVenta;
    private double costoCompra;
    private double subtotal;

    // Constructor vacío
    public DetalleVenta() {
    }

    // Constructor con parámetros
    public DetalleVenta(Variante v, int cantidad) {
        this.idVariante = v.getId();
        this.nombre = v.getNombreProducto();
        this.medida = v.getMedida();
        this.clase = v.getClase();
        this.grosor = v.getGrosor();
        
        this.cantidad = cantidad;
        this.costoCompra = v.getCostoCompra(); 
        this.precioVenta = v.getPrecioVenta();
        
        // Calcular ftTotal
        calcularFtTotal();
        
        // Calcular subtotal inicial
        this.subtotal = cantidad * v.getPrecioVenta();
    }
    
    // MÉTODO PARA RECALCULAR EL FT TOTAL (pies cuadrados)
    private void calcularFtTotal() {
        if (this.medida == null || this.grosor == null) return;
        
        String[] medidaPartes = this.medida.split("x");
        
        if (medidaPartes.length != 2) return;
        
        String part1Medida = medidaPartes[0]; 
        String part2Medida = medidaPartes[1];

        int part1EnteroMedida = Integer.parseInt(part1Medida);
        int part2EnteroMedida = Integer.parseInt(part2Medida);
        
        try {
            if (this.grosor.contains("/") && this.grosor.split("/").length == 2) {
                String[] partes = this.grosor.split("/");
                float numerador = Float.parseFloat(partes[0]);
                float denominador = Float.parseFloat(partes[1]);
                float grosorFloat = numerador / denominador;   
                this.ftTotal = ((grosorFloat * part1EnteroMedida * part2EnteroMedida) / 12) * this.cantidad;
            } else {
                float grosorFloat = Float.parseFloat(this.grosor);
                this.ftTotal = ((grosorFloat * part1EnteroMedida * part2EnteroMedida) / 12) * this.cantidad;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error en cálculo de ftTotal: " + e.getMessage());
            this.ftTotal = 0;
        }
    }
    
    // MÉTODO PARA RECALCULAR EL SUBTOTAL
    public void recalcularSubtotal() {
        this.subtotal = this.cantidad * this.precioVenta;
    }

    // Getters y Setters
    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdVariante() {
        return idVariante;
    }

    public void setIdVariante(int idVariante) {
        this.idVariante = idVariante;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        // Recalcular ftTotal cuando cambia la cantidad
        calcularFtTotal();
        // Recalcular subtotal automáticamente
        recalcularSubtotal();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
        // Recalcular ftTotal si cambia la medida
        calcularFtTotal();
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getGrosor() {
        return grosor;
    }

    public void setGrosor(String grosor) {
        this.grosor = grosor;
        // Recalcular ftTotal si cambia el grosor
        calcularFtTotal();
    }

    public double getFtTotal() {
        return ftTotal;
    }

    public void setFtTotal(double ftTotal) {
        this.ftTotal = ftTotal;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
        // Recalcular subtotal automáticamente cuando cambia el precio
        recalcularSubtotal();
    }

    public double getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(double costoCompra) {
        this.costoCompra = costoCompra;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}