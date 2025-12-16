package modelo;

public class Venta {
    private int idVenta;
    private String fechaVenta;
    private double subtotal;
    private int descuentoPorcentaje;
    private double descuentoDinero;
    private double total;
    private double pagoEfectivo;
    private double pagoTarjeta;
    private int idUsuario;
    private String estatus;
    private String nombreUsuario;
    private int folioTicket;

    // Constructor Vacío
    public Venta() {
    }

    // --- GETTERS Y SETTERS ---

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getDescuentoPorcentaje() {
        return descuentoPorcentaje;
    }

    public void setDescuentoPorcentaje(int descuentoPorcentaje) {
        this.descuentoPorcentaje = descuentoPorcentaje;
    }

    public double getDescuentoDinero() {
        return descuentoDinero;
    }

    public void setDescuentoDinero(double descuentoDinero) {
        this.descuentoDinero = descuentoDinero;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPagoEfectivo() {
        return pagoEfectivo;
    }

    public void setPagoEfectivo(double pagoEfectivo) {
        this.pagoEfectivo = pagoEfectivo;
    }

    public double getPagoTarjeta() {
        return pagoTarjeta;
    }

    public void setPagoTarjeta(double pagoTarjeta) {
        this.pagoTarjeta = pagoTarjeta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public int getFolioTicket() {
        return folioTicket;
    }

    public void setFolioTicket(int folioTicket) {
        this.folioTicket = folioTicket;
    }
}