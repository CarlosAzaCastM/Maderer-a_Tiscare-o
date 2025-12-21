package modelo;

public class Variante {
    private int id;
    private String nombreProducto; // Ojo: Traeremos el nombre usando un JOIN
    private String clase;
    private String medida;
    private String grosor;
    private double piesPorPieza;
    private double costoCompra;
    private double precioVenta;
    private int stockPiezas;
    private int stockMinimo;

    // Constructor Vacío
    public Variante() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    
    
    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public String getGrosor() {
        return grosor;
    }

    public void setGrosor(String grosor) {
        this.grosor = grosor;
    }

    public double getPiesPorPieza() {
        return piesPorPieza;
    }

    public void setPiesPorPieza(double piesPorPieza) {
        this.piesPorPieza = piesPorPieza;
    }

    public double getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(double costoCompra) {
        this.costoCompra = costoCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getStockPiezas() {
        return stockPiezas;
    }

    public void setStockPiezas(int stockPiezas) {
        this.stockPiezas = stockPiezas;
    }

    
}