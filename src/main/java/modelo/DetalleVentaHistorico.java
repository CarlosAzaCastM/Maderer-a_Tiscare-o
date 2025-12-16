package modelo;

public class DetalleVentaHistorico {
    private int idDetalle;
    private int idVenta;
    private int idVariante;
    private int cantidad;
    private double precioVentaHistorico;
    private double costoCompraHistorico;
    private double importe;
    
    // Campos adicionales de la variante (para mostrar en la tabla)
    private String clase;
    private String medida;
    private String grosor;
    private String nombreProducto;
    
    // Constructor vacío
    public DetalleVentaHistorico() {
    }
    
    // Getters y Setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }
    
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
    
    public int getIdVariante() { return idVariante; }
    public void setIdVariante(int idVariante) { this.idVariante = idVariante; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public double getPrecioVentaHistorico() { return precioVentaHistorico; }
    public void setPrecioVentaHistorico(double precioVentaHistorico) { this.precioVentaHistorico = precioVentaHistorico; }
    
    public double getCostoCompraHistorico() { return costoCompraHistorico; }
    public void setCostoCompraHistorico(double costoCompraHistorico) { this.costoCompraHistorico = costoCompraHistorico; }
    
    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }
    
    public String getClase() { return clase; }
    public void setClase(String clase) { this.clase = clase; }
    
    public String getMedida() { return medida; }
    public void setMedida(String medida) { this.medida = medida; }
    
    public String getGrosor() { return grosor; }
    public void setGrosor(String grosor) { this.grosor = grosor; }
    
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
}