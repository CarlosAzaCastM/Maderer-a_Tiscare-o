package modelo;

public class EntradaInventario {
    private int idEntrada;
    private int idVariante;
    private int cantidadAgregada;
    private String fechaEntrada;
    private double nuevoCostoCompra;  // Cambiado a double
    private double nuevoPrecioVenta;  // Cambiado a double
    
    // Campos adicionales para mostrar en la tabla (no están en la BD)
    private String nombreProducto;
    private String clase;
    private String medida;
    private String grosor;

    public EntradaInventario() {
    }

    // Getters y Setters
    public int getIdEntrada() { return idEntrada; }
    public void setIdEntrada(int idEntrada) { this.idEntrada = idEntrada; }
    
    public int getIdVariante() { return idVariante; }
    public void setIdVariante(int idVariante) { this.idVariante = idVariante; }
    
    public int getCantidadAgregada() { return cantidadAgregada; }
    public void setCantidadAgregada(int cantidadAgregada) { this.cantidadAgregada = cantidadAgregada; }
    
    public String getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(String fechaEntrada) { this.fechaEntrada = fechaEntrada; }
    
    public double getNuevoCostoCompra() { return nuevoCostoCompra; }
    public void setNuevoCostoCompra(double nuevoCostoCompra) { this.nuevoCostoCompra = nuevoCostoCompra; }
    
    public double getNuevoPrecioVenta() { return nuevoPrecioVenta; }
    public void setNuevoPrecioVenta(double nuevoPrecioVenta) { this.nuevoPrecioVenta = nuevoPrecioVenta; }
    
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    
    public String getClase() { return clase; }
    public void setClase(String clase) { this.clase = clase; }
    
    public String getMedida() { return medida; }
    public void setMedida(String medida) { this.medida = medida; }
    
    public String getGrosor() { return grosor; }
    public void setGrosor(String grosor) { this.grosor = grosor; }
}